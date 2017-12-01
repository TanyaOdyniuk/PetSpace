package com.netcracker.service.media;

import com.netcracker.model.record.AbstractRecord;
import com.netcracker.model.record.PhotoRecord;
import com.netcracker.model.user.Profile;
import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.util.List;

@Service
public interface MediaService {
    //поддерживать функцию просмотра следующего/предыдущего изображения при клике на кнопки “<”, ”>”
    PhotoRecord imageRotation(Profile profile);

    //открывать страничку с миниатюрами изображений после нажатия на кнопку просмотра
    // фотогалереи в профиле пользователя или питомца
//    List<WallRecord> imagesGalary(Profile profile);
    List<PhotoRecord> getImagesGalary(BigInteger albumId);

    //создание и редактирование пользователем одного и более фотоальбомов для каждого из своих питомцев
    void createAndEditPetAlbul(Profile profile);

    //добавление и сохранение нового изображения в ленте, комментариях, на страничке или в группах
    void addNewMedia(AbstractRecord abstractRecord, Profile profile);

    //возможность настроек приватности для доступа других пользователей к своим медиа-ресурсам,
    // которое будет настраиваться в профиле пользователя
    void mediaSecurity(Profile profile);

}
