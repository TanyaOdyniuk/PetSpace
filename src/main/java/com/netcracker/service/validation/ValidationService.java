package com.netcracker.service.validation;

import com.netcracker.model.user.User;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationService {


    public static boolean validateName(String txt) {
        Pattern pattern = Pattern.compile("[A-Z][a-z]*", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(txt);
        return matcher.find();
    }
}