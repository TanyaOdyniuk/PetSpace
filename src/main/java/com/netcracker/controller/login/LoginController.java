package com.netcracker.controller.login;

import com.netcracker.model.user.User;
import com.netcracker.model.user.UserAuthority;
import com.netcracker.service.authorization.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/loginform")
public class LoginController {

    @Autowired
    private AuthorizationService authorizationService;

    @PostMapping
    public User authenticate(@RequestBody User reqUser) {
        String email = reqUser.getLogin();
        String password = reqUser.getPassword();
        List<UserAuthority> authorities = reqUser.getUserAuthorities();
        return authorizationService.authenticate(email, password, authorities);
    }
}
