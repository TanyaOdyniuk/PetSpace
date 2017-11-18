package com.netcracker.model.category;

import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.user.UserType;

import java.util.List;
import java.util.Set;

public enum Category {
    CATEGORY_ONE("Category 1"),
    CATEGORY_TWO("Category 2"),
    CATEGORY_THREE("Category 3");

    private String type;
    private Set<UserType> userTypes;
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
