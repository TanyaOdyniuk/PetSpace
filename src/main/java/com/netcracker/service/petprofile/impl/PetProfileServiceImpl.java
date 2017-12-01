package com.netcracker.service.petprofile.impl;

import com.netcracker.dao.managerapi.ManagerAPI;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.user.Profile;
import com.netcracker.service.petprofile.PetProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
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
    public List<Pet> getAllProfilePets(BigInteger profileId) {
        return managerApi.getObjectsBySQL("SELECT OBJECT_ID FROM OBJREFERENCE WHERE reference = " + profileId + " AND ATTRTYPE_ID = 303", Pet.class);
    }
}
