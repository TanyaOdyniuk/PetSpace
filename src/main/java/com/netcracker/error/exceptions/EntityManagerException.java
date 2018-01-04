package com.netcracker.error.exceptions;

public class EntityManagerException extends RuntimeException {

    private String message;

    public EntityManagerException() {
        super();
    }

    public EntityManagerException(String message) {
        super(message);
        this.message = message;
    }
    public EntityManagerException(String uiMessage, Throwable cause) {
        super(cause);
        this.message = uiMessage;
    }
    public EntityManagerException(String uiMessage, String message, Throwable cause) {
        super(message, cause);
        this.message = uiMessage;
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

