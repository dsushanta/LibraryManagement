package com.learning.exception;

public class CannotDeleteException extends RuntimeException {

    public CannotDeleteException(String message) {
        super(message);
    }
}
