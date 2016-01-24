package com.michaelsnowden.unit_converter;

import java.util.List;

/**
 * @author michael.snowden
 */
public class SquareRootFunction implements Function {
    @Override
    public QualifiedNumber evaluate(List<QualifiedNumber> arguments) {
        QualifiedNumber number = arguments.get(0);
        if (!number.isUnitless()) {
            throw new IllegalArgumentException("The number passed into a square root function must be unitless");
        }
        return new QualifiedNumber(Math.sqrt(number.getValue()));
    }
}
