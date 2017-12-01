package com.netcracker.model.record;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.comment.WallComment;
import com.netcracker.model.user.Profile;
import com.netcracker.model.user.UsersProfileConstant;

import java.util.List;

@ObjectType(RecordConstant.REC_TYPE)
public class WallRecord extends AbstractRecord {
    @Reference(RecordConstant.REC_AUTOR)
    private Profile recordAuthor;
    @Reference(UsersProfileConstant.PROFILE_WALLREC)
    private List<WallComment> wallComments;

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

    public List<WallComment> getWallComments() {
        return wallComments;
    }

    public void setWallComments(List<WallComment> wallComments) {
        this.wallComments = wallComments;
    }

    @Override
    public String toString() {
        return "WallRecord{" +
                "recordAuthor=" + recordAuthor +
                ", wallComments=" + wallComments +
                '}';
    }
}
