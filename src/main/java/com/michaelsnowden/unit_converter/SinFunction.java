package com.michaelsnowden.unit_converter;

import java.util.HashMap;
import java.util.List;

/**
 * @author michael.snowden
 */
public class SinFunction implements Function {
    private final AbstractSymbolLookup unitsProvider;

    public SinFunction(AbstractSymbolLookup unitsProvider) {
        this.unitsProvider = unitsProvider;
    }

    @Override
    public Term evaluate(List<Term> arguments) {
        if (arguments.size() != 1) {
            throw new IllegalArgumentException("The sin function requires exactly 1 argument");
        }
        Term number = arguments.get(0);
        if (!number.isUnitless()) {
            throw new IllegalArgumentException("The sin function requires a unitless argument");
        }
        return new Term(Math.sin(number.getValue()), new HashMap<>(), unitsProvider);
    }
}
