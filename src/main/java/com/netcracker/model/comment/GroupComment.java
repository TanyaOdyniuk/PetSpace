package com.netcracker.model.comment;

import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.model.record.GroupRecord;

@ObjectType(CommentConstant.COM_TYPE)
public class GroupComment extends AbstractComment {
    //нет списка комментов
    private GroupRecord commentGroupRecord;

    public GroupComment() {
    }

    public GroupComment(String name) {
        super(name);
    }

    public GroupComment(String name, String description) {
        super(name, description);
    }

    public GroupRecord getCommentGroupRecord() {
        return commentGroupRecord;
    }

    public void setCommentGroupRecord(GroupRecord commentGroupRecord) {
        this.commentGroupRecord = commentGroupRecord;
    }

    @Override
    public String toString() {
        return "GroupComment{" +
                "commentGroupRecord=" + commentGroupRecord +
                '}';
    }
}
