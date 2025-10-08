package com.banktask.welcome;

import com.banktask.app.AccHolder;
import com.banktask.app.BankStaff;

public final class Utility {
    private Utility() {
        // Utility class
    }

    public static void mainFunc(int choice) {
        switch (choice) {
            case 1 -> Login.bankStaffLogin();
            case 2 -> Login.accHolderLogin();
            case 3 -> {
                ConsoleHelper.printMessage("Thank you for using the Bank App. Goodbye!");
                System.exit(0);
            }
            default -> {
                printErrorMessage("invalid");
                ConsoleHelper.printMessage("Enter Valid Choice");
            }
        }
    }

    public static void accHolderOps(String username) {
        boolean exit = false;
        while (!exit) {
            ConsoleHelper.printMessage("1. Deposit");
            ConsoleHelper.printMessage("2. Withdraw");
            ConsoleHelper.printMessage("3. Transfer");
            ConsoleHelper.printMessage("4. View Transaction History");
            ConsoleHelper.printMessage("5. Exit");
            int choice = ConsoleHelper.readInt("Choice:");
            exit = AccHolder.accHolderFunctionalities(choice, username);
        }
    }

    public static void bankStaffOps() {
        boolean exit = false;
        while (!exit) {
            ConsoleHelper.printMessage("1. Create New Account");
            ConsoleHelper.printMessage("2. Update Account");
            ConsoleHelper.printMessage("3. Delete Account");
            ConsoleHelper.printMessage("4. Add Currency");
            ConsoleHelper.printMessage("5. Add Service Charges for same bank");
            ConsoleHelper.printMessage("6. Add Service Charges for other bank");
            ConsoleHelper.printMessage("7. View Transaction History");
            ConsoleHelper.printMessage("8. Revert Transaction");
            ConsoleHelper.printMessage("9. Exit");
            ConsoleHelper.printMessage("10. Display");
            int choice = ConsoleHelper.readInt("Choice:");
            exit = BankStaff.bankStaffFunctionalities(choice);
        }
    }

    private static void printErrorMessage(String message) {
        ConsoleHelper.printFormatted("Choice entered is %s", message);
    }
}
