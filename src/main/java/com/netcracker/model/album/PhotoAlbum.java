package com.netcracker.model.album;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.record.PhotoRecord;

import java.util.List;

@ObjectType(value = 202)
public class PhotoAlbum extends BaseEntity {

    @Attribute(value = 207)
    private String photoAlbumName;
    @Attribute(value = 208)
    private String photoAlbumDesc;
    @Reference(value = 302)
    private Pet pet;
    //TODO SERVICE TO GET PHOTORECORDS
    @Attribute(value = 303)
    private List<PhotoRecord> photoRecords;

    public PhotoAlbum() {
    }

    public PhotoAlbum(String name) {
        super(name);
    }

    public PhotoAlbum(String name, String description) {
        super(name, description);
    }

    public String getPhotoAlbumName() {
        return photoAlbumName;
    }

    public void setPhotoAlbumName(String photoAlbumName) {
        this.photoAlbumName = photoAlbumName;
    }

    public String getPhotoAlbumDesc() {
        return photoAlbumDesc;
    }

    public void setPhotoAlbumDesc(String photoAlbumDesc) {
        this.photoAlbumDesc = photoAlbumDesc;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public List<PhotoRecord> getPhotoRecords() {
        return photoRecords;
    }

    public void setPhotoRecords(List<PhotoRecord> photoRecords) {
        this.photoRecords = photoRecords;
    }

    @Override
    public String toString() {
        return "PhotoAlbum{" +
                "photoAlbumName='" + photoAlbumName + '\'' +
                ", photoAlbumDesc='" + photoAlbumDesc + '\'' +
                ", pet=" + pet +
                ", photoRecords=" + photoRecords +
                '}';
    }
}
