package com.michaelsnowden.unit_converter.functions;

import com.michaelsnowden.unit_converter.Function;
import com.michaelsnowden.unit_converter.Term;

import java.util.HashMap;
import java.util.List;

/**
 * @author michael.snowden
 */
public class SquareRootFunction implements Function {
    @Override
    public Term evaluate(List<Term> arguments) {
        Term number = arguments.get(0);
        if (!number.isUnitless()) {
            throw new IllegalArgumentException("The number passed into a square root function must be unitless");
        }
        return new Term(Math.sqrt(number.getValue()), new HashMap<>());
    }
}
