package com.netcracker.error.exceptions;

public class DataValidationException extends IllegalArgumentException {

    private String message;

    public DataValidationException() {
        super();
    }

    public DataValidationException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public String getLocalizedMessage() {
        return message;
    }
}
