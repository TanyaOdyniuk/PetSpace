package com.netcracker.model.user;

import com.netcracker.model.Model;
import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.comment.GroupComment;
import com.netcracker.model.comment.PhotoComment;
import com.netcracker.model.comment.WallComment;
import com.netcracker.model.group.Group;
import com.netcracker.model.like.*;
import com.netcracker.model.securityBook.SecurityBook;
import com.netcracker.model.service.Service;

import java.util.List;
import java.util.Set;

public class User extends Model {

    private String login;
    private String password;
    private Profile profile;
    private UserType userType;
    private List<Advertisement> userAdvertisements;
    private Set<Group> userGroups;
    private List<PhotoComment> userPhotoComments;
    private List<WallComment> userWallComments;
    private List<GroupComment> userGroupComments;
    private List<Service> services;
    private List<GroupRecordLikeDislike> groupRecordLikeDislikes;
    private List<GroupCommentLikeDislike> groupCommentLikeDislikes;
    private List<WallRecordLikeDislike> wallRecordLikeDislikes;
    private List<WallCommentLikeDislike> commentLikeDislikes;
    private List<PhotoRecordLikeDislike> photoRecordLikeDislikes;
    private List<PhotoCommentLikeDislike> photoCommentLikeDislikes;
    private Set<SecurityBook> securityBooks;

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

    public Set<Group> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(Set<Group> userGroups) {
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

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public List<GroupRecordLikeDislike> getGroupRecordLikeDislikes() {
        return groupRecordLikeDislikes;
    }

    public void setGroupRecordLikeDislikes(List<GroupRecordLikeDislike> groupRecordLikeDislikes) {
        this.groupRecordLikeDislikes = groupRecordLikeDislikes;
    }

    public List<GroupCommentLikeDislike> getGroupCommentLikeDislikes() {
        return groupCommentLikeDislikes;
    }

    public void setGroupCommentLikeDislikes(List<GroupCommentLikeDislike> groupCommentLikeDislikes) {
        this.groupCommentLikeDislikes = groupCommentLikeDislikes;
    }

    public List<WallRecordLikeDislike> getWallRecordLikeDislikes() {
        return wallRecordLikeDislikes;
    }

    public void setWallRecordLikeDislikes(List<WallRecordLikeDislike> wallRecordLikeDislikes) {
        this.wallRecordLikeDislikes = wallRecordLikeDislikes;
    }

    public List<WallCommentLikeDislike> getCommentLikeDislikes() {
        return commentLikeDislikes;
    }

    public void setCommentLikeDislikes(List<WallCommentLikeDislike> commentLikeDislikes) {
        this.commentLikeDislikes = commentLikeDislikes;
    }

    public Set<SecurityBook> getSecurityBooks() {
        return securityBooks;
    }

    public void setSecurityBooks(Set<SecurityBook> securityBooks) {
        this.securityBooks = securityBooks;
    }

    public List<PhotoRecordLikeDislike> getPhotoRecordLikeDislikes() {
        return photoRecordLikeDislikes;
    }

    public void setPhotoRecordLikeDislikes(List<PhotoRecordLikeDislike> photoRecordLikeDislikes) {
        this.photoRecordLikeDislikes = photoRecordLikeDislikes;
    }

    public List<PhotoCommentLikeDislike> getPhotoCommentLikeDislikes() {
        return photoCommentLikeDislikes;
    }

    public void setPhotoCommentLikeDislikes(List<PhotoCommentLikeDislike> photoCommentLikeDislikes) {
        this.photoCommentLikeDislikes = photoCommentLikeDislikes;
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
                ", services=" + services +
                ", groupRecordLikeDislikes=" + groupRecordLikeDislikes +
                ", groupCommentLikeDislikes=" + groupCommentLikeDislikes +
                ", wallRecordLikeDislikes=" + wallRecordLikeDislikes +
                ", commentLikeDislikes=" + commentLikeDislikes +
                ", photoRecordLikeDislikes=" + photoRecordLikeDislikes +
                ", photoCommentLikeDislikes=" + photoCommentLikeDislikes +
                ", securityBooks=" + securityBooks +
                '}';
    }
}
