package com.netcracker.service.communityProfile;

import com.netcracker.model.group.Group;
import com.netcracker.model.group.GroupType;
import com.netcracker.model.user.Profile;

public interface GroupService {
//    Система должна отображать страницу группы
    Group showGroup();

//    Система должна позволять зарегистрированному пользователю создавать страницу группы
    Group createNewGroup(Profile user);

//    Система должна проверять входящие данные при создании новой страницы группы
//    думаю этим будет занят какой-то JS ну или наш Vaadin
//    прост такие валидации много где
    boolean validation (Object dataForValidate); //??

//    Система должна позволять админу вносить изменения в группу
    void editProfile(Group groupForChangeOnlyAdmin);

//    Система должна позволять администратору удалить группу
    void deleteProfile(Group group);

    //Система должна позволять администратору наделять определенных участников группы вносить изменения в группу.
    void empower(Group group);

//    Уровни публичности  группы: открыта, закрыта
    void setAccessLevel(Group group, GroupType groupType); //то же что и обычный setter группы

    void inviteUser(Profile invited, Group linkToGroup);

    void removeParticipant(Group group, int userID);

//    Приглашенный пользователь должен подтвердить свое участие в группе
    boolean confirmInvitation(Profile invited);   //?

}
