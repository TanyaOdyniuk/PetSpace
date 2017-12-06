package com.netcracker.service.petprofile;

import com.netcracker.model.pet.Pet;
import com.netcracker.model.user.Profile;
import javafx.util.Pair;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface PetProfileService {
//    Система должна позволять зарегистрированному пользователю создавать страницу питомца
    Pet createPetProfile(Pet pet);

//    Система должна валидировать входящие данные на правильность ввода  при создании новой страницы
    void validation(Object dataForValidate);

//    Система должна позволять владельцу питомца редактировать информацию о питомце.
    void editProfile(Profile profile);

//    Система должна позволять владельцу питомца удалять страницу питомца.
    void deleteProfile(BigInteger profileId);

//    Система должна позволять владельцу питомца изменить хозяина питомца (при продаже/передачи питомца по объявлению
    void changePetOwner(Profile owner, Profile newOwner);

    Date ageCalculation(Pet pet);

    //Получить владельца питомца
    Profile getOwner(BigInteger ownerId);

    //Получить питомца с конкретным ID
    Pet getPetById(BigInteger petId);

    //Получить список всех животных
    List<Pet> getAllPets(boolean isPaging, Pair<Integer, Integer> pagingDesc, Map<String, String> sortingDesc);

    //Получить список животных переданного профиля
    List<Pet> getAllProfilePets(BigInteger profileId, boolean isPaging, Pair<Integer, Integer> pagingDesc, Map<String, String> sortingDesc);

}
