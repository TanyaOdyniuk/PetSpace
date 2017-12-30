package com.netcracker.service.user.impl;

import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.model.user.User;
import com.netcracker.model.user.UsersProfileConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class UserDetailsServiceImpl {

    @Autowired
    private EntityManagerService entityManagerService;

    public boolean validateUserExistence(List<User> users, String login) {
        if(users.isEmpty()){
            return false;
        }
        boolean isInDB = true;
        for (User user : users) {
            String existingLogin = user.getLogin();
            isInDB = existingLogin.equals(login);
            if (isInDB) {
                break;
            }
        }
        return isInDB;
    }

    public User findUserByUsernameAndPassword(String username, String password) {
        String query = "" +
                "SELECT obj.object_id " +
                "FROM Objects obj " +
                "INNER JOIN Attributes attr ON obj.object_id = attr.object_id " +
                "WHERE obj.object_type_id = " + UsersProfileConstant.USER_TYPE + " " +
                "AND attr.attrtype_id = " + UsersProfileConstant.USER_LOGIN + " " +
                "AND attr.value = '" + username + "'" +
                "INTERSECT " +
                "SELECT obj.object_id " +
                "FROM Objects obj " +
                "INNER JOIN Attributes attr ON obj.object_id = attr.object_id " +
                "WHERE obj.object_type_id = " + UsersProfileConstant.USER_TYPE + " " +
                "AND attr.attrtype_id = " + UsersProfileConstant.USER_PASSWORD + " " +
                "AND attr.value = '" + password + "'";
        List<User> userList = entityManagerService.getObjectsBySQL(query, User.class, new QueryDescriptor());
        if (userList.isEmpty()) {
            return null;
        }
        return userList.get(0);
    }

    public User loadUserByUsername(String username) {
        String query = "" +
                "SELECT obj.object_id\n " +
                "FROM Objects obj, ATTRIBUTES atr\n" +
                "where obj.OBJECT_ID = atr.OBJECT_ID\n" +
                "and obj.object_type_id = 1\n" +
                "and atr.attrtype_id = 1\n" +
                "AND atr.value = '" + username + "'";
        List<User> userList = entityManagerService.getObjectsBySQL(query, User.class, new QueryDescriptor());
        if (userList.isEmpty()) {
            return null;
        }
        return userList.get(0);
    }

    public List<User> getUsers() {
        return entityManagerService.getAll(BigInteger.valueOf(1), User.class, new QueryDescriptor());
    }
}
