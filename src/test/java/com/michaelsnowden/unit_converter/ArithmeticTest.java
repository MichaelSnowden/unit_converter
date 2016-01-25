package com.michaelsnowden.unit_converter;

import org.antlr.v4.runtime.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * @author michael.snowden
 */
public class ArithmeticTest {
    @Test
    public void testSyntax() throws Exception {
        ArithmeticParser parser = getParser();
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int
                    charPositionInLine, String msg, RecognitionException e) {
                super.syntaxError(recognizer, offendingSymbol, line, charPositionInLine, msg, e);
                throw new IllegalStateException("Syntax error at " + line + ":" + charPositionInLine + " due to " +
                        msg, e);
            }
        });
        parser.expression();
    }

    @Test
    public void testSemantics() throws Exception {
        ArithmeticParser parser = getParser();
        ArithmeticParser.ExpressionContext program = parser.expression();
        UnitsProviderImpl unitsProvider = new UnitsProviderImpl();
        Calculator calculator = new Calculator(new FunctionProviderFactory(unitsProvider).getDefaultFunctionProvider(), unitsProvider);
        QualifiedNumber qualifiedNumber = calculator.calculate(program);
        Assert.assertEquals(qualifiedNumber.getValue(), 2016.0);
    }

    private ArithmeticParser getParser() throws IOException {
        return new ArithmeticParser(new CommonTokenStream(new ArithmeticLexer(new
                ANTLRInputStream(getClass().getClassLoader
                ().getResourceAsStream("test.unit_converter")))));
    }
}
