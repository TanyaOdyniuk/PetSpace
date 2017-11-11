package com.netcracker.model.user;

import com.netcracker.model.Model;
import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.comment.AbstractComment;
import com.netcracker.model.comment.GroupComment;
import com.netcracker.model.comment.PhotoComment;
import com.netcracker.model.comment.WallComment;
import com.netcracker.model.group.Group;
import com.netcracker.model.like.Like;
import com.netcracker.model.service.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class User extends Model {

    private String login;
    private String password;
    private Profile profile;
    private UserType userType;
    private List<Advertisement> userAdvertisements;
    private List<Group> userGroups;
    private List<PhotoComment> userPhotoComments;
    private List<WallComment> userWallComments;
    private List<GroupComment> userGroupComments;
    private List<Like> likes;
    private List<Service> services;

    public User() {
    }

    public User(String name) {
        super(name);
    }

    public User(String name, String description) {
        super(name, description);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public List<Advertisement> getUserAdvertisements() {
        return userAdvertisements;
    }

    public void setUserAdvertisements(List<Advertisement> userAdvertisements) {
        this.userAdvertisements = userAdvertisements;
    }

    public List<Group> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(List<Group> userGroups) {
        this.userGroups = userGroups;
    }

    public List<PhotoComment> getUserPhotoComments() {
        return userPhotoComments;
    }

    public void setUserPhotoComments(List<PhotoComment> userPhotoComments) {
        this.userPhotoComments = userPhotoComments;
    }

    public List<WallComment> getUserWallComments() {
        return userWallComments;
    }

    public void setUserWallComments(List<WallComment> userWallComments) {
        this.userWallComments = userWallComments;
    }

    public List<GroupComment> getUserGroupComments() {
        return userGroupComments;
    }

    public void setUserGroupComments(List<GroupComment> userGroupComments) {
        this.userGroupComments = userGroupComments;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", profile=" + profile +
                ", userType=" + userType +
                ", userAdvertisements=" + userAdvertisements +
                ", userGroups=" + userGroups +
                ", userPhotoComments=" + userPhotoComments +
                ", userWallComments=" + userWallComments +
                ", userGroupComments=" + userGroupComments +
                ", likes=" + likes +
                ", services=" + services +
                '}';
    }
}
