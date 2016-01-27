package com.michaelsnowden.unit_converter;

/**
 * @author michael.snowden
 */
public class FunctionProviderFactory {
    private final AbstractSymbolLookup unitsProvider;

    public FunctionProviderFactory(AbstractSymbolLookup unitsProvider) {
        this.unitsProvider = unitsProvider;
    }

    public FunctionProvider getDefaultFunctionProvider() {
        return new FunctionProviderBuilder()
                .withFunction("cos", new CosineFunction(unitsProvider))
                .withFunction("sqrt", new SquareRootFunction(unitsProvider))
                .withFunction("sin", new SinFunction(unitsProvider))
                .toFunctionProviderImpl();
    }
}