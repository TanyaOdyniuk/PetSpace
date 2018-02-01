package com.netcracker.asserts;

import com.netcracker.error.ErrorMessage;
import com.netcracker.error.exceptions.DataValidationException;
import com.netcracker.ui.UIConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileDataAssert {

    public static void assertName(String name) throws DataValidationException {
        commonStringValidation(name, RegexTemplate.NAME, ErrorMessage.PROFILE_VALIDATION_NAME);
    }

    public static String assertAvatarURL(String url) throws DataValidationException {
        if (!(url == null || "".equals(url)))
            return commonURLValidation(url, RegexTemplate.URL_IMAGE, ErrorMessage.VALIDATION_AVATAR_URL);
        else
            return UIConstants.PROFILE_NO_IMAGE_URL;
    }

    public static Boolean isAvatarURL(String url) {
        return isMatchingRegex(url, RegexTemplate.URL_IMAGE);
    }

    public static Integer assertAge(String age) throws DataValidationException {
        return commonAgeValidation(age, ErrorMessage.PROFILE_VALIDATION_AGE);
    }

    private static void commonStringValidation(String toCheck, String regex, String messageToThrow) throws DataValidationException {
        try {
            ObjectAssert.isNullOrEmpty(toCheck, messageToThrow);
            if (!isMatchingRegex(toCheck, regex))
                throw new IllegalArgumentException();
        } catch (IllegalArgumentException ex) {
            throw new DataValidationException(messageToThrow);
        }
    }

    private static String commonURLValidation(String toCheck, String regex, String messageToThrow) throws DataValidationException {
        try {
            if (!isMatchingRegex(toCheck, regex))
                throw new IllegalArgumentException();
            return toCheck;
        } catch (IllegalArgumentException ex) {
            throw new DataValidationException(messageToThrow);
        }
    }

    private static Integer commonAgeValidation(String toCheck, String messageToThrow) throws DataValidationException {
        if ("".equals(toCheck))
            return null;
        if (ObjectAssert.isConvertibleToInteger(toCheck)) {
            Integer number = Integer.parseInt(toCheck);
            if (number < 0)
                throw new DataValidationException(messageToThrow);
            return number;
        }
        throw new DataValidationException(messageToThrow);
    }

    private static boolean isMatchingRegex(String toCheck, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(toCheck);
        return matcher.find();
    }
}
