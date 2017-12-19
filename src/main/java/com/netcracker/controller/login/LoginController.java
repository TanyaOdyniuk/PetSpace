package com.netcracker.controller.login;

import com.netcracker.model.user.User;
import com.netcracker.model.user.UserAuthority;
import com.netcracker.service.authorization.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import java.util.List;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private AuthorizationService authorizationService;

    @PostMapping
    @Consumes(value = "application/json")
    @Produces(value = "application/json")
    public @ResponseBody User authenticate(@RequestBody User reqUser) {
        String email = reqUser.getLogin();
        String password = reqUser.getPassword();
        List<UserAuthority> authorities = reqUser.getUserAuthorities();
        return authorizationService.authenticate(email, password, authorities);
    }
}
