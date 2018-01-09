package com.netcracker.controller.group;

import com.netcracker.model.group.Group;
import com.netcracker.model.group.GroupType;
import com.netcracker.model.user.Profile;
import com.netcracker.service.groups.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {
    @Autowired
    GroupService groupService;

    @GetMapping("/{id}")
    public Group getGroup(@PathVariable("id") BigInteger groupId){
        return groupService.getGroup(groupId);
    }

    @GetMapping("/{id}/type")
    public GroupType getGroupType(@PathVariable("id") BigInteger groupId){
        return groupService.getGroupType(groupId);
    }

    @GetMapping("/{id}/subscribers")
    public List<Profile> getGroupSubscribers(@PathVariable("id") BigInteger groupId){
        return groupService.getGroupSubscribers(groupId);
    }

    @GetMapping("/{id}/admin")
    public Profile getGroupAdmin(@PathVariable("id") BigInteger groupId){
        return groupService.getGroupAdmin(groupId);
    }
}
