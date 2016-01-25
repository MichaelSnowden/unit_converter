package com.michaelsnowden.unit_converter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author michael.snowden
 */
public class QualifiedNumber {
    private final double value;
    private final Map<String, Fraction> units;
    private final UnitsProvider unitsProvider;

    public QualifiedNumber(double value, Map<String, Fraction> units, UnitsProvider unitsProvider) {
        this.unitsProvider = unitsProvider;
        units = units.entrySet()
                .stream()
                .map(e -> unitsProvider.decompose(e.getKey(), e.getValue()))
                .reduce(new HashMap<>(), (m1, m2) -> Stream.of(m1, m2)
                        .map(Map::entrySet)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                Fraction::plus
                        )))
                .entrySet()
                .stream()
                .filter(e -> e.getValue().isNotZero())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        this.value = value * units
                .entrySet()
                .stream()
                .map(e -> unitsProvider.conversionFactor(e.getKey(), e.getValue()))
                .reduce(1.0, (a, b) -> a * b);
        this.units = units.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> unitsProvider.conversionSymbol(e.getKey()),
                        Map.Entry::getValue
                ));
    }

    public QualifiedNumber plus(QualifiedNumber number) {
        verifySameUnits(number);
        return new QualifiedNumber(value + number.getValue(), getUnits(), unitsProvider);
    }

    public QualifiedNumber minus(QualifiedNumber number) {
        verifySameUnits(number);
        return new QualifiedNumber(value - number.getValue(), getUnits(), unitsProvider);
    }

    public Map<String, Fraction> getUnits() {
        return units;
    }

    public double getValue() {
        return value;
    }

    boolean isUnitless() {
        return units.size() == 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Fraction fraction = new Fraction(value);
        if (fraction.getD() == 1) {
            builder.append(fraction.getN());
        } else {
            builder.append(fraction.getDouble());
        }
        for (String unit : units.keySet().stream().sorted().collect(Collectors.toList())) {
            Fraction power = units.get(unit);
            builder.append(" ").append(unit);
            if (!power.isOne()) {
                builder.append("^").append(power);
            }
        }
        return builder.toString();
    }

    public QualifiedNumber sine() {
        return new QualifiedNumber(Math.sin(getValue()), getUnits(), unitsProvider);
    }

    public QualifiedNumber cosine() {
        return new QualifiedNumber(Math.cos(getValue()), getUnits(), unitsProvider);
    }

    public QualifiedNumber raisedTo(QualifiedNumber number) {
        if (!number.isUnitless()) {
            throw new IllegalArgumentException("Exponents cannot have units");
        }
        Map<String, Fraction> units = new HashMap<>();
        for (String unit : this.units.keySet()) {
            units.put(unit, this.units.get(unit).times(new Fraction(number.getValue())));
        }
        return new QualifiedNumber(Math.pow(getValue(), number.getValue()), units, unitsProvider);
    }

    public QualifiedNumber negated() {
        return new QualifiedNumber(-getValue(), getUnits(), unitsProvider);
    }

    public QualifiedNumber dividedBy(QualifiedNumber number) {
        Map<String, Fraction> units = new HashMap<>(getUnits());
        for (String s : number.getUnits().keySet()) {
            if (units.get(s) == null) {
                units.put(s, new Fraction(0, 1));
            }
            units.put(s, units.get(s).minus(number.getUnits().get(s)));
        }
        return new QualifiedNumber(value / number.getValue(), units, unitsProvider);
    }

    public QualifiedNumber times(QualifiedNumber number) {
        Map<String, Fraction> units = Stream.of(this.units, number.getUnits())
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Fraction::plus));
        return new QualifiedNumber(value * number.getValue(), units, unitsProvider);
    }

    private void verifySameUnits(QualifiedNumber number) {
        if (getUnits().size() != number.getUnits().size()) {
            throw new IllegalArgumentException("Cannot add numbers with different dimensions");
        }
        for (String s : getUnits().keySet()) {
            if (!getUnits().get(s).equals(number.getUnits().get(s))) {
                throw new IllegalArgumentException("Cannot add numbers with different dimensions");
            }
        }
    }

    public UnitsProvider getUnitsProvider() {
        return unitsProvider;
    }
}
