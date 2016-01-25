package com.michaelsnowden.unit_converter;

import java.util.HashMap;
import java.util.List;

/**
 * @author michael.snowden
 */
public class SquareRootFunction implements Function {
    private final UnitsProvider unitsProvider;

    public SquareRootFunction(UnitsProvider unitsProvider) {
        this.unitsProvider = unitsProvider;
    }

    @Override
    public QualifiedNumber evaluate(List<QualifiedNumber> arguments) {
        QualifiedNumber number = arguments.get(0);
        if (!number.isUnitless()) {
            throw new IllegalArgumentException("The number passed into a square root function must be unitless");
        }
        return new QualifiedNumber(Math.sqrt(number.getValue()), new HashMap<>(), unitsProvider);
    }
}
