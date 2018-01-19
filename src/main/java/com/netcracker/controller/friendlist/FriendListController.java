package com.netcracker.controller.friendlist;

import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.model.user.Profile;
import com.netcracker.service.managefriends.ManageFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;


@RestController
@RequestMapping("/friends")
public class FriendListController {

    @Autowired
    ManageFriendService manageFriendService;

    @GetMapping("/{id}")
    public List<Profile> getMyFriends(@PathVariable("id") BigInteger id) {
        QueryDescriptor queryDescriptor = new QueryDescriptor();
        return manageFriendService.getFriendList(id);
    }

    @GetMapping("/search/{name}/{surname}")
    public List<Profile> searchFriendsByName(@PathVariable("name") String name, @PathVariable("surname") String surname) {
        return manageFriendService.searchForFriendsByName(name, surname);
    }

    @GetMapping("/search/{email}")
    public List<Profile> searchFriendsByEmail(@PathVariable("email") String email) {
        return manageFriendService.searchForFriendsByEmail(email);
    }
}
