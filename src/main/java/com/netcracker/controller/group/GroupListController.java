package com.netcracker.controller.group;

import com.netcracker.model.group.Group;
import com.netcracker.model.group.GroupType;
import com.netcracker.service.groups.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/groupList")
public class GroupListController {
    @Autowired
    GroupService groupService;

    @GetMapping("/{profileId}")
    public List<Group> getMyGroupsList(@PathVariable("profileId") BigInteger profileId){
        return groupService.getMyGroupsList(profileId);
    }

    @PostMapping("/{profileId}/add")
    public Group createNewGroup(@RequestBody Group group, @PathVariable("profileId") BigInteger profileId){
        return groupService.createNewGroup(group, profileId);
    }

    @GetMapping("/allGroupTypes")
    public List<GroupType> getAllGroupTypes(){
        return groupService.getAllGroupTypes();
    }
}
