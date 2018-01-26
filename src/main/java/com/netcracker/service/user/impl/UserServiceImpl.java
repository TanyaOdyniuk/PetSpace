package com.netcracker.service.user.impl;

import com.netcracker.model.user.User;
import com.netcracker.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    public User getCurrentUser(String login) {
        return userDetailsService.loadUserByUsername(login);
    }
}
