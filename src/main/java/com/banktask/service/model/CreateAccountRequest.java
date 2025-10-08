package com.banktask.service.model;

import java.math.BigDecimal;
import java.util.Objects;

public record CreateAccountRequest(
        String holderName,
        String username,
        String password,
        String bankName,
        String bankId,
        BigDecimal openingBalance) {

    public CreateAccountRequest {
        Objects.requireNonNull(holderName, "holderName");
        Objects.requireNonNull(username, "username");
        Objects.requireNonNull(password, "password");
        Objects.requireNonNull(bankName, "bankName");
        Objects.requireNonNull(bankId, "bankId");
        Objects.requireNonNull(openingBalance, "openingBalance");
    }
}
