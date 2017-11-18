package com.netcracker.model.group;

import com.netcracker.model.Model;
import com.netcracker.model.Status;
import com.netcracker.model.record.GroupWallRecord;
import com.netcracker.model.record.old.GroupRecord;
import com.netcracker.model.user.Profile;

import java.util.List;
import java.util.Set;

public class Group extends Model {

    private String groupName;
    private String groupDescription;
    //TODO SERVICE GETGROUPUSERS
    private Set<Profile> groupProfiles;
    private GroupType groupType;
    private Status groupStatus;
    //TODO SERVICE GETGROUPRECORDS
    private List<GroupWallRecord> groupRecords;
    //TODO SERVICE GETGROUPADMINS
    private List<Profile> groupAdmins;

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

    public Set<Profile> getGroupProfiles() {
        return groupProfiles;
    }

    public void setGroupProfiles(Set<Profile> groupProfiles) {
        this.groupProfiles = groupProfiles;
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

    public List<GroupWallRecord> getGroupRecords() {
        return groupRecords;
    }

    public void setGroupRecords(List<GroupWallRecord> groupRecords) {
        this.groupRecords = groupRecords;
    }

    public List<Profile> getGroupAdmins() {
        return groupAdmins;
    }

    public void setGroupAdmins(List<Profile> groupAdmins) {
        this.groupAdmins = groupAdmins;
    }

    @Override
    public String toString() {
        return "Group{" +
                "groupName='" + groupName + '\'' +
                ", groupDescription='" + groupDescription + '\'' +
                ", groupProfiles=" + groupProfiles +
                ", groupType=" + groupType +
                ", groupStatus=" + groupStatus +
                ", groupRecords=" + groupRecords +
                ", groupAdmins=" + groupAdmins +
                '}';
    }
}
