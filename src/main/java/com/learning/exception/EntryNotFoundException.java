package com.learning.exception;

public class EntryNotFoundException extends RuntimeException {

    public EntryNotFoundException(String message) {
        super(message);
    }
}
