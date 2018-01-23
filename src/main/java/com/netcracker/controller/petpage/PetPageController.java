package com.netcracker.controller.petpage;

import com.netcracker.model.pet.Pet;
import com.netcracker.model.pet.PetSpecies;
import com.netcracker.model.user.User;
import com.netcracker.service.petprofile.PetProfileService;
import com.netcracker.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/pet")
public class PetPageController {

    @Autowired
    PetProfileService petProfileService;

    @Autowired
    UserService userService;

    @GetMapping("/{id}")
    public Pet getPetById(@PathVariable("id") BigInteger id){
        return petProfileService.getPetById(id);
    }

    @GetMapping("/{id}/species")
    public PetSpecies getConcretePetSpecies(@PathVariable("id") BigInteger id){
        return petProfileService.getConcretePetSpecies(id);
    }

    @PostMapping("/add")
    public Pet createNewPet(@RequestBody Pet pet, @RequestHeader("login") String login){
        User user = userService.getCurrentUser(login);
        pet.setPetOwner(user.getProfile());
       return petProfileService.createPetProfile(pet);
    }

    @PostMapping("/update")
    public void updatePet(@RequestBody Pet pet){
        petProfileService.updatePet(pet);
    }

    @PostMapping("/delete")
    public void deletePet(@RequestBody BigInteger petId){
        petProfileService.deleteProfile(petId);
    }
}
