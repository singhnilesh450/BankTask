package com.banktask.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Bank {
    private String bankName;
    private String bankID;
    private double rtgsForSameBank;
    private double impsForSameBank;
    private double rtgsForOtherBank;
    private double impsForOtherBank;
    private final List<Map<String, Double>> acceptedCurrency;
    private final List<Map<String, String>> bankStaffs;

    public Bank() {
        this.acceptedCurrency = new ArrayList<>();
        this.bankStaffs = new ArrayList<>();
    }

    public Bank(
            String bankName,
            String bankID,
            double rtgsForSameBank,
            double impsForSameBank,
            double rtgsForOtherBank,
            double impsForOtherBank,
            List<Map<String, Double>> acceptedCurrency,
            List<Map<String, String>> bankStaffs) {
        this.bankName = bankName;
        this.bankID = bankID;
        this.rtgsForSameBank = rtgsForSameBank;
        this.impsForSameBank = impsForSameBank;
        this.rtgsForOtherBank = rtgsForOtherBank;
        this.impsForOtherBank = impsForOtherBank;
        this.acceptedCurrency = new ArrayList<>(acceptedCurrency);
        this.bankStaffs = new ArrayList<>(bankStaffs);
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankID() {
        return bankID;
    }

    public void setBankID(String bankID) {
        this.bankID = bankID;
    }

    public double getRtgsForSameBank() {
        return rtgsForSameBank;
    }

    public void setRtgsForSameBank(double rtgsForSameBank) {
        this.rtgsForSameBank = rtgsForSameBank;
    }

    public double getImpsForSameBank() {
        return impsForSameBank;
    }

    public void setImpsForSameBank(double impsForSameBank) {
        this.impsForSameBank = impsForSameBank;
    }

    public double getRtgsForOtherBank() {
        return rtgsForOtherBank;
    }

    public void setRtgsForOtherBank(double rtgsForOtherBank) {
        this.rtgsForOtherBank = rtgsForOtherBank;
    }

    public double getImpsForOtherBank() {
        return impsForOtherBank;
    }

    public void setImpsForOtherBank(double impsForOtherBank) {
        this.impsForOtherBank = impsForOtherBank;
    }

    public List<Map<String, Double>> getAcceptedCurrency() {
        return acceptedCurrency;
    }

    public List<Map<String, String>> getBankStaffs() {
        return bankStaffs;
    }
}
