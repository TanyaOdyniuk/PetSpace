package com.netcracker.UI;

import org.springframework.web.client.RestTemplate;

/**
 * Created by Odyniuk on 07/11/2017.
 */
class StubConstants {
    static final RestTemplate REST_TEMPLATE = new RestTemplate();
    static final String RESOURCE_URL = "http://localhost:8888/restcontroller";
}
