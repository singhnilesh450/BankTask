package com.banktask.service.model;

import java.math.BigDecimal;
import java.util.Objects;

public record UpdateAccountRequest(
        String accountId,
        String holderName,
        String bankName,
        BigDecimal balance) {

    public UpdateAccountRequest {
        Objects.requireNonNull(accountId, "accountId");
        Objects.requireNonNull(holderName, "holderName");
        Objects.requireNonNull(bankName, "bankName");
        Objects.requireNonNull(balance, "balance");
    }
}
