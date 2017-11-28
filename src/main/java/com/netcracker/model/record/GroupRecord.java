package com.netcracker.model.record;

import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.comment.GroupComment;
import com.netcracker.model.group.Group;

import java.util.List;

public class GroupRecord extends AbstractRecord {
    @Reference(value = 428)
    private Group recordGroup;
    private List<GroupComment> recordGroupComments;

    public GroupRecord() {
    }

    public GroupRecord(String name) {
        super(name);
    }

    public GroupRecord(String name, String description) {
        super(name, description);
    }

    public Group getRecordGroup() {
        return recordGroup;
    }

    public void setRecordGroup(Group recordGroup) {
        this.recordGroup = recordGroup;
    }

    public List<GroupComment> getRecordGroupComments() {
        return recordGroupComments;
    }

    public void setRecordGroupComments(List<GroupComment> recordGroupComments) {
        this.recordGroupComments = recordGroupComments;
    }

    @Override
    public String toString() {
        return "GroupRecord{" +
                "recordGroup=" + recordGroup +
                ", recordGroupComments=" + recordGroupComments +
                '}';
    }
}
