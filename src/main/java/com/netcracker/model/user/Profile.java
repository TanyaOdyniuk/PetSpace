package com.netcracker.model.user;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.Status;
import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.advertisement.AdvertisementConstant;
import com.netcracker.model.comment.AbstractComment;
import com.netcracker.model.comment.CommentConstant;
import com.netcracker.model.group.Group;
import com.netcracker.model.group.GroupConstant;
import com.netcracker.model.like.AbstractLikeDislike;
import com.netcracker.model.like.LikeConstant;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.pet.PetConstant;
import com.netcracker.model.record.WallRecord;
import com.netcracker.model.service.Service;

import java.util.List;
import java.util.Set;

@ObjectType(UsersProfileConstant.PROFILE_TYPE)
public class Profile extends BaseEntity {

    @Attribute(UsersProfileConstant.PROFILE_AVATAR)
    private String profileAvatar;
    @Attribute(UsersProfileConstant.PROFILE_NAME)
    private String profileName;
    @Attribute(UsersProfileConstant.PROFILE_SURNAME)
    private String profileSurname;
    @Attribute(UsersProfileConstant.PROFILE_AGE)
    private Integer profileAge;
    @Attribute(UsersProfileConstant.PROFILE_HOBBIE)
    private List<String> profileHobbies;
    @Attribute(UsersProfileConstant.PROFILE_FAVBREEDS)
    private List<String> profileFavouriteBreeds;
    @Attribute(UsersProfileConstant.PROFILE_CURRBALANCE)
    private Double profileCurrencyBalance;
    @Reference(UsersProfileConstant.PROFILE_STATUS)
    private Status profileStatus;
    @Reference(UsersProfileConstant.USER_PROFILE)
    private User profileUser;
    //TODO SERVICE GETPETS
    @Reference(PetConstant.PET_OWNER) //303
    private List<Pet> profilePets; //нет аттрибута
    //TODO SERVICE GETWALLRECORDS
    @Attribute(UsersProfileConstant.PROFILE_WALLREC)
    private List<WallRecord> profileWallRecords;
    //TODO SERVICE GETPROFILEADVERTISEMENTS
    @Reference(AdvertisementConstant.AD_AUTHOR)
    private List<Advertisement> profileAdvertisements;
    //TODO SERVICE GETPROFILEGROUPS
    @Reference(GroupConstant.GR_PROFILE)
    private Set<Group> profileGroups;
    //TODO SERVICE GETCOMMENTS
    @Reference(CommentConstant.COM_AUTOR)
    private List<AbstractComment> profileComments;
    //TODO SERVICE GETLIKES
    @Reference(LikeConstant.LDL_AUTOR)
    private List<AbstractLikeDislike> profileLikes;
    //TODO SERVICE GETDISLIKES
    @Reference(LikeConstant.LDL_AUTOR)
    private List<AbstractLikeDislike> profileDislikes;
    //TODO SERVICE GETSERVICES
    @Attribute(UsersProfileConstant.PROFILE_SERVICES)
    private Set<Service> services;
    //TODO SERVICE GETADMINISTRATEDGROUPS
    @Reference(GroupConstant.GR_ADMIN)
    private List<Group> profileAdministratedGroups;

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

    public List<WallRecord> getProfileWallRecords() {
        return profileWallRecords;
    }

    public void setProfileWallRecords(List<WallRecord> profileWallRecords) {
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

    public List<AbstractComment> getProfileComments() {
        return profileComments;
    }

    public void setProfileComments(List<AbstractComment> profileComments) {
        this.profileComments = profileComments;
    }

    public List<AbstractLikeDislike> getProfileLikes() {
        return profileLikes;
    }

    public void setProfileLikes(List<AbstractLikeDislike> profileLikes) {
        this.profileLikes = profileLikes;
    }

    public List<AbstractLikeDislike> getProfileDislikes() {
        return profileDislikes;
    }

    public void setProfileDislikes(List<AbstractLikeDislike> profileDislikes) {
        this.profileDislikes = profileDislikes;
    }

    public Set<Service> getServices() {
        return services;
    }

    public void setServices(Set<Service> services) {
        this.services = services;
    }

    public List<Group> getProfileAdministratedGroups() {
        return profileAdministratedGroups;
    }

    public void setProfileAdministratedGroups(List<Group> profileAdministratedGroups) {
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
