package com.netcracker.model.comment;

import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.group.GroupConstant;
import com.netcracker.model.record.GroupRecord;

@ObjectType(CommentConstant.COM_TYPE)
public class GroupRecordComment extends AbstractComment {
    @Reference(value = GroupConstant.GR_TYPE, isParentChild = 0)//нет списка комментов
    private GroupRecord commentedGroupRecord;

    public GroupRecordComment() {
    }

    public GroupRecordComment(String name) {
        super(name);
    }

    public GroupRecordComment(String name, String description) {
        super(name, description);
    }

    public GroupRecord getCommentedGroupRecord() {
        return commentedGroupRecord;
    }

    public void setCommentedGroupRecord(GroupRecord commentedGroupRecord) {
        this.commentedGroupRecord = commentedGroupRecord;
    }

    @Override
    public String toString() {
        return "GroupRecordComment{" +
                "commentedGroupRecord=" + commentedGroupRecord +
                '}';
    }
}