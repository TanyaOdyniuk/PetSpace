package com.netcracker.controller.group;

import com.netcracker.model.group.Group;
import com.netcracker.model.group.GroupType;
import com.netcracker.model.user.Profile;
import com.netcracker.model.user.User;
import com.netcracker.service.groups.GroupService;
import com.netcracker.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {
    @Autowired
    GroupService groupService;
    @Autowired
    UserService userService;

    @GetMapping("/{id}")
    public Group getGroup(@PathVariable("id") BigInteger groupId) {
        return groupService.getGroup(groupId);
    }

    @GetMapping("/{id}/subscribers")
    public List<Profile> getGroupSubscribers(@PathVariable("id") BigInteger groupId) {
        return groupService.getGroupSubscribers(groupId);
    }

    @GetMapping("/{id}/subscribers/users")
    public List<User> getSubscribedUsersList(@PathVariable("id") BigInteger groupId) {
        return groupService.getSubscribedUsersList(groupId);
    }

    @GetMapping("/{id}/admin")
    public Profile getGroupAdmin(@PathVariable("id") BigInteger groupId) {
        return groupService.getGroupAdmin(groupId);
    }

    @GetMapping("/curUserId/{login}")
    public User getCurUserId(@RequestHeader("login") String login){
        return userService.getCurrentUser(login);
    }

    @GetMapping("/leaveGroup/{groupId}/{userId}")
    public void leaveGroup(@PathVariable("groupId") BigInteger groupId, @PathVariable("userId") BigInteger userId){

    }
}
