package com.netcracker.service.media.impl;

import com.netcracker.dao.managerapi.ManagerAPI;
import com.netcracker.model.record.AbstractRecord;
import com.netcracker.model.record.PhotoRecord;
import com.netcracker.model.user.Profile;
import com.netcracker.service.media.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class MediaServiceImpl implements MediaService {
    @Autowired
    ManagerAPI managerAPI;

    @Override
    public PhotoRecord imageRotation(Profile profile) {
        return null;
    }

    @Override
    public List<PhotoRecord> getImagesGalary(BigInteger albumId) {
        String getRecordsQuery =
                "SELECT OBJECT_ID FROM OBJREFERENCE WHERE ATTRTYPE_ID = 303 AND REFERENCE = " + albumId;
        List<PhotoRecord> albumsRecords = managerAPI.getObjectsBySQL(getRecordsQuery, PhotoRecord.class);
        return albumsRecords;
    }

    @Override
    public void createAndEditPetAlbul(Profile profile) {

    }

    @Override
    public void addNewMedia(AbstractRecord abstractRecord, Profile profile) {

    }

    @Override
    public void mediaSecurity(Profile profile) {

    }
}
