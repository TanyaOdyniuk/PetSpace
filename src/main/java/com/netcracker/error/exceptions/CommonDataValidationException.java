package com.netcracker.error.exceptions;

public class CommonDataValidationException extends IllegalArgumentException {

    private String message;

    public CommonDataValidationException() {
        super();
    }

    public CommonDataValidationException(String message) {
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
