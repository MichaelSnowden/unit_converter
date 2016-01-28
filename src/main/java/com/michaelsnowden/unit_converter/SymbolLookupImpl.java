package com.michaelsnowden.unit_converter;

import static com.google.appengine.repackaged.com.google.common.collect.ImmutableMap.of;
import static java.util.Collections.emptyMap;

/**
 * @author michael.snowden
 */
public class SymbolLookupImpl extends AbstractSymbolLookup {

    public SymbolLookupImpl() {
        super();
        map.put("g", new Term(0.001, of("kg", new Fraction(1))));
        map.put("pi", new Term(Math.PI, emptyMap()));
        map.put("N", new Term(1.0, of("kg", new Fraction(1), "m", new Fraction(1), "s", new Fraction(-2))));
        map.put("Hz", new Term(1.0, of("s", new Fraction(-1))));
        map.put("Pa", new Term(1.0, of("m", new Fraction(-1), "kg", new Fraction(1), "s", new Fraction(-2))));
        map.put("G", new Term(6.67408E-11, emptyMap()));
        map.put("ms", new Term(0.001, of("s", new Fraction(1))));
        map.put("cm", new Term(0.01, of("m", new Fraction(1))));
    }
}
