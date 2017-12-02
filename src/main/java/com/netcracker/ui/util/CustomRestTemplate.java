package com.netcracker.ui.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CustomRestTemplate extends RestTemplate {
    private static volatile CustomRestTemplate instance;

    public static CustomRestTemplate getInstance() {
        CustomRestTemplate localInstance = instance;
        if (localInstance == null) {
            synchronized (CustomRestTemplate.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new CustomRestTemplate();
                }
            }
        }
        return localInstance;
    }

    public <T> T customGetForObject(String url, Class<T> responseType) throws RestClientException {
        return getForObject(getURL() + url, responseType);
    }

    public <T> ResponseEntity<T> customExchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType) throws RestClientException {
        return exchange(getURL() + url, method, requestEntity, responseType);
    }
    public void customDelete(String url) throws RestClientException{
        delete(getURL() + url);
    }
    private String getURL() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            String serverAddress = properties.getProperty("server.address");
            String serverPort = properties.getProperty("server.port");
            return "http://" + serverAddress + ":" + serverPort;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
