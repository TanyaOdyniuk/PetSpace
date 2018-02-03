package com.netcracker.controller.userlist;

import com.netcracker.model.user.Profile;
import com.netcracker.service.search.SearchService;
import com.netcracker.service.user.UserService;
import com.netcracker.service.util.NameValidator;
import com.netcracker.service.util.RestResponsePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserListController {
    @Autowired
    private SearchService searchService;

    @Autowired
    private NameValidator nameValidator;

    @Autowired
    private UserService userService;

    @GetMapping("/all/{page}")
    public RestResponsePage<Profile> getAllUsers(@PathVariable("page") int page) {
        Integer count = userService.getAllUsersPageCount();
        List<Profile> users = userService.getAllUsers(page);
        return new RestResponsePage<>(users, null, count);
    }

    @GetMapping("/search/byFullName")
    public List<Profile> searchPeopleByFullName(@RequestParam("name") String name, @RequestParam("surname") String surname) {
        name = nameValidator.validateName(name);
        surname = nameValidator.validateName(surname);
        return searchService.searchForPeopleByFullName(name, surname);
    }

    @GetMapping("/search/byName")
    public List<Profile> searchPeopleByNameOrSurname(@RequestParam("name") String name) {
        name = nameValidator.validateName(name);
        return searchService.searchPeopleByNameOrSurname(name);
    }

    @GetMapping("/search/byEmail")
    public List<Profile> searchPeopleByEmail(@RequestParam("email") String email) {
        return searchService.searchForPeopleByEmail(email);
    }
}
