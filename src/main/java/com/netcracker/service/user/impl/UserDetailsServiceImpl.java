package com.netcracker.service.user.impl;

import com.netcracker.dao.managerapi.ManagerAPI;
import com.netcracker.model.user.User;
import com.netcracker.model.user.UsersProfileConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

@Service
public class UserDetailsServiceImpl {

    @Autowired
    private ManagerAPI managerAPI;

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
        List<User> userList = managerAPI.getObjectsBySQL(query, User.class, false, null, null);
        if (userList.isEmpty()) {
            return null;
        }
        User user = userList.get(0);
        return user;
    }

    public User loadUserByUsername(String username) {
        String query = "" +
                "SELECT obj.object_id\n " +
                "FROM Objects obj, ATTRIBUTES atr\n" +
                "where obj.OBJECT_ID = atr.OBJECT_ID\n" +
                "and obj.object_type_id = 1\n" +
                "and atr.attrtype_id = 1\n" +
                "AND atr.value = '" + username + "'";
        List<User> userList = managerAPI.getObjectsBySQL(query, User.class, false, null, null);
        if (userList.isEmpty()) {
            return null;
        }
        User user = userList.get(0);
        System.out.println(user.toString());
        System.out.println();
        Collection<? extends GrantedAuthority> typeList = user.getAuthorities();
        for(GrantedAuthority ga : typeList){
            System.out.println(ga.toString());
        }
        return user;
    }

    public List<User> getUsers() {
        return managerAPI.getAll(BigInteger.valueOf(1), User.class, false, null, null);
    }
}
