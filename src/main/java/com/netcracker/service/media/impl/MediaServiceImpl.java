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
import com.netcracker.service.record.RecordService;
import com.netcracker.service.status.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

import static com.netcracker.dao.manager.query.Query.IGNORING_DELETED_ELEMENTS;
import static com.netcracker.dao.manager.query.Query.IGNORING_DELETED_ELEMENTS_IN_REF;

@Service
public class MediaServiceImpl implements MediaService {
    @Autowired
    private EntityManagerService entityManagerService;
    @Autowired
    private StatusService statusService;

    @Override
    public List<PhotoRecord> getImagesGallery(BigInteger albumId) {
        String getRecordsQuery = "SELECT OBJECT_ID FROM OBJREFERENCE " +
                "WHERE REFERENCE = " + albumId + " AND ATTRTYPE_ID = " + PhotoAlbumConstant.PA_CONTPHOTO +
                " and " + IGNORING_DELETED_ELEMENTS_IN_REF;
        QueryDescriptor queryDescriptor = new QueryDescriptor();
        queryDescriptor.addSortingDesc(408, "DESC", true);
        return entityManagerService.getObjectsBySQL(getRecordsQuery, PhotoRecord.class, queryDescriptor);
    }

    @Override
    public List<PhotoRecord> getImagesGalleryByPhotoRecord(BigInteger recordId) {
        String getRecordsQuery = "SELECT OBJECT_ID FROM OBJREFERENCE " +
                "WHERE REFERENCE = (SELECT REFERENCE FROM OBJREFERENCE " +
                "WHERE OBJECT_ID = " + recordId + " AND ATTRTYPE_ID = " + PhotoAlbumConstant.PA_CONTPHOTO +
                " and " + IGNORING_DELETED_ELEMENTS_IN_REF +  ") " +
                "AND ATTRTYPE_ID = " + PhotoAlbumConstant.PA_CONTPHOTO +
                " and " + IGNORING_DELETED_ELEMENTS_IN_REF;
        QueryDescriptor queryDescriptor = new QueryDescriptor();
        queryDescriptor.addSortingDesc(408, "DESC", true);
        return entityManagerService.getObjectsBySQL(getRecordsQuery, PhotoRecord.class, queryDescriptor);
    }

    @Override
    public List<PhotoAlbum> getMyAlbums(BigInteger profileId) {
        String getAlbumsQuery = "SELECT OBJECT_ID FROM OBJREFERENCE\n" +
                "WHERE REFERENCE IN(SELECT OBJECT_ID FROM OBJREFERENCE WHERE " +
                "REFERENCE = " + profileId + " AND ATTRTYPE_ID = " + PetConstant.PET_OWNER +
                " and " + IGNORING_DELETED_ELEMENTS + " )\n" +
                "AND ATTRTYPE_ID = " + PhotoAlbumConstant.PET_PHOTOALBUM;
        QueryDescriptor queryDescriptor = new QueryDescriptor();
        queryDescriptor.addSortingDesc(434, "DESC", true);
        return entityManagerService.getObjectsBySQL(getAlbumsQuery, PhotoAlbum.class, queryDescriptor);
    }

    @Override
    public PhotoAlbum createAlbum(PhotoAlbum album, BigInteger petId) {
        Pet pet = entityManagerService.getById(petId, Pet.class);
        album.setPhotoAlbumStatus(statusService.getActiveStatus());
        album.setPet(pet);
        album.setName("album");
        return entityManagerService.create(album);
    }

    @Override
    public PhotoRecord createPhotoRecord(PhotoRecord photoRecord, BigInteger albumId) {
        PhotoAlbum album = entityManagerService.getById(albumId, PhotoAlbum.class);
        photoRecord.setRecordState(statusService.getActiveStatus());
        photoRecord.setPhotoAlbum(album);
        photoRecord.setName("PhotoRecord");
        photoRecord.setName("For album №" + album.getObjectId());
        return entityManagerService.create(photoRecord);
    }

    @Override
    public List<PhotoRecord> getLastPhotos(/*BigInteger profileId*/) {
        String getRecordsQuery = "SELECT OBJECT_ID FROM OBJREFERENCE Y, ( SELECT O.REFERENCE, MAX(O.SEQ_NO) MM  " +
                "FROM OBJREFERENCE O, ( SELECT REFERENCE RE, SEQ_NO AS S FROM OBJREFERENCE " +
                "WHERE REFERENCE IN ( " +
                "SELECT OBJECT_ID FROM OBJREFERENCE WHERE REFERENCE IN ( " +
                "SELECT OBJECT_ID FROM OBJREFERENCE WHERE REFERENCE = " + 25 + " AND ATTRTYPE_ID = 303" +
                ") AND ATTRTYPE_ID = 302) AND ATTRTYPE_ID = 304) X WHERE X.RE = O.REFERENCE AND X.S = O.SEQ_NO " +
                "AND O.OBJECT_ID IN ( SELECT OBJECT_ID ODJ FROM OBJREFERENCE Z WHERE Z.ATTRTYPE_ID = 431 AND REFERENCE = 8) " +
                "GROUP BY O.REFERENCE) N WHERE Y.REFERENCE = N.REFERENCE AND Y.SEQ_NO = N.MM ORDER BY Y.REFERENCE";
//        String sql = "SELECT OBJECT_ID FROM OBJECTS WHERE OBJECT_ID = 167";
        return entityManagerService.getObjectsBySQL(getRecordsQuery, PhotoRecord.class, new QueryDescriptor());
    }

    @Override
    public void deleteAlbum(BigInteger albumId) {
        entityManagerService.delete(albumId, -1);
    }

    @Override
    public Profile getUserProfileIdOfAlbum(BigInteger albumId) {
        String query = "SELECT REFERENCE FROM OBJREFERENCE WHERE ATTRTYPE_ID = 303" +
                "AND OBJECT_ID = (SELECT REFERENCE FROM OBJREFERENCE WHERE ATTRTYPE_ID = 302 AND OBJECT_ID = " + albumId + ")";
        List<Profile> profs = entityManagerService.getObjectsBySQL(query, Profile.class, new QueryDescriptor());
        return profs.get(0);
    }

    @Override
    public Profile getUserProfileIdOfAlbumByPhotoRecord(BigInteger recordId) {
        String query = "SELECT REFERENCE FROM OBJREFERENCE WHERE ATTRTYPE_ID = " + PetConstant.PET_OWNER +
                "AND OBJECT_ID = (SELECT REFERENCE FROM OBJREFERENCE " +
                "WHERE ATTRTYPE_ID = " + PhotoAlbumConstant.PET_PHOTOALBUM + " AND OBJECT_ID = " +
                "(SELECT REFERENCE FROM OBJREFERENCE WHERE OBJECT_ID = " + recordId + " AND ATTRTYPE_ID = " + PhotoAlbumConstant.PA_CONTPHOTO + "))";
        List<Profile> profs = entityManagerService.getObjectsBySQL(query, Profile.class, new QueryDescriptor());
        return profs.get(0);
    }
}
