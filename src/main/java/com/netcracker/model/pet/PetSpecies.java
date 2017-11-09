package com.netcracker.model.pet;

import com.netcracker.model.Model;

public class PetSpecies extends Model {

    private String speciesName;

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

    @Override
    public String toString() {
        return "PetSpecies{" +
                "speciesName='" + speciesName + '\'' +
                '}';
    }
}
