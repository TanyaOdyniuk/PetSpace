package com.netcracker.error;

public interface ErrorMessage {

    //STANDART MESSAGES
    String ERROR_400 = "Error 400. Bad request. Please, check your input data.";
    String ERROR_401 = "Error 401. You're not authorised.";
    String ERROR_403 = "Error 403. Your account do not have enough privileges.";
    String ERROR_404 = "Error 404. Oops. The page, you're looking for is not found.";
    String ERROR_500 = "Error 500. Internal server error!\nPlease, contact support.";
    String ERROR_UNKNOWN = "Unknown error occurred!\nPlease, try again.";

    //VALIDATION
    String VALIDATION_NAME = "Validation error!\nPlease, check entered name.";
    String VALIDATION_AGE = "Validation error!\nPlease, check entered age.";
    String VALIDATION_WRONG = "Validation error!\nPlease, check entered data.";
    String VALIDATION_URL = "Validation error!\nPlease, check entered URL.";

    //PET_VALIDATION
    String PET_VALIDATION_NAME = "Please, check entered\npet's name!";
    String PET_VALIDATION_AVATAR_URL = "Please, check entered\navatar's URL!";
    String PET_VALIDATION_HEIGHT = "Please, check entered\npet's height!";
    String PET_VALIDATION_WEIGHT = "Please, check entered\npet's weight!";
    String PET_VALIDATION_AGE = "Please, check entered\npet's age!";

    //PRIVILIGIES
    String PRIVILIGIES_GROUP = "У вас недостаточно прав\nдля просмотра данной группы.";
    String PRIVILIGIES_PET = "У вас недостаточно прав\nдля просмотра страницы питомца.";
    String PRIVILIGIES_COMMON = "У вас недостаточно прав\nдля просмотра данной страницы.";
}
