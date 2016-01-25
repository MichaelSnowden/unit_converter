package com.michaelsnowden.unit_converter;

import jline.console.ConsoleReader;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.misc.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @author michael.snowden
 */
public class REPL {
    public static void main(String[] args) throws SQLException, IOException {
        ConsoleReader console = new ConsoleReader();
        console.setPrompt("uc> ");

        String line = console.readLine();

        while (line != null && !line.trim().equals("quit")) {
            try {
                BaseErrorListener errorListener = new BaseErrorListener() {
                    @Override
                    public void syntaxError(@NotNull Recognizer<?, ?> recognizer, @Nullable Object offendingSymbol,
                                            int line, int charPositionInLine, @NotNull String msg, @Nullable
                                            RecognitionException e) {
                        throw new IllegalArgumentException("line " + line + ":" + charPositionInLine + " " + msg);
                    }
                };
                ArithmeticLexer lexer = new ArithmeticLexer(new
                        ANTLRInputStream(new ByteArrayInputStream(line.getBytes())));
                lexer.removeErrorListeners();
                lexer.addErrorListener(errorListener);
                ArithmeticParser parser = new ArithmeticParser(new CommonTokenStream(lexer));
                parser.removeErrorListeners();
                parser.addErrorListener(errorListener);
                ArithmeticParser.ExpressionContext expression = parser.expression();
                Calculator calculator = new Calculator(new FunctionProviderFactory(new UnitsProviderImpl()).getDefaultFunctionProvider(),
                        new UnitsProviderImpl());
                console.println(calculator.calculate(expression).toString());
            } catch (Exception e) {
                String message = e.getMessage();
                if (message == null) {
                    console.println("An unknown error occurred");
                } else {
                    console.println(message);
                }
            }
            line = console.readLine();
        }
    }
}
