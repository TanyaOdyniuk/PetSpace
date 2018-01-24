package com.netcracker.controller.species;

import com.netcracker.model.pet.PetSpecies;
import com.netcracker.service.petprofile.PetProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/species")
public class SpeciesDataController {

    @Autowired
    private PetProfileService petProfileService;

    @GetMapping
    public List<PetSpecies> getAllSpecies() {
        return petProfileService.getAllSpecies();
    }
}
