package com.banktask.app;

import com.banktask.entity.AccountList;
import com.banktask.welcome.ConsoleHelper;

import java.util.List;

public final class AccHolder {
    private AccHolder() {
        // Utility class
    }

    public static boolean accHolderFunctionalities(int choice, String username) {
        return switch (choice) {
            case 1 -> {
                depositMoney(username);
                yield false;
            }
            case 2 -> {
                withdrawMoney(username);
                yield false;
            }
            case 3 -> {
                transferFunds(username);
                yield false;
            }
            case 4 -> {
                printTransactionHistory();
                yield false;
            }
            case 5 -> true;
            default -> {
                ConsoleHelper.printMessage("Invalid choice");
                yield false;
            }
        };
    }

    private static void transferFunds(String username) {
        String targetAccountId = ConsoleHelper.readLine("Enter Target Account Id");
        double amount = ConsoleHelper.readDouble("Enter Amount in INR only");
        AccountList.transfer(targetAccountId, amount, username);
    }

    private static void withdrawMoney(String username) {
        double amount = ConsoleHelper.readDouble("Enter Amount in INR only");
        AccountList.withdraw(amount, username);
    }

    private static void printTransactionHistory() {
        String accountId = ConsoleHelper.readLine("Enter Account ID");
        List<String> history = AccountList.getMatchedTransactionHistory(accountId);
        if (history.isEmpty()) {
            ConsoleHelper.printMessage("No such acc_id exist");
            return;
        }
        history.forEach(ConsoleHelper::printMessage);
    }

    private static void depositMoney(String username) {
        String currency = ConsoleHelper.readLine("Enter Currency");
        double amount = ConsoleHelper.readDouble("Enter Amount");
        AccountList.deposit(currency, amount, username);
    }
}
