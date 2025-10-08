package com.banktask.welcome;

import com.banktask.entity.AccountList;
import com.banktask.entity.BankList;

public final class Login {
    private Login() {
        // Utility class
    }

    public static void bankStaffLogin() {
        String username = ConsoleHelper.readLine("Enter Username");
        String password = ConsoleHelper.readLine("Enter Password");
        boolean success = BankList.checkUserCredentials(username, password);
        if (success) {
            Utility.bankStaffOps();
        } else {
            ConsoleHelper.printMessage("Incorrect Credentials, Try Again");
        }
    }

    public static void accHolderLogin() {
        String username = ConsoleHelper.readLine("Enter Username");
        String password = ConsoleHelper.readLine("Enter Password");
        boolean success = AccountList.checkUserCredentials(username, password);
        if (success) {
            Utility.accHolderOps(username);
        } else {
            ConsoleHelper.printMessage("Incorrect Credentials, Try Again");
        }
    }
}
