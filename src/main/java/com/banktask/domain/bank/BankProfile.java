package com.banktask.domain.bank;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class BankProfile {
    private final String bankId;
    private final String bankName;
    private volatile BankChargeProfile sameBankCharges;
    private volatile BankChargeProfile otherBankCharges;
    private final ConcurrentHashMap<String, BigDecimal> forexRates;
    private final List<BankStaffCredential> staffCredentials;

    private BankProfile(Builder builder) {
        this.bankId = Objects.requireNonNull(builder.bankId, "bankId");
        this.bankName = Objects.requireNonNull(builder.bankName, "bankName");
        this.sameBankCharges = Objects.requireNonNull(builder.sameBankCharges, "sameBankCharges");
        this.otherBankCharges = Objects.requireNonNull(builder.otherBankCharges, "otherBankCharges");
        this.forexRates = new ConcurrentHashMap<>(builder.forexRates);
        this.staffCredentials = new ArrayList<>(builder.staffCredentials);
    }

    public String getBankId() {
        return bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public BankChargeProfile getSameBankCharges() {
        return sameBankCharges;
    }

    public BankChargeProfile getOtherBankCharges() {
        return otherBankCharges;
    }

    public void updateSameBankCharges(BankChargeProfile charges) {
        this.sameBankCharges = Objects.requireNonNull(charges);
    }

    public void updateOtherBankCharges(BankChargeProfile charges) {
        this.otherBankCharges = Objects.requireNonNull(charges);
    }

    public Optional<BigDecimal> exchangeRateFor(String currency) {
        return Optional.ofNullable(forexRates.get(currency.toUpperCase()));
    }

    public void registerCurrency(String currency, BigDecimal rate) {
        forexRates.put(currency.toUpperCase(), rate);
    }

    public boolean authenticate(String username, String password) {
        return staffCredentials.stream().anyMatch(credential -> credential.matches(username, password));
    }

    public static Builder builder() {
        return new Builder();
    }

    public record BankStaffCredential(String username, String password) {
        public boolean matches(String user, String pass) {
            return Objects.equals(username, user) && Objects.equals(password, pass);
        }
    }

    public static final class Builder {
        private String bankId;
        private String bankName;
        private BankChargeProfile sameBankCharges = new BankChargeProfile(BigDecimal.ZERO, BigDecimal.ZERO);
        private BankChargeProfile otherBankCharges = new BankChargeProfile(BigDecimal.ZERO, BigDecimal.ZERO);
        private Map<String, BigDecimal> forexRates = Map.of();
        private List<BankStaffCredential> staffCredentials = List.of();

        private Builder() {
        }

        public Builder bankId(String bankId) {
            this.bankId = bankId;
            return this;
        }

        public Builder bankName(String bankName) {
            this.bankName = bankName;
            return this;
        }

        public Builder sameBankCharges(BankChargeProfile charges) {
            this.sameBankCharges = charges;
            return this;
        }

        public Builder otherBankCharges(BankChargeProfile charges) {
            this.otherBankCharges = charges;
            return this;
        }

        public Builder forexRates(Map<String, BigDecimal> forexRates) {
            this.forexRates = forexRates;
            return this;
        }

        public Builder staffCredentials(List<BankStaffCredential> staffCredentials) {
            this.staffCredentials = staffCredentials;
            return this;
        }

        public BankProfile build() {
            return new BankProfile(this);
        }
    }
}
