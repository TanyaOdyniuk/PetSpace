package com.netcracker.model.user;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.Status;
import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.comment.Comment;
import com.netcracker.model.group.Group;
import com.netcracker.model.like.LikeDislike;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.record.GroupWallRecord;
import com.netcracker.model.service.Service;


import java.util.List;
import java.util.Set;

@ObjectType(value = 403)
public class Profile extends BaseEntity {

    @Attribute(value = 412)
    private String profileAvatar;
    @Attribute(value = 413)
    private String profileName;
    @Attribute(value = 414)
    private String profileSurname;
    @Attribute(value = 415)
    private Integer profileAge;
    @Attribute(value = 416)
    private List<String> profileHobbies;
    @Attribute(value = 417)
    private List<String> profileFavouriteBreeds;
    @Attribute(value = 418)
    private Double profileCurrencyBalance;
    @Reference(value = 419)
    private Status profileStatus;
    @Reference(value = 3)
    private User profileUser;
    //TODO SERVICE GETPETS
    @Reference(value = 201)
    private List<Pet> profilePets; //нет аттрибута
    //TODO SERVICE GETWALLRECORDS
    @Attribute(value = 421)
    private List<GroupWallRecord> profileWallRecords;
    //TODO SERVICE GETPROFILEADVERTISEMENTS
    @Reference(value = 14)
    private List<Advertisement> profileAdvertisements;
    //TODO SERVICE GETPROFILEGROUPS
    @Reference(value = 425)
    private Set<Group> profileGroups;
    //TODO SERVICE GETCOMMENTS
    @Reference(value = 406)
    private List<Comment> profileComments;
    //TODO SERVICE GETLIKES
    @Reference(value = 401)
    private List<LikeDislike> profileLikes;
    //TODO SERVICE GETDISLIKES
    @Reference(value = 401)
    private List<LikeDislike> profileDislikes;
    //TODO SERVICE GETSERVICES
    @Attribute(value = 420)
    private Set<Service> services;
    //TODO SERVICE GETADMINISTRATEDGROUPS
    @Reference(value = 424)
    private Set<Group> profileAdministratedGroups;

    public Profile() {
    }

    public Profile(String name) {
        super(name);
    }

    public Profile(String name, String description) {
        super(name, description);
    }

    public String getProfileAvatar() {
        return profileAvatar;
    }

    public void setProfileAvatar(String profileAvatar) {
        this.profileAvatar = profileAvatar;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileSurname() {
        return profileSurname;
    }

    public void setProfileSurname(String profileSurname) {
        this.profileSurname = profileSurname;
    }

    public Integer getProfileAge() {
        return profileAge;
    }

    public void setProfileAge(Integer profileAge) {
        this.profileAge = profileAge;
    }

    public List<String> getProfileHobbies() {
        return profileHobbies;
    }

    public void setProfileHobbies(List<String> profileHobbies) {
        this.profileHobbies = profileHobbies;
    }

    public List<String> getProfileFavouriteBreeds() {
        return profileFavouriteBreeds;
    }

    public void setProfileFavouriteBreeds(List<String> profileFavouriteBreeds) {
        this.profileFavouriteBreeds = profileFavouriteBreeds;
    }

    public Double getProfileCurrencyBalance() {
        return profileCurrencyBalance;
    }

    public void setProfileCurrencyBalance(Double profileCurrencyBalance) {
        this.profileCurrencyBalance = profileCurrencyBalance;
    }

    public Status getProfileStatus() {
        return profileStatus;
    }

    public void setProfileStatus(Status profileStatus) {
        this.profileStatus = profileStatus;
    }

    public User getProfileUser() {
        return profileUser;
    }

    public void setProfileUser(User profileUser) {
        this.profileUser = profileUser;
    }

    public List<Pet> getProfilePets() {
        return profilePets;
    }

    public void setProfilePets(List<Pet> profilePets) {
        this.profilePets = profilePets;
    }

    public List<GroupWallRecord> getProfileWallRecords() {
        return profileWallRecords;
    }

    public void setProfileWallRecords(List<GroupWallRecord> profileWallRecords) {
        this.profileWallRecords = profileWallRecords;
    }

    public List<Advertisement> getProfileAdvertisements() {
        return profileAdvertisements;
    }

    public void setProfileAdvertisements(List<Advertisement> profileAdvertisements) {
        this.profileAdvertisements = profileAdvertisements;
    }

    public Set<Group> getProfileGroups() {
        return profileGroups;
    }

    public void setProfileGroups(Set<Group> profileGroups) {
        this.profileGroups = profileGroups;
    }

    public List<Comment> getProfileComments() {
        return profileComments;
    }

    public void setProfileComments(List<Comment> profileComments) {
        this.profileComments = profileComments;
    }

    public List<LikeDislike> getProfileLikes() {
        return profileLikes;
    }

    public void setProfileLikes(List<LikeDislike> profileLikes) {
        this.profileLikes = profileLikes;
    }

    public List<LikeDislike> getProfileDislikes() {
        return profileDislikes;
    }

    public void setProfileDislikes(List<LikeDislike> profileDislikes) {
        this.profileDislikes = profileDislikes;
    }

    public Set<Service> getServices() {
        return services;
    }

    public void setServices(Set<Service> services) {
        this.services = services;
    }

    public Set<Group> getProfileAdministratedGroups() {
        return profileAdministratedGroups;
    }

    public void setProfileAdministratedGroups(Set<Group> profileAdministratedGroups) {
        this.profileAdministratedGroups = profileAdministratedGroups;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "profileAvatar='" + profileAvatar + '\'' +
                ", profileName='" + profileName + '\'' +
                ", profileSurname='" + profileSurname + '\'' +
                ", profileAge=" + profileAge +
                ", profileHobbies=" + profileHobbies +
                ", profileFavouriteBreeds=" + profileFavouriteBreeds +
                ", profileCurrencyBalance=" + profileCurrencyBalance +
                ", profileStatus=" + profileStatus +
                ", profileUser=" + profileUser +
                ", profilePets=" + profilePets +
                ", profileWallRecords=" + profileWallRecords +
                ", profileAdvertisements=" + profileAdvertisements +
                ", profileGroups=" + profileGroups +
                ", profileComments=" + profileComments +
                ", profileLikes=" + profileLikes +
                ", profileDislikes=" + profileDislikes +
                ", services=" + services +
                ", profileAdministratedGroups=" + profileAdministratedGroups +
                '}';
    }
}
