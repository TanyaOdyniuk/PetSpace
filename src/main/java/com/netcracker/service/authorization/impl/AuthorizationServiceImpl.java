package com.netcracker.service.authorization.impl;

import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.model.user.User;
import com.netcracker.service.authorization.AuthorizationService;
import com.netcracker.service.user.impl.UserDetailsServiceImpl;
import com.netcracker.service.util.EmailService;
import com.netcracker.service.util.RandomStringGenerator;
import com.netcracker.ui.login.LoginPage;
import com.netcracker.service.util.BCryptEncoder;
import com.vaadin.ui.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;

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

    @Autowired
    BCryptEncoder bCryptEncoder;

    @Override
    public User passwordRecovery(String email) {
        User user = userDetailsService.loadUserByUsername(email);
        if (user == null) {
            return null;
        } else {
            String newTempPassword = newPassword.nextString();
            user.setPassword(bCryptEncoder.encode(newTempPassword));
            entityManagerService.update(user);
            emailService = (EmailService) new ClassPathXmlApplicationContext("SpringMail.xml").getBean("mailMail");
            emailService.sendMail("PetSpaceInfo@gmail.com", "levil133@gmail.com", "Your new password MATE", "Your new password is: " + newTempPassword);
            emailService.sendMail("PetSpaceInfo@gmail.com", "tanyaodyniuk@gmail.com", "Your new password MATE", "Your new password is: " + newTempPassword);
            emailService.sendMail("PetSpaceInfo@gmail.com", "dok.ttv@gmail.com", "Your new password MATE", "Your new password is: " + newTempPassword);
            emailService.sendMail("PetSpaceInfo@gmail.com", "vlad.drabynka@gmail.com", "Your new password MATE", "Your new password is: " + newTempPassword);
            emailService.sendMail("PetSpaceInfo@gmail.com", "galaolala@gmail.coma", "Your new password MATE", "Your new password is: " + newTempPassword);
            return user;
        }
    }


    @Override
    public User authenticate(String emailField, String passwordField, Collection<? extends GrantedAuthority> authorities) {
        User user = userDetailsService.loadUserByUsername(emailField);
        Boolean bool = bCryptEncoder.matches(passwordField, user.getPassword());

        // TO UI
        if (!bool) {
            Notification.show("Wrong email or password!");
        } else {
            Authentication auth = new UsernamePasswordAuthenticationToken(emailField, passwordField, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
            LoginPage.getCurrent().getPage().setLocation("/testpage");
        }
        return user;
    }
}
