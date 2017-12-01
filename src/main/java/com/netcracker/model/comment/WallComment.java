package com.netcracker.model.comment;

import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.record.WallRecord;
import com.netcracker.model.user.UsersProfileConstant;

@ObjectType(CommentConstant.COM_TYPE)
public class WallComment extends AbstractComment {
    @Reference(UsersProfileConstant.PROFILE_WALLREC)
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
