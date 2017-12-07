package com.netcracker.service.managefriends.impl;

import com.netcracker.dao.managerapi.ManagerAPI;
import com.netcracker.model.StatusConstant;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.pet.PetConstant;
import com.netcracker.model.request.FriendRequestConstant;
import com.netcracker.model.user.Profile;
import com.netcracker.service.managefriends.ManageFriendService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Service
public class ManageFriendServiceImpl implements ManageFriendService {

    @Autowired
    ManagerAPI managerApi;

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
    public List<Profile> getFriendList(BigInteger profileId, boolean isPaging, Pair<Integer, Integer> pagingDesc, Map<String, String> sortingDesc) {
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
        return managerApi.getObjectsBySQL(sqlQuery, Profile.class, isPaging, pagingDesc, sortingDesc);
    }
}
