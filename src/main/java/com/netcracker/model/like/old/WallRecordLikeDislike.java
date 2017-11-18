package com.netcracker.model.like.old;

import com.netcracker.model.record.old.WallRecord;

public class WallRecordLikeDislike extends AbstractLikeDislike {
    private WallRecord wallRecord;

    public WallRecordLikeDislike() {
    }

    public WallRecordLikeDislike(String name) {
        super(name);
    }

    public WallRecordLikeDislike(String name, String description) {
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
        return "WallRecordLikeDislike{" +
                "wallRecord=" + wallRecord +
                '}';
    }
}
