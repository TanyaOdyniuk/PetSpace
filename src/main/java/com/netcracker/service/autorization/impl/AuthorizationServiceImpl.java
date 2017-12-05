package com.netcracker.service.autorization.impl;

import com.netcracker.model.user.Profile;
import com.netcracker.service.autorization.AuthorizationService;
import com.netcracker.service.user.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {


//    @Autowired
//    MyDaoAuthenticationProvider daoAuthenticationProvider;

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
        /*
        Authentication auth = new UsernamePasswordAuthenticationToken(emailField, passwordField);
        Authentication authenticated = daoAuthenticationProvider.authenticate(auth);
        SecurityContextHolder.getContext().setAuthentication(authenticated);
        User user = userDetailsService.findUserByUsername(emailField);
        if (user == null) {
            Notification.show("Wrong email or password!");
        } else {
            if (userDetailsService.hasRole("ROLE_USER")) {
                LoginPage.getCurrent().getPage().setLocation("/user");
            } else if (userDetailsService.hasRole("ROLE_ADMIN")) {
                LoginPage.getCurrent().getPage().setLocation("/admin");
            }
        }
        */
    }
}
