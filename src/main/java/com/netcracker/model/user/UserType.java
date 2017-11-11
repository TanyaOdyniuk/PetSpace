package com.netcracker.model.user;

enum UserType {
    USER("User"),
    ADMIN("Aamin"),
    VET("vet");

    private String type;

    UserType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
