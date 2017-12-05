package com.netcracker.service.autorization.impl;

import com.netcracker.model.user.Profile;
import com.netcracker.model.user.User;
import com.netcracker.service.autorization.AuthorizationService;
import com.netcracker.service.user.impl.UserDetailsServiceImpl;
import com.netcracker.ui.login.LoginPage;
import com.vaadin.ui.Notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {


    @Autowired
    DaoAuthenticationProvider daoAuthenticationProvider;


    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Override
    public Profile checkCredentials(String enteredPassword, String email) {
        return null;
    }

    @Override
    public String passwordRecovery(String email) {
        return null;
    }


    @Override
    public void authenticate(String emailField, String passwordField) {
        Authentication auth = new UsernamePasswordAuthenticationToken(emailField, passwordField);
//        Authentication authenticated = daoAuthenticationProvider.authenticate(auth);
        SecurityContextHolder.getContext().setAuthentication(auth);
        User user = userDetailsService.findUserByUsername(emailField, passwordField);
        if (user == null) {
            Notification.show("Wrong email or password!");
        } else {
            LoginPage.getCurrent().getPage().setLocation("/testpage");
            /*
            if (userDetailsService.hasRole("ROLE_USER")) {
                LoginPage.getCurrent().getPage().setLocation("/user");
            } else if (userDetailsService.hasRole("ROLE_ADMIN")) {
                LoginPage.getCurrent().getPage().setLocation("/admin");
            }
            */
        }
    }


}
