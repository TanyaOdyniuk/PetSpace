package com.netcracker.model.album;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.pet.PetConstant;
import com.netcracker.model.status.Status;

import java.sql.Timestamp;

@ObjectType(PhotoAlbumConstant.PA_TYPE)
public class PhotoAlbum extends BaseEntity {

    @Attribute(PhotoAlbumConstant.PA_NAME)
    private String photoAlbumName;
    @Attribute(PhotoAlbumConstant.PA_DESCR)
    private String photoAlbumDesc;
    @Attribute(PhotoAlbumConstant.PA_DATE)
    private Timestamp photoAlbumDate;
    @Reference(value = PetConstant.PET_PHOTOALBUM, isParentChild = 0)
    private Pet pet;
//    @Reference(value = /*PhotoAlbumConstant.PA_STATE*/, isParentChild = 0)
//    private Status photoAlbumStatus;


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

    public Timestamp getPhotoAlbumDate() {
        return photoAlbumDate;
    }

    public void setPhotoAlbumDate(Timestamp photoAlbumDate) {
        this.photoAlbumDate = photoAlbumDate;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

//    public Status getAlbumStatus() {
//        return photoAlbumStatus;
//    }
//
//    public void setAlbumStatus(Status albumStatus) {
//        this.photoAlbumStatus = albumStatus;
//    }

    @Override
    public String toString() {
        return "PhotoAlbum{" +
//                "photoAlbumState='" + photoAlbumStatus + '\'' +
                "photoAlbumName='" + photoAlbumName + '\'' +
                ", photoAlbumDesc='" + photoAlbumDesc + '\'' +
                ", photoAlbumDate='" + photoAlbumDate + '\'' +
                ", pet=" + pet +
                '}';
    }
}
