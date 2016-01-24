package com.michaelsnowden.unit_converter;

/**
 * @author michael.snowden
 */
public class FunctionProviderFactory {
    public FunctionProvider getDefaultFunctionProvider() {
        return new FunctionProviderBuilder()
                .withFunction("cos", new CosineFunction())
                .withFunction("sqrt", new SquareRootFunction())
                .toFunctionProviderImpl();
    }
}