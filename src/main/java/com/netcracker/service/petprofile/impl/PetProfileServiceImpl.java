package com.netcracker.service.petprofile.impl;

import com.netcracker.dao.managerapi.ManagerAPI;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.pet.PetConstant;
import com.netcracker.model.pet.PetSpecies;
import com.netcracker.model.user.Profile;
import com.netcracker.service.petprofile.PetProfileService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PetProfileServiceImpl implements PetProfileService {

    @Autowired
    ManagerAPI managerApi;

    @Override
    public Pet createPetProfile(Pet pet) {
        return managerApi.create(pet);
    }

    @Override
    public void validation(Object dataForValidate) {

    }

    @Override
    public void editProfile(Profile profile) {
        managerApi.update(profile);
    }

    @Override
    public void deleteProfile(BigInteger profileId) {
        managerApi.delete(profileId, 0);
    }

    @Override
    public void changePetOwner(Profile owner, Profile newOwner) {

    }

    @Override
    public Date ageCalculation(Pet pet) {
        return null;
    }

    @Override
    public Profile getOwner(BigInteger ownerId){
        return managerApi.getById(ownerId, Profile.class);
    }

    @Override
    public Pet getPetById(BigInteger petId){
        return managerApi.getById(petId, Pet.class);
    }

    @Override
    public List<Pet> getAllPets(boolean isPaging, Pair<Integer, Integer> pagingDesc, Map<String, String> sortingDesc) {
        return managerApi.getAll(BigInteger.valueOf(PetConstant.PET_TYPE), Pet.class, isPaging, pagingDesc, sortingDesc);
    }

    @Override
    public List<Pet> getAllProfilePets(BigInteger profileId, boolean isPaging, Pair<Integer, Integer> pagingDesc, Map<String, String> sortingDesc) {
        String sqlQuery = "SELECT OBJECT_ID FROM OBJREFERENCE " +
                "WHERE reference = " + profileId + " AND ATTRTYPE_ID = " + PetConstant.PET_OWNER;
        return managerApi.getObjectsBySQL(sqlQuery, Pet.class, isPaging, pagingDesc, sortingDesc);
    }

    @Override
    public List<PetSpecies> getAllSpecies(boolean isPaging, Pair<Integer, Integer> pagingDesc, Map<String, String> sortingDesc) {
        return managerApi.getAll(BigInteger.valueOf(PetConstant.PETSPEC_TYPE), PetSpecies.class, isPaging, pagingDesc, sortingDesc);
    }
}
