package com.netcracker.model.comment;

import com.netcracker.model.record.GroupRecord;

public class GroupComment extends AbstractComment {
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
