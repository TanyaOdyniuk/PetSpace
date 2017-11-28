package com.netcracker.model.comment;

import com.netcracker.model.record.WallRecord;

public class WallComment extends AbstractComment {

    private WallRecord commentWallRecord;

    public WallComment() {
    }

    public WallComment(String name) {
        super(name);
    }

    public WallComment(String name, String description) {
        super(name, description);
    }

    public WallRecord getCommentWallRecord() {
        return commentWallRecord;
    }

    public void setCommentWallRecord(WallRecord commentWallRecord) {
        this.commentWallRecord = commentWallRecord;
    }

    @Override
    public String toString() {
        return "WallComment{" +
                "commentWallRecord=" + commentWallRecord +
                '}';
    }
}
