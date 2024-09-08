package com.ing.broker.exceptions;

public class NotSufficientFundsException extends RuntimeException {
    public NotSufficientFundsException(String message) {
        super(message);
    }

}