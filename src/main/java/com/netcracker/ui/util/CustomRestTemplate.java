package com.netcracker.ui.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

@Service
public class CustomRestTemplate extends RestTemplate {
    @Autowired
    private Logger logger;
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

    public <T> T customPostForObject(String url, Object request, Class<T> responseType) throws RestClientException {
        return postForObject(getURL() + url, request, responseType);
    }

    public <T> T customGetForObject(String url, Class<T> responseType) throws RestClientException {
        return getForObject(getURL() + url, responseType);
    }

    public <T> ResponseEntity<T> customExchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType) throws RestClientException {
        return exchange(getURL() + url, method, requestEntity, responseType);
    }

    public void customDelete(String url) throws RestClientException {
        delete(getURL() + url);
    }

    public <T> ResponseEntity<T> customExchangeForParametrizedTypes(String url, HttpMethod method, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType) throws RestClientException {
        return exchange(getURL() + url, method, requestEntity, responseType);
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
            logger.throwing(this.getClass().getName(), "getURL()", e);
        }
        return "";
    }

}
