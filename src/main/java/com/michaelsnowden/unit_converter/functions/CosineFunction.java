package com.michaelsnowden.unit_converter.functions;

import com.michaelsnowden.unit_converter.Function;
import com.michaelsnowden.unit_converter.Term;

import java.util.HashMap;
import java.util.List;

/**
 * @author michael.snowden
 */
public class CosineFunction implements Function {

    @Override
    public Term evaluate(List<Term> arguments) {
        if (arguments.size() != 1) {
            throw new IllegalArgumentException("The sin function requires exactly 1 argument");
        }
        Term number = arguments.get(0);
        if (!number.isUnitless()) {
            throw new IllegalArgumentException("The sin function requires a unitless argument");
        }
        return new Term(Math.cos(number.getValue()), new HashMap<>());
    }
}
