package com.banktask.app;

import com.banktask.entity.Account;
import com.banktask.entity.AccountList;
import com.banktask.entity.BankList;
import com.banktask.welcome.ConsoleHelper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class BankStaff {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("ddMMyyyy");

    private BankStaff() {
        // Utility class
    }

    public static boolean bankStaffFunctionalities(int choice) {
        return switch (choice) {
            case 1 -> {
                createNewAccount();
                yield false;
            }
            case 2 -> {
                updateAccount();
                yield false;
            }
            case 3 -> {
                deleteAccount();
                yield false;
            }
            case 4 -> {
                addCurrencyAndForex();
                yield false;
            }
            case 5 -> {
                addServiceChargeForSameBank();
                yield false;
            }
            case 6 -> {
                addServiceChargeForOtherBank();
                yield false;
            }
            case 7 -> {
                printTransactionHistory();
                yield false;
            }
            case 8 -> {
                revertTransaction();
                yield false;
            }
            case 9 -> true;
            case 10 -> {
                AccountList.displayAcc();
                yield false;
            }
            default -> {
                ConsoleHelper.printMessage("Invalid choice");
                yield false;
            }
        };
    }

    private static void addServiceChargeForOtherBank() {
        String bankId = ConsoleHelper.readLine("Enter Bank ID");
        double rtgs = ConsoleHelper.readDouble("Enter RTGS rate");
        double imps = ConsoleHelper.readDouble("Enter IMPS rate");
        BankList.updateServiceChargeForOtherBank(bankId, rtgs, imps);
    }

    private static void addServiceChargeForSameBank() {
        String bankId = ConsoleHelper.readLine("Enter Bank ID");
        double rtgs = ConsoleHelper.readDouble("Enter RTGS rate");
        double imps = ConsoleHelper.readDouble("Enter IMPS rate");
        BankList.updateServiceChargeForSameBank(bankId, rtgs, imps);
    }

    private static void revertTransaction() {
        String accountId = ConsoleHelper.readLine("Enter Account Id");
        String transactionId = ConsoleHelper.readLine("Enter Transaction ID to be reverted");
        AccountList.transactRevert(accountId, transactionId);
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

    private static void addCurrencyAndForex() {
        String currency = ConsoleHelper.readLine("Enter Currency").toUpperCase(Locale.ROOT);
        double rate = ConsoleHelper.readDouble("Enter Exchange rate");
        Map<String, Double> entry = new HashMap<>();
        entry.put(currency, rate);
        BankList.addCurrencyAndForex(entry);
    }

    private static void deleteAccount() {
        String accountId = ConsoleHelper.readLine("Enter Account ID to be deleted");
        int index = AccountList.getIndexByAccId(accountId);
        if (index == -1) {
            ConsoleHelper.printMessage("Account ID not found");
            return;
        }
        AccountList.deleteAccount(index);
        ConsoleHelper.printMessage("Account successfully deleted");
    }

    private static void updateAccount() {
        String accountId = ConsoleHelper.readLine("Enter Account ID to be updated");
        int index = AccountList.getIndexByAccId(accountId);
        Account account = AccountList.getAccByAccId(accountId);
        if (index == -1 || account == null) {
            ConsoleHelper.printMessage("Account ID not found");
            return;
        }
        String name = ConsoleHelper.readLine("Enter Your Name");
        String bankName = ConsoleHelper.readLine("Enter Bank Name");
        double balance = ConsoleHelper.readDouble("Account Balance");
        String suffix = account.getBankId().substring(Math.min(3, account.getBankId().length()));
        account.setName(name);
        account.setBankName(bankName);
        account.setBalance(balance);
        account.setBankId(prefix(bankName) + suffix);
        account.setAccountId(prefix(name) + suffix);
        AccountList.updateAccount(index, account);
        ConsoleHelper.printMessage("Account successfully updated");
    }

    private static void createNewAccount() {
        String name = ConsoleHelper.readLine("Enter Your Name");
        String bankName = ConsoleHelper.readLine("Enter Bank Name");
        String username = ConsoleHelper.readLine("Give Username");
        String password = ConsoleHelper.readLine("Give Password");
        double balance = ConsoleHelper.readDouble("Account Balance");
        String date = LocalDate.now().format(DATE_FORMAT);
        String bankId = prefix(bankName) + date;
        String accountId = prefix(name) + date;
        Account account = new Account(
                name,
                username,
                password,
                bankName,
                bankId,
                accountId,
                balance,
                new ArrayList<>());
        AccountList.addAccount(account);
        ConsoleHelper.printMessage("Account successfully created");
    }

    private static String prefix(String value) {
        if (value == null || value.isEmpty()) {
            return "ACC";
        }
        String trimmed = value.replaceAll("\\s+", "");
        if (trimmed.isEmpty()) {
            return "ACC";
        }
        return trimmed.substring(0, Math.min(3, trimmed.length())).toUpperCase(Locale.ROOT);
    }
}
