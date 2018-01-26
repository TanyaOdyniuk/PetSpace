package com.netcracker.asserts;

import com.netcracker.error.ErrorMessage;
import com.netcracker.error.exceptions.CommonDataValidationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonDataAssert {

    public static Boolean isCorrectString(String stringToCheck) {
        return commonStringValidation(stringToCheck, RegexTemplate.COMMON_STRING, ErrorMessage.VALIDATION_WRONG);
    }

    public static Boolean isCommonURL(String URL) {
        return commonURLValidation(URL, RegexTemplate.URL, ErrorMessage.VALIDATION_URL);
    }

    public static Boolean isUrlImage(String URL) {
        return commonURLValidation(URL, RegexTemplate.URL_IMAGE, ErrorMessage.VALIDATION_URL);
    }

    private static Boolean commonURLValidation(String toCheck, String regex, String messageToThrow) throws CommonDataValidationException {
        return isMatchingRegex(toCheck, regex);
    }

    private static Boolean commonStringValidation(String toCheck, String regex, String messageToThrow) throws CommonDataValidationException {
        ObjectAssert.isNullOrEmpty(toCheck, messageToThrow);
        if (!isMatchingRegex(toCheck, regex))
            throw new CommonDataValidationException(messageToThrow);
        return true;
    }

    private static boolean isMatchingRegex(String toCheck, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(toCheck);
        return matcher.find();
    }
}
