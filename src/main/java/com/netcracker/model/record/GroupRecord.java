package com.netcracker.model.record;

import com.netcracker.model.comment.GroupComment;
import com.netcracker.model.group.Group;

import java.util.List;

public class GroupRecord extends AbstractRecord {

    private Group group;
    private List<GroupComment> groupComments;

    public GroupRecord() {
    }

    public GroupRecord(String name) {
        super(name);
    }

    public GroupRecord(String name, String description) {
        super(name, description);
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<GroupComment> getGroupComments() {
        return groupComments;
    }

    public void setGroupComments(List<GroupComment> groupComments) {
        this.groupComments = groupComments;
    }

    @Override
    public String toString() {
        return "GroupRecord{" +
                "group=" + group +
                ", groupComments=" + groupComments +
                '}';
    }
}
