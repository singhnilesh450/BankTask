package com.banktask;

import com.banktask.welcome.WelcomeScreen;

public final class BankTask {
    private BankTask() {
        // Utility class
    }

    public static void main(String[] args) {
        WelcomeScreen.welcome();
    }
}
