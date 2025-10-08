package com.banktask.service.exceptions;

public final class NotFoundException extends BankingException {
    public NotFoundException(String message) {
        super(message);
    }
}
