package com.netcracker.service.media;

import com.netcracker.model.album.PhotoAlbum;
import com.netcracker.model.record.PhotoRecord;
import com.netcracker.model.user.Profile;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public interface MediaService {

    List<PhotoRecord> getImagesGallery(BigInteger albumId);

    List<PhotoRecord> getImagesGalleryByPhotoRecord(BigInteger recordId);

    List<PhotoAlbum> getMyAlbums(BigInteger profileId);

    PhotoAlbum createAlbum(PhotoAlbum album, BigInteger petId);

    PhotoRecord createPhotoRecord(PhotoRecord photoRecord, BigInteger albumId);

    List<PhotoRecord> getLastPhotos(/*BigInteger profileId*/);

    void deleteAlbum(BigInteger albumId);

    Profile getUserProfileIdOfAlbum(BigInteger albumId);

    Profile getUserProfileIdOfAlbumByPhotoRecord(BigInteger recordId);
}
