package com.michaelsnowden.unit_converter;

import java.util.HashMap;
import java.util.Map;

import static com.google.appengine.repackaged.com.google.common.collect.ImmutableMap.of;
import static java.util.Collections.emptyMap;

/**
 * @author michael.snowden
 */
public class SymbolLookup implements AbstractSymbolLookup {
    private final Map<String, Term> map;

    public SymbolLookup() {
        map = new HashMap<>();
        map.put("kg", new Term(1.0, of("kg", new Fraction(1)), this));
        map.put("pi", new Term(1.0, emptyMap(), this));
//        Map<String, Fraction> derivedUnit;
//
//        derivedUnit = new HashMap<>();
//        derivedUnit.put("kg", new Fraction(1));
//        derivedUnit.put("m", new Fraction(1));
//        derivedUnit.put("s", new Fraction(-2));
//        derivedUnits.put("N", derivedUnit);
//
//        derivedUnit = new HashMap<>();
//        derivedUnit.put("s", new Fraction(-1));
//        derivedUnits.put("Hz", derivedUnit);
//
//        derivedUnit = new HashMap<>();
//        derivedUnit.put("m", new Fraction(-1));
//        derivedUnit.put("kg", new Fraction(1));
//        derivedUnit.put("s", new Fraction(-2));
//        derivedUnits.put("Pa", derivedUnit);
//
//        derivedUnit = new HashMap<>();
//        derivedUnit.put("m", new Fraction(2));
//        derivedUnit.put("kg", new Fraction(1));
//        derivedUnit.put("s", new Fraction(-2));
//        derivedUnits.put("J", derivedUnit);
//
//        derivedUnit = new HashMap<>();
//        derivedUnit.put("m", new Fraction(2));
//        derivedUnit.put("kg", new Fraction(1));
//        derivedUnit.put("s", new Fraction(-3));
//        derivedUnits.put("W", derivedUnit);
//
//        conversionFactors.put("g", 1 / 1000.0);
//        conversionFactors.put("ms", 1 / 1000.0);
//
//        symbolConversions.put("g", "kg");
//        symbolConversions.put("ms", "s");
//
//        constants.put("pi", Math.PI);
    }

    @Override
    public Term lookup(String symbol) {
        return null;
    }
}
