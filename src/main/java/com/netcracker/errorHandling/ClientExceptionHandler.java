package com.netcracker.errorHandling;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

/**
 * Created by V.Drabynka on 25.11.2017.
 */
public class ClientExceptionHandler {

    public static void handle(ResponseEntity entity) {
        if (entity == null)
            return;
        handleWithStatus(entity.getStatusCode());
    }

    public static void handle(HttpClientErrorException ex) {
        if (ex == null)
            return;
        handleWithStatus(ex.getStatusCode());
    }

    private static void handleWithStatus(HttpStatus httpStatus) {
        if (httpStatus == null)
            return;
        if (httpStatus.equals(HttpStatus.OK)) {
            return;
        }

        if (httpStatus.equals(HttpStatus.BAD_REQUEST)) {
            showNotification("Error 400. Bad request. Please, check your input data.", Notification.Type.WARNING_MESSAGE);
            return;
        }
        if (httpStatus.equals(HttpStatus.UNAUTHORIZED)) {
            showNotification("Error 401. You're not authorised.", Notification.Type.WARNING_MESSAGE);
            return;
        }
        if (httpStatus.equals(HttpStatus.FORBIDDEN)) {
            showNotification("Error 403. Your account do not have enough privileges.", Notification.Type.WARNING_MESSAGE);
            return;
        }
        if (httpStatus.equals(HttpStatus.NOT_FOUND)) {
            showNotification("Error 404. Oops. The page, you're looking for is not found.", Notification.Type.WARNING_MESSAGE);
            return;
        }
        if (httpStatus.equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
            showNotification("Error 500. Internal server error!\nPlease, contact support.", Notification.Type.ERROR_MESSAGE);
        } else {
            showNotification(httpStatus.toString() + ". " + httpStatus.getReasonPhrase(), Notification.Type.ERROR_MESSAGE);
        }
    }

    private static void showNotification(String message, Notification.Type type) {
        Notification notification = new Notification(message, type);
        notification.show(Page.getCurrent());
    }
}
