package com.michaelsnowden.unit_converter;

import java.util.HashMap;
import java.util.List;

/**
 * @author michael.snowden
 */
public class SquareRootFunction implements Function {
    private final AbstractSymbolLookup unitsProvider;

    public SquareRootFunction(AbstractSymbolLookup unitsProvider) {
        this.unitsProvider = unitsProvider;
    }

    @Override
    public Term evaluate(List<Term> arguments) {
        Term number = arguments.get(0);
        if (!number.isUnitless()) {
            throw new IllegalArgumentException("The number passed into a square root function must be unitless");
        }
        return new Term(Math.sqrt(number.getValue()), new HashMap<>(), unitsProvider);
    }
}
