package com.netcracker.model.record;

import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.comment.WallRecordComment;
import com.netcracker.model.user.Profile;

import java.util.List;

@ObjectType(RecordConstant.REC_TYPE)
public class WallRecord extends AbstractRecord {
    @Reference(value = RecordConstant.REC_AUTOR, isParentChild = 0)
    private Profile recordAuthor;
    @Reference(value = RecordConstant.REC_WALLOWNER, isParentChild = 0)
    private Profile wallOwner;

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

    public Profile getWallOwner() {
        return wallOwner;
    }

    public void setWallOwner(Profile wallOwner) {
        this.wallOwner = wallOwner;
    }

    @Override
    public String toString() {
        return "WallRecord{" +
                "recordAuthor=" + recordAuthor +
                "wallOwner=" + wallOwner +
                '}';
    }
}
