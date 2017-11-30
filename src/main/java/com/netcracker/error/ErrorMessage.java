package com.netcracker.error;

public final class ErrorMessage {

    //STANDART MESSAGES
    public static String ERROR_400 = "Error 400. Bad request. Please, check your input data.";
    public static String ERROR_401 = "Error 401. You're not authorised.";
    public static String ERROR_403 = "Error 403. Your account do not have enough privileges.";
    public static String ERROR_404 = "Error 404. Oops. The page, you're looking for is not found.";
    public static String ERROR_500 = "Error 500. Internal server error!\nPlease, contact support.";

    //VALIDATION
    public static String VALIDATION_NAME = "Ошибка валидации!\nПроверьте правильность введенного имени.";
    public static String VALIDATION_AGE = "Ошибка валидации!\nПроверьте правильность введенного возраста.";


    //PRIVILIGIES
    public static String PRIVILIGIES_GROUP = "У вас недостаточно прав\nдля просмотра данной группы.";
    public static String PRIVILIGIES_PET = "У вас недостаточно прав\nдля просмотра страницы питомца.";
    public static String PRIVILIGIES_COMMON = "У вас недостаточно прав\nдля просмотра данной страницы.";


}
