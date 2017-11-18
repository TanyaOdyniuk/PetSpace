package com.netcracker.model.comment.old;

import com.netcracker.model.record.old.GroupRecord;

public class GroupComment extends AbstractComment {

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
