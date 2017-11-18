package com.netcracker.model.record;

import com.netcracker.model.album.PhotoAlbum;
import com.netcracker.model.comment.Comment;
import com.netcracker.model.like.LikeDislike;
import com.netcracker.model.pet.Pet;

import java.util.List;

public class PhotoRecord extends AbstractRecord {

    private String photo;
    //TODO SERVICE GETPHOTOCOMMENTS
    private List<Comment> photoComments;
    private PhotoAlbum photoAlbum;
    //TODO SERVICE TO GET LIKES
    private List<LikeDislike> photoRecordLikes;
    //TODO SERVICE TO GET DISLIKES
    private List<LikeDislike> photoRecordDislikes;
    private Pet pet;

    public PhotoRecord() {
    }

    public PhotoRecord(String name) {
        super(name);
    }

    public PhotoRecord(String name, String description) {
        super(name, description);
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<Comment> getPhotoComments() {
        return photoComments;
    }

    public void setPhotoComments(List<Comment> photoComments) {
        this.photoComments = photoComments;
    }

    public PhotoAlbum getPhotoAlbum() {
        return photoAlbum;
    }

    public void setPhotoAlbum(PhotoAlbum photoAlbum) {
        this.photoAlbum = photoAlbum;
    }

    public List<LikeDislike> getPhotoRecordLikes() {
        return photoRecordLikes;
    }

    public void setPhotoRecordLikes(List<LikeDislike> photoRecordLikes) {
        this.photoRecordLikes = photoRecordLikes;
    }

    public List<LikeDislike> getPhotoRecordDislikes() {
        return photoRecordDislikes;
    }

    public void setPhotoRecordDislikes(List<LikeDislike> photoRecordDislikes) {
        this.photoRecordDislikes = photoRecordDislikes;
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
                "photo='" + photo + '\'' +
                ", photoComments=" + photoComments +
                ", photoAlbum=" + photoAlbum +
                ", photoRecordLikes=" + photoRecordLikes +
                ", photoRecordDislikes=" + photoRecordDislikes +
                ", pet=" + pet +
                '}';
    }
}
