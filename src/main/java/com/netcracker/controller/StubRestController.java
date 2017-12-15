package com.netcracker.controller;

import com.netcracker.model.StubUser;
import com.netcracker.service.StubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        stubService.addUser(user);
    }

    @PutMapping("/{id}")
    public void editUserById(@PathVariable("id") Integer id, @RequestBody StubUser stubUser) {
        stubService.editUserById(id, stubUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable("id") Integer id) {
        stubService.deleteUserById(id);
    }
}
