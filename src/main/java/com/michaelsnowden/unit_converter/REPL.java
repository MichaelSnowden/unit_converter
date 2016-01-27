package com.michaelsnowden.unit_converter;

import jline.console.ConsoleReader;

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
                Calculator calculator = new Calculator();
                console.println(calculator.calculate(line).toString());
            } catch (IllegalArgumentException e) {
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
