package com.michaelsnowden.unit_converter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author michael.snowden
 */
public class Term {
    private double value;
    private Map<String, Fraction> units;

    public Term(double value, Map<String, Fraction> units) {
        this.units = units
                .entrySet()
                .stream()
                .filter(e -> e.getValue().isNotZero())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        this.value = value;
    }

    public Term plus(Term number) {
        verifySameUnits(number);
        return new Term(value + number.getValue(), getUnits());
    }

    public Term minus(Term number) {
        verifySameUnits(number);
        return new Term(value - number.getValue(), getUnits());
    }

    public Map<String, Fraction> getUnits() {
        return units;
    }

    public double getValue() {
        return value;
    }

    public boolean isUnitless() {
        return units.size() == 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Fraction fraction = new Fraction(value);
        if (fraction.getD() == 1 && !(fraction.getN() == 0 && value != 0)) {
            builder.append(fraction.getN());
        } else {
            builder.append(value);
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

    public Term raisedTo(Term number) {
        if (!number.isUnitless()) {
            throw new IllegalArgumentException("Exponents cannot have units");
        }
        Map<String, Fraction> units = new HashMap<>();
        for (String unit : this.units.keySet()) {
            units.put(unit, this.units.get(unit).times(number.getValue()));
        }
        return new Term(Math.pow(getValue(), number.getValue()), units);
    }

    public Term dividedBy(Term number) {
        Map<String, Fraction> units = Stream.of(this.units, number.getUnits())
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Fraction::minus));
        return new Term(value / number.getValue(), units);
    }

    public Term times(Term number) {
        Map<String, Fraction> units = Stream.of(this.units, number.getUnits())
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Fraction::plus));
        return new Term(value * number.getValue(), units);
    }

    private void verifySameUnits(Term number) {
        if (getUnits().size() != number.getUnits().size()) {
            throw new IllegalArgumentException("Cannot add numbers with different dimensions");
        }
        for (String s : getUnits().keySet()) {
            if (!getUnits().get(s).equals(number.getUnits().get(s))) {
                throw new IllegalArgumentException("Cannot add numbers with different dimensions");
            }
        }
    }
}
