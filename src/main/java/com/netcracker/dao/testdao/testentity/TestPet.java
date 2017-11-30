package com.netcracker.dao.testdao.testentity;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.Boolean;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;

import java.sql.Date;

@ObjectType(3)
public class TestPet extends BaseEntity {
    @Attribute(9)
    private Integer age;
    @Attribute(10)
    private String petname;
    @Attribute(11)
    private Date birth;
    @Reference(7)
    private TestUser owner;
    @Boolean(value = 13, yesno = "yes")
    private java.lang.Boolean good;

    public TestPet() {
    }

    public TestPet(String name) {
        super(name);
    }

    public TestPet(String name, String description) {
        super(name, description);
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPetname() {
        return petname;
    }

    public void setPetname(String petname) {
        this.petname = petname;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public TestUser getOwner() {
        return owner;
    }

    public void setOwner(TestUser owner) {
        this.owner = owner;
    }

    public java.lang.Boolean getGood() {
        return good;
    }

    public void setGood(java.lang.Boolean good) {
        this.good = good;
    }

}
