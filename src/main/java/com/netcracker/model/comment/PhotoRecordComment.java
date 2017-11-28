package com.netcracker.model.comment;

import com.netcracker.model.record.PhotoRecord;

public class PhotoRecordComment extends AbstractComment {
    private PhotoRecord commentPhotoRecord;

    public PhotoRecordComment() {
    }

    public PhotoRecordComment(String name) {
        super(name);
    }

    public PhotoRecordComment(String name, String description) {
        super(name, description);
    }

    public PhotoRecord getCommentPhotoRecord() {
        return commentPhotoRecord;
    }

    public void setCommentPhotoRecord(PhotoRecord commentPhotoRecord) {
        this.commentPhotoRecord = commentPhotoRecord;
    }

    @Override
    public String toString() {
        return "PhotoRecordComment{" +
                "commentPhotoRecord=" + commentPhotoRecord +
                '}';
    }
}
