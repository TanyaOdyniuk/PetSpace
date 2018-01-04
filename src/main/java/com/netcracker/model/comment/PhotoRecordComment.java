package com.netcracker.model.comment;

import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.record.PhotoRecord;
import com.netcracker.model.record.RecordConstant;

@ObjectType(CommentConstant.COM_TYPE)
public class PhotoRecordComment extends AbstractComment {
    @Reference(value = RecordConstant.PR_COMMENTS, isParentChild = 0)
    private PhotoRecord commentedPhotoRecord;

    public PhotoRecordComment() {
    }

    public PhotoRecordComment(String name) {
        super(name);
    }

    public PhotoRecordComment(String name, String description) {
        super(name, description);
    }

    public PhotoRecord getCommentedPhotoRecord() {
        return commentedPhotoRecord;
    }

    public void setCommentedPhotoRecord(PhotoRecord commentedPhotoRecord) {
        this.commentedPhotoRecord = commentedPhotoRecord;
    }

    @Override
    public String toString() {
        return "PhotoRecordComment{" +
                "commentedPhotoRecord=" + commentedPhotoRecord +
                '}';
    }
}
