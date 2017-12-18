package com.netcracker.error.exceptions;

public class PetDataValidationException extends IllegalArgumentException {

    private String message;

    public PetDataValidationException() {
        super();
    }

    public PetDataValidationException(String message) {
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
