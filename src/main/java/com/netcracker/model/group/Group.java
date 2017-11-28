package com.netcracker.model.group;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.Status;
import com.netcracker.model.record.GroupRecord;
import com.netcracker.model.user.Profile;
import java.util.List;
import java.util.Set;

@ObjectType(value = 404)
public class Group extends BaseEntity {

    @Attribute(value = 422)
    private String groupName;
    @Attribute(value = 423)
    private String groupDescription;
    //TODO SERVICE GETGROUPUSERS
    @Attribute(value = 425)
    private Set<Profile> groupParticipants;
    @Attribute(value = 426)
    private GroupType groupType;
    @Attribute(value = 427)
    private Status groupStatus;
    //TODO SERVICE GETGROUPRECORDS
    @Attribute(value = 428)
    private List<GroupRecord> groupRecords;
    //TODO SERVICE GETGROUPADMINS
    @Attribute(value = 424)
    private Profile groupAdmin;

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

    public Set<Profile> getGroupParticipants() {
        return groupParticipants;
    }

    public void setGroupParticipants(Set<Profile> groupParticipants) {
        this.groupParticipants = groupParticipants;
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

    public Profile getGroupAdmin() {
        return groupAdmin;
    }

    public void setGroupAdmin(Profile groupAdmin) {
        this.groupAdmin = groupAdmin;
    }

    @Override
    public String toString() {
        return "Group{" +
                "groupName='" + groupName + '\'' +
                ", groupDescription='" + groupDescription + '\'' +
                ", groupParticipants=" + groupParticipants +
                ", groupType=" + groupType +
                ", groupStatus=" + groupStatus +
                ", groupRecords=" + groupRecords +
                ", groupAdmin=" + groupAdmin +
                '}';
    }
}
