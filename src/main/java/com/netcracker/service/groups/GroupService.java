package com.netcracker.service.groups;

import com.netcracker.model.group.Group;
import com.netcracker.model.group.GroupType;
import com.netcracker.model.user.Profile;
import com.netcracker.model.user.User;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public interface GroupService {

    Group getGroup(BigInteger groupId);

    List<Group> getMyGroupsList(BigInteger profileId);

    List<Profile> getGroupSubscribers(BigInteger groupId);

    Profile getGroupAdmin(BigInteger groupId);

    List<User> getSubscribedUsersList(BigInteger groupId);

    Group createNewGroup(Group newGroup, BigInteger profileId);

    void editGroup(Group groupForChangeOnlyAdmin);

//    Система должна позволять администратору удалить группу
    void deleteGroup(Group group);

//    Система должна позволять администратору наделять определенных участников группы вносить изменения в группу.
    void empowerForGroup(Group group, BigInteger userId);

//    Уровни публичности  группы: открыта, закрыта
    void setAccessLevelOfGroup(Group group, GroupType groupType); //то же что и обычный setter группы

    void inviteUserToGroup(Profile invited, Group linkToGroup);

    void removeParticipantFromGroup(Group group, BigInteger userID);

//    Приглашенный пользователь должен подтвердить свое участие в группе
    boolean confirmInvitationToGroup(Profile invited);
}
