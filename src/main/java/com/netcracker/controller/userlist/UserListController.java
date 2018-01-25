package com.netcracker.controller.userlist;

import com.netcracker.model.user.Profile;
import com.netcracker.service.search.SearchService;
import com.netcracker.service.util.NameValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserListController {
    @Autowired
    private SearchService searchService;

    @Autowired
    private NameValidator nameValidator;

    @GetMapping("/search/byFullName/{name}/{surname}")
    public List<Profile> searchPeopleByFullName(@PathVariable("name") String name, @PathVariable("surname") String surname) {
        name = nameValidator.validateName(name);
        surname = nameValidator.validateName(surname);
        return searchService.searchForPeopleByFullName(name, surname);
    }

    @GetMapping("/search/byName/{name}")
    public List<Profile> searchPeopleByNameOrSurname(@PathVariable("name") String name) {
        name = nameValidator.validateName(name);
        return searchService.searchPeopleByNameOrSurname(name);
    }

    @GetMapping("/search/byEmail/{email:.+}")
    public List<Profile> searchPeopleByEmail(@PathVariable("email") String email) {
        return searchService.searchForPeopleByEmail(email);
    }
}
