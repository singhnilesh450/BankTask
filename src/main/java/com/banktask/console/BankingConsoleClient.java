package com.banktask.console;

import com.banktask.domain.account.AccountProfile;
import com.banktask.domain.account.TransactionRecord;
import com.banktask.service.AccountService;
import com.banktask.service.BankService;
import com.banktask.service.exceptions.BankingException;
import com.banktask.service.model.CreateAccountRequest;
import com.banktask.service.model.TransferReceipt;
import com.banktask.service.model.TransferRequest;
import com.banktask.service.model.UpdateAccountRequest;
import java.math.BigDecimal;
import java.util.List;

public final class BankingConsoleClient {
    private final ConsoleIO io;
    private final BankService bankService;
    private final AccountService accountService;

    public BankingConsoleClient(ConsoleIO io, BankService bankService, AccountService accountService) {
        this.io = io;
        this.bankService = bankService;
        this.accountService = accountService;
    }

    public void launch() {
        io.printHeader("Welcome to the Enterprise Banking Console");
        boolean running = true;
        while (running) {
            io.printLine("Please choose an option:");
            io.printLine("1. Bank Staff Portal");
            io.printLine("2. Account Holder Portal");
            io.printLine("3. Exit");
            int choice = io.readInt("Choice:");
            switch (choice) {
                case 1 -> handleBankStaffSession();
                case 2 -> handleAccountHolderSession();
                case 3 -> running = false;
                default -> io.printLine("Please choose a valid option.");
            }
        }
        io.printLine("Thank you for banking with us. Goodbye!");
    }

    private void handleBankStaffSession() {
        String username = io.readLine("Enter staff username");
        String password = io.readLine("Enter staff password");
        if (!bankService.authenticateStaff(username, password)) {
            io.printLine("Invalid credentials. Returning to main menu.");
            return;
        }
        boolean exit = false;
        while (!exit) {
            io.printHeader("Bank Staff Operations");
            io.printLine("1. Create account");
            io.printLine("2. Update account");
            io.printLine("3. Delete account");
            io.printLine("4. Register currency and forex rate");
            io.printLine("5. Update same bank charges");
            io.printLine("6. Update other bank charges");
            io.printLine("7. View transaction history");
            io.printLine("8. Revert transaction");
            io.printLine("9. View account summaries");
            io.printLine("10. Exit");
            int choice = io.readInt("Choice:");
            switch (choice) {
                case 1 -> createAccount();
                case 2 -> updateAccount();
                case 3 -> deleteAccount();
                case 4 -> registerCurrency();
                case 5 -> updateSameBankCharges();
                case 6 -> updateOtherBankCharges();
                case 7 -> viewTransactions();
                case 8 -> revertTransaction();
                case 9 -> displayAccountSummaries();
                case 10 -> exit = true;
                default -> io.printLine("Invalid option. Try again.");
            }
        }
    }

    private void handleAccountHolderSession() {
        String username = io.readLine("Enter account username");
        String password = io.readLine("Enter account password");
        if (!accountService.authenticateAccountHolder(username, password)) {
            io.printLine("Invalid credentials. Returning to main menu.");
            return;
        }
        boolean exit = false;
        while (!exit) {
            io.printHeader("Account Holder Operations");
            io.printLine("1. Deposit funds");
            io.printLine("2. Withdraw funds");
            io.printLine("3. Transfer funds");
            io.printLine("4. View transaction history");
            io.printLine("5. Exit");
            int choice = io.readInt("Choice:");
            switch (choice) {
                case 1 -> depositFunds(username);
                case 2 -> withdrawFunds(username);
                case 3 -> transferFunds(username);
                case 4 -> displayTransactions();
                case 5 -> exit = true;
                default -> io.printLine("Invalid option. Try again.");
            }
        }
    }

    private void createAccount() {
        String name = io.readLine("Enter account holder name");
        String bankName = io.readLine("Enter bank name");
        String bankId = io.readLine("Enter bank id");
        String username = io.readLine("Choose username");
        String password = io.readLine("Choose password");
        BigDecimal openingBalance = io.readMoney("Opening balance");
        try {
            AccountProfile profile = accountService.createAccount(new CreateAccountRequest(
                    name,
                    username,
                    password,
                    bankName,
                    bankId,
                    openingBalance)).join();
            io.printFormatted("Account %s created with id %s", profile.getUserName(), profile.getAccountId());
        } catch (BankingException ex) {
            io.printLine(ex.getMessage());
        }
    }

