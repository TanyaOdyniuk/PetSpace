package com.netcracker.service.managefriends.impl;

import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.model.status.StatusConstant;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.request.FriendRequestConstant;
import com.netcracker.model.user.Profile;
import com.netcracker.model.user.UsersProfileConstant;
import com.netcracker.service.managefriends.ManageFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class ManageFriendServiceImpl implements ManageFriendService {

    @Autowired
    EntityManagerService entityManagerService;

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
                " AND REFERENCE <> '" + profileId + "'";
        return entityManagerService.getObjectsBySQL(sqlQuery, Profile.class, new QueryDescriptor());
    }

    @Override
    public List<Profile> searchForFriendsByName(String name, String surname) {
        String query = "" +
                "SELECT obj.object_id " +
                "FROM Objects obj " +
                "INNER JOIN Attributes attr ON obj.object_id = attr.object_id " +
                "WHERE obj.object_type_id = " + UsersProfileConstant.USER_TYPE + " " +
                "AND attr.attrtype_id = " + BigInteger.valueOf(413) + " " +
                "AND attr.value = '" + name + "'" +
                "INTERSECT " +
                "SELECT obj.object_id " +
                "FROM Objects obj " +
                "INNER JOIN Attributes attr ON obj.object_id = attr.object_id " +
                "WHERE obj.object_type_id = " + UsersProfileConstant.USER_TYPE + " " +
                "AND attr.attrtype_id = " + BigInteger.valueOf(414) + " " +
                "AND attr.value = '" + surname + "'";
        return entityManagerService.getObjectsBySQL(query, Profile.class, new QueryDescriptor());
    }

    @Override
    public List<Profile> searchForFriendsByEmail(String email) {
        String query = "" +
                "SELECT obj.object_id " +
                "FROM Objects obj " +
                "INNER JOIN Attributes attr ON obj.object_id = attr.object_id " +
                "WHERE obj.object_type_id = " + UsersProfileConstant.USER_TYPE + " " +
                "AND attr.attrtype_id = " + UsersProfileConstant.USER_LOGIN + " " +
                "AND attr.value = '" + email + "'";
        return entityManagerService.getObjectsBySQL(query, Profile.class, new QueryDescriptor());
    }
}
