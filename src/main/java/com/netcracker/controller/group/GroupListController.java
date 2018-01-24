package com.netcracker.controller.group;

import com.netcracker.model.group.Group;
import com.netcracker.service.groups.GroupService;
import com.netcracker.service.util.RestResponsePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/groupList")
public class GroupListController {
    @Autowired
    GroupService groupService;

    @GetMapping("/all")
    public List<Group> getAllGroupsList() {
        return groupService.getAllGroups();
    }

    @GetMapping("/all/{page}")
    public RestResponsePage<Group> getAllGroups(@PathVariable("page") Integer page) {
        Integer pageCount = groupService.getAllGroupsPageCount();
        List<Group> allGroups = groupService.getAllGroupsList(page);
        return new RestResponsePage<>(allGroups, null, pageCount);
    }

    @GetMapping("/{profileId}")
    public List<Group> getMyGroupsList(@PathVariable("profileId") BigInteger profileId) {
        return groupService.getMyGroups(profileId);
    }

    @GetMapping("/{profileId}/{page}")
    public RestResponsePage<Group> getMyGroups(@PathVariable("profileId") BigInteger profileId, @PathVariable("page") Integer page) {
        Integer pageCount = groupService.getMyGroupsPageCount(profileId);
        List<Group> allGroups = groupService.getMyGroupsList(profileId, page);
        return new RestResponsePage<>(allGroups, null, pageCount);
    }

    @PostMapping("/{profileId}/add")
    public Group createNewGroup(@RequestBody Group group, @PathVariable("profileId") BigInteger profileId) {
        return groupService.createNewGroup(group, profileId);
    }
}
