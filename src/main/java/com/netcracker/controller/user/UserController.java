package com.netcracker.controller.user;

import com.netcracker.error.exceptions.UserNotValidException;
import com.netcracker.model.user.User;
import com.netcracker.service.registration.RegistrationService;
import com.netcracker.service.user.impl.UserDetailsServiceImpl;
import com.netcracker.service.validation.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private RegistrationService registrationService;


    @GetMapping
    public User getCurrentUser() {
        String login = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetailsService.loadUserByUsername(login);
    }
    @PostMapping
    public User registrateUser(@RequestBody User user) {
        List<User> userList = userDetailsService.getUsers();
        if (!userDetailsService.validateUserExistence(userList, user.getLogin()))
            throw new UserNotValidException("User with email: " + user.getLogin() + " is already exist");
        return registrationService.registerUser(user);
    }

    @PutMapping("/increasebalance")
    public void editUserById(@RequestBody String login) {
        registrationService.invitedByUser(login);
    }

}
