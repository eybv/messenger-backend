package com.github.eybv.messenger.application.excaption;

public class DataConsistencyViolationException extends RuntimeException {

    public DataConsistencyViolationException(String message) {
        super(message);
    }

}
