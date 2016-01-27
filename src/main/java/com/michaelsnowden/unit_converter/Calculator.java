package com.michaelsnowden.unit_converter;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.misc.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author michael.snowden
 */
public class Calculator {

    private final FunctionProvider functionProvider;
    private final SymbolLookup unitsProvider;

    public Calculator() {
        this(new FunctionProviderFactory().getDefaultFunctionProvider(), new SymbolLookupImpl
                ());
    }

    public Calculator(FunctionProvider functionProvider) {
        this(functionProvider, new SymbolLookupImpl());
    }

    public Calculator(SymbolLookup unitsProvider) {
        this(new FunctionProviderFactory().getDefaultFunctionProvider(), unitsProvider);
    }

    public Calculator(FunctionProvider functionProvider, SymbolLookup unitsProvider) {
        this.functionProvider = functionProvider;
        this.unitsProvider = unitsProvider;
    }

    public Term calculate(String string) throws IOException {
        BaseErrorListener errorListener = new BaseErrorListener() {
            @Override
            public void syntaxError(@NotNull Recognizer<?, ?> recognizer, @Nullable Object offendingSymbol,
                                    int line, int charPositionInLine, @NotNull String msg, @Nullable
                                    RecognitionException e) {
                throw new IllegalArgumentException("line " + line + ":" + charPositionInLine + " " + msg);
            }
        };
        ArithmeticLexer lexer = new ArithmeticLexer(new
                ANTLRInputStream(new ByteArrayInputStream(string.getBytes())));
        lexer.removeErrorListeners();
        lexer.addErrorListener(errorListener);
        ArithmeticParser parser = new ArithmeticParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);
        return calculate(parser.expression());
    }

    public Term calculate(ArithmeticParser.ExpressionContext context) {
        if (context.op == null) {
            return calculate(context.term());
        }
        if (context.op.getText().equals("+")) {
            return calculate(context.expression(0)).plus(calculate(context.expression(1)));
        } else {
            return calculate(context.expression(0)).minus(calculate(context.expression(1)));
        }
    }

    private Term calculate(ArithmeticParser.TermContext context) {
        if (context.term().size() == 0) {
            return calculate(context.factor()).times(new Term(Math.pow(-1, context.NEG().size()), new
                    HashMap<>()));
        }
        if (context.op == null) {
            return calculate(context.term(0)).times(calculate(context.factor()));
        }
        if (context.op.getText().equals("*")) {
            return calculate(context.term(0)).times(calculate(context.term(1)));
        } else {
            return calculate(context.term(0)).dividedBy(calculate(context.term(1)));
        }
    }

    private Term calculate(ArithmeticParser.FactorContext context) {
        if (context.number() != null) {
            return calculate(context.number());
        } else if (context.function() != null) {
            return calculate(context.function());
        } else if (context.string() != null) {
            return calculate(context.string());
        } else if (context.expression() != null) {
            return calculate(context.expression());
        } else {
            return calculate(context.factor(0)).raisedTo(calculate(context.factor(1)).times(new Term(Math
                    .pow(-1, context.NEG().size()), new HashMap<>())));
        }
    }

    private Term calculate(ArithmeticParser.StringContext context) {
        return unitsProvider.lookup(context.getText());
    }

    private Term calculate(ArithmeticParser.FunctionContext context) {
        String identifier = context.IDENTIFIER().getText();
        Function function = functionProvider.getFunction(identifier);
        if (function == null) {
            throw new IllegalStateException(identifier + " is not defined");
        }
        List<Term> arguments = context.expression()
                .stream()
                .map(this::calculate)
                .collect(Collectors.toList());
        return function.evaluate(arguments);
    }

    private Term calculate(ArithmeticParser.NumberContext context) {
        Double value;
        if (context.INTEGER() != null) {
            value = (double) Integer.parseInt(context.INTEGER().getText());
        } else {
            value = Double.parseDouble(context.FLOAT().getText());
        }
        return new Term(value, new HashMap<>());
    }
}
