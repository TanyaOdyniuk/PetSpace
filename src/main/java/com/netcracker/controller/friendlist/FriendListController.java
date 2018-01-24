package com.netcracker.controller.friendlist;

import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.model.user.Profile;
import com.netcracker.service.managefriends.ManageFriendService;
import com.netcracker.service.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;


@RestController
@RequestMapping("/friends")
public class FriendListController {

    @Autowired
    ManageFriendService manageFriendService;

    @Autowired
    SearchService searchService;

    @GetMapping("/{id}")
    public List<Profile> getMyFriends(@PathVariable("id") BigInteger id) {
        return manageFriendService.getFriendList(id);
    }

    @GetMapping("/search/byFullName/{name}/{surname}/{id}")
    public List<Profile> searchFriendsByFullName(@PathVariable("name") String name, @PathVariable("surname") String surname, @PathVariable("id") BigInteger id) {
        return searchService.searchForFriendsByFullName(name, surname, getMyFriends(id));
    }

    @GetMapping("/search/byName/{name}/{id}")
    public List<Profile> searchFriendsByNameOrSurname(@PathVariable("name") String name, @PathVariable("id") BigInteger id) {
        return searchService.searchFriendsByNameOrSurname(name, getMyFriends(id));
    }

    @GetMapping("/search/byEmail/{email:.+}/{id}")
    public List<Profile> searchFriendsByEmail(@PathVariable("email") String email, @PathVariable("id") BigInteger id) {
        return searchService.searchForFriendsByEmail(email, getMyFriends(id));
    }


}
