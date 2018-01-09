package com.netcracker.service.groups.impl;

import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.model.group.Group;
import com.netcracker.model.group.GroupConstant;
import com.netcracker.model.group.GroupType;
import com.netcracker.model.user.Profile;
import com.netcracker.service.groups.GroupService;
import com.netcracker.service.status.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService{
    @Autowired
    EntityManagerService entityManagerService;
    @Autowired
    StatusService statusService;

    @Override
    public Group getGroup(BigInteger groupId) {
        Group group = entityManagerService.getById(groupId, Group.class);
        return group;
    }

    @Override
    public GroupType getGroupType(BigInteger groupId) {
        GroupType groupType = ((Group)entityManagerService.getById(groupId, Group.class)).getGroupType();
        return entityManagerService.getById(groupType.getObjectId(), GroupType.class);
    }

    @Override
    public List<Group> getMyGroupsList(BigInteger profileId) {
        String sqlQuery = "SELECT OBJECT_ID FROM OBJREFERENCE " +
                "WHERE REFERENCE = " + profileId + " AND ATTRTYPE_ID = " + GroupConstant.GR_ADMIN;
        return entityManagerService.getObjectsBySQL(sqlQuery, Group.class, new QueryDescriptor());
    }

    @Override
    public List<Profile> getGroupSubscribers(BigInteger groupId) {
        String sqlQuery = "SELECT REFERENCE FROM OBJREFERENCE " +
                "WHERE OBJECT_ID = " + groupId + " AND ATTRTYPE_ID = " + GroupConstant.GR_PARTICIPANTS;
        return entityManagerService.getObjectsBySQL(sqlQuery, Profile.class, new QueryDescriptor());
    }

    @Override
    public Profile getGroupAdmin(BigInteger groupId) {
        String sqlQuery = "SELECT REFERENCE FROM OBJREFERENCE " +
                "WHERE OBJECT_ID = " + groupId + " AND ATTRTYPE_ID = " + GroupConstant.GR_ADMIN;
        List<Profile> groupAdmin = entityManagerService.getObjectsBySQL(sqlQuery, Profile.class, new QueryDescriptor());
        return groupAdmin.get(0);
    }

    @Override
    public Group createNewGroup(Group newGroup, BigInteger profileId) {
        Profile owner = entityManagerService.getById(profileId, Profile.class);
        newGroup.setGroupAdmin(owner);
        newGroup.setGroupStatus(statusService.getActiveStatus());
        List<Profile> participants = new ArrayList<>();
        participants.add(owner);
        newGroup.setGroupParticipants(participants);
        newGroup.setName("group");
        return entityManagerService.create(newGroup);
    }

    @Override
    public List<GroupType> getAllGroupTypes() {
        return entityManagerService.getAll(
                BigInteger.valueOf(GroupConstant.GRT_TYPE), GroupType.class, new QueryDescriptor());
    }

    @Override
    public void editGroup(Group groupForChangeOnlyAdmin) {

    }

    @Override
    public void deleteGroup(Group group) {

    }

    @Override
    public void empowerForGroup(Group group, BigInteger userId) {

    }

    @Override
    public void setAccessLevelOfGroup(Group group, GroupType groupType) {

    }

    @Override
    public void inviteUserToGroup(Profile invited, Group linkToGroup) {

    }

    @Override
    public void removeParticipantFromGroup(Group group, BigInteger userID) {

    }

    @Override
    public boolean confirmInvitationToGroup(Profile invited) {
        return false;
    }
}
