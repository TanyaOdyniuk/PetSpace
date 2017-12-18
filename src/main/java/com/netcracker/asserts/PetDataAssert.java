package com.netcracker.asserts;

import com.netcracker.error.exceptions.PetDataValidationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PetDataAssert {

    public static void assertName(String name) throws PetDataValidationException {
        String message = "Проверьте правильность введенного\nимени питомца!";
        commomStringValidation(name, RegexTemplate.PET_NAME, message);
    }

    public static void assertAvatarURL(String url) throws PetDataValidationException {
        String message = "Проверьте правильность введенного\nадреса картинки аватара!";
        commomStringValidation(url, RegexTemplate.URL_IMAGE, message);
    }

    public static Double assertHeight(String height) throws PetDataValidationException {
        String messageToThrow = "Проверьте правильность ввода\nроста питомца";
        return commonSizeValidation(height, messageToThrow);
    }

    public static Double assertWeight(String weight) throws PetDataValidationException {
        String messageToThrow = "Проверьте правильность ввода\nвеса питомца";
        return commonSizeValidation(weight, messageToThrow);
    }

    public static Integer assertAge(String age) throws PetDataValidationException {
        String messageToThrow = "Проверьте правильность ввода\nвозраста питомца";
        try {
            ObjectAssert.isNullOrEmpty(age, messageToThrow);
            if (ObjectAssert.isConvertibleToInteger(age)){
                Integer result = Integer.parseInt(age);
                if(result > 0)
                    return result;
            }
            throw new IllegalArgumentException();
        } catch (IllegalArgumentException ex) {
            throw new PetDataValidationException(messageToThrow);
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
