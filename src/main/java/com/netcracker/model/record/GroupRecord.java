package com.netcracker.model.record;

import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.comment.GroupRecordComment;
import com.netcracker.model.group.Group;
import com.netcracker.model.group.GroupConstant;

import java.util.List;

@ObjectType(RecordConstant.REC_TYPE)
public class GroupRecord extends AbstractRecord {
    @Reference(GroupConstant.GR_RECORDS)
    private Group parentGroup;
    @Reference(RecordConstant.REC_COMREF)
    private List<GroupRecordComment> groupRecordComments;

    public GroupRecord() {
    }

    public GroupRecord(String name) {
        super(name);
    }

    public GroupRecord(String name, String description) {
        super(name, description);
    }

    public Group getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(Group parentGroup) {
        this.parentGroup = parentGroup;
    }

    public List<GroupRecordComment> getGroupRecordComments() {
        return groupRecordComments;
    }

    public void setGroupRecordComments(List<GroupRecordComment> groupRecordComments) {
        this.groupRecordComments = groupRecordComments;
    }

    @Override
    public String toString() {
        return "GroupRecord{" +
                "parentGroup=" + parentGroup +
                ", groupRecordComments=" + groupRecordComments +
                '}';
    }
}
