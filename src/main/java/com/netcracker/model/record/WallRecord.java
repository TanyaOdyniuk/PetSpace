package com.netcracker.model.record;

import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.comment.WallRecordComment;
import com.netcracker.model.user.Profile;

import java.util.List;

@ObjectType(RecordConstant.REC_TYPE)
public class WallRecord extends AbstractRecord {
    @Reference(RecordConstant.REC_AUTOR)
    private Profile recordAuthor;
    @Reference(RecordConstant.REC_COMREF)
    private List<WallRecordComment> wallRecordComments;

    public WallRecord() {
    }

    public WallRecord(String name) {
        super(name);
    }

    public WallRecord(String name, String description) {
        super(name, description);
    }

    public Profile getRecordAuthor() {
        return recordAuthor;
    }

    public void setRecordAuthor(Profile recordAuthor) {
        this.recordAuthor = recordAuthor;
    }

    public List<WallRecordComment> getWallRecordComments() {
        return wallRecordComments;
    }

    public void setWallRecordComments(List<WallRecordComment> wallRecordComments) {
        this.wallRecordComments = wallRecordComments;
    }

    @Override
    public String toString() {
        return "WallRecord{" +
                "recordAuthor=" + recordAuthor +
                ", wallRecordComments=" + wallRecordComments +
                '}';
    }
}
