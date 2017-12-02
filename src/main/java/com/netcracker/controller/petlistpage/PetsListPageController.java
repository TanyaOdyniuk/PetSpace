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
@RequestMapping("/pets")
public class PetsListPageController {

    @Autowired
    PetProfileService petProfileService;

    @GetMapping("/{id}")
    public List<Pet> getMyPets(@PathVariable("id") BigInteger id) {
        return petProfileService.getAllProfilePets(id);
    }

    @GetMapping
    public List<Pet> getAllPets() {
        return petProfileService.getAllPets();
    }
}
