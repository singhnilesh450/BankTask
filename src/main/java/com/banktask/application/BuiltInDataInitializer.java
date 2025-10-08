package com.banktask.application;

import com.banktask.domain.bank.BankChargeProfile;
import com.banktask.domain.bank.BankProfile;
import com.banktask.service.AccountService;
import com.banktask.service.BankService;
import com.banktask.service.model.CreateAccountRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public final class BuiltInDataInitializer {
    private final BankService bankService;
    private final AccountService accountService;

    public BuiltInDataInitializer(BankService bankService, AccountService accountService) {
        this.bankService = bankService;
        this.accountService = accountService;
    }

    public void initialize() {
        BankProfile icici = BankProfile.builder()
                .bankId("SBI10102011")
                .bankName("ICICI")
                .sameBankCharges(new BankChargeProfile(BigDecimal.ZERO, new BigDecimal("5")))
                .otherBankCharges(new BankChargeProfile(new BigDecimal("2"), new BigDecimal("6")))
                .forexRates(Map.of(
                        "INR", BigDecimal.ONE,
                        "RUB", new BigDecimal("0.92")
                ))
                .staffCredentials(List.of(
                        new BankProfile.BankStaffCredential("abc@123", "abc"),
                        new BankProfile.BankStaffCredential("xyz@234", "xyz")
                ))
                .build();
        bankService.registerBank(icici);

        accountService.createAccount(new CreateAccountRequest(
                "Nilesh",
                "123@abc",
                "123",
                icici.getBankName(),
                icici.getBankId(),
                new BigDecimal("1281.10"))).join();

        accountService.createAccount(new CreateAccountRequest(
                "Singh",
                "111@abc",
                "111",
                icici.getBankName(),
                icici.getBankId(),
                new BigDecimal("1828.10"))).join();
    }
}
