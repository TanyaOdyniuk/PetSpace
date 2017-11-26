package com.netcracker.service.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by V.Drabynka on 19.11.2017.
 */
public class ValidationService {

    public static boolean validateName (String txt){
        Pattern pattern = Pattern.compile("[A-Z][a-z]*", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(txt);
        return matcher.find();
    }
}
