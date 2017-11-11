package com.netcracker.model.pet;

import com.netcracker.model.Model;

import java.util.List;

public class PetSpecies extends Model {

    private String speciesName;
    private List<Pet> petList;

    public PetSpecies() {
    }

    public PetSpecies(String name) {
        super(name);
    }

    public PetSpecies(String name, String description) {
        super(name, description);
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public List<Pet> getPetList() {
        return petList;
    }

    public void setPetList(List<Pet> petList) {
        this.petList = petList;
    }

    @Override
    public String toString() {
        return "PetSpecies{" +
                "speciesName='" + speciesName + '\'' +
                ", petList=" + petList +
                '}';
    }
}
