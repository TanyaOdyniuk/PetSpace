package com.netcracker.controller.petlistpage;

import com.netcracker.model.pet.Pet;
import com.netcracker.service.petprofile.PetProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("{id}/mypets")
public class PetsListPageController {

    @Autowired
    PetProfileService profileService;

    @GetMapping
    public List<Pet> getMyPets(@PathVariable("id") BigInteger id) {
        return profileService.getAllProfilePets(id);
    }
}
