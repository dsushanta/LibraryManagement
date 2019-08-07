package com.learning.exception;

public class FieldValueRequiredException extends RuntimeException {

    public FieldValueRequiredException(String message) {
        super(message);
    }
}
