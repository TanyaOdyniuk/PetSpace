package com.netcracker.service.rest;

import com.netcracker.ui.StubConstants;
import com.netcracker.error.handler.ClientExceptionHandler;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;

public class RestServices {

    public static void restExchange(String url, HttpMethod method, HttpEntity<?> entities, Class clazz, String exceptionError) {
        try {
            StubConstants.REST_TEMPLATE
                    .exchange(url, method, entities, clazz);
        } catch (HttpClientErrorException ex) {
            ClientExceptionHandler.handle(ex, exceptionError);
        }
    }
}