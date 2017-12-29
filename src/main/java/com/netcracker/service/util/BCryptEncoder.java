package com.netcracker.service.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BCryptEncoder extends BCryptPasswordEncoder {
    private static volatile BCryptEncoder instance;

    public static BCryptPasswordEncoder getInstance(){
        BCryptEncoder localInstance = instance;
        if (localInstance == null) {
            synchronized (BCryptEncoder.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new BCryptEncoder();
                }
            }
        }
        return localInstance;
    }
}
