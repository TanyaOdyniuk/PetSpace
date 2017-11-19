package com.netcracker.model.user;

import com.netcracker.model.Model;
import com.netcracker.model.securityBook.SecurityBook;

import java.util.Set;

public class User extends Model {

    private String login;
    private String password;
    private Profile profile;
    private UserType userType;
    //TODO SERVICE GETSECURITYBOOKS
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
