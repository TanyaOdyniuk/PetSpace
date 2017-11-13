package com.netcracker.model.category;

import com.netcracker.model.user.UserType;

import java.util.Set;

public enum Category {
    CATEGORY_ONE("Category 1"),
    CATEGORY_TWO("Category 2"),
    CATEGORY_THREE("Category 3");

    private String type;
    private Set<UserType> userTypes;

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
}
