package com.netcracker.errorHandling.exceptions;

import javax.validation.ValidationException;

/**
 * Created by V.Drabynka on 19.11.2017.
 */
public class UserNotValidException extends RuntimeException {

    private String currentName;

    public UserNotValidException() {
        super();
    }

    public UserNotValidException(String message) {
        super(message);
    }

    public UserNotValidException(String message, String currentName) {
        super(message);
        this.currentName = currentName;
    }

    public String getCurrentName() {
        return currentName;
    }

    @Override
    public String toString() {
        return "Get wrong username: " + currentName + ".\n Message: " + this.getMessage();
    }
}
