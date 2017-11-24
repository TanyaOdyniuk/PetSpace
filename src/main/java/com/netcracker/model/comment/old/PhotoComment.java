package com.netcracker.model.comment.old;

import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.record.PhotoRecord;

@ObjectType(value = 401)
public class PhotoComment extends AbstractComment {

    @Reference(value = 304)
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
