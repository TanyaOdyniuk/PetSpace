package com.netcracker.service.petprofile.impl;

import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.pet.PetConstant;
import com.netcracker.model.pet.PetSpecies;
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
    EntityManagerService entityManagerService;

    @Override
    public Pet createPetProfile(Pet pet) {
        return entityManagerService.create(pet);
    }

    @Override
    public void validation(Object dataForValidate) {

    }

    @Override
    public void editProfile(Profile profile) {
        entityManagerService.update(profile);
    }

    @Override
    public void deleteProfile(BigInteger profileId) {
        entityManagerService.delete(profileId, 0);
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
        return entityManagerService.getById(ownerId, Profile.class);
    }

    @Override
    public Pet getPetById(BigInteger petId){
        return entityManagerService.getById(petId, Pet.class);
    }

    @Override
    public List<Pet> getAllPets() {
        return entityManagerService.getAll(BigInteger.valueOf(PetConstant.PET_TYPE), Pet.class, new QueryDescriptor());
    }

    @Override
    public List<Pet> getAllProfilePets(BigInteger profileId) {
        String sqlQuery = "SELECT OBJECT_ID FROM OBJREFERENCE " +
                "WHERE reference = " + profileId + " AND ATTRTYPE_ID = " + PetConstant.PET_OWNER;
        return entityManagerService.getObjectsBySQL(sqlQuery, Pet.class, new QueryDescriptor());
    }

    @Override
    public List<PetSpecies> getAllSpecies() {
        return entityManagerService.getAll(BigInteger.valueOf(PetConstant.PETSPEC_TYPE), PetSpecies.class, new QueryDescriptor());
    }
}
