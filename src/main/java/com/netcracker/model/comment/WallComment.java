package com.netcracker.model.comment;

import com.netcracker.model.record.WallRecord;

public class WallComment extends AbstractComment {
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
