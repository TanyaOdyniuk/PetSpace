package com.netcracker.service.media.impl;

import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.model.album.PhotoAlbum;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.record.AbstractRecord;
import com.netcracker.model.record.PhotoRecord;
import com.netcracker.model.user.Profile;
import com.netcracker.service.media.MediaService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MediaServiceImpl implements MediaService {
    @Autowired
    EntityManagerService entityManagerService;

    @Override
    public PhotoRecord imageRotation(Profile profile) {
        return null;
    }

    @Override
    public List<PhotoRecord> getImagesGallery(BigInteger albumId) {
        String getRecordsQuery =
                "SELECT OBJECT_ID FROM OBJREFERENCE WHERE ATTRTYPE_ID = 304 AND REFERENCE = " + albumId;
        return entityManagerService.getObjectsBySQL(getRecordsQuery, PhotoRecord.class, new QueryDescriptor());
    }

    @Override
    public List<PhotoAlbum> getMyAlbums(BigInteger petId, boolean isPaging, Pair<Integer, Integer> pagingDesc, Map<String, String> sortingDesc) {
        String getAlbumsQuery =
                "SELECT OBJECT_ID FROM OBJREFERENCE WHERE ATTRTYPE_ID = 302 AND REFERENCE = " + petId;
        List<PhotoAlbum> albumsList = entityManagerService.getObjectsBySQL(getAlbumsQuery, PhotoAlbum.class, new QueryDescriptor());
        return albumsList;
    }

    @Override
    public PhotoAlbum getAlbum(BigInteger albumId) {
        return entityManagerService.getById(albumId, PhotoAlbum.class);
    }

    @Override
    public PhotoAlbum createAlbum(PhotoAlbum album, BigInteger petId) {
        PhotoAlbum newAlbum = entityManagerService.create(album);
        Pet pet = entityManagerService.getById(/*BigInteger.valueOf(203)*/petId, Pet.class);
        List<PhotoAlbum> newAlbumList = new ArrayList<>(pet.getPetPhotoAlbums());
        newAlbumList.add(newAlbum);
        pet.setPetPhotoAlbums(newAlbumList);
        pet.setPetOwner(null);
        pet.setPetSpecies(null);
        pet.setPetAdvertisements(null);
        pet.setPetStatus(null);
        entityManagerService.update(pet);
        Pet cutPet = new Pet();
        cutPet.setObjectId(pet.getObjectId());
        newAlbum.setPet(cutPet);
        return newAlbum;
    }

    @Override
    public PhotoRecord createPhotoRecord(PhotoRecord photoRecord, BigInteger albumId) {
        PhotoRecord newPhotoRecord = entityManagerService.create(photoRecord);
        PhotoAlbum album = entityManagerService.getById(/*BigInteger.valueOf(100)*/albumId, PhotoAlbum.class);
        List<PhotoRecord> newPhotosList = new ArrayList<>(album.getPhotoRecords());
        newPhotosList.add(newPhotoRecord);
        album.setPhotoRecords(newPhotosList);
        entityManagerService.update(album);
        PhotoAlbum cutAlbum = new PhotoAlbum();
        cutAlbum.setObjectId(album.getObjectId());
        newPhotoRecord.setPhotoAlbum(cutAlbum);
        return newPhotoRecord;
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
