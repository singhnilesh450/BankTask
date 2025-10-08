package com.banktask.application;

public final class BankApplication {
    private BankApplication() {
        // Application entry point only
    }

    public static void main(String[] args) {
        new BankingRuntime().start();
    }
}
