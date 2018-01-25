package com.netcracker.service.groups.impl;

import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.model.group.Group;
import com.netcracker.model.group.GroupConstant;
import com.netcracker.model.user.Profile;
import com.netcracker.service.groups.GroupService;
import com.netcracker.service.status.StatusService;
import com.netcracker.service.util.PageCounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    private EntityManagerService entityManagerService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private PageCounterService pageCounterService;
    @Value("${groups.pageCapacity}")
    private String allGroupsPageCapacity;


    @Override
    public Group getGroup(BigInteger groupId) {
        return entityManagerService.getById(groupId, Group.class);
    }

    @Override
    public int getMyGroupsPageCount(BigInteger profileId) {
        Integer profileGroupsCapacity = new Integer(allGroupsPageCapacity);
        String query = "SELECT OBJECT_ID FROM OBJREFERENCE " +
                "WHERE REFERENCE = " + profileId + " AND ATTRTYPE_ID = " + GroupConstant.GR_PARTICIPANTS;
        return pageCounterService.getPageCount(profileGroupsCapacity, entityManagerService.getBySqlCount(query));
    }

    @Override
    public List<Group> getMyGroups(BigInteger profileId) {
        String sqlQuery = "SELECT OBJECT_ID FROM OBJREFERENCE " +
                "WHERE REFERENCE = " + profileId + " AND ATTRTYPE_ID = " + GroupConstant.GR_PARTICIPANTS;
        return entityManagerService.getObjectsBySQL(sqlQuery, Group.class, new QueryDescriptor());
    }

    @Override
    public List<Group> getMyGroupsList(BigInteger profileId, Integer page) {
        String sqlQuery = "SELECT OBJECT_ID FROM OBJREFERENCE " +
                "WHERE REFERENCE = " + profileId + " AND ATTRTYPE_ID = " + GroupConstant.GR_PARTICIPANTS;
        QueryDescriptor descriptor = new QueryDescriptor();
        descriptor.addPagingDescriptor(page, Integer.valueOf(allGroupsPageCapacity));
        return entityManagerService.getObjectsBySQL(sqlQuery, Group.class, descriptor);
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
    public void leaveGroup(BigInteger groupId, BigInteger profileId) {
        entityManagerService.dropRefDual(GroupConstant.GR_PARTICIPANTS, profileId, groupId);
    }

    @Override
    public int getAdministerGroupsPageCount(BigInteger profileId) {
        Integer profileGroupsCapacity = new Integer(allGroupsPageCapacity);
        String query = "SELECT OBJECT_ID FROM OBJREFERENCE " +
                "WHERE REFERENCE = " + profileId + " AND ATTRTYPE_ID = " + GroupConstant.GR_ADMIN;
        return pageCounterService.getPageCount(profileGroupsCapacity, entityManagerService.getBySqlCount(query));
    }

    @Override
    public List<Group> getAdministerGroupsList(BigInteger profileId, Integer page) {
        String query = "SELECT OBJECT_ID FROM OBJREFERENCE " +
                "WHERE REFERENCE = " + profileId + " AND ATTRTYPE_ID = " + GroupConstant.GR_ADMIN;
        QueryDescriptor descriptor = new QueryDescriptor();
        descriptor.addPagingDescriptor(page, Integer.valueOf(allGroupsPageCapacity));
        return entityManagerService.getObjectsBySQL(query, Group.class, descriptor);
    }

    @Override
    public int getAllGroupsPageCount() {
        Integer allGroupsCapacity = new Integer(allGroupsPageCapacity);
        return pageCounterService.getPageCount(allGroupsCapacity,
                entityManagerService.getAllCount(BigInteger.valueOf(GroupConstant.GR_TYPE)));
    }

    @Override
    public List<Group> getAllGroups() {
        return entityManagerService.getAll(BigInteger.valueOf(GroupConstant.GR_TYPE), Group.class, new QueryDescriptor());
    }

    @Override
    public List<Group> getAllGroupsList(Integer page) {
        Integer allGroupsCapacity = new Integer(allGroupsPageCapacity);
        QueryDescriptor descriptor = new QueryDescriptor();
        descriptor.addPagingDescriptor(page, allGroupsCapacity);
        return entityManagerService.getAll(BigInteger.valueOf(GroupConstant.GR_TYPE), Group.class, descriptor);
    }

    @Override
    public void subscribe(BigInteger groupId, BigInteger profileId) {
        entityManagerService.insertObjref(GroupConstant.GR_PARTICIPANTS, profileId, groupId);
    }

    @Override
    public List<Group> getProfileCreatedGroups(BigInteger profileId) {
        String sqlQuery = "SELECT OBJECT_ID FROM OBJREFERENCE " +
                "WHERE REFERENCE = " + profileId + " AND ATTRTYPE_ID = " + GroupConstant.GR_ADMIN;
        return entityManagerService.getObjectsBySQL(sqlQuery, Group.class, new QueryDescriptor());
    }

    @Override
    public void editGroup(Group group) {
        entityManagerService.update(group);
    }
    @Override
    public void deleteGroup(BigInteger groupId) {
        entityManagerService.delete(groupId, -1);
    }

    @Override
    public void empowerForGroup(Group group, BigInteger userId) {

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
