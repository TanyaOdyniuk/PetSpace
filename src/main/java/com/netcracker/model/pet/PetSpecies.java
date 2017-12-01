package com.netcracker.model.pet;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;

import java.util.List;

@ObjectType(PetConstant.PETSPEC_TYPE)
public class PetSpecies extends BaseEntity {

    @Attribute(PetConstant.PETSPEC_NAME)
    private String speciesName;
    //TODO SERVICE GET PET LIST BY SPECIES
    @Reference(PetConstant.PET_SPECOFPET)
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
