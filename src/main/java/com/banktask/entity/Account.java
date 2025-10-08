package com.banktask.entity;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private String name;
    private String userName;
    private String password;
    private String bankName;
    private String bankId;
    private String accountId;
    private double balance;
    private final List<String> transactionHistory;

    public Account() {
        this.transactionHistory = new ArrayList<>();
    }

    public Account(
            String name,
            String userName,
            String password,
            String bankName,
            String bankId,
            String accountId,
            double balance,
            List<String> transactionHistory) {
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.bankName = bankName;
        this.bankId = bankId;
        this.accountId = accountId;
        this.balance = balance;
        this.transactionHistory = new ArrayList<>(transactionHistory);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }
}
