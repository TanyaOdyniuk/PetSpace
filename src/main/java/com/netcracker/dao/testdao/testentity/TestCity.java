package com.netcracker.dao.testdao.testentity;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;

import java.util.Set;

@ObjectType(2)
public class TestCity extends BaseEntity {
    @Attribute(12)
    private String cityName;
    @Reference(8)
    private Set<TestUser> myUsers;

    public TestCity() {
    }

    public TestCity(String name) {
        super(name);
    }

    public TestCity(String name, String description) {
        super(name, description);
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Set<TestUser> getMyUsers() {
        return myUsers;
    }

    public void setMyUsers(Set<TestUser> myUsers) {
        this.myUsers = myUsers;
    }
}
