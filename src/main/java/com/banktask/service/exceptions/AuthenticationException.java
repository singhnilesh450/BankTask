package com.banktask.service.exceptions;

public final class AuthenticationException extends BankingException {
    public AuthenticationException(String message) {
        super(message);
    }
}
