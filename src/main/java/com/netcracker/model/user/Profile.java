package com.netcracker.model.user;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.status.Status;

import java.math.BigDecimal;
import java.util.List;

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
    private BigDecimal profileCurrencyBalance;
    @Reference(value = UsersProfileConstant.PROFILE_STATUS, isParentChild = 0)
    private Status profileStatus;
    @Reference(value = UsersProfileConstant.USER_PROFILE, isParentChild = 0)
    private User profileUser;

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

    public BigDecimal getProfileCurrencyBalance() {
        return profileCurrencyBalance;
    }

    public void setProfileCurrencyBalance(BigDecimal profileCurrencyBalance) {
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

    public String getProfileFullName(){
        return this.profileName + " " + this.profileSurname;
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
                '}';
    }
}
