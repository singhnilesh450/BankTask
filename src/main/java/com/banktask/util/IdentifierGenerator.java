package com.banktask.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class IdentifierGenerator {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("ddMMyyyy");

    private IdentifierGenerator() {
    }

    public static String accountId(String name, String bankId) {
        String prefix = sanitize(name);
        return prefix + '-' + bankId + '-' + LocalDate.now().format(FORMATTER);
    }

    private static String sanitize(String value) {
        if (value == null || value.isBlank()) {
            return "ACC";
        }
        String cleaned = value.replaceAll("\\s+", "");
        if (cleaned.isEmpty()) {
            return "ACC";
        }
        return cleaned.substring(0, Math.min(3, cleaned.length())).toUpperCase();
    }
}
