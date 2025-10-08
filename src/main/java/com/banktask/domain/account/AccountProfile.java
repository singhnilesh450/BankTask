package com.banktask.domain.account;

import com.banktask.util.IdentifierGenerator;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

public final class AccountProfile {
    private final String accountId;
    private final String bankId;
    private final String userName;
    private final ReentrantLock lock = new ReentrantLock();
    private final List<TransactionRecord> history = new ArrayList<>();

    private volatile String holderName;
    private volatile String bankName;
    private volatile String password;
    private BigDecimal balance;

    public AccountProfile(
            String holderName,
            String userName,
            String password,
            String bankName,
            String bankId,
            BigDecimal openingBalance) {
        this.holderName = holderName;
        this.userName = userName;
        this.password = password;
        this.bankName = bankName;
        this.bankId = bankId;
        this.accountId = IdentifierGenerator.accountId(holderName, bankId);
        this.balance = openingBalance;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getBankId() {
        return bankId;
    }

    public String getUserName() {
        return userName;
    }

    public String getHolderName() {
        return holderName;
    }

    public String getBankName() {
        return bankName;
    }

    public String getPassword() {
        return password;
    }

    public BigDecimal getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }

    public boolean passwordMatches(String candidate) {
        return Objects.equals(password, candidate);
    }

    public void updateProfile(String newHolderName, String newBankName, BigDecimal newBalance) {
        lock.lock();
        try {
            this.holderName = newHolderName;
            this.bankName = newBankName;
            this.balance = newBalance;
        } finally {
            lock.unlock();
        }
    }

    public TransactionRecord deposit(BigDecimal amount, String description) {
        lock.lock();
        try {
            balance = balance.add(amount);
            TransactionRecord record = new TransactionRecord(
                    transactionId(),
                    TransactionType.DEPOSIT,
                    amount,
                    balance,
                    description,
                    Instant.now());
            history.add(record);
            return record;
        } finally {
            lock.unlock();
        }
    }

    public TransactionRecord withdraw(BigDecimal amount, String description) {
        lock.lock();
        try {
            balance = balance.subtract(amount);
            TransactionRecord record = new TransactionRecord(
                    transactionId(),
                    TransactionType.WITHDRAWAL,
                    amount.negate(),
                    balance,
                    description,
                    Instant.now());
            history.add(record);
            return record;
        } finally {
            lock.unlock();
        }
    }

    public Optional<TransactionRecord> revert(String transactionId) {
        lock.lock();
        try {
            for (int i = 0; i < history.size(); i++) {
                TransactionRecord record = history.get(i);
                if (record.id().equals(transactionId)) {
                    history.remove(i);
                    balance = balance.subtract(record.delta());
                    TransactionRecord reversal = new TransactionRecord(
                            transactionId(),
                            TransactionType.REVERSAL,
                            record.delta().negate(),
                            balance,
                            "Reversal of " + transactionId,
                            Instant.now());
                    history.add(reversal);
                    return Optional.of(reversal);
                }
            }
            return Optional.empty();
        } finally {
            lock.unlock();
        }
    }

    public List<TransactionRecord> historySnapshot() {
        lock.lock();
        try {
            return List.copyOf(history);
        } finally {
            lock.unlock();
        }
    }

    public static TransferEntries transfer(AccountProfile source, AccountProfile target, BigDecimal amount) {
        Objects.requireNonNull(target, "target");
        if (source == target) {
            throw new IllegalArgumentException("Source and target account cannot be same");
        }
        AccountProfile first = source;
        AccountProfile second = target;
        if (System.identityHashCode(first) > System.identityHashCode(second)) {
            first = target;
            second = source;
        }
        first.lock.lock();
        second.lock.lock();
        try {
            if (source.balance.compareTo(amount) < 0) {
                throw new IllegalStateException("Insufficient balance");
            }
            source.balance = source.balance.subtract(amount);
            TransactionRecord sourceRecord = new TransactionRecord(
                    source.transactionId(),
                    TransactionType.TRANSFER_OUT,
                    amount.negate(),
                    source.balance,
                    String.format(Locale.ROOT, "Transfer to %s", target.getAccountId()),
                    Instant.now());
            source.history.add(sourceRecord);

            target.balance = target.balance.add(amount);
            TransactionRecord targetRecord = new TransactionRecord(
                    target.transactionId(),
                    TransactionType.TRANSFER_IN,
                    amount,
                    target.balance,
                    String.format(Locale.ROOT, "Transfer from %s", source.getAccountId()),
                    Instant.now());
            target.history.add(targetRecord);
            return new TransferEntries(sourceRecord, targetRecord);
        } finally {
            second.lock.unlock();
            first.lock.unlock();
        }
    }

    private String transactionId() {
        return "TXN-" + accountId + '-' + UUID.randomUUID();
    }

    public record TransferEntries(TransactionRecord sourceRecord, TransactionRecord targetRecord) {
    }
}
