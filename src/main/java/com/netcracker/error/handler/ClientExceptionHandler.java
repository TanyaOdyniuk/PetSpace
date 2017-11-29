package com.netcracker.error.handler;

import com.netcracker.error.ErrorMessage;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

public class ClientExceptionHandler {

    public static void handle(ResponseEntity entity, String messageToShow) {
        if (entity == null)
            return;
        handleWithStatus(entity.getStatusCode(), messageToShow);
    }

    public static void handle(HttpClientErrorException ex){
        handle(ex, ex.getLocalizedMessage());
    }

    public static void handle(HttpClientErrorException ex, String messageToShow) {
        if (ex == null)
            return;
        handleWithStatus(ex.getStatusCode(), messageToShow);
    }

    private static void handleWithStatus(HttpStatus httpStatus, String messageToShow) {
        if(httpStatus.is4xxClientError()){
            showNotification(messageToShow, Notification.Type.WARNING_MESSAGE);
            return;
        }
        else if(httpStatus.is5xxServerError()) {
            showNotification(messageToShow, Notification.Type.ERROR_MESSAGE);
        }
    }

    private static void showNotification(String message, Notification.Type type) {
        Notification notification = new Notification(message, type);
        notification.show(Page.getCurrent());
    }
}
