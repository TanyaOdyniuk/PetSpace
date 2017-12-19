package com.netcracker.service.user.impl;

import com.netcracker.model.user.User;
import com.netcracker.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    public User getCurrentUser() {
        SecurityContext securityContextHolder = SecurityContextHolder.getContext();
        String login = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetailsService.loadUserByUsername(login);
    }
}
