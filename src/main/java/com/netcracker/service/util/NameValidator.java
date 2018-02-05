package com.netcracker.service.util;

import org.springframework.stereotype.Service;

@Service
public class NameValidator {
    public String validateName(String name) {
        name = name.replaceAll("[^a-zA-Z]+", "");
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }
}
