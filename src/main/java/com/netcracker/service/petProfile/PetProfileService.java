package com.netcracker.service.petProfile;

import com.netcracker.model.pet.Pet;
import com.netcracker.model.user.Profile;
import java.util.Date;

public interface PetProfileService {
//    Система должна позволять зарегистрированному пользователю создавать страницу питомца
    Pet createPetProfile(Profile profile);

//    Система должна валидировать входящие данные на правильность ввода  при создании новой страницы
    boolean validation(Object dataForValidate); // думаю, это JS там какой-то(Vaadin)

//    Система должна позволять владельцу питомца редактировать информацию о питомце.
    void editProfile(Profile profile);

//    Система должна позволять владельцу питомца удалять страницу питомца.
    void deleteProfile(Profile profile);

//    Система должна позволять владельцу питомца изменить хозяина питомца (при продаже/передачи питомца по объявлению
    void changePetOwner(Profile owner, Profile newOwner);

    Date ageCalculation(Pet pet);

}
