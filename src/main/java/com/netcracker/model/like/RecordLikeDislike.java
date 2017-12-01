package com.netcracker.model.like;

import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.record.AbstractRecord;
import com.netcracker.model.record.RecordConstant;

@ObjectType(LikeConstant.LDL_TYPE)
public class RecordLikeDislike extends AbstractLikeDislike {
    @Reference(RecordConstant.REC_LDLREF)
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
