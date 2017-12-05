package com.netcracker.service.user.impl;

import com.netcracker.dao.managerapi.ManagerAPI;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.user.User;
import com.netcracker.model.user.UsersProfileConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ManagerAPI managerAPI;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }

    public boolean hasRole(String role) {
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>)
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        boolean hasRole = false;
        for (GrantedAuthority authority : authorities) {
            hasRole = authority.getAuthority().equals(role);
            if (hasRole) {
                break;
            }
        }
        return hasRole;
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

//       User user = managerAPI.getById(BigInteger.valueOf(48), User.class);
        return user;
    }

    public <T extends BaseEntity> T getCurrentUser() {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            BigInteger objectTypeId = BigInteger.valueOf(5);//.getObjectTypeIdByUser(user);
            user = managerAPI.getById(objectTypeId, User.class);
            return (T) user;
        } catch (Exception e) {
        }
        return null;
    }

    public User getUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }
}
