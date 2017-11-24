package com.netcracker.model.group;

import java.util.List;

public enum GroupType {
    OPEN("Open Group"),
    CLOSED("Closed Group");

    private String type;
    private List<Group> groups;

    GroupType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
}
