package com.netcracker.model.comment;

import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.record.PhotoRecord;
import com.netcracker.model.record.RecordConstant;

@ObjectType(CommentConstant.COM_TYPE)
public class PhotoRecordComment extends AbstractComment {
    @Reference(RecordConstant.PR_COMMENTS)
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
