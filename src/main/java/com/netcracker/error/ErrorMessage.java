package com.netcracker.error;

public interface ErrorMessage {

    //STANDART MESSAGES
    String ERROR_400 = "Error 400. Bad request. Please, check your input data.";
    String ERROR_401 = "Error 401. You're not authorised.";
    String ERROR_403 = "Error 403. Your account do not have enough privileges.";
    String ERROR_404 = "Error 404. Oops. The page, you're looking for is not found.";
    String ERROR_500 = "Error 500. Internal server error!\nPlease, contact support.";
    String ERROR_UNKNOWN = "Произошла неизвестная ошибка!\nПопробуйте ещё раз.";

    //VALIDATION
    String VALIDATION_NAME = "Ошибка валидации!\nПроверьте правильность введенного имени.";
    String VALIDATION_AGE = "Ошибка валидации!\nПроверьте правильность введенного возраста.";
    String VALIDATION_WRONG = "Ошибка валидации!\nПроверьте правильность введенных данных.";

    //PET_VALIDATION
    String PET_VALIDATION_NAME = "Проверьте правильность введенного\nимени питомца!";
    String PET_VALIDATION_AVATAR_URL = "Проверьте правильность введенного\nадреса картинки аватара!";
    String PET_VALIDATION_HEIGHT = "Проверьте правильность ввода\nроста питомца";
    String PET_VALIDATION_WEIGHT = "Проверьте правильность ввода\nвеса питомца";
    String PET_VALIDATION_AGE = "Проверьте правильность ввода\nвозраста питомца";

    //PRIVILIGIES
    String PRIVILIGIES_GROUP = "У вас недостаточно прав\nдля просмотра данной группы.";
    String PRIVILIGIES_PET = "У вас недостаточно прав\nдля просмотра страницы питомца.";
    String PRIVILIGIES_COMMON = "У вас недостаточно прав\nдля просмотра данной страницы.";
}
