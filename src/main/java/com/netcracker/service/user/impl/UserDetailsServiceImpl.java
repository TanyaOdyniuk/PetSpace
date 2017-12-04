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

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    ManagerAPI managerAPI;

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

    public User findUserByUsername(String username) {
        User user = new User();
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

    public List<User> getUsers() {
        return managerAPI.getAll(BigInteger.valueOf(UsersProfileConstant.USER_TYPE), User.class);
    }
}
