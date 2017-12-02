package com.netcracker.controller.petpage;

import com.netcracker.model.pet.Pet;
import com.netcracker.service.petprofile.PetProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@RestController
@RequestMapping("/pet/{id}")
public class PetPageController {

    @Autowired
    PetProfileService petProfileService;

    @GetMapping
    public Pet getPetById(@PathVariable("id") BigInteger id){
        return petProfileService.getPetById(id);
    }
}
