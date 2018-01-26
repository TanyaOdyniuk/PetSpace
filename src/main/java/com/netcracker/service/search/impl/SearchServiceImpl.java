package com.netcracker.service.search.impl;

import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.model.user.Profile;
import com.netcracker.model.user.User;
import com.netcracker.model.user.UsersProfileConstant;
import com.netcracker.service.search.SearchService;
import com.netcracker.service.user.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    EntityManagerService entityManagerService;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

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
        User curUser = userDetailsService.loadUserByUsername(email);
        if (curUser != null) {
            BigInteger ProfileId = curUser.getProfile().getObjectId();
            foundFriends.add(entityManagerService.getById(ProfileId, Profile.class));
        }
        return foundFriends;
    }

    @Override
    public List<Profile> searchForFriendsByFullName(String name, String surname, List<Profile> friendList) {
        List<Profile> foundFriends = new ArrayList<>();
        for (Profile p : friendList) {
            if (name.equals(p.getProfileName()) && surname.equals(p.getProfileSurname())) {
                foundFriends.add(p);
            }
        }
        return foundFriends;
    }

    @Override
    public List<Profile> searchForFriendsByEmail(String email, List<Profile> friendList) {
        List<Profile> foundFriends = new ArrayList<>();
        for (Profile p : friendList) {
            BigInteger userId = p.getProfileUser().getObjectId();
            User user = entityManagerService.getById(userId, User.class);
            if (email.equals(user.getLogin())) {
                foundFriends.add(p);
            }
        }
        return foundFriends;
    }

    @Override
    public List<Profile> searchFriendsByNameOrSurname(String name, List<Profile> friendList) {
        List<Profile> foundFriends = new ArrayList<>();
        for (Profile p : friendList) {
            if (name.equals(p.getProfileName()) || name.equals(p.getProfileSurname())) {
                foundFriends.add(p);
            }
        }
        return foundFriends;
    }
}
