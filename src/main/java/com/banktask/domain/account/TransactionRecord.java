package com.banktask.domain.account;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public record TransactionRecord(
        String id,
        TransactionType type,
        BigDecimal delta,
        BigDecimal resultingBalance,
        String description,
        Instant timestamp) {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.systemDefault());

    public String toConsoleString() {
        return String.format("[%s] %s %s (%s) -> Balance: %s",
                FORMATTER.format(timestamp),
                type,
                delta,
                description,
                resultingBalance);
    }
}
