package com.netcracker.controller.petpage;

import com.netcracker.model.pet.Pet;
import com.netcracker.model.pet.PetSpecies;
import com.netcracker.service.petprofile.PetProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/pet")
public class PetPageController {

    @Autowired
    PetProfileService petProfileService;

    @GetMapping("/{id}")
    public Pet getPetById(@PathVariable("id") BigInteger id){
        return petProfileService.getPetById(id);
    }

    @GetMapping("/{id}/species")
    public PetSpecies getConcretePetSpecies(@PathVariable("id") BigInteger id){
        return petProfileService.getConcretePetSpecies(id);
    }

    @PostMapping("/add")
    public Pet createNewPet(@RequestBody Pet pet){
        return petProfileService.createPetProfile(pet);
    }

    @PostMapping("/update")
    public void updatePet(@RequestBody Pet pet){
        petProfileService.updatePet(pet);
    }
}
