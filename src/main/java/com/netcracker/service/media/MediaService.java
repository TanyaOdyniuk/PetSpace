package com.netcracker.service.media;

import com.netcracker.model.record.AbstractRecord;
import com.netcracker.model.record.GroupWallRecord;
import com.netcracker.model.user.Profile;
import java.util.List;

public interface MediaService {
    //поддерживать функцию просмотра следующего/предыдущего изображения при клике на кнопки “<”, ”>”
    GroupWallRecord imageRotation(Profile profile);

    //открывать страничку с миниатюрами изображений после нажатия на кнопку просмотра
    // фотогалереи в профиле пользователя или питомца
    List<GroupWallRecord> imagesGalary(Profile profile);

    //создание и редактирование пользователем одного и более фотоальбомов для каждого из своих питомцев
    void petAlbul(Profile profile);

    //добавление и сохранение нового изображения в ленте, комментариях, на страничке или в группах
    void addNewMedia(AbstractRecord abstractRecord, Profile profile);

    //возможность настроек приватности для доступа других пользователей к своим медиа-ресурсам,
    // которое будет настраиваться в профиле пользователя
    void mediaSecurity(Profile profile);

}
