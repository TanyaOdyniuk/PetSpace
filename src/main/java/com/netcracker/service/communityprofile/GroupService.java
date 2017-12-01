package com.netcracker.service.communityprofile;

import com.netcracker.model.group.Group;
import com.netcracker.model.group.GroupType;
import com.netcracker.model.user.Profile;

public interface GroupService {
//    Система должна отображать страницу группы
    Group getGroup(Integer idGroup);

//    Система должна позволять зарегистрированному пользователю создавать страницу группы
    Group createNewGroup(Profile pfofile, Group newGroup);

//    Система должна проверять входящие данные при создании новой страницы группы
    void validationOfDataForNewGroup (Object dataForValidate); //??

//    Система должна позволять админу вносить изменения в группу
    void editGroup(Group groupForChangeOnlyAdmin);

//    Система должна позволять администратору удалить группу
    void deleteGroup(Group group);

    //Система должна позволять администратору наделять определенных участников группы вносить изменения в группу.
    void empowerForGroup(Group group, Integer userId);

//    Уровни публичности  группы: открыта, закрыта
    void setAccessLevelOfGroup(Group group, GroupType groupType); //то же что и обычный setter группы

    void inviteUserToGroup(Profile invited, Group linkToGroup);

    void removeParticipantFromGroup(Group group, Integer userID);

//    Приглашенный пользователь должен подтвердить свое участие в группе
    boolean confirmInvitationToGroup(Profile invited);   //?

}