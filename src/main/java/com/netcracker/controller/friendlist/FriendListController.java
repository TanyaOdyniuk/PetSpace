package com.netcracker.controller.friendlist;

import com.netcracker.model.user.Profile;
import com.netcracker.service.managefriends.ManageFriendService;
import com.netcracker.service.petprofile.PetProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;


@RestController
@RequestMapping("/friends")
public class FriendListController {

    @Autowired
    ManageFriendService friendProfileService;

    @GetMapping("/{id}")
    public List<Profile> getMyFriends(@PathVariable("id") BigInteger id) {
        return friendProfileService.getFriendList(id, false, null, null);// temp values
    }
}
