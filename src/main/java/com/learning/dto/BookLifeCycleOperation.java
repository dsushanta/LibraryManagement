package com.learning.dto;

public enum BookLifeCycleOperation {

    REISSUE("reissue"),
    RETURN("return");

    private String operation;

    BookLifeCycleOperation(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }
}
