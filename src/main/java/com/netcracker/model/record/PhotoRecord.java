package com.netcracker.model.record;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.album.PhotoAlbum;
import com.netcracker.model.album.PhotoAlbumConstant;
import com.netcracker.model.comment.PhotoRecordComment;

import java.util.List;

@ObjectType(RecordConstant.PR_TYPE)
public class PhotoRecord extends AbstractRecord {

    @Attribute(RecordConstant.PR_PHOTO)
    private String photo;
    @Reference(PhotoAlbumConstant.PA_CONTPHOTO)
    private PhotoAlbum photoAlbum;
/*  @Reference(RecordConstant.PR_LIKE)
    private List<AbstractLikeDislike> photoRecordLikes;
    @Reference(RecordConstant.PR_LIKE)
    private List<AbstractLikeDislike> photoRecordDislikes;*/
    //TODO SERVICE TO GET PHOTO COMMENTS
    @Reference(RecordConstant.PR_COMMENTS)
    private List<PhotoRecordComment> photoRecordComments;

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

    public List<PhotoRecordComment> getPhotoRecordComments() {
        return photoRecordComments;
    }

    public void setPhotoRecordComments(List<PhotoRecordComment> photoRecordComments) {
        this.photoRecordComments = photoRecordComments;
    }

    public PhotoAlbum getPhotoAlbum() {
        return photoAlbum;
    }

    public void setPhotoAlbum(PhotoAlbum photoAlbum) {
        this.photoAlbum = photoAlbum;
    }

    @Override
    public String toString() {
        return "PhotoRecord{" +
                "photo='" + photo + '\'' +
                ", photoAlbum=" + photoAlbum +
                ", photoRecordComments=" + photoRecordComments +
                '}';
    }
}
