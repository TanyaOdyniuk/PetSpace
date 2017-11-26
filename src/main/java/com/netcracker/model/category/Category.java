package com.netcracker.model.category;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.user.UserType;

import java.util.List;
import java.util.Set;

@ObjectType(value = 4)
public enum Category {
    CATEGORY_ONE("Category 1"),
    CATEGORY_TWO("Category 2"),
    CATEGORY_THREE("Category 3");

    @Attribute(value = 17)
    private String type;
    @Attribute(value = 18)
    private Set<UserType> userTypes;
    @Reference(value = 12)
    private List<Advertisement> advertisements;

    Category(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public Set<UserType> getUserTypes() {
        return userTypes;
    }

    public void setUserTypes(Set<UserType> userTypes) {
        this.userTypes = userTypes;
    }

    public List<Advertisement> getAdvertisements() {
        return advertisements;
    }

    public void setAdvertisements(List<Advertisement> advertisements) {
        this.advertisements = advertisements;
    }
}
