package com.banktask.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public final class BankList {
    private static final List<Bank> BANKS = new ArrayList<>();

    private BankList() {
        // Utility class
    }

    public static void init() {
        if (!BANKS.isEmpty()) {
            return;
        }
        List<Map<String, Double>> acceptedCurrency = new ArrayList<>();
        Map<String, Double> inr = new HashMap<>();
        inr.put("INR", 1.0);
        Map<String, Double> rub = new HashMap<>();
        rub.put("RUB", 0.92);
        acceptedCurrency.add(inr);
        acceptedCurrency.add(rub);

        List<Map<String, String>> bankStaffs = new ArrayList<>();
        Map<String, String> staffOne = new HashMap<>();
        staffOne.put("abc@123", "abc");
        Map<String, String> staffTwo = new HashMap<>();
        staffTwo.put("xyz@234", "xyz");
        bankStaffs.add(staffOne);
        bankStaffs.add(staffTwo);

        BANKS.add(new Bank(
                "ICICI",
                "SBI10102011",
                0,
                5,
                2,
                6,
                acceptedCurrency,
                bankStaffs));
    }

    public static void addBank(Bank bank) {
        BANKS.add(bank);
    }

    public static boolean checkUserCredentials(String username, String password) {
        return BANKS.stream()
                .flatMap(bank -> bank.getBankStaffs().stream())
                .anyMatch(staff -> staff.containsKey(username)
                        && passwordMatches(staff.get(username), password));
    }

    private static boolean passwordMatches(String storedPassword, String candidate) {
        return storedPassword != null && storedPassword.equals(candidate);
    }

    public static double getExchangeRate(String bankId, String currency) {
        Optional<Bank> bank = BANKS.stream()
                .filter(b -> b.getBankID().equals(bankId))
                .findFirst();
        if (bank.isEmpty()) {
            return -1;
        }
        String normalized = currency.toUpperCase(Locale.ROOT);
        for (Map<String, Double> entry : bank.get().getAcceptedCurrency()) {
            Double rate = entry.get(normalized);
            if (rate != null) {
                return rate;
            }
        }
        return -1;
    }

    public static void addCurrencyAndForex(Map<String, Double> currencyWithRate) {
        Map<String, Double> normalized = new HashMap<>();
        currencyWithRate.forEach((key, value) -> normalized.put(key.toUpperCase(Locale.ROOT), value));
        BANKS.forEach(bank -> bank.getAcceptedCurrency().add(new HashMap<>(normalized)));
    }

    public static void updateServiceChargeForSameBank(String bankId, double rtgs, double imps) {
        Bank bank = findBankById(bankId);
        if (bank == null) {
            System.out.println("Incorrect Bank id");
            return;
        }
        bank.setRtgsForSameBank(rtgs);
        bank.setImpsForSameBank(imps);
        System.out.printf(Locale.ROOT, "Updated same-bank charges to RTGS: %.2f, IMPS: %.2f%n", rtgs, imps);
    }

    public static void updateServiceChargeForOtherBank(String bankId, double rtgs, double imps) {
        Bank bank = findBankById(bankId);
        if (bank == null) {
            System.out.println("Incorrect Bank id");
            return;
        }
        bank.setRtgsForOtherBank(rtgs);
        bank.setImpsForOtherBank(imps);
        System.out.printf(Locale.ROOT, "Updated other-bank charges to RTGS: %.2f, IMPS: %.2f%n", rtgs, imps);
    }

    private static Bank findBankById(String bankId) {
        return BANKS.stream()
                .filter(bank -> bank.getBankID().equals(bankId))
                .findFirst()
                .orElse(null);
    }
}
