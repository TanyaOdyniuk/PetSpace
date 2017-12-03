package com.netcracker.controller.user;

import com.netcracker.error.exceptions.UserNotValidException;
import com.netcracker.model.user.Profile;
import com.netcracker.model.user.User;
import com.netcracker.service.registration.RegistrationService;
import com.netcracker.service.user.UserService;
import com.netcracker.service.validation.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }


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
