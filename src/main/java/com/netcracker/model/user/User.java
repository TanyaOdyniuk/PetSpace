package com.netcracker.model.user;

import com.netcracker.model.Model;

import java.math.BigInteger;

public class User extends Model {

    private BigInteger userId;
    private String login;
    private String password;
    private BigInteger profileId;
    private BigInteger userTypeId;

    public BigInteger getUserId() {return userId;}

    public String getLogin() {return login;}

    public String getPassword() {return password;}

    public BigInteger getProfileId() {return profileId;}

    public BigInteger getUserTypeId() {return userTypeId;}

    public void setUserId(BigInteger userId) {this.userId = userId;}

    public void setLogin(String login) {this.login = login;}

    public void setPassword(String password) {this.password = password;}

    public void setProfileId(BigInteger profileId) {this.profileId = profileId;}

    public void setUserTypeId(BigInteger userTypeId) {this.userTypeId = userTypeId;}

}
