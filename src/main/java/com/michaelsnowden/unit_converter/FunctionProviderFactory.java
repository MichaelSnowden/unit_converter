package com.michaelsnowden.unit_converter;

/**
 * @author michael.snowden
 */
public class FunctionProviderFactory {
    private final UnitsProvider unitsProvider;

    public FunctionProviderFactory(UnitsProvider unitsProvider) {
        this.unitsProvider = unitsProvider;
    }

    public FunctionProvider getDefaultFunctionProvider() {
        return new FunctionProviderBuilder()
                .withFunction("cos", new CosineFunction())
                .withFunction("sqrt", new SquareRootFunction(unitsProvider))
                .toFunctionProviderImpl();
    }
}