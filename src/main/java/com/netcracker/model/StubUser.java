package com.netcracker.model;

/**
 * Created by Odyniuk on 06/11/2017.
 */
public class StubUser {
    public static int objectCount;
    private Integer id;

    private String firstName;

    private String lastName;

    public StubUser() {
        firstName = "";
        lastName = "";
    }
    public StubUser(Integer id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return String.format("Customer[id=%d, firstName='%s', lastName='%s']", id,
                firstName, lastName);
    }

}
