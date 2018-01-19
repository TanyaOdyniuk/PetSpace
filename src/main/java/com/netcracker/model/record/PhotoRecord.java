package com.netcracker.model.record;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.album.PhotoAlbum;
import com.netcracker.model.album.PhotoAlbumConstant;

@ObjectType(RecordConstant.PR_TYPE)
public class PhotoRecord extends AbstractRecord {

    @Attribute(RecordConstant.PR_PHOTO)
    private String photo;
    @Reference(value = PhotoAlbumConstant.PA_CONTPHOTO, isParentChild = 0)
    private PhotoAlbum photoAlbum;

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
                '}';
    }
}
