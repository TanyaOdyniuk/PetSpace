package com.netcracker.model.comment.old;

import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.record.old.GroupRecord;

@ObjectType(value = 401)
public class GroupComment extends AbstractComment {

    @Reference(value = 428)
    private GroupRecord groupRecord;

    public GroupComment() {
    }

    public GroupComment(String name) {
        super(name);
    }

    public GroupComment(String name, String description) {
        super(name, description);
    }

    public GroupRecord getGroupRecord() {
        return groupRecord;
    }

    public void setGroupRecord(GroupRecord groupRecord) {
        this.groupRecord = groupRecord;
    }

    @Override
    public String toString() {
        return "GroupComment{" +
                "groupRecord=" + groupRecord +
                '}';
    }
}
