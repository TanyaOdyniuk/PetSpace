package com.netcracker.model.user;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.securitybook.SecurityBook;

import java.util.Set;

@ObjectType(value = 1)
public class User extends BaseEntity {

    @Attribute(value = 1)
    private String login;
    @Attribute(value = 2)
    private String password;
    @Attribute(value = 3)
    private Profile profile;
    @Attribute(value = 4)
    private UserType userType;
    //TODO SERVICE GETSECURITYBOOKS
    @Attribute(value = 5)
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

    public Set<SecurityBook> getSecurityBooks() {
        return securityBooks;
    }

    public void setSecurityBooks(Set<SecurityBook> securityBooks) {
        this.securityBooks = securityBooks;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", profile=" + profile +
                ", userType=" + userType +
                ", securityBooks=" + securityBooks +
                '}';
    }
}
