package com.netcracker.service.petprofile.impl;

import com.netcracker.dao.managerapi.ManagerAPI;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.pet.PetConstant;
import com.netcracker.model.user.Profile;
import com.netcracker.service.petprofile.PetProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PetProfileServiceImpl implements PetProfileService {

    @Autowired
    ManagerAPI managerApi;

    @Override
    public Pet createPetProfile(Pet pet) {
        return null;
    }

    @Override
    public void validation(Object dataForValidate) {

    }

    @Override
    public void editProfile(Profile profile) {

    }

    @Override
    public void deleteProfile(BigInteger profileId) {

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
    public List<Pet> getAllPets() {
        return managerApi.getAll(BigInteger.valueOf(PetConstant.PET_TYPE), Pet.class);
    }

    @Override
    public List<Pet> getAllProfilePets(BigInteger profileId) {
        String sqlQuery = "SELECT OBJECT_ID FROM OBJREFERENCE " +
                "WHERE reference = " + profileId + " AND ATTRTYPE_ID = " + PetConstant.PET_OWNER;
        return managerApi.getObjectsBySQL(sqlQuery, Pet.class);
    }
}
