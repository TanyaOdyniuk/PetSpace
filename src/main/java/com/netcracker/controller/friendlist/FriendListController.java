package com.netcracker.controller.friendlist;

import com.netcracker.model.user.Profile;
import com.netcracker.service.managefriends.ManageFriendService;
import com.netcracker.service.search.SearchService;
import com.netcracker.service.util.NameValidator;
import com.netcracker.service.util.RestResponsePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;


@RestController
@RequestMapping("/friends")
public class FriendListController {

    @Autowired
    private ManageFriendService manageFriendService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private NameValidator nameValidator;

    @GetMapping("/{id}/all/{page}")
    public RestResponsePage<Profile> getAllFriends(@PathVariable("page") int page, @PathVariable("id") BigInteger id) {
        Integer count = manageFriendService.getAllFriendsPageCount(id);
        List<Profile> users = manageFriendService.getFriendList(id, page);
        return new RestResponsePage<>(users, null, count);
    }

    @GetMapping("/{id}")
    public List<Profile> getMyFriends(@PathVariable("id") BigInteger id) {
        return manageFriendService.getFriendList(id);
    }

    @GetMapping("/search/byFullName/{id}")
    public List<Profile> searchFriendsByFullName(@RequestParam("name") String name, @RequestParam("surname") String surname, @PathVariable("id") BigInteger id) {
        name = nameValidator.validateName(name);
        surname = nameValidator.validateName(surname);
        return searchService.searchForFriendsByFullName(name, surname, getMyFriends(id));
    }

    @GetMapping("/search/byName/{id}")
    public List<Profile> searchFriendsByNameOrSurname(@RequestParam("name") String name, @PathVariable("id") BigInteger id) {
        name = nameValidator.validateName(name);
        return searchService.searchFriendsByNameOrSurname(name, getMyFriends(id));
    }

    @GetMapping("/search/byEmail/{id}")
    public List<Profile> searchFriendsByEmail(@RequestParam("email") String email, @PathVariable("id") BigInteger id) {
        return searchService.searchForFriendsByEmail(email, getMyFriends(id));
    }


}
