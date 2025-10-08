package com.banktask.service;

import com.banktask.domain.account.AccountProfile;
import com.banktask.domain.account.TransactionRecord;
import com.banktask.domain.account.AccountProfile.TransferEntries;
import com.banktask.domain.bank.BankProfile;
import com.banktask.infrastructure.account.AccountDirectory;
import com.banktask.infrastructure.bank.BankDirectory;
import com.banktask.service.exceptions.BankingException;
import com.banktask.service.exceptions.NotFoundException;
import com.banktask.service.exceptions.ValidationException;
import com.banktask.service.model.CreateAccountRequest;
import com.banktask.service.model.TransferReceipt;
import com.banktask.service.model.TransferRequest;
import com.banktask.service.model.UpdateAccountRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public final class AccountService {
    private final AccountDirectory accountDirectory;
    private final BankDirectory bankDirectory;
    private final ExecutorService executorService;

    public AccountService(AccountDirectory accountDirectory, BankDirectory bankDirectory, ExecutorService executorService) {
        this.accountDirectory = accountDirectory;
        this.bankDirectory = bankDirectory;
        this.executorService = executorService;
    }

    public boolean authenticateAccountHolder(String username, String password) {
        return accountDirectory.findByUsername(username)
                .map(profile -> profile.passwordMatches(password))
                .orElse(false);
    }

    public CompletableFuture<AccountProfile> createAccount(CreateAccountRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            if (accountDirectory.existsByUsername(request.username())) {
                throw new ValidationException("Username already exists: " + request.username());
            }
            BankProfile bank = locateBank(request.bankId(), request.bankName());
            AccountProfile profile = new AccountProfile(
                    request.holderName(),
                    request.username(),
                    request.password(),
                    bank.getBankName(),
                    bank.getBankId(),
                    request.openingBalance());
            accountDirectory.register(profile);
            return profile;
        }, executorService);
    }

    public CompletableFuture<AccountProfile> updateAccount(UpdateAccountRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            AccountProfile profile = accountDirectory.findById(request.accountId())
                    .orElseThrow(() -> new NotFoundException("Account not found: " + request.accountId()));
            profile.updateProfile(request.holderName(), request.bankName(), request.balance());
            return profile;
        }, executorService);
    }

    public CompletableFuture<Void> deleteAccount(String accountId) {
        return CompletableFuture.runAsync(() -> {
            if (accountDirectory.findById(accountId).isEmpty()) {
                throw new NotFoundException("Account not found: " + accountId);
            }
            accountDirectory.delete(accountId);
        }, executorService);
    }

    public CompletableFuture<TransactionRecord> deposit(String username, String currency, BigDecimal amount) {
        return CompletableFuture.supplyAsync(() -> {
            validateAmount(amount);
            AccountProfile profile = accountDirectory.findByUsername(username)
                    .orElseThrow(() -> new NotFoundException("Account not found for username: " + username));
            BankProfile bank = bankDirectory.findById(profile.getBankId())
                    .orElseThrow(() -> new BankingException("Bank not found: " + profile.getBankId()));
            BigDecimal rate = bank.exchangeRateFor(currency)
                    .orElseThrow(() -> new BankingException("Currency not supported: " + currency));
            BigDecimal credited = amount.multiply(rate).setScale(2, RoundingMode.HALF_EVEN);
            return profile.deposit(credited, String.format("Deposit via %s", currency.toUpperCase()));
        }, executorService);
    }

    public CompletableFuture<TransactionRecord> withdraw(String username, BigDecimal amount) {
        return CompletableFuture.supplyAsync(() -> {
            validateAmount(amount);
            AccountProfile profile = accountDirectory.findByUsername(username)
                    .orElseThrow(() -> new NotFoundException("Account not found for username: " + username));
            BigDecimal balance = profile.getBalance();
            if (balance.compareTo(amount) < 0) {
                throw new BankingException("Insufficient balance");
            }
            return profile.withdraw(amount, "Cash withdrawal");
        }, executorService);
    }

    public CompletableFuture<TransferReceipt> transfer(TransferRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            validateAmount(request.amount());
            AccountProfile source = accountDirectory.findByUsername(request.sourceUsername())
                    .orElseThrow(() -> new NotFoundException("Account not found for username: " + request.sourceUsername()));
            AccountProfile target = accountDirectory.findById(request.targetAccountId())
                    .orElseThrow(() -> new NotFoundException("Target account not found: " + request.targetAccountId()));
            if (source.getAccountId().equals(target.getAccountId())) {
                throw new ValidationException("Cannot transfer to the same account");
            }
            try {
                TransferEntries entries = AccountProfile.transfer(source, target, request.amount());
                return new TransferReceipt(entries.sourceRecord(), entries.targetRecord());
            } catch (IllegalStateException ex) {
                throw new BankingException(ex.getMessage(), ex);
            }
        }, executorService);
    }

    public List<TransactionRecord> fetchTransactionHistory(String accountId) {
        return accountDirectory.findById(accountId)
                .map(AccountProfile::historySnapshot)
                .orElse(List.of());
    }

    public CompletableFuture<Boolean> revertTransaction(String accountId, String transactionId) {
        return CompletableFuture.supplyAsync(() -> accountDirectory.findById(accountId)
                .flatMap(profile -> profile.revert(transactionId))
                .isPresent(), executorService);
    }

    public List<AccountProfile> listAccounts() {
        return List.copyOf(accountDirectory.snapshot());
    }

    private BankProfile locateBank(String bankId, String bankName) {
        if (bankId != null && !bankId.isBlank()) {
            return bankDirectory.findById(bankId)
                    .orElseThrow(() -> new BankingException("Bank not found: " + bankId));
        }
        return bankDirectory.findByName(bankName)
                .orElseThrow(() -> new BankingException("Bank not found: " + bankName));
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Amount must be positive");
        }
    }
}
