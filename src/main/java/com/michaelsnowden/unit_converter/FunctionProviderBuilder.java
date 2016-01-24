package com.michaelsnowden.unit_converter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author michael.snowden
 */
public class FunctionProviderBuilder {
    private Map<String, Function> functionMap = new HashMap<>();

    public FunctionProviderBuilder withFunction(String key, Function function) {
        functionMap.put(key, function);
        return this;
    }

    public FunctionProvider toFunctionProviderImpl() {
        return new FunctionProvider(functionMap);
    }
}
