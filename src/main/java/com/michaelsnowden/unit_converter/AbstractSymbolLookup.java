package com.michaelsnowden.unit_converter;

import java.util.HashMap;
import java.util.Map;

import static com.google.appengine.repackaged.com.google.common.collect.ImmutableMap.of;

/**
 * @author michael.snowden
 */
public abstract class AbstractSymbolLookup implements SymbolLookup {
    protected final Map<String, Term> map;

    public AbstractSymbolLookup() {
        map = new HashMap<>();
    }

    @Override
    public Term lookup(String symbol) {
        return recursiveLookup(symbol, new Fraction(1));
    }

    private Term recursiveLookup(String symbol, Fraction power) {
        if (map.containsKey(symbol)) {
            return map.get(symbol)
                    .getUnits()
                    .entrySet()
                    .stream()
                    .map(e -> recursiveLookup(e.getKey(), e.getValue()))
                    .reduce(new Term(map.get(symbol).getValue(), new HashMap<>()), Term::times);
        } else {
            return new Term(1.0, of(symbol, power));
        }
    }

    public abstract Map<String, Term> getMap();
}
