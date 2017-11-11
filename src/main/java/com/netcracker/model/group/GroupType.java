package com.netcracker.model.group;

enum GroupType {
    OPEN("Open Group"),
    CLOSED("Closed Group");

    private String type;

    GroupType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
