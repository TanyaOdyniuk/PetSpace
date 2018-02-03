package com.netcracker.service.managefriends.impl;

import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.model.status.StatusConstant;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.request.FriendRequestConstant;
import com.netcracker.model.user.Profile;
import com.netcracker.model.user.User;
import com.netcracker.model.user.UsersProfileConstant;
import com.netcracker.service.managefriends.ManageFriendService;
import com.netcracker.service.user.impl.UserDetailsServiceImpl;
import com.netcracker.service.util.PageCounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static com.netcracker.dao.manager.query.Query.IGNORING_DELETED_ELEMENTS_IN_REF;

@Service
public class ManageFriendServiceImpl implements ManageFriendService {

    @Value("${friendlist.pageCapacity}")
    String friendListPageCapacity;

    @Autowired
    private EntityManagerService entityManagerService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PageCounterService pageCounterService;

    @Override
    public void addFriend(Profile profile) {

    }

    @Override
    public void deleteFriend(Profile profile) {

    }

    @Override
    public Pet checkFriendsPets(Profile profile) {
        return null;
    }

    @Override
    public void sortFriendList(SortListFriendMod sortMod) {
    }

    @Override
    public List<Profile> getFriendList(BigInteger profileId) {
        String sqlQuery = "SELECT REFERENCE " +
                "FROM OBJREFERENCE " +
                "WHERE OBJECT_ID IN (" +
                "SELECT OBJECT_ID " +
                "FROM OBJREFERENCE " +
                "WHERE REFERENCE = '" + StatusConstant.ST_IS_FRIEND + "'" +
                "AND OBJECT_ID IN (" +
                "SELECT OBJECT_ID " +
                "FROM OBJREFERENCE " +
                "WHERE REFERENCE = '" + profileId + "'" +
                " AND ATTRTYPE_ID IN ('" + FriendRequestConstant.REQ_FROM + "', '" + FriendRequestConstant.REQ_TO + "')))" +
                " AND ATTRTYPE_ID IN ('" + FriendRequestConstant.REQ_FROM + "', '" + FriendRequestConstant.REQ_TO + "')" +
                " AND REFERENCE <> '" + profileId + "'" +
                " and " + IGNORING_DELETED_ELEMENTS_IN_REF;
        return entityManagerService.getObjectsBySQL(sqlQuery, Profile.class, new QueryDescriptor());
    }

    @Override
    public List<Profile> getFriendList(BigInteger profileId, int page) {
        String sqlQuery = "SELECT REFERENCE " +
                "FROM OBJREFERENCE " +
                "WHERE OBJECT_ID IN (" +
                "SELECT OBJECT_ID " +
                "FROM OBJREFERENCE " +
                "WHERE REFERENCE = '" + StatusConstant.ST_IS_FRIEND + "'" +
                "AND OBJECT_ID IN (" +
                "SELECT OBJECT_ID " +
                "FROM OBJREFERENCE " +
                "WHERE REFERENCE = '" + profileId + "'" +
                " AND ATTRTYPE_ID IN ('" + FriendRequestConstant.REQ_FROM + "', '" + FriendRequestConstant.REQ_TO + "')))" +
                " AND ATTRTYPE_ID IN ('" + FriendRequestConstant.REQ_FROM + "', '" + FriendRequestConstant.REQ_TO + "')" +
                " AND REFERENCE <> '" + profileId + "'" +
                " and " + IGNORING_DELETED_ELEMENTS_IN_REF;
        QueryDescriptor descriptor = new QueryDescriptor();
        descriptor.addPagingDescriptor(page, Integer.valueOf(friendListPageCapacity));
        return entityManagerService.getObjectsBySQL(sqlQuery, Profile.class, descriptor);
    }


    @Override
    public int getAllFriendsPageCount(BigInteger profileId) {
        String getAllFriendsQuery = "SELECT REFERENCE " +
                "FROM OBJREFERENCE " +
                "WHERE OBJECT_ID IN (" +
                "SELECT OBJECT_ID " +
                "FROM OBJREFERENCE " +
                "WHERE REFERENCE = '" + StatusConstant.ST_IS_FRIEND + "'" +
                "AND OBJECT_ID IN (" +
                "SELECT OBJECT_ID " +
                "FROM OBJREFERENCE " +
                "WHERE REFERENCE = '" + profileId + "'" +
                " AND ATTRTYPE_ID IN ('" + FriendRequestConstant.REQ_FROM + "', '" + FriendRequestConstant.REQ_TO + "')))" +
                " AND ATTRTYPE_ID IN ('" + FriendRequestConstant.REQ_FROM + "', '" + FriendRequestConstant.REQ_TO + "')" +
                " AND REFERENCE <> '" + profileId + "'" +
                " and " + IGNORING_DELETED_ELEMENTS_IN_REF;
        Integer usersPageCapacity = new Integer(friendListPageCapacity);
        return pageCounterService.getPageCount(usersPageCapacity, entityManagerService.getBySqlCount(getAllFriendsQuery));
    }

    @Override
    public List<Profile> searchForPeopleByFullName(String name, String surname) {
        String query = "" +
                "SELECT obj.object_id " +
                "FROM Objects obj " +
                "INNER JOIN Attributes attr ON obj.object_id = attr.object_id " +
                "WHERE obj.object_type_id = " + UsersProfileConstant.PROFILE_TYPE + " " +
                "AND attr.attrtype_id = " + BigInteger.valueOf(413) + " " +
                "AND attr.value = '" + name + "'" +
                " INTERSECT " +
                "SELECT obj.object_id " +
                "FROM Objects obj " +
                "INNER JOIN Attributes attr ON obj.object_id = attr.object_id " +
                "WHERE obj.object_type_id = " + UsersProfileConstant.PROFILE_TYPE + " " +
                "AND attr.attrtype_id = " + BigInteger.valueOf(414) + " " +
                "AND attr.value = '" + surname + "'";
        return entityManagerService.getObjectsBySQL(query, Profile.class, new QueryDescriptor());
    }

    @Override
    public List<Profile> searchPeopleByNameOrSurname(String name) {
        String surnameQuery = "" +
                "SELECT DISTINCT obj.object_id " +
                "FROM Objects obj " +
                "INNER JOIN Attributes attr ON obj.object_id = attr.object_id " +
                "WHERE obj.object_type_id = " + UsersProfileConstant.PROFILE_TYPE + " " +
                "AND attr.attrtype_id = " + BigInteger.valueOf(413) + " " +
                "AND attr.value = '" + name + "' " +
                "OR " +
                "attr.attrtype_id = " + BigInteger.valueOf(414) + " " +
                "AND attr.value = '" + name + "' ";
        return entityManagerService.getObjectsBySQL(surnameQuery, Profile.class, new QueryDescriptor());
    }

    @Override
    public List<Profile> searchForPeopleByEmail(String email) {
        List<Profile> foundFriends = new ArrayList<>();
        BigInteger ProfileId = userDetailsService.loadUserByUsername(email).getProfile().getObjectId();
        foundFriends.add(entityManagerService.getById(ProfileId, Profile.class));
        return foundFriends;
    }
}
