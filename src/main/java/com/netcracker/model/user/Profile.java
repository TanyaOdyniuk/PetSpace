package com.netcracker.model.user;

import com.netcracker.model.Model;

import java.math.BigInteger;


public class Profile extends Model{

    private static final byte defaultPets = 2;
    private BigInteger avatarUserId;
    private String userName;
    private String userSurname;
    private int age;
    private String userEmail;
    private String userSkype;
    private String userHobbies;
    private BigInteger[] usersPetsId ;
    private double userRating;

    public Profile(String userName, String userSurname, String userEmail) {
        this.userName = userName;
        this.userSurname = userSurname;
        this.userEmail = userEmail;
        this.usersPetsId = new BigInteger[defaultPets];
    }

    public BigInteger getAvatarUserId() { return avatarUserId; }

    public String getUserName() { return userName; }

    public String getUserSurname() { return userSurname; }

    public int getAge() { return age; }

    public String getUserEmail() { return userEmail; }

    public String getUserSkype() { return userSkype; }

    public String getUserHobbies() { return userHobbies; }

    public BigInteger[] getUsersPetsId() { return usersPetsId; }

    public double getUserRating() { return userRating; }

    public void setAvatarUserId(BigInteger avatarUserId) { this.avatarUserId = avatarUserId; }

    public void setUserName(String userName) { this.userName = userName; }

    public void setUserSurname(String userSurname) { this.userSurname = userSurname; }

    public void setAge(int age) { this.age = age; }

    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public void setUserSkype(String userSkype) { this.userSkype = userSkype; }

    public void setUserHobbies(String userHobbies) { this.userHobbies = userHobbies; }

    public void setUsersPetsId(BigInteger[] usersPetsId) { this.usersPetsId = usersPetsId; }

    public void setUserRating(double userRating) { this.userRating = userRating; }



}
