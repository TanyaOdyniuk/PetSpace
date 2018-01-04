package com.netcracker.model.group;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.status.Status;
import com.netcracker.model.record.GroupRecord;
import com.netcracker.model.user.Profile;
import java.util.List;
import java.util.Set;

@ObjectType(GroupConstant.GR_TYPE)
public class Group extends BaseEntity {

    @Attribute(GroupConstant.GR_NAME)
    private String groupName;
    @Attribute(GroupConstant.GR_DESCR)
    private String groupDescription;
    @Reference(value = GroupConstant.GR_GROUPTYPE, isParentChild = 0)
    private GroupType groupType;
    @Reference(value = GroupConstant.GR_STATUS, isParentChild = 0)
    private Status groupStatus;
    @Reference(value = GroupConstant.GR_ADMIN, isParentChild = 1)
    private Profile groupAdmin;
    @Attribute(GroupConstant.GR_AVATAR)
    private String groupAvatar;

    public Group() {
    }

    public Group(String name) {
        super(name);
    }

    public Group(String name, String description) {
        super(name, description);
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public GroupType getGroupType() {
        return groupType;
    }

    public void setGroupType(GroupType groupType) {
        this.groupType = groupType;
    }

    public Status getGroupStatus() {
        return groupStatus;
    }

    public void setGroupStatus(Status groupStatus) {
        this.groupStatus = groupStatus;
    }

    public Profile getGroupAdmin() {
        return groupAdmin;
    }

    public void setGroupAdmin(Profile groupAdmin) {
        this.groupAdmin = groupAdmin;
    }

    public String getGroupAvatar() {
        return groupAvatar;
    }

    public void setGroupAvatar(String groupAvatar) {
        this.groupAvatar = groupAvatar;
    }

    @Override
    public String toString() {
        return "Group{" +
                "groupName='" + groupName + '\'' +
                ", groupAvatar='" + groupAvatar + '\'' +
                ", groupDescription='" + groupDescription + '\'' +
                ", groupType=" + groupType +
                ", groupStatus=" + groupStatus +
                ", groupAdmin=" + groupAdmin +
                '}';
    }
}
