package com.netcracker.model.like.old;

import com.netcracker.model.record.PhotoRecord;

public class PhotoRecordLikeDislike extends AbstractLikeDislike {
    private PhotoRecord photoRecord;

    public PhotoRecordLikeDislike() {
    }

    public PhotoRecordLikeDislike(String name) {
        super(name);
    }

    public PhotoRecordLikeDislike(String name, String description) {
        super(name, description);
    }

    public PhotoRecord getPhotoRecord() {
        return photoRecord;
    }

    public void setPhotoRecord(PhotoRecord photoRecord) {
        this.photoRecord = photoRecord;
    }

    @Override
    public String toString() {
        return "PhotoRecordLikeDislike{" +
                "photoRecord=" + photoRecord +
                '}';
    }
}
