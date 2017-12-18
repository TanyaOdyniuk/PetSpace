package com.netcracker.service.user.impl;

import com.netcracker.model.user.User;
import com.netcracker.ui.login.LoginPage;
import com.vaadin.ui.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;



@Service
public class UserService {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    public User getCurrentUser() {
        SecurityContext securityContextHolder = SecurityContextHolder.getContext();
        String login = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetailsService.loadUserByUsername(login);
    }
}
