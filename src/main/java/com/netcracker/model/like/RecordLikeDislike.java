package com.netcracker.model.like;

import com.netcracker.model.record.AbstractRecord;

public class RecordLikeDislike extends AbstractLikeDislike {
    private AbstractRecord likeDislikeRecord;

    public RecordLikeDislike() {
    }

    public RecordLikeDislike(String name) {
        super(name);
    }

    public RecordLikeDislike(String name, String description) {
        super(name, description);
    }

    public AbstractRecord getLikeDislikeRecord() {
        return likeDislikeRecord;
    }

    public void setLikeDislikeRecord(AbstractRecord likeDislikeRecord) {
        this.likeDislikeRecord = likeDislikeRecord;
    }

    @Override
    public String toString() {
        return "RecordLikeDislike{" +
                "likeDislikeRecord=" + likeDislikeRecord +
                '}';
    }
}
