package com.netcracker.service.groups;

import com.netcracker.model.group.Group;
import com.netcracker.model.user.Profile;
import com.netcracker.model.user.User;
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

//    List<User> getSubscribedUsersList(BigInteger groupId);

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

//    Система должна позволять администратору удалить группу
    void deleteGroup(Group group);

//    Система должна позволять администратору наделять определенных участников группы вносить изменения в группу.
    void empowerForGroup(Group group, BigInteger userId);

    void inviteUserToGroup(Profile invited, Group linkToGroup);

    void removeParticipantFromGroup(Group group, BigInteger userID);

//    Приглашенный пользователь должен подтвердить свое участие в группе
    boolean confirmInvitationToGroup(Profile invited);
}
