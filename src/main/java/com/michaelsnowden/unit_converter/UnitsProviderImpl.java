package com.michaelsnowden.unit_converter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author michael.snowden
 */
public class UnitsProviderImpl implements UnitsProvider {
    private final Map<String, Map<String, Fraction>> derivedUnits;
    private final Map<String, Double> factors;
    private final Map<String, String> symbols;

    public UnitsProviderImpl() {
        derivedUnits = new HashMap<>();
        Map<String, Fraction> derivedUnit;

        derivedUnit = new HashMap<>();
        derivedUnit.put("kg", new Fraction(1));
        derivedUnit.put("m", new Fraction(1));
        derivedUnit.put("s", new Fraction(-2));
        derivedUnits.put("N", derivedUnit);

        derivedUnit = new HashMap<>();
        derivedUnit.put("s", new Fraction(-1));
        derivedUnits.put("Hz", derivedUnit);

        derivedUnit = new HashMap<>();
        derivedUnit.put("m", new Fraction(-1));
        derivedUnit.put("kg", new Fraction(1));
        derivedUnit.put("s", new Fraction(-2));
        derivedUnits.put("Pa", derivedUnit);

        derivedUnit = new HashMap<>();
        derivedUnit.put("m", new Fraction(2));
        derivedUnit.put("kg", new Fraction(1));
        derivedUnit.put("s", new Fraction(-2));
        derivedUnits.put("J", derivedUnit);

        derivedUnit = new HashMap<>();
        derivedUnit.put("m", new Fraction(2));
        derivedUnit.put("kg", new Fraction(1));
        derivedUnit.put("s", new Fraction(-3));
        derivedUnits.put("W", derivedUnit);

        factors = new HashMap<>();
        factors.put("g", 1 / 1000.0);
        factors.put("ms", 1 / 1000.0);

        symbols = new HashMap<>();
        symbols.put("g", "kg");
    }

    @Override
    public Map<String, Fraction> decompose(String unit, Fraction power) {
        return derivedUnits.getOrDefault(unit, Stream.of(unit).collect(Collectors.toMap(e -> unit, e -> new Fraction
                (1))))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().times(power)));
    }

    @Override
    public Double conversionFactor(String key, Fraction value) {
        return Math.pow(factors.getOrDefault(key, 1.0), value.getDouble());
    }

    @Override
    public String conversionSymbol(String key) {
        return symbols.getOrDefault(key, key);
    }
}
