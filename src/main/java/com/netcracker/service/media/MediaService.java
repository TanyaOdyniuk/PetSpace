package com.netcracker.service.media;

import com.netcracker.model.album.PhotoAlbum;
import com.netcracker.model.record.AbstractRecord;
import com.netcracker.model.record.PhotoRecord;
import com.netcracker.model.user.Profile;
import javafx.util.Pair;
import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Service
public interface MediaService {
    //поддерживать функцию просмотра следующего/предыдущего изображения при клике на кнопки “<”, ”>”
    PhotoRecord imageRotation(Profile profile);

    //открывать страничку с миниатюрами изображений после нажатия на кнопку просмотра
    // фотогалереи в профиле пользователя или питомца
//    List<WallRecord> imagesGalary(Profile profile);
    List<PhotoRecord> getImagesGallery(BigInteger albumId);

    List<PhotoAlbum> getMyAlbums(BigInteger petId, boolean isPaging, Pair<Integer, Integer> pagingDesc, Map<String, String> sortingDesc);

    PhotoAlbum getAlbum(BigInteger albumId);

    PhotoAlbum createAlbum(PhotoAlbum album, BigInteger petId);

    PhotoRecord createPhotoRecord(PhotoRecord photoRecord, BigInteger albumId);

    //создание и редактирование пользователем одного и более фотоальбомов для каждого из своих питомцев
    void createAndEditPetAlbum(Profile profile);

    //добавление и сохранение нового изображения в ленте, комментариях, на страничке или в группах
    void addNewMedia(AbstractRecord abstractRecord, Profile profile);

    //возможность настроек приватности для доступа других пользователей к своим медиа-ресурсам,
    // которое будет настраиваться в профиле пользователя
    void mediaSecurity(Profile profile);

}
