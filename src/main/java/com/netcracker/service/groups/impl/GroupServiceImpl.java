package com.netcracker.service.groups.impl;

import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.model.group.Group;
import com.netcracker.model.group.GroupType;
import com.netcracker.model.user.Profile;
import com.netcracker.service.groups.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService{
    @Autowired
    EntityManagerService entityManagerService;

    @Override
    public Group getGroup(BigInteger groupId) {
        Group group = entityManagerService.getById(groupId, Group.class);
        return group;
    }

    @Override
    public List<GroupType> getGroupType(BigInteger groupId) {
        String sqlQuery = "SELECT REFERENCE FROM OBJREFERENCE WHERE OBJECT_ID =" + groupId;
        List<GroupType> groupType = entityManagerService.getObjectsBySQL(sqlQuery, GroupType.class, new QueryDescriptor());
        return groupType;
    }

    @Override
    public Group createNewGroup(Profile pfofile, Group newGroup) {
        return null;
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
