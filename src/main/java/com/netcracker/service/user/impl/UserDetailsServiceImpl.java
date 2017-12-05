package com.netcracker.service.user.impl;

import com.netcracker.dao.managerapi.ManagerAPI;
import com.netcracker.model.user.User;
import com.netcracker.model.user.UsersProfileConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ManagerAPI managerAPI;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String query = "" +
                "SELECT obj.object_id " +
                "FROM Objects obj " +
                "INNER JOIN Attributes attr ON obj.object_id = attr.object_id " +
                "WHERE obj.object_type_id = " + UsersProfileConstant.USER_TYPE + " " +
                "AND attr.attrtype_id = " + UsersProfileConstant.USER_LOGIN + " " +
                "AND attr.value = '" + username + "'";
        List<User> userList = managerAPI.getObjectsBySQL(query, User.class);
        if (userList.isEmpty()) {
            return null;
        }
        User user = userList.get(0);
        return user;
    }

    public User findUserByUsername(String username, String password) {
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
        List<User> userList = managerAPI.getObjectsBySQL(query, User.class);
        if (userList.isEmpty()) {
            return null;
        }
        User user = userList.get(0);
        return user;
    }

    public List<User> getUsers(){
        return managerAPI.getAll(BigInteger.valueOf(1), User.class);
    }
}