    private void updateAccount() {
        String accountId = io.readLine("Enter account id to update");
        String name = io.readLine("Enter new account holder name");
        String bankName = io.readLine("Enter bank name");
        BigDecimal balance = io.readMoney("Updated balance");
        try {
            AccountProfile profile = accountService.updateAccount(new UpdateAccountRequest(
                    accountId,
                    name,
                    bankName,
                    balance)).join();
            io.printFormatted("Account %s updated. Current balance: %s", profile.getAccountId(), profile.getBalance());
        } catch (BankingException ex) {
            io.printLine(ex.getMessage());
        }
    }

    private void deleteAccount() {
        String accountId = io.readLine("Enter account id to delete");
        try {
            accountService.deleteAccount(accountId).join();
            io.printLine("Account deleted successfully");
        } catch (BankingException ex) {
            io.printLine(ex.getMessage());
        }
    }

    private void registerCurrency() {
        String currency = io.readLine("Enter currency (ISO code)").toUpperCase();
        BigDecimal rate = io.readMoney("Enter forex rate against INR");
        bankService.registerCurrencyRate(currency, rate);
        io.printLine("Currency registered");
    }

    private void updateSameBankCharges() {
        String bankId = io.readLine("Enter bank id");
        BigDecimal rtgs = io.readMoney("Enter RTGS charge for same bank");
        BigDecimal imps = io.readMoney("Enter IMPS charge for same bank");
        try {
            bankService.updateSameBankCharges(bankId, rtgs, imps);
            io.printLine("Charges updated successfully");
        } catch (BankingException ex) {
            io.printLine(ex.getMessage());
        }
    }

    private void updateOtherBankCharges() {
        String bankId = io.readLine("Enter bank id");
        BigDecimal rtgs = io.readMoney("Enter RTGS charge for other bank");
        BigDecimal imps = io.readMoney("Enter IMPS charge for other bank");
        try {
            bankService.updateOtherBankCharges(bankId, rtgs, imps);
            io.printLine("Charges updated successfully");
        } catch (BankingException ex) {
            io.printLine(ex.getMessage());
        }
    }

    private void viewTransactions() {
        String accountId = io.readLine("Enter account id");
        List<TransactionRecord> transactions = accountService.fetchTransactionHistory(accountId);
        if (transactions.isEmpty()) {
            io.printLine("No transactions available");
            return;
        }
        transactions.forEach(record -> io.printLine(record.toConsoleString()));
    }

    private void revertTransaction() {
        String accountId = io.readLine("Enter account id");
        String transactionId = io.readLine("Enter transaction id to revert");
        try {
            boolean reverted = accountService.revertTransaction(accountId, transactionId).join();
            if (reverted) {
                io.printLine("Transaction reverted successfully");
            } else {
                io.printLine("Transaction not found");
            }
        } catch (BankingException ex) {
            io.printLine(ex.getMessage());
        }
    }

    private void displayAccountSummaries() {
        io.printHeader("Account Summaries");
        accountService.listAccounts().forEach(profile ->
                io.printFormatted("%s (%s) - Balance: %s", profile.getUserName(), profile.getBankName(), profile.getBalance()));
    }

    private void depositFunds(String username) {
        String currency = io.readLine("Enter currency code");
        BigDecimal amount = io.readMoney("Amount");
        try {
            TransactionRecord record = accountService.deposit(username, currency, amount).join();
            io.printFormatted("Deposit successful. Transaction id: %s. Balance: %s", record.id(), record.resultingBalance());
        } catch (BankingException ex) {
            io.printLine(ex.getMessage());
        }
    }

    private void withdrawFunds(String username) {
        BigDecimal amount = io.readMoney("Amount in INR");
        try {
            TransactionRecord record = accountService.withdraw(username, amount).join();
            io.printFormatted("Withdrawal successful. Transaction id: %s. Balance: %s", record.id(), record.resultingBalance());
        } catch (BankingException ex) {
            io.printLine(ex.getMessage());
        }
    }

    private void transferFunds(String username) {
        String targetAccountId = io.readLine("Target account id");
        BigDecimal amount = io.readMoney("Amount in INR");
        try {
            TransferReceipt receipt = accountService.transfer(new TransferRequest(username, targetAccountId, amount)).join();
            io.printFormatted("Transfer successful. Transaction id: %s. Balance: %s", receipt.sourceRecord().id(), receipt.sourceRecord().resultingBalance());
        } catch (BankingException ex) {
            io.printLine(ex.getMessage());
        }
    }

    private void displayTransactions() {
        String accountId = io.readLine("Enter account id");
        List<TransactionRecord> transactions = accountService.fetchTransactionHistory(accountId);
        if (transactions.isEmpty()) {
            io.printLine("No transactions available");
            return;
        }
        transactions.forEach(record -> io.printLine(record.toConsoleString()));
    }
}
