package com.michaelsnowden.unit_converter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author michael.snowden
 */
public class UnitsProviderImpl implements UnitsProvider {
    Map<String, Map<String, Fraction>> derivedUnits;
    Map<String, Double> conversionFactors;
    Map<String, String> conversionSymbols;

    public UnitsProviderImpl() {
        derivedUnits = new HashMap<>();
        Map<String, Fraction> units = new HashMap<>();
        units.put("kg", new Fraction(1));
        units.put("m", new Fraction(1));
        units.put("s", new Fraction(-2));
        derivedUnits.put("N", units);

        conversionFactors = new HashMap<>();
        conversionFactors.put("g", 1 / 1000.0);

        conversionSymbols = new HashMap<>();
        conversionSymbols.put("g", "kg");
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
        return Math.pow(conversionFactors.getOrDefault(key, 1.0), value.getDouble());
    }

    @Override
    public String conversionSymbol(String key) {
        return conversionSymbols.getOrDefault(key, key);
    }
}
