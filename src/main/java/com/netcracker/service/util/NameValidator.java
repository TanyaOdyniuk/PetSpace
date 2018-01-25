package com.netcracker.service.util;

import org.springframework.stereotype.Service;

@Service
public class NameValidator {
    public String validateName(String name) {
        String validatedName = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        return validatedName;
    }
}
