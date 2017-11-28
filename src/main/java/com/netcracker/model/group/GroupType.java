package com.netcracker.model.group;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;

import java.util.List;

@ObjectType(value = 405)
public class GroupType extends BaseEntity {

    @Attribute(value = 429)
    private String groupType;
    @Reference(value = 426)
    private List<Group> groups;

    public GroupType() {
    }

    public GroupType(String name) {
        super(name);
    }

    public GroupType(String name, String description) {
        super(name, description);
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "GroupType{" +
                "groupType='" + groupType + '\'' +
                ", groups=" + groups +
                '}';
    }
}
