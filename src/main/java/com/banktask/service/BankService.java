package com.banktask.service;

import com.banktask.domain.bank.BankChargeProfile;
import com.banktask.domain.bank.BankProfile;
import com.banktask.infrastructure.bank.BankDirectory;
import com.banktask.service.exceptions.BankingException;
import java.math.BigDecimal;
import java.util.Optional;

public final class BankService {
    private final BankDirectory bankDirectory;

    public BankService(BankDirectory bankDirectory) {
        this.bankDirectory = bankDirectory;
    }

    public void registerBank(BankProfile profile) {
        bankDirectory.register(profile);
    }

    public boolean authenticateStaff(String username, String password) {
        return bankDirectory.authenticateStaff(username, password);
    }

    public BigDecimal resolveExchangeRate(String bankId, String currency) {
        Optional<BankProfile> bank = bankDirectory.findById(bankId);
        if (bank.isEmpty()) {
            throw new BankingException("Bank not found: " + bankId);
        }
        return bank.get().exchangeRateFor(currency)
                .orElseThrow(() -> new BankingException("Currency not supported: " + currency));
    }

    public void registerCurrencyRate(String currency, BigDecimal rate) {
        bankDirectory.snapshot().forEach(bank -> bank.registerCurrency(currency, rate));
    }

    public void updateSameBankCharges(String bankId, BigDecimal rtgs, BigDecimal imps) {
        BankProfile bank = bankDirectory.findById(bankId)
                .orElseThrow(() -> new BankingException("Bank not found: " + bankId));
        bank.updateSameBankCharges(new BankChargeProfile(rtgs, imps));
    }

    public void updateOtherBankCharges(String bankId, BigDecimal rtgs, BigDecimal imps) {
        BankProfile bank = bankDirectory.findById(bankId)
                .orElseThrow(() -> new BankingException("Bank not found: " + bankId));
        bank.updateOtherBankCharges(new BankChargeProfile(rtgs, imps));
    }
}
