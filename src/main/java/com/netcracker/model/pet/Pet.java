package com.netcracker.model.pet;

import com.netcracker.model.Model;

import java.math.BigInteger;

public class Pet extends Model {

    //objectId in Model is for petId
    private BigInteger avatarId;
    private String petName;
    private int age;
    private BigInteger speciesId;
    private String breed;
    private double weight;
    private double height;
    private String specificParam;
    private BigInteger userId;
    private BigInteger petStateId;

    public Pet() {
    }

    public Pet(String name) {
        super(name);
    }

    public Pet(String name, String description) {
        super(name, description);
    }

    public BigInteger getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(BigInteger avatarId) {
        this.avatarId = avatarId;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public BigInteger getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(BigInteger speciesId) {
        this.speciesId = speciesId;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getSpecificParam() {
        return specificParam;
    }

    public void setSpecificParam(String specificParam) {
        this.specificParam = specificParam;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public BigInteger getPetStateId() {
        return petStateId;
    }

    public void setPetStateId(BigInteger petStateId) {
        this.petStateId = petStateId;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "avatarId=" + avatarId +
                ", name='" + petName + '\'' +
                ", age=" + age +
                ", speciesId=" + speciesId +
                ", breed='" + breed + '\'' +
                ", weight=" + weight +
                ", height=" + height +
                ", specificParam='" + specificParam + '\'' +
                ", userId=" + userId +
                ", petStateId=" + petStateId +
                '}';
    }
}
