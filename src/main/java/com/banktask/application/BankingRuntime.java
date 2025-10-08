package com.banktask.application;

import com.banktask.console.BankingConsoleClient;
import com.banktask.console.ConsoleIO;
import com.banktask.infrastructure.account.AccountDirectory;
import com.banktask.infrastructure.bank.BankDirectory;
import com.banktask.service.AccountService;
import com.banktask.service.BankService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class BankingRuntime {
    private final ExecutorService executorService;
    private final BankDirectory bankDirectory;
    private final AccountDirectory accountDirectory;
    private final BankService bankService;
    private final AccountService accountService;
    private final BuiltInDataInitializer initializer;
    private final BankingConsoleClient consoleClient;

    public BankingRuntime() {
        this.executorService = Executors.newFixedThreadPool(Math.max(4, Runtime.getRuntime().availableProcessors()));
        this.bankDirectory = new BankDirectory();
        this.accountDirectory = new AccountDirectory();
        this.bankService = new BankService(bankDirectory);
        this.accountService = new AccountService(accountDirectory, bankDirectory, executorService);
        this.initializer = new BuiltInDataInitializer(bankService, accountService);
        this.consoleClient = new BankingConsoleClient(new ConsoleIO(), bankService, accountService);
    }

    public void start() {
        initializer.initialize();
        try {
            consoleClient.launch();
        } finally {
            shutdown();
        }
    }

    private void shutdown() {
        executorService.shutdownNow();
    }
}
