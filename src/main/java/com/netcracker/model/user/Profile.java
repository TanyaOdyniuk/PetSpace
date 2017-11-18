package com.netcracker.model.user;

import com.netcracker.model.BaseEntity;
import com.netcracker.model.Status;
import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.comment.Comment;
import com.netcracker.model.group.Group;
import com.netcracker.model.like.LikeDislike;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.record.GroupWallRecord;



import java.util.List;
import java.util.Set;


public class Profile extends BaseEntity {

    private String profileAvatar;
    private String profileName;
    private String profileSurname;
    private Integer profileAge;
    private List<String> profileHobbies;
    private List<String> profileFavouriteBreeds;
    private Double profileCurrencyBalance;
    private Status profileStatus;
    private User profileUser;
    //TODO SERVICE GETPETS
    private List<Pet> profilePets;
    //TODO SERVICE GETWALLRECORDS
    private List<GroupWallRecord> profileGroupWallRecords;
    //TODO SERVICE GETPROFILEADVERTISEMENTS
    private List<Advertisement> profileAdvertisements;
    //TODO SERVICE GETPROFILEGROUPS
    private Set<Group> profileGroups;
    //TODO SERVICE GETCOMMENTS
    private List<Comment> profileComments;
    //TODO SERVICE GETLIKES
    private List<LikeDislike> profileLikes;
    //TODO SERVICE GETDISLIKES
    private List<LikeDislike> profileDislikes;


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

    public List<GroupWallRecord> getProfileGroupWallRecords() {
        return profileGroupWallRecords;
    }

    public void setProfileGroupWallRecords(List<GroupWallRecord> profileGroupWallRecords) {
        this.profileGroupWallRecords = profileGroupWallRecords;
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
                ", profileGroupWallRecords=" + profileGroupWallRecords +
                ", profileAdvertisements=" + profileAdvertisements +
                ", profileGroups=" + profileGroups +
                ", profileComments=" + profileComments +
                ", profileLikes=" + profileLikes +
                ", profileDislikes=" + profileDislikes +
                '}';
    }
}
