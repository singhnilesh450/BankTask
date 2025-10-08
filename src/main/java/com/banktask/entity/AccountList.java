package com.banktask.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public final class AccountList {
    private static final List<Account> ACCOUNTS = new ArrayList<>();
    private static final DateTimeFormatter TXN_DATE = DateTimeFormatter.ofPattern("ddMMyyyy");

    private AccountList() {
        // Utility class
    }

    public static void init() {
        if (!ACCOUNTS.isEmpty()) {
            return;
        }
        List<String> transactions = new ArrayList<>();
        transactions.add("TXNPNS10102012ANS1010201220120229 +123");
        transactions.add("TXNKNU10102012NSJ1010201222121223 -233");

        ACCOUNTS.add(new Account(
                "Nilesh",
                "123@abc",
                "123",
                "SBI",
                "SBI10102011",
                "NIL10102011",
                1281.10,
                transactions));
        ACCOUNTS.add(new Account(
                "Singh",
                "111@abc",
                "111",
                "SBI",
                "SBI10102011",
                "SIN10102011",
                1828.10,
                transactions));
    }

    public static void addAccount(Account account) {
        ACCOUNTS.add(account);
    }

    public static void deleteAccount(int index) {
        if (index < 0 || index >= ACCOUNTS.size()) {
            System.out.println("Account not found");
            return;
        }
        ACCOUNTS.remove(index);
    }

    public static void updateAccount(int index, Account account) {
        if (index < 0 || index >= ACCOUNTS.size()) {
            System.out.println("Account not found");
            return;
        }
        ACCOUNTS.set(index, account);
    }

    public static void deposit(String currency, double amount, String username) {
        Account account = findAccountByUsername(username);
        if (account == null) {
            System.out.println("Account not found");
            return;
        }
        double exchangeRate = BankList.getExchangeRate(account.getBankId(), currency);
        if (exchangeRate == -1) {
            System.out.println("Currency is Not Valid Or Accepted");
            return;
        }
        double convertedAmount = amount * exchangeRate;
        account.setBalance(account.getBalance() + convertedAmount);
        account.getTransactionHistory().add(createTransactionId(account) + " +" + formatAmount(convertedAmount));
    }

    public static void withdraw(double amount, String username) {
        Account account = findAccountByUsername(username);
        if (account == null) {
            System.out.println("Account not found");
            return;
        }
        if (account.getBalance() < amount) {
            System.out.println("Balance is not sufficient");
            return;
        }
        account.setBalance(account.getBalance() - amount);
        account.getTransactionHistory().add(createTransactionId(account) + " -" + formatAmount(amount));
    }

    public static void transfer(String targetAccountId, double amount, String username) {
        Account source = findAccountByUsername(username);
        if (source == null) {
            System.out.println("Source account not found");
            return;
        }
        if (source.getBalance() < amount) {
            System.out.println("Amount Insufficient");
            return;
        }
        Account target = getAccByAccId(targetAccountId);
        if (target == null) {
            System.out.println("Target Account Id Incorrect");
            return;
        }
        source.setBalance(source.getBalance() - amount);
        target.setBalance(target.getBalance() + amount);
        String transactionId = createTransactionId(source);
        source.getTransactionHistory().add(transactionId + " transfer " + formatAmount(amount));
        target.getTransactionHistory().add(transactionId + " received " + formatAmount(amount));
    }

    public static boolean checkUserCredentials(String username, String password) {
        return ACCOUNTS.stream()
                .anyMatch(account -> account.getUserName().equals(username)
                        && account.getPassword().equals(password));
    }

    public static int getIndexByAccId(String accountId) {
        for (int i = 0; i < ACCOUNTS.size(); i++) {
            if (ACCOUNTS.get(i).getAccountId().equals(accountId)) {
                return i;
            }
        }
        return -1;
    }

    public static Account getAccByAccId(String accountId) {
        Optional<Account> account = ACCOUNTS.stream()
                .filter(ac -> ac.getAccountId().equals(accountId))
                .findFirst();
        return account.orElse(null);
    }

    public static boolean transactRevert(String accountId, String transactionId) {
        Account account = getAccByAccId(accountId);
        if (account == null) {
            System.out.println("Account not found");
            return false;
        }
        List<String> history = account.getTransactionHistory();
        for (int i = 0; i < history.size(); i++) {
            String entry = history.get(i);
            if (entry.startsWith(transactionId)) {
                double originalChange = getBalanceImpact(entry);
                account.setBalance(account.getBalance() - originalChange);
                history.remove(i);
                System.out.println("Transaction reverted");
                return true;
            }
        }
        System.out.println("Transaction id not found");
        return false;
    }

    public static List<String> getMatchedTransactionHistory(String accountId) {
        Account account = getAccByAccId(accountId);
        if (account == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(account.getTransactionHistory());
    }

    public static void displayAcc() {
        System.out.println("Accounts summary:");
        for (Account account : ACCOUNTS) {
            System.out.printf(Locale.ROOT, "%s (%s) - Balance: %.2f%n",
                    account.getUserName(), account.getBankName(), account.getBalance());
        }
    }

    public static void transferFunds(int sourceIndex, int destinationIndex, double amount) {
        if (sourceIndex < 0 || sourceIndex >= ACCOUNTS.size() ||
                destinationIndex < 0 || destinationIndex >= ACCOUNTS.size()) {
            System.out.println("Invalid account selection");
            return;
        }
        Account source = ACCOUNTS.get(sourceIndex);
        Account destination = ACCOUNTS.get(destinationIndex);
        if (source.getBalance() < amount) {
            System.out.println("Amount Insufficient");
            return;
        }
        source.setBalance(source.getBalance() - amount);
        destination.setBalance(destination.getBalance() + amount);
        source.getTransactionHistory().add(createTransactionId(source) + " -" + formatAmount(amount));
        destination.getTransactionHistory().add(createTransactionId(destination) + " +" + formatAmount(amount));
    }

    private static Account findAccountByUsername(String username) {
        return ACCOUNTS.stream()
                .filter(account -> account.getUserName().equals(username))
                .findFirst()
                .orElse(null);
    }

    private static String createTransactionId(Account account) {
        return "TXN" + account.getBankId() + account.getAccountId() + LocalDate.now().format(TXN_DATE);
    }

    private static double getBalanceImpact(String entry) {
        String[] parts = entry.split("\\s+");
        if (parts.length < 2) {
            return 0;
        }
        String descriptor = parts[1];
        try {
            if (descriptor.startsWith("+") || descriptor.startsWith("-")) {
                double amount = Double.parseDouble(descriptor.substring(1));
                return descriptor.startsWith("+") ? amount : -amount;
            }
            if ("transfer".equalsIgnoreCase(descriptor) && parts.length >= 3) {
                return -Double.parseDouble(parts[2]);
            }
            if ("received".equalsIgnoreCase(descriptor) && parts.length >= 3) {
                return Double.parseDouble(parts[2]);
            }
        } catch (NumberFormatException ex) {
            System.out.println("Unable to parse transaction amount for entry: " + entry);
        }
        return 0;
    }

    private static String formatAmount(double amount) {
        return String.format(Locale.ROOT, "%.2f", amount);
    }
}
