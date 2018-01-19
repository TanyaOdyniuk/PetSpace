package com.netcracker.service.media.impl;

import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.model.album.PhotoAlbum;
import com.netcracker.model.album.PhotoAlbumConstant;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.pet.PetConstant;
import com.netcracker.model.record.AbstractRecord;
import com.netcracker.model.record.PhotoRecord;
import com.netcracker.model.user.Profile;
import com.netcracker.service.media.MediaService;
import com.netcracker.service.status.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class MediaServiceImpl implements MediaService {
    @Autowired
    private EntityManagerService entityManagerService;
    @Autowired
    private StatusService statusService;

    @Override
    public PhotoRecord imageRotation(Profile profile) {
        return null;
    }

    @Override
    public List<PhotoRecord> getImagesGallery(BigInteger albumId) {
        String getRecordsQuery = "SELECT OBJECT_ID FROM OBJREFERENCE " +
                "WHERE REFERENCE = " + albumId + " AND ATTRTYPE_ID = " + PhotoAlbumConstant.PA_CONTPHOTO;
        QueryDescriptor queryDescriptor = new QueryDescriptor();
        queryDescriptor.addSortingDesc(408, "DESC", true);
        return entityManagerService.getObjectsBySQL(getRecordsQuery, PhotoRecord.class, queryDescriptor);
    }

    @Override
    public List<PhotoAlbum> getMyAlbums(BigInteger profileId) {
        String getAlbumsQuery = "SELECT OBJECT_ID FROM OBJREFERENCE\n" +
                "WHERE REFERENCE IN(SELECT OBJECT_ID FROM OBJREFERENCE WHERE " +
                "REFERENCE = " + profileId + " AND ATTRTYPE_ID = " + PetConstant.PET_OWNER + ")\n" +
                "AND ATTRTYPE_ID = " + PhotoAlbumConstant.PET_PHOTOALBUM;
        QueryDescriptor queryDescriptor = new QueryDescriptor();
        queryDescriptor.addSortingDesc(434, "DESC", true);
        return entityManagerService.getObjectsBySQL(getAlbumsQuery, PhotoAlbum.class, queryDescriptor);
    }

    @Override
    public PhotoAlbum createAlbum(PhotoAlbum album, BigInteger petId) {
        Pet pet = entityManagerService.getById(petId, Pet.class);
//        album.setAlbumStatus(statusService.getActiveStatus());
        album.setPet(pet);
        album.setName("album");
        return entityManagerService.create(album);
    }

    @Override
    public PhotoRecord createPhotoRecord(PhotoRecord photoRecord, BigInteger albumId) {
        PhotoAlbum album = entityManagerService.getById(albumId, PhotoAlbum.class);
        photoRecord.setRecordState(statusService.getActiveStatus());
        photoRecord.setPhotoAlbum(album);
        photoRecord.setName("protoRecord");
        return entityManagerService.create(photoRecord);
    }

    @Override
    public List<PhotoRecord> getLastPhotos() {
        String getRecordsQuery = "SELECT OBJECT_ID FROM OBJREFERENCE Y, ( SELECT O.REFERENCE, MAX(O.SEQ_NO) MM  " +
                "FROM OBJREFERENCE O, ( SELECT REFERENCE RE, SEQ_NO AS S FROM OBJREFERENCE " +
                "WHERE REFERENCE IN ( " +
                "SELECT OBJECT_ID FROM OBJREFERENCE WHERE REFERENCE IN ( " +
                "SELECT OBJECT_ID FROM OBJREFERENCE WHERE REFERENCE = 25 AND ATTRTYPE_ID = 303" +
                ") AND ATTRTYPE_ID = 302) AND ATTRTYPE_ID = 304) X WHERE X.RE = o.REFERENCE AND X.S = O.SEQ_NO " +
                "AND O.OBJECT_ID IN ( SELECT OBJECT_ID ODJ FROM OBJREFERENCE Z WHERE Z.ATTRTYPE_ID = 431 AND REFERENCE = 8)" +
                "GROUP BY O.REFERENCE) N WHERE Y.REFERENCE = N.REFERENCE AND Y.SEQ_NO = N.MM ORDER BY Y.REFERENCE";
        return entityManagerService.getObjectsBySQL(getRecordsQuery, PhotoRecord.class, new QueryDescriptor());
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
