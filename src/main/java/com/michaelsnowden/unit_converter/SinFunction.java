package com.michaelsnowden.unit_converter;

import java.util.HashMap;
import java.util.List;

/**
 * @author michael.snowden
 */
public class SinFunction implements Function {
    private final UnitsProvider unitsProvider;

    public SinFunction(UnitsProvider unitsProvider) {
        this.unitsProvider = unitsProvider;
    }

    @Override
    public QualifiedNumber evaluate(List<QualifiedNumber> arguments) {
        if (arguments.size() != 1) {
            throw new IllegalArgumentException("The sin function requires exactly 1 argument");
        }
        QualifiedNumber number = arguments.get(0);
        if (!number.isUnitless()) {
            throw new IllegalArgumentException("The sin function requires a unitless argument");
        }
        return new QualifiedNumber(Math.sin(number.getValue()), new HashMap<>(), unitsProvider);
    }
}
