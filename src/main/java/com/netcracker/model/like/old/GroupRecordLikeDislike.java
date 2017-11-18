package com.netcracker.model.like.old;

import com.netcracker.model.record.old.GroupRecord;

public class GroupRecordLikeDislike extends AbstractLikeDislike {
    private GroupRecord groupRecord;

    public GroupRecordLikeDislike() {
    }

    public GroupRecordLikeDislike(String name) {
        super(name);
    }

    public GroupRecordLikeDislike(String name, String description) {
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
        return "GroupRecordLikeDislike{" +
                "groupRecord=" + groupRecord +
                '}';
    }
}
