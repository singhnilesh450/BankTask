package com.banktask.service.model;

import java.math.BigDecimal;
import java.util.Objects;

public record TransferRequest(
        String sourceUsername,
        String targetAccountId,
        BigDecimal amount) {

    public TransferRequest {
        Objects.requireNonNull(sourceUsername, "sourceUsername");
        Objects.requireNonNull(targetAccountId, "targetAccountId");
        Objects.requireNonNull(amount, "amount");
    }
}
