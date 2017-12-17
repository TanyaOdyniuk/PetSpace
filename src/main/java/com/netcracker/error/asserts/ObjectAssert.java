package com.netcracker.error.asserts;

import com.netcracker.error.ErrorMessage;
import com.netcracker.error.handler.ClientExceptionHandler;

import java.util.Collection;

public class ObjectAssert {

    public static void isNull(Object obj){
        isNull(obj, ErrorMessage.VALIDATION_WRONG);
    }

    public static void isNull(Object obj, String messageToShow){
        if(obj == null){
            handle(messageToShow);
        }
    }

    public static void isTrue(Boolean fact){
        isTrue(fact, ErrorMessage.VALIDATION_WRONG);
    }

    public static void isTrue(Boolean fact, String messageToShow){
        if(!fact){
            handle(messageToShow);
        }
    }

    public static void isNullOrEmpty(String obj){
        isNullOrEmpty(obj, ErrorMessage.VALIDATION_WRONG);
    }

    public static void isNullOrEmpty(String obj, String messageToShow){
        if(obj == null || obj.isEmpty()){
            handle(messageToShow);
        }
    }

    public static void notNullElements(Object[] array){
        notNullElements(array, ErrorMessage.VALIDATION_WRONG);
    }

    public static void notNullElements(Object[] array, String messageToShow){
        for (Object obj: array) {
            if(obj == null)
                handle(messageToShow);
        }
    }

    public void notEmpty(Collection<?> collection) {
        notEmpty(collection, ErrorMessage.VALIDATION_WRONG);
    }

    public void notEmpty(Collection<?> collection, String messageToShow){
        if(collection.isEmpty()){
            handle(messageToShow);
        }
    }

    private static void handle(String messageToShow){
        ClientExceptionHandler.handleAssert(messageToShow);
        throw new IllegalArgumentException(messageToShow);
    }
}
