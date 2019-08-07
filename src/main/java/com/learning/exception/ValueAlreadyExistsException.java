package com.learning.exception;

public class ValueAlreadyExistsException extends RuntimeException {

    public ValueAlreadyExistsException(String message) {
        super(message);
    }
}
