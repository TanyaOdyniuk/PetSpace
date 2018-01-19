package com.netcracker.asserts;

import com.netcracker.error.ErrorMessage;
import com.netcracker.error.exceptions.PetDataValidationException;
import com.netcracker.ui.UIConstants;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PetDataAssert {

    public static void assertName(String name) throws PetDataValidationException {
        commomStringValidation(name, RegexTemplate.PET_NAME, ErrorMessage.PET_VALIDATION_NAME);
    }

    public static String assertAvatarURL(String url) throws PetDataValidationException {
        if (!(url == null || "".equals(url)))
            return commomURLValidation(url, RegexTemplate.URL_IMAGE, ErrorMessage.PET_VALIDATION_AVATAR_URL);
        else
            return UIConstants.NO_IMAGE_URL;
    }

    public static Boolean isAvatarURL(String url) {
        return isMatchingRegex(url, RegexTemplate.URL_IMAGE);
    }

    public static Double assertHeight(String height) throws PetDataValidationException {
        return commonSizeValidation(height, ErrorMessage.PET_VALIDATION_HEIGHT);
    }

    public static Double assertWeight(String weight) throws PetDataValidationException {
        return commonSizeValidation(weight, ErrorMessage.PET_VALIDATION_WEIGHT);
    }

    public static Integer assertAge(String age) throws PetDataValidationException {
        try {
            ObjectAssert.isNullOrEmpty(age, ErrorMessage.PET_VALIDATION_AGE);
            if (ObjectAssert.isConvertibleToInteger(age)) {
                Integer result = Integer.parseInt(age);
                if (result > 0)
                    return result;
            }
            throw new IllegalArgumentException();
        } catch (IllegalArgumentException ex) {
            throw new PetDataValidationException(ErrorMessage.PET_VALIDATION_AGE);
        }
    }

    private static void commomStringValidation(String toCheck, String regex, String messageToThrow) throws PetDataValidationException {
        try {
            ObjectAssert.isNullOrEmpty(toCheck, messageToThrow);
            if (!isMatchingRegex(toCheck, regex))
                throw new IllegalArgumentException();
        } catch (IllegalArgumentException ex) {
            throw new PetDataValidationException(messageToThrow);
        }
    }

    private static String commomURLValidation(String toCheck, String regex, String messageToThrow) throws PetDataValidationException {
        try {
            if (!isMatchingRegex(toCheck, regex))
                throw new IllegalArgumentException();
            return toCheck;
        } catch (IllegalArgumentException ex) {
            throw new PetDataValidationException(messageToThrow);
        }
    }


    private static Double commonSizeValidation(String toCheck, String messageToThrow) throws PetDataValidationException {
        if ("".equals(toCheck))
            return null;
        if (ObjectAssert.isConvertibleToDouble(toCheck)) {
            Double number = Double.parseDouble(toCheck);
            if (number < 0.0)
                throw new PetDataValidationException(messageToThrow);
            return number;
        }
        throw new PetDataValidationException(messageToThrow);
    }

    private static boolean isMatchingRegex(String toCheck, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(toCheck);
        return matcher.find();
    }
}
