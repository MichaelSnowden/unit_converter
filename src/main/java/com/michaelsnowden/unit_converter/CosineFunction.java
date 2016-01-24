package com.michaelsnowden.unit_converter;

import java.util.List;

/**
 * @author michael.snowden
 */
public class CosineFunction implements Function {
    @Override
    public QualifiedNumber evaluate(List<QualifiedNumber> arguments) {
        QualifiedNumber number = arguments.get(0);
        if (!number.isUnitless()) {
            throw new IllegalArgumentException("The number passed into a cosine function must be unitless");
        }
        return number.cosine();
    }
}
