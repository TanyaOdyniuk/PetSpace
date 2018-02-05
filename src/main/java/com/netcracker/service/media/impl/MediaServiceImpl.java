package com.netcracker.service.media.impl;

import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.model.album.PhotoAlbum;
import com.netcracker.model.album.PhotoAlbumConstant;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.pet.PetConstant;
import com.netcracker.model.record.PhotoRecord;
import com.netcracker.model.user.Profile;
import com.netcracker.service.media.MediaService;
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

    @Override
    public Pet getPetByAlbum(BigInteger albumId) {
        String query = "SELECT REFERENCE FROM OBJREFERENCE " +
                "WHERE OBJECT_ID = " + albumId + " AND ATTRTYPE_ID = " + PhotoAlbumConstant.PET_PHOTOALBUM;
        List<Pet> albums = entityManagerService.getObjectsBySQL(query, Pet.class, new QueryDescriptor());
        return albums.get(0);
    }

    @Override
    public PhotoAlbum getAlbumByPhotoRecord(BigInteger recordId) {
        String query = "SELECT REFERENCE FROM OBJREFERENCE " +
                "WHERE OBJECT_ID = " + recordId + " AND ATTRTYPE_ID = " + PhotoAlbumConstant.PA_CONTPHOTO;
        List<PhotoAlbum> albums = entityManagerService.getObjectsBySQL(query, PhotoAlbum.class, new QueryDescriptor());
        return albums.get(0);
    }
}
