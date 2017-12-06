package com.netcracker.service.media.impl;

import com.netcracker.dao.managerapi.ManagerAPI;
import com.netcracker.model.album.PhotoAlbum;
import com.netcracker.model.record.AbstractRecord;
import com.netcracker.model.record.PhotoRecord;
import com.netcracker.model.user.Profile;
import com.netcracker.service.media.MediaService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Service
public class MediaServiceImpl implements MediaService {
    @Autowired
    ManagerAPI managerAPI;

    @Override
    public PhotoRecord imageRotation(Profile profile) {
        return null;
    }

    @Override
    public List<PhotoRecord> getImagesGallery(BigInteger albumId, boolean isPaging, Pair<Integer, Integer> pagingDesc, Map<String, String> sortingDesc) {
        String getRecordsQuery =
                "SELECT OBJECT_ID FROM OBJREFERENCE WHERE ATTRTYPE_ID = 304 AND REFERENCE = " + albumId;
        List<PhotoRecord> albumsRecords = managerAPI.getObjectsBySQL(getRecordsQuery, PhotoRecord.class, isPaging, pagingDesc, sortingDesc);
        System.out.println(albumsRecords.toString());
        return albumsRecords;
    }

    @Override
    public PhotoAlbum getAlbum(BigInteger albumId) {
        PhotoAlbum photoAlbum = managerAPI.getById(albumId, PhotoAlbum.class);
        System.out.println(photoAlbum.toString());
        return photoAlbum;
    }

    @Override
    public void createAndEditPetAlbum(Profile profile) {

    }

    @Override
    public void addNewMedia(AbstractRecord abstractRecord, Profile profile) {

    }

    @Override
    public void mediaSecurity(Profile profile) {

    }
}
