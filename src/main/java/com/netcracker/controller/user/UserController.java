package com.netcracker.controller.user;

import com.netcracker.error.exceptions.UserNotValidException;
import com.netcracker.model.user.Profile;
import com.netcracker.model.user.User;
import com.netcracker.service.registration.RegistrationService;
import com.netcracker.service.user.impl.UserDetailsServiceImpl;
import com.netcracker.service.validation.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {



    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UserDetailsServiceImpl userService;

    @GetMapping
    public Object getCurrentUser()
    {
        Object object =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return  object;
    }
    /*public List<User> getUsers() {
        return userService.getUsers();
    }*/


    @PostMapping
    public Profile registrateUser(@RequestBody User user) {
        List<User> userList = userService.getUsers();
        if (!ValidationService.validateUserExistence(userList, user.getLogin()))
            throw new UserNotValidException("User with email: " + user.getLogin() + " is already exist");
        return registrationService.registrateUser(user);
    }

    @PutMapping("/increasebalance")
    public void editUserById(@RequestBody String login) {
        registrationService.invitedByUser(login);
    }

}
