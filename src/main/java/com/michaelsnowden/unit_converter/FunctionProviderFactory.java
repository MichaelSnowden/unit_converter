package com.michaelsnowden.unit_converter;

import com.michaelsnowden.unit_converter.functions.CosineFunction;
import com.michaelsnowden.unit_converter.functions.SinFunction;
import com.michaelsnowden.unit_converter.functions.SquareRootFunction;

/**
 * @author michael.snowden
 */
public class FunctionProviderFactory {
    public FunctionProvider getDefaultFunctionProvider() {
        return new FunctionProviderBuilder()
                .withFunction("cos", new CosineFunction())
                .withFunction("sqrt", new SquareRootFunction())
                .withFunction("sin", new SinFunction())
                .toFunctionProviderImpl();
    }
}