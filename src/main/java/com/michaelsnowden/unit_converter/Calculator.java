package com.michaelsnowden.unit_converter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author michael.snowden
 */
public class Calculator {

    private FunctionProvider functionProvider;

    public Calculator(FunctionProvider functionProvider) {
        this.functionProvider = functionProvider;
    }

//    public QualifiedNumber calculateTop(ArithmeticParser.ProgramContext context) {
//        return calculate(context.expression());
//    }

    public QualifiedNumber calculate(ArithmeticParser.ExpressionContext context) {
        if (context.op == null) {
            return calculate(context.term());
        }
        if (context.op.getText().equals("+")) {
            return calculate(context.expression(0)).plus(calculate(context.expression(1)));
        } else {
            return calculate(context.expression(0)).minus(calculate(context.expression(1)));
        }
    }

    private QualifiedNumber calculate(ArithmeticParser.TermContext context) {
        if (context.term().size() == 0) {
            return calculate(context.factor()).times(new QualifiedNumber(Math.pow(-1, context.NEG().size())));
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

    private QualifiedNumber calculate(ArithmeticParser.FactorContext context) {
        if (context.number() != null) {
            return calculate(context.number());
        } else if (context.function() != null) {
            return calculate(context.function());
        } else if (context.string() != null) {
            return calculate(context.string());
        } else if (context.expression() != null) {
            return calculate(context.expression());
        } else {
            return calculate(context.factor(0)).raisedTo(calculate(context.factor(1)).times(new QualifiedNumber(Math
                    .pow(-1, context.NEG().size()))));
        }
    }

    private QualifiedNumber calculate(ArithmeticParser.StringContext context) {
        Map<String, Fraction> units = new HashMap<>();
        units.put(context.getText(), new Fraction(1, 1));
        return new QualifiedNumber(1.0, units);
    }

    private QualifiedNumber calculate(ArithmeticParser.FunctionContext context) {
        String identifier = context.IDENTIFIER().getText();
        Function function = functionProvider.getFunction(identifier);
        if (function == null) {
            throw new IllegalStateException(identifier + " is not defined");
        }
        List<QualifiedNumber> arguments = context.expression().stream().map(this::calculate).collect(Collectors
                .toList());
        return function.evaluate(arguments);
    }

    private QualifiedNumber calculate(ArithmeticParser.NumberContext context) {
        Double value;
        if (context.INTEGER() != null) {
            value = (double) Integer.parseInt(context.INTEGER().getText());
        } else {
            value = Double.parseDouble(context.FLOAT().getText());
        }
        return new QualifiedNumber(value, new HashMap<>());
    }
}