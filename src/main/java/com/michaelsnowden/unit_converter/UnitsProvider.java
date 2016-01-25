package com.michaelsnowden.unit_converter;

import java.util.Map;

/**
 * @author michael.snowden
 */
public interface UnitsProvider {
    Map<String, Fraction> decompose(String unit, Fraction power);

    Double conversionFactor(String key, Fraction value);

    String conversionSymbol(String key);
}
