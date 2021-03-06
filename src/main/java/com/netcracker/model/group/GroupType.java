package com.netcracker.model.group;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;

import java.util.List;

@ObjectType(GroupConstant.GRT_TYPE)
public class GroupType extends BaseEntity {

    @Attribute(GroupConstant.GRT_NAME)
    private String groupType;

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

    @Override
    public String toString() {
        return "GroupType{" +
                "groupType='" + groupType + '\'' +
                '}';
    }
}
