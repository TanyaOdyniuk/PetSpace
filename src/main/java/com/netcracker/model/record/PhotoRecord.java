package com.netcracker.model.record;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.album.PhotoAlbum;
import com.netcracker.model.comment.PhotoRecordComment;
import com.netcracker.model.like.AbstractLikeDislike;

import java.sql.Date;
import java.util.List;

@ObjectType(value = 203)
public class PhotoRecord extends AbstractRecord {

    @Attribute(value = 209)
    private String photo;
    @Reference(value = 303)
    private PhotoAlbum photoAlbum;
    private Date photoUploadDate;
    //TODO SERVICE TO GET LIKES
    @Attribute(value = 305)
    private List<AbstractLikeDislike> photoRecordLikes;
    //TODO SERVICE TO GET DISLIKES
    @Attribute(value = 305)
    private List<AbstractLikeDislike> photoRecordDislikes;
    //TODO SERVICE TO GET PHOTO COMMENTS
    @Attribute(value = 304)
    private List<PhotoRecordComment> photoComments;

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

    public List<PhotoRecordComment> getPhotoComments() {
        return photoComments;
    }

    public void setPhotoComments(List<PhotoRecordComment> photoComments) {
        this.photoComments = photoComments;
    }

    public PhotoAlbum getPhotoAlbum() {
        return photoAlbum;
    }

    public void setPhotoAlbum(PhotoAlbum photoAlbum) {
        this.photoAlbum = photoAlbum;
    }

    public List<AbstractLikeDislike> getPhotoRecordLikes() {
        return photoRecordLikes;
    }

    public void setPhotoRecordLikes(List<AbstractLikeDislike> photoRecordLikes) {
        this.photoRecordLikes = photoRecordLikes;
    }

    public List<AbstractLikeDislike> getPhotoRecordDislikes() {
        return photoRecordDislikes;
    }

    public void setPhotoRecordDislikes(List<AbstractLikeDislike> photoRecordDislikes) {
        this.photoRecordDislikes = photoRecordDislikes;
    }

    public Date getPhotoUploadDate() {
        return photoUploadDate;
    }

    public void setPhotoUploadDate(Date photoUploadDate) {
        this.photoUploadDate = photoUploadDate;
    }

    @Override
    public String toString() {
        return "PhotoRecord{" +
                "photo='" + photo + '\'' +
                ", photoAlbum=" + photoAlbum +
                ", photoUploadDate=" + photoUploadDate +
                ", photoRecordLikes=" + photoRecordLikes +
                ", photoRecordDislikes=" + photoRecordDislikes +
                ", photoComments=" + photoComments +
                '}';
    }
}
