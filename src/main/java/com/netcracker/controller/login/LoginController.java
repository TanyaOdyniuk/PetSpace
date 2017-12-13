package com.netcracker.controller.login;

import com.netcracker.model.user.User;
import com.netcracker.service.authorization.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/loginform")
public class LoginController {

    @Autowired
    private AuthorizationService authorizationService;

    @PostMapping
    public User authenticate(String email, String password, Collection<? extends GrantedAuthority> authorities) {
        return authorizationService.authenticate(email, password, authorities);
    }
}
