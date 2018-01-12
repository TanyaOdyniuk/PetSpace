package com.netcracker.model.comment;

import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.record.RecordConstant;
import com.netcracker.model.record.WallRecord;

@ObjectType(CommentConstant.COM_TYPE)
public class WallRecordComment extends AbstractComment {
    @Reference(value = RecordConstant.REC_COMREF, isParentChild = 0)
    private WallRecord commentedWallRecord;

    public WallRecordComment() {
    }

    public WallRecordComment(String name) {
        super(name);
    }

    public WallRecordComment(String name, String description) {
        super(name, description);
    }

    public WallRecord getCommentedWallRecord() {
        return commentedWallRecord;
    }

    public void setCommentedWallRecord(WallRecord commentedWallRecord) {
        this.commentedWallRecord = commentedWallRecord;
    }

    @Override
    public String toString() {
        return "WallRecordComment{" +
                "commentedWallRecord=" + commentedWallRecord +
                '}';
    }
}
