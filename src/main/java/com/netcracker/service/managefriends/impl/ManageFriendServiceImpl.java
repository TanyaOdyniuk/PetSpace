package com.netcracker.service.managefriends.impl;

import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.model.StatusConstant;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.request.FriendRequestConstant;
import com.netcracker.model.user.Profile;
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
    public void addFriend(Profile profile)
    {

    }

    @Override
    public void deleteFriend(Profile profile)
    {

    }

    @Override
    public Pet checkFriendsPets(Profile profile)
    {
        return null;
    }

    @Override
    public void sortFriendList(SortListFriendMod sortMod)
    {

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
}
