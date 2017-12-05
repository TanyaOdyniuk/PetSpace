package com.netcracker.controller.user;

import com.netcracker.service.user.impl.UserDetailsServiceImpl;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {

    /*

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UserDetailsServiceImpl userService;

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
    */
}
