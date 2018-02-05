package com.netcracker.service.groups.impl;

import com.netcracker.dao.manager.query.Query;
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

import static com.netcracker.dao.manager.query.Query.IGNORING_DELETED_ELEMENTS;
import static com.netcracker.dao.manager.query.Query.IGNORING_DELETED_ELEMENTS_IN_REF;

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
    private String allGroupsQuery = "SELECT object_id " +
            "FROM Objects " +
            "WHERE object_id in (SELECT OBJECT_ID FROM OBJREFERENCE " +
            "                       WHERE ATTRTYPE_ID = " + GroupConstant.GR_ADMIN + " AND " + Query.IGNORING_DELETED_ELEMENTS_IN_REF +
            " )" +
            "and object_type_id = ";

    @Override
    public Group getGroup(BigInteger groupId) {
        return entityManagerService.getById(groupId, Group.class);
    }

    @Override
    public int getMyGroupsPageCount(BigInteger profileId) {
        Integer profileGroupsCapacity = new Integer(allGroupsPageCapacity);
        String query = "SELECT OBJECT_ID FROM OBJREFERENCE " +
                "WHERE REFERENCE = " + profileId + " AND ATTRTYPE_ID = " + GroupConstant.GR_PARTICIPANTS +
                " and object_id in (select object_id from objreference " +
                "                   where  ATTRTYPE_ID = " + GroupConstant.GR_ADMIN +"" +
                "                   and " + IGNORING_DELETED_ELEMENTS_IN_REF +")"+
                " and " + IGNORING_DELETED_ELEMENTS_IN_REF;
        return pageCounterService.getPageCount(profileGroupsCapacity, entityManagerService.getBySqlCount(query));
    }

    @Override
    public List<Group> getMyGroups(BigInteger profileId) {
        String sqlQuery = "SELECT OBJECT_ID FROM OBJREFERENCE " +
                "WHERE REFERENCE = " + profileId + " AND ATTRTYPE_ID = " + GroupConstant.GR_PARTICIPANTS +
                " and object_id in (select object_id from objreference " +
                "                   where  ATTRTYPE_ID = " + GroupConstant.GR_ADMIN +"" +
                "                   and " + IGNORING_DELETED_ELEMENTS_IN_REF +")"+
                " and " + IGNORING_DELETED_ELEMENTS_IN_REF;
        return entityManagerService.getObjectsBySQL(sqlQuery, Group.class, new QueryDescriptor());
    }

    @Override
    public List<Group> getMyGroupsList(BigInteger profileId, Integer page) {
        String sqlQuery = "SELECT OBJECT_ID FROM OBJREFERENCE " +
                "WHERE REFERENCE = " + profileId + " AND ATTRTYPE_ID = " + GroupConstant.GR_PARTICIPANTS +
                " and object_id in (select object_id from objreference " +
                "                   where  ATTRTYPE_ID = " + GroupConstant.GR_ADMIN +"" +
                "                   and " + IGNORING_DELETED_ELEMENTS_IN_REF +")"+
                " and " + IGNORING_DELETED_ELEMENTS_IN_REF;
        QueryDescriptor descriptor = new QueryDescriptor();
        descriptor.addPagingDescriptor(page, Integer.valueOf(allGroupsPageCapacity));
        return entityManagerService.getObjectsBySQL(sqlQuery, Group.class, descriptor);
    }

    @Override
    public List<Profile> getGroupSubscribers(BigInteger groupId) {
        String sqlQuery = "SELECT REFERENCE FROM OBJREFERENCE " +
                "WHERE OBJECT_ID = " + groupId + " AND ATTRTYPE_ID = " + GroupConstant.GR_PARTICIPANTS +
                " and " + IGNORING_DELETED_ELEMENTS_IN_REF;
        return entityManagerService.getObjectsBySQL(sqlQuery, Profile.class, new QueryDescriptor());
    }

    @Override
    public Profile getGroupAdmin(BigInteger groupId) {
        String sqlQuery = "SELECT REFERENCE FROM OBJREFERENCE " +
                "WHERE OBJECT_ID = " + groupId + " AND ATTRTYPE_ID = " + GroupConstant.GR_ADMIN +
                " and " + IGNORING_DELETED_ELEMENTS;
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
                "WHERE REFERENCE = " + profileId + " AND ATTRTYPE_ID = " + GroupConstant.GR_ADMIN +
                " and " + IGNORING_DELETED_ELEMENTS_IN_REF;
        return pageCounterService.getPageCount(profileGroupsCapacity, entityManagerService.getBySqlCount(query));
    }

    @Override
    public List<Group> getAdministerGroupsList(BigInteger profileId, Integer page) {
        String query = "SELECT OBJECT_ID FROM OBJREFERENCE " +
                "WHERE REFERENCE = " + profileId + " AND ATTRTYPE_ID = " + GroupConstant.GR_ADMIN +
                " and " + IGNORING_DELETED_ELEMENTS_IN_REF;
        QueryDescriptor descriptor = new QueryDescriptor();
        descriptor.addPagingDescriptor(page, Integer.valueOf(allGroupsPageCapacity));
        return entityManagerService.getObjectsBySQL(query, Group.class, descriptor);
    }

    @Override
    public int getAllGroupsPageCount() {
        Integer allGroupsCapacity = new Integer(allGroupsPageCapacity);
        return pageCounterService.getPageCount(allGroupsCapacity,
                entityManagerService.getBySqlCount(allGroupsQuery + BigInteger.valueOf(GroupConstant.GR_TYPE)));
    }

    @Override
    public List<Group> getAllGroups() {
        return entityManagerService.getObjectsBySQL(allGroupsQuery + BigInteger.valueOf(GroupConstant.GR_TYPE), Group.class, new QueryDescriptor());
    }

    @Override
    public List<Group> getAllGroupsList(Integer page) {
        Integer allGroupsCapacity = new Integer(allGroupsPageCapacity);
        QueryDescriptor descriptor = new QueryDescriptor();
        descriptor.addPagingDescriptor(page, allGroupsCapacity);
        return entityManagerService.getObjectsBySQL(allGroupsQuery + BigInteger.valueOf(GroupConstant.GR_TYPE), Group.class, descriptor);
    }

    @Override
    public void subscribe(BigInteger groupId, BigInteger profileId) {
        entityManagerService.insertObjref(GroupConstant.GR_PARTICIPANTS, profileId, groupId);
    }

    @Override
    public List<Group> getProfileCreatedGroups(BigInteger profileId) {
        String sqlQuery = "SELECT OBJECT_ID FROM OBJREFERENCE " +
                "WHERE REFERENCE = " + profileId + " AND ATTRTYPE_ID = " + GroupConstant.GR_ADMIN +
                " and " + IGNORING_DELETED_ELEMENTS_IN_REF;
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
}
