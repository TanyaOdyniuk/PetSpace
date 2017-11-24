package com.netcracker.model.comment.old;

import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.record.old.WallRecord;

@ObjectType(value = 401)
public class WallComment extends AbstractComment {
    @Reference(value = 421)
    private WallRecord wallRecord;

    public WallComment() {
    }

    public WallComment(String name) {
        super(name);
    }

    public WallComment(String name, String description) {
        super(name, description);
    }

    public WallRecord getWallRecord() {
        return wallRecord;
    }

    public void setWallRecord(WallRecord wallRecord) {
        this.wallRecord = wallRecord;
    }

    @Override
    public String toString() {
        return "WallComment{" +
                "wallRecord=" + wallRecord +
                '}';
    }
}
