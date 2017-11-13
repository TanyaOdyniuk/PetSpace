package com.netcracker.model.record;

import com.netcracker.model.album.PhotoAlbum;
import com.netcracker.model.comment.PhotoComment;
import com.netcracker.model.like.PhotoRecordLikeDislike;
import com.netcracker.model.pet.Pet;

import java.util.List;

public class PhotoRecord extends AbstractRecord {

    private Object photo;
    private List<PhotoComment> photoComments;
    private PhotoAlbum photoAlbum;
    private List<PhotoRecordLikeDislike> photoRecordLikeDislikes;
    private Pet pet;

    public PhotoRecord() {
    }

    public PhotoRecord(String name) {
        super(name);
    }

    public PhotoRecord(String name, String description) {
        super(name, description);
    }

    public Object getPhoto() {
        return photo;
    }

    public void setPhoto(Object photo) {
        this.photo = photo;
    }

    public List<PhotoComment> getPhotoComments() {
        return photoComments;
    }

    public void setPhotoComments(List<PhotoComment> photoComments) {
        this.photoComments = photoComments;
    }

    public PhotoAlbum getPhotoAlbum() {
        return photoAlbum;
    }

    public void setPhotoAlbum(PhotoAlbum photoAlbum) {
        this.photoAlbum = photoAlbum;
    }

    public List<PhotoRecordLikeDislike> getPhotoRecordLikeDislikes() {
        return photoRecordLikeDislikes;
    }

    public void setPhotoRecordLikeDislikes(List<PhotoRecordLikeDislike> photoRecordLikeDislikes) {
        this.photoRecordLikeDislikes = photoRecordLikeDislikes;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    @Override
    public String toString() {
        return "PhotoRecord{" +
                "photo=" + photo +
                ", photoComments=" + photoComments +
                ", photoAlbum=" + photoAlbum +
                ", likes=" + photoRecordLikeDislikes +
                ", pet=" + pet +
                '}';
    }
}
