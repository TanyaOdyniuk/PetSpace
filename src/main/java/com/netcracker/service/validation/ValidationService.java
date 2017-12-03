package com.netcracker.service.validation;

import com.netcracker.model.user.User;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationService {
    public static boolean validateUserExistence(List<User> users, String login) {
        boolean isInDB = true;
        for (User user : users) {
            String existingLogin = user.getLogin();
            isInDB = existingLogin.equals(login);
            if (isInDB) {
                break;
            }
        }
        return !isInDB;
    }

    public static boolean validateName(String txt) {
        Pattern pattern = Pattern.compile("[A-Z][a-z]*", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(txt);
        return matcher.find();
    }
}