package com.netcracker.model.like.old;

import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.record.old.GroupRecord;

import javax.persistence.FieldResult;

@ObjectType(value = 400)
public class GroupRecordLikeDislike extends AbstractLikeDislike {
    @Reference(value = 428)
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
