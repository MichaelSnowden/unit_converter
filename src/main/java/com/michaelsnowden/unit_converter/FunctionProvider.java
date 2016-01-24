package com.michaelsnowden.unit_converter;

import java.util.Map;

/**
 * @author michael.snowden
 */
public class FunctionProvider {
    private final Map<String, Function> functionMap;

    public FunctionProvider(Map<String, Function> functionMap) {
        this.functionMap = functionMap;
    }

    public Function getFunction(String key) {
        return functionMap.get(key);
    }
}
