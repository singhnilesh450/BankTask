package com.banktask.console;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Scanner;

public final class ConsoleIO {
    private final Scanner scanner = new Scanner(System.in);

    public void printLine(String message) {
        System.out.println(message);
    }

    public void printHeader(String message) {
        System.out.println("============================================================");
        System.out.println(message);
        System.out.println("============================================================");
    }

    public void printFormatted(String format, Object... args) {
        System.out.printf(Locale.ROOT, format + "%n", args);
    }

    public String readLine(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine().trim();
    }

    public int readInt(String prompt) {
        while (true) {
            String value = readLine(prompt);
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException ex) {
                printLine("Please enter a valid integer");
            }
        }
    }

    public BigDecimal readMoney(String prompt) {
        while (true) {
            String value = readLine(prompt);
            try {
                return new BigDecimal(value);
            } catch (NumberFormatException ex) {
                printLine("Please enter a valid decimal value");
            }
        }
    }
}
