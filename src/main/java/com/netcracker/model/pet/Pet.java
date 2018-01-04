package com.netcracker.model.pet;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.status.Status;
import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.advertisement.AdvertisementConstant;
import com.netcracker.model.album.PhotoAlbum;
import com.netcracker.model.user.Profile;

import java.util.List;
import java.util.Set;

@ObjectType(PetConstant.PET_TYPE)
public class Pet extends BaseEntity {

    @Attribute(PetConstant.PET_AVATAR)
    private String petAvatar;
    @Attribute(PetConstant.PET_NAME)
    private String petName;
    @Attribute(PetConstant.PET_AGE)
    private Integer petAge;
    @Reference(PetConstant.PET_SPECOFPET)
    private PetSpecies petSpecies;
    @Attribute(PetConstant.PET_BREED)
    private String petBreed;
    @Attribute(PetConstant.PET_WEIGHT)
    private Double petWeight;
    @Attribute(PetConstant.PET_HEIGHT)
    private Double petHeight;
    @Attribute(PetConstant.PET_SPECPARAM)
    private String petSpecificParam;
    @Reference(PetConstant.PET_OWNER)
    private Profile petOwner;
    @Reference(PetConstant.PET_STATE)
    private Status petStatus;

    //TODO SERVICE GETADVERTISEMENTS
    @Reference(AdvertisementConstant.AD_PETS)
    private Set<Advertisement> petAdvertisements;
    //TODO SERVICE GETPHOTOALBUMS
    @Reference(PetConstant.PET_PHOTOALBUM)
    private List<PhotoAlbum> petPhotoAlbums;

    public Pet() {
    }

    public Pet(String name) {
        super(name);
    }

    public Pet(String petAvatar, String petName, Integer petAge, PetSpecies petSpecies, String petBreed, Double petWeight, Double petHeight, String petSpecificParam, Profile petOwner) {
        this.petAvatar = petAvatar;
        this.petName = petName;
        this.petAge = petAge;
        this.petSpecies = petSpecies;
        this.petBreed = petBreed;
        this.petWeight = petWeight;
        this.petHeight = petHeight;
        this.petSpecificParam = petSpecificParam;
        this.petOwner = petOwner;
    }

    public Pet(String petAvatar, String petName, Integer petAge, PetSpecies petSpecies, String petBreed, Double petWeight, Double petHeight, String petSpecificParam) {
        this.petAvatar = petAvatar;
        this.petName = petName;
        this.petAge = petAge;
        this.petSpecies = petSpecies;
        this.petBreed = petBreed;
        this.petWeight = petWeight;
        this.petHeight = petHeight;
        this.petSpecificParam = petSpecificParam;
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

    public Integer getPetAge() {
        return petAge;
    }

    public void setPetAge(Integer petAge) {
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

    public Double getPetWeight() {
        return petWeight;
    }

    public void setPetWeight(Double petWeight) {
        this.petWeight = petWeight;
    }

    public Double getPetHeight() {
        return petHeight;
    }

    public void setPetHeight(Double petHeight) {
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

    public void setPetAdvertisements(Set<Advertisement> petAdvertisements) {
        this.petAdvertisements = petAdvertisements;
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
                //", petOwner=" + petOwner +
                ", petStatus=" + petStatus +
                /*", advertisementPets=" + petAdvertisements +
                ", petPhotoAlbums=" + petPhotoAlbums +*/
                '}';
    }
}
