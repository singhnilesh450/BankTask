package com.banktask.service.exceptions;

public final class ValidationException extends BankingException {
    public ValidationException(String message) {
        super(message);
    }
}
