package com.netcracker.service.groups;

import com.netcracker.model.group.Group;
import com.netcracker.model.user.Profile;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public interface GroupService {

    Group getGroup(BigInteger groupId);

    int getMyGroupsPageCount(BigInteger profileId);
    List<Group> getMyGroups(BigInteger profileId);
    List<Group> getMyGroupsList(BigInteger profileId, Integer page);

    List<Profile> getGroupSubscribers(BigInteger groupId);

    Profile getGroupAdmin(BigInteger groupId);

    Group createNewGroup(Group newGroup, BigInteger profileId);

    void leaveGroup(BigInteger groupId, BigInteger profileId);

    int getAdministerGroupsPageCount(BigInteger profileId);
    List<Group> getAdministerGroupsList(BigInteger profileId, Integer page);
    int getAllGroupsPageCount();

    List<Group> getAllGroups();
    List<Group> getAllGroupsList(Integer page);

    void subscribe(BigInteger groupId, BigInteger profileId);

    List<Group> getProfileCreatedGroups(BigInteger profileId);

    void editGroup(Group groupForChangeOnlyAdmin);

    void deleteGroup(BigInteger groupId);
}
