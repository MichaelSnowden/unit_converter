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
    private final AbstractSymbolLookup unitsProvider;

    public Term(double value, Map<String, Fraction> units, AbstractSymbolLookup unitsProvider) {
        this.unitsProvider = unitsProvider;
        this.units = units;
        this.value = value;
    }

    public Term plus(Term number) {
        verifySameUnits(number);
        return new Term(value + number.getValue(), getUnits(), unitsProvider);
    }

    public Term minus(Term number) {
        verifySameUnits(number);
        return new Term(value - number.getValue(), getUnits(), unitsProvider);
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

    public Term raisedTo(Term number) {
        if (!number.isUnitless()) {
            throw new IllegalArgumentException("Exponents cannot have units");
        }
        Map<String, Fraction> units = new HashMap<>();
        for (String unit : this.units.keySet()) {
            units.put(unit, this.units.get(unit).times(new Fraction(number.getValue())));
        }
        return new Term(Math.pow(getValue(), number.getValue()), units, unitsProvider);
    }

    public Term negated() {
        return new Term(-getValue(), getUnits(), unitsProvider);
    }

    public Term dividedBy(Term number) {
        Map<String, Fraction> units = new HashMap<>(getUnits());
        for (String s : number.getUnits().keySet()) {
            if (units.get(s) == null) {
                units.put(s, new Fraction(0, 1));
            }
            units.put(s, units.get(s).minus(number.getUnits().get(s)));
        }
        return new Term(value / number.getValue(), units, unitsProvider);
    }

    public Term times(Term number) {
        Map<String, Fraction> units = Stream.of(this.units, number.getUnits())
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Fraction::plus));
        return new Term(value * number.getValue(), units, unitsProvider);
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

    public AbstractSymbolLookup getUnitsProvider() {
        return unitsProvider;
    }
}
