package com.netcracker.service.petprofile.impl;

import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.pet.PetConstant;
import com.netcracker.model.pet.PetSpecies;
import com.netcracker.model.user.Profile;
import com.netcracker.service.petprofile.PetProfileService;
import com.netcracker.service.status.StatusService;
import com.netcracker.service.user.UserService;
import com.netcracker.service.util.PageCounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Service
public class PetProfileServiceImpl implements PetProfileService {

    @Value("${petlist.pageCapacity}")
    private String petsPageCapacity;

    @Autowired
    EntityManagerService entityManagerService;

    @Autowired
    PageCounterService pageCounterService;

    @Autowired
    StatusService statusService;

    String profilePetsQuery = "SELECT OBJECT_ID FROM OBJREFERENCE " +
            "WHERE ATTRTYPE_ID = " + PetConstant.PET_OWNER + " AND REFERENCE = ";

    @Override
    public int getAllPetsPageCount(BigInteger profileId) {
        Integer ptsPageCapacity = new Integer(petsPageCapacity);
        return pageCounterService.getPageCount(ptsPageCapacity, entityManagerService.getBySqlCount(profilePetsQuery + profileId));
    }

    @Override
    public int getAllPetsPageCount() {
        Integer ptsPageCapacity = new Integer(petsPageCapacity);
        return pageCounterService.getPageCount(ptsPageCapacity, entityManagerService.getAllCount(BigInteger.valueOf(PetConstant.PET_TYPE)));
    }

    @Override
    public Pet createPetProfile(Pet pet) {
        pet.setPetStatus(statusService.getActiveStatus());
        return entityManagerService.create(pet);
    }

    @Override
    public void updatePet(Pet pet) {
        entityManagerService.update(pet);
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
    public PetSpecies getConcretePetSpecies(BigInteger petId) {
        PetSpecies petSpecies = ((Pet)entityManagerService.getById(petId, Pet.class)).getPetSpecies();
        return entityManagerService.getById(petSpecies.getObjectId(), PetSpecies.class);
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
    public List<Pet> getAllPets(int page) {
        QueryDescriptor descriptor = new QueryDescriptor();
        descriptor.addPagingDescriptor(page, Integer.valueOf(petsPageCapacity));
        return entityManagerService.getAll(BigInteger.valueOf(PetConstant.PET_TYPE), Pet.class, descriptor);
    }

    @Override
    public List<Pet> getAllProfilePets(BigInteger profileId, int page) {
        QueryDescriptor descriptor = new QueryDescriptor();
        descriptor.addPagingDescriptor(page, Integer.valueOf(petsPageCapacity));
        descriptor.addSortingDesc(PetConstant.PET_NAME, "ASC", false);
        return entityManagerService.getObjectsBySQL(profilePetsQuery + profileId, Pet.class, descriptor);
    }

    @Override
    public List<PetSpecies> getAllSpecies() {
        return entityManagerService.getAll(BigInteger.valueOf(PetConstant.PETSPEC_TYPE), PetSpecies.class, new QueryDescriptor());
    }
}
