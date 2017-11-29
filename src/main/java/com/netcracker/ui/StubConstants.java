package com.netcracker.UI;

import org.springframework.web.client.RestTemplate;

public interface StubConstants {
    RestTemplate REST_TEMPLATE = new RestTemplate();
    String RESOURCE_URL = "http://localhost:8888/restcontroller";
}
