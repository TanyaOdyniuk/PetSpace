package com.netcracker.controller;

import com.netcracker.errorHandling.exceptions.UserNotValidException;
import com.netcracker.model.StubUser;
import com.netcracker.service.validation.ValidationService;
import com.netcracker.service.StubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Odyniuk on 06/11/2017.
 */
@RestController
@RequestMapping("/restcontroller")
public class StubRestController {
    @Autowired
    private StubService stubService;

    @GetMapping
    public List<StubUser> getUsers() {
        return stubService.getUsers();
    }

    @GetMapping("/{id}")
    public StubUser getUserById(@PathVariable("id") Integer id) {
        return stubService.getUserById(id);
    }

    @PostMapping
    public void addUser(@RequestBody StubUser user) {
        if (!ValidationService.validateName(user.getFirstName()))
            throw new UserNotValidException("Wrong name " + user.getFirstName(), user.getFirstName());
        stubService.addUser(user);
    }

    @PutMapping("/{id}")
    public void editUserById(@PathVariable("id") Integer id, @RequestBody StubUser stubUser) {
        if (!ValidationService.validateName(stubUser.getFirstName()))
            throw new UserNotValidException("Wrong name " + stubUser.getFirstName(), stubUser.getFirstName());
        stubService.editUserById(id, stubUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable("id") Integer id) {
        stubService.deleteUserById(id);
    }
}
