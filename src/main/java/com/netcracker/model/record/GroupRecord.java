package com.netcracker.model.record;

import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.comment.GroupRecordComment;
import com.netcracker.model.group.Group;
import com.netcracker.model.group.GroupConstant;
import com.netcracker.model.user.Profile;

import java.util.List;

@ObjectType(RecordConstant.REC_TYPE)
public class GroupRecord extends AbstractRecord {
    @Reference(value = GroupConstant.GR_AUTOR, isParentChild = 0)
    private Profile recordAuthor;
    @Reference(value = GroupConstant.GR_RECORDS, isParentChild = 0)
    private Group parentGroup;

    public GroupRecord() {
    }

    public GroupRecord(String name) {
        super(name);
    }

    public GroupRecord(String name, String description) {
        super(name, description);
    }

    public Profile getRecordAuthor() {
        return recordAuthor;
    }

    public void setRecordAuthor(Profile recordAuthor) {
        this.recordAuthor = recordAuthor;
    }

    public Group getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(Group parentGroup) {
        this.parentGroup = parentGroup;
    }

    @Override
    public String toString() {
        return "GroupRecord{" +
                "recordAuthor=" + recordAuthor +
                ", parentGroup=" + parentGroup +
                '}';
    }
}
