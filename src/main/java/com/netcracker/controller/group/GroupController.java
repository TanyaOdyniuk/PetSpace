package com.netcracker.controller.group;

import com.netcracker.model.group.Group;
import com.netcracker.model.user.Profile;
import com.netcracker.service.groups.GroupService;
import com.netcracker.service.util.RestResponsePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {
    @Autowired
    private GroupService groupService;

    @GetMapping("/{id}")
    public Group getGroup(@PathVariable("id") BigInteger groupId) {
        return groupService.getGroup(groupId);
    }

    @GetMapping("/{id}/subscribers")
    public List<Profile> getGroupSubscribers(@PathVariable("id") BigInteger groupId) {
        return groupService.getGroupSubscribers(groupId);
    }

    @GetMapping("/{id}/admin")
    public Profile getGroupAdmin(@PathVariable("id") BigInteger groupId) {
        return groupService.getGroupAdmin(groupId);
    }

    @GetMapping("/leaveGroup/{groupId}/{profileId}")
    public void leaveGroup(@PathVariable("groupId") BigInteger groupId, @PathVariable("profileId") BigInteger profileId){
        groupService.leaveGroup(groupId, profileId);
    }

    @GetMapping("/subscribe/{groupId}/{profileId}")
    public void subscribe(@PathVariable("groupId") BigInteger groupId, @PathVariable("profileId") BigInteger profileId){
        groupService.subscribe(groupId, profileId);
    }

    @GetMapping("/isAdmin/{id}")
    public List<Group> getProfileCreatedGroupsList(@PathVariable("id") BigInteger profileId) {
        return groupService.getProfileCreatedGroups(profileId);
    }

    @GetMapping("/isAdmin/{id}/{page}")
    public RestResponsePage<Group> getAdministerGroups(@PathVariable("id") BigInteger profileId, @PathVariable("page") Integer page) {
        Integer pageCount = groupService.getAdministerGroupsPageCount(profileId);
        List<Group> allGroups =  groupService.getAdministerGroupsList(profileId, page);
        return new RestResponsePage<>(allGroups, null, pageCount);
    }

    @GetMapping("/delete/{id}")
    public void deleteGroup(@PathVariable("id") BigInteger groupId){
        groupService.deleteGroup(groupId);
    }

    @PostMapping("/update")
    public void updateGroup(@RequestBody Group group){
        groupService.editGroup(group);
    }
}
