package com.michaelsnowden.unit_converter;

import java.util.List;

/**
 * @author michael.snowden
 */
public interface Function {
    QualifiedNumber evaluate(List<QualifiedNumber> arguments);
}
