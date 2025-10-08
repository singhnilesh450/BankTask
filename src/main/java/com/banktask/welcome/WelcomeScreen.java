package com.banktask.welcome;

import com.banktask.entity.AccountList;
import com.banktask.entity.BankList;

public final class WelcomeScreen {
    private WelcomeScreen() {
        // Utility class
    }

    public static void welcome() {
        ConsoleHelper.printDivider();
        ConsoleHelper.printMessage("--------------------Welcome to the Bank App-----------------");
        ConsoleHelper.printDivider();

        BankList.init();
        AccountList.init();

        while (true) {
            ConsoleHelper.printMessage("Enter your choice");
            ConsoleHelper.printMessage("1. Bank Staff");
            ConsoleHelper.printMessage("2. Account Holder");
            ConsoleHelper.printMessage("3. Exit");
            int choice = ConsoleHelper.readInt("Choice:");
            Utility.mainFunc(choice);
        }
    }
}
