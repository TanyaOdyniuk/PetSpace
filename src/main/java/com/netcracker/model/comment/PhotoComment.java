package com.netcracker.model.comment;

import com.netcracker.model.record.PhotoRecord;

public class PhotoComment extends AbstractComment {

    private PhotoRecord photoRecord;

    public PhotoComment() {
    }

    public PhotoComment(String name) {
        super(name);
    }

    public PhotoComment(String name, String description) {
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
        return "PhotoComment{" +
                "photoRecord=" + photoRecord +
                '}';
    }
}
