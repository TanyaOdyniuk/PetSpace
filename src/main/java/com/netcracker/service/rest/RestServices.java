package com.netcracker.service.rest;

import com.netcracker.error.handler.ClientExceptionHandler;
import com.netcracker.ui.util.CustomRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;

public class RestServices {

    public static void restExchange(String url, HttpMethod method, HttpEntity<?> entities, Class clazz, String exceptionError) {
        try {
            CustomRestTemplate.getInstance().customExchange(url, method, entities, clazz);
        } catch (HttpClientErrorException ex) {
            ClientExceptionHandler.handle(ex, exceptionError);
        }
    }
}