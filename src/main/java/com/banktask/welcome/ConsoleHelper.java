package com.banktask.welcome;

import java.util.Locale;
import java.util.Scanner;

public final class ConsoleHelper {
    private static final Scanner SCANNER = new Scanner(System.in);

    private ConsoleHelper() {
        // Utility class
    }

    public static String readLine(String prompt) {
        System.out.println(prompt);
        return SCANNER.nextLine().trim();
    }

    public static int readInt(String prompt) {
        while (true) {
            String input = readLine(prompt);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid integer");
            }
        }
    }

    public static double readDouble(String prompt) {
        while (true) {
            String input = readLine(prompt);
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid number");
            }
        }
    }

    public static void printDivider() {
        System.out.println("------------------------------------------------------------");
    }

    public static void printMessage(String message) {
        System.out.println(message);
    }

    public static void printFormatted(String format, Object... args) {
        System.out.printf(Locale.ROOT, format + "%n", args);
    }
}
