package com.netcracker.error.handler;

import com.vaadin.server.ErrorEvent;
import com.vaadin.server.ErrorHandler;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;

import static com.netcracker.error.ErrorMessage.ERROR_UNKNOWN;

public class ClientExceptionHandler implements ErrorHandler {

    @Override
    public void error(ErrorEvent event) {
        showNotification(/*event.getThrowable().getLocalizedMessage()*/ ERROR_UNKNOWN, Notification.Type.WARNING_MESSAGE);
    }

    public static void handle(Exception ex){
        showNotification(ex.getLocalizedMessage(), Notification.Type.ERROR_MESSAGE);
    }

    public static void handleAssert(String message){
        showNotification(message, Notification.Type.WARNING_MESSAGE);
    }

    public static void handle(ResponseEntity entity, String messageToShow) {
        if (entity == null)
            return;
        handleWithStatus(entity.getStatusCode(), messageToShow);
    }

    public static void handle(HttpStatusCodeException ex){
        handle(ex, ex.getLocalizedMessage());
    }

    public static void handle(HttpStatusCodeException ex, String messageToShow) {
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
