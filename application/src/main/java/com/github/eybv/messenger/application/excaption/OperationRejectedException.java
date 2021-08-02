package com.github.eybv.messenger.application.excaption;

public class OperationRejectedException extends RuntimeException {

    public OperationRejectedException(String message) {
        super(message);
    }

}
