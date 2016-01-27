package com.michaelsnowden.unit_converter;

import java.util.List;

/**
 * @author michael.snowden
 */
public interface Function {
    Term evaluate(List<Term> arguments);
}
