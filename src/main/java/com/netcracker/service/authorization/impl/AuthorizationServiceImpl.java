package com.netcracker.service.authorization.impl;

import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.model.user.Profile;
import com.netcracker.model.user.User;
import com.netcracker.service.authorization.AuthorizationService;
import com.netcracker.service.user.impl.UserDetailsServiceImpl;
import com.netcracker.service.util.EmailService;
import com.netcracker.service.util.RandomStringGenerator;
import com.netcracker.ui.login.LoginPage;
import com.vaadin.ui.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {
    @Autowired
    EmailService emailService;

    @Autowired
    RandomStringGenerator newPassword;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    EntityManagerService entityManagerService;

    @Override
    public Profile checkCredentials(String enteredPassword, String email) {
        return null;
    }

    @Override
    public User passwordRecovery(String email) {
        User user = userDetailsService.loadUserByUsername(email);
        if (user == null) {
            return null;
        } else {
            user.setPassword(newPassword.nextString());
            entityManagerService.update(user);
            emailService = (EmailService) new ClassPathXmlApplicationContext("SpringMail.xml").getBean("mailMail");
            emailService.sendMail("PetSpaceInfo@gmail.com", "levil133@gmail.com", "Your new password MATE", "Your new password is: " + user.getPassword());
            emailService.sendMail("PetSpaceInfo@gmail.com", "tanyaodyniuk@gmail.com", "Your new password MATE", "Your new password is: " + user.getPassword());
            emailService.sendMail("PetSpaceInfo@gmail.com", "dok.ttv@gmail.com", "Your new password MATE", "Your new password is: " + user.getPassword());
            emailService.sendMail("PetSpaceInfo@gmail.com", "vlad.drabynka@gmail.com", "Your new password MATE", "Your new password is: " + user.getPassword());
            emailService.sendMail("PetSpaceInfo@gmail.com", "galaolala@gmail.coma", "Your new password MATE", "Your new password is: " + user.getPassword());
            return user;
        }
    }


    @Override
    public User authenticate(String emailField, String passwordField, Collection<? extends GrantedAuthority> authorities) {
        Authentication auth = new UsernamePasswordAuthenticationToken(emailField, passwordField, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
        User user = userDetailsService.findUserByUsernameAndPassword(emailField, passwordField);

        // TO UI
        if (user == null) {
            Notification.show("Wrong email or password!");
        } else {
            LoginPage.getCurrent().getPage().setLocation("/testpage");
        }
        return user;
    }
}