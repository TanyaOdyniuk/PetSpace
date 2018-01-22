package com.netcracker.controller.petlistpage;

import com.netcracker.model.pet.Pet;
import com.netcracker.service.petprofile.PetProfileService;
import com.netcracker.service.util.RestResponsePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetsListPageController {

    @Autowired
    PetProfileService petProfileService;

    @GetMapping("/{id}/{page}")
    public RestResponsePage<Pet> getMyMessages(@PathVariable("id") BigInteger profileId, @PathVariable("page") int page) {
        Integer count = petProfileService.getAllPetsPageCount(profileId);
        List<Pet> petsProfileList =  petProfileService.getAllProfilePets(profileId, page);
        return new RestResponsePage<>(petsProfileList, null, count);
    }


    @GetMapping("/all/{page}")
    public RestResponsePage<Pet> getAllPets(@PathVariable("page") int page) {
        Integer count = petProfileService.getAllPetsPageCount();
        List<Pet> petsList =  petProfileService.getAllPets(page);
        return new RestResponsePage<>(petsList, null, count);
    }

    @GetMapping("/{id}")
    public List<Pet> getMyPets(@PathVariable("id") BigInteger id) {
        return petProfileService.getAllProfilePets(id);
    }
}
