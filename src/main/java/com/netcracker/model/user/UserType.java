package com.netcracker.model.user;

import com.netcracker.model.category.Category;

import java.util.List;
import java.util.Set;

public enum UserType {
    USER("User"),
    ADMIN("Admin"),
    VET("Vet");

    private String type;
    private Set<Category> categories;
    private List<User> users;

    UserType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
