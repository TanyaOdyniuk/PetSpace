package com.netcracker.model.group;

import com.netcracker.model.Model;
import com.netcracker.model.Status;
import com.netcracker.model.record.GroupRecord;
import com.netcracker.model.user.User;

import java.util.List;
import java.util.Set;

public class Group extends Model {

    private String groupName;
    private String groupDescription;
    private Set<User> groupUsers;
    private GroupType groupType;
    private Status groupStatus;
    private List<GroupRecord> groupRecords;

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

    public Set<User> getGroupUsers() {
        return groupUsers;
    }

    public void setGroupUsers(Set<User> groupUsers) {
        this.groupUsers = groupUsers;
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

    public List<GroupRecord> getGroupRecords() {
        return groupRecords;
    }

    public void setGroupRecords(List<GroupRecord> groupRecords) {
        this.groupRecords = groupRecords;
    }

    @Override
    public String toString() {
        return "Group{" +
                "groupName='" + groupName + '\'' +
                ", groupDescription='" + groupDescription + '\'' +
                ", groupUsers=" + groupUsers +
                ", groupType=" + groupType +
                ", groupStatus=" + groupStatus +
                ", groupRecords=" + groupRecords +
                '}';
    }
}
