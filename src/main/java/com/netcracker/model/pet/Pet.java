package com.netcracker.model.pet;

import com.netcracker.model.Model;
import com.netcracker.model.Status;
import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.album.PhotoAlbum;
import com.netcracker.model.user.Profile;

import java.util.List;
import java.util.Set;

public class Pet extends Model {

    private String petAvatar;
    private String petName;
    private Integer petAge;
    private PetSpecies petSpecies;
    private String petBreed;
    private Double petWeight;
    private Double petHeight;
    private String petSpecificParam;
    private Profile petOwner;
    private Status petStatus;
    //TODO SERVICE GETADVERTISEMENTS
    private Set<Advertisement> petAdvertisements;
    //TODO SERVICE GETPHOTOALBUMS
    private List<PhotoAlbum> petPhotoAlbums;

    public Pet() {
    }

    public Pet(String name) {
        super(name);
    }

    public Pet(String name, String description) {
        super(name, description);
    }

    public String getPetAvatar() {
        return petAvatar;
    }

    public void setPetAvatar(String petAvatar) {
        this.petAvatar = petAvatar;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public int getPetAge() {
        return petAge;
    }

    public void setPetAge(int petAge) {
        this.petAge = petAge;
    }

    public PetSpecies getPetSpecies() {
        return petSpecies;
    }

    public void setPetSpecies(PetSpecies petSpecies) {
        this.petSpecies = petSpecies;
    }

    public String getPetBreed() {
        return petBreed;
    }

    public void setPetBreed(String petBreed) {
        this.petBreed = petBreed;
    }

    public double getPetWeight() {
        return petWeight;
    }

    public void setPetWeight(double petWeight) {
        this.petWeight = petWeight;
    }

    public double getPetHeight() {
        return petHeight;
    }

    public void setPetHeight(double petHeight) {
        this.petHeight = petHeight;
    }

    public String getPetSpecificParam() {
        return petSpecificParam;
    }

    public void setPetSpecificParam(String petSpecificParam) {
        this.petSpecificParam = petSpecificParam;
    }

    public Profile getPetOwner() {
        return petOwner;
    }

    public void setPetOwner(Profile petOwner) {
        this.petOwner = petOwner;
    }

    public Status getPetStatus() {
        return petStatus;
    }

    public void setPetStatus(Status petStatus) {
        this.petStatus = petStatus;
    }

    public Set<Advertisement> getPetAdvertisements() {
        return petAdvertisements;
    }

    public void setAdvertisementPets(Set<Advertisement> advertisements) {
        this.petAdvertisements = advertisements;
    }

    public List<PhotoAlbum> getPetPhotoAlbums() {
        return petPhotoAlbums;
    }

    public void setPetPhotoAlbums(List<PhotoAlbum> petPhotoAlbums) {
        this.petPhotoAlbums = petPhotoAlbums;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "petAvatar=" + petAvatar +
                ", petName='" + petName + '\'' +
                ", petAge=" + petAge +
                ", petSpecies=" + petSpecies +
                ", petBreed='" + petBreed + '\'' +
                ", petWeight=" + petWeight +
                ", petHeight=" + petHeight +
                ", petSpecificParam='" + petSpecificParam + '\'' +
                ", petOwner=" + petOwner +
                ", petStatus=" + petStatus +
                ", advertisementPets=" + petAdvertisements +
                ", petPhotoAlbums=" + petPhotoAlbums +
                '}';
    }
}
