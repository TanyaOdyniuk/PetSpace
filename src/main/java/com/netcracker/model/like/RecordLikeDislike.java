package com.netcracker.model.like;

import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.record.AbstractRecord;
import com.netcracker.model.record.RecordConstant;

@ObjectType(LikeConstant.LDL_TYPE)
public class RecordLikeDislike extends AbstractLikeDislike {
    @Reference(RecordConstant.REC_LDLREF)
    private AbstractRecord likedDislikedRecord;

    public RecordLikeDislike() {
    }

    public RecordLikeDislike(String name) {
        super(name);
    }

    public RecordLikeDislike(String name, String description) {
        super(name, description);
    }

    public AbstractRecord getLikedDislikedRecord() {
        return likedDislikedRecord;
    }

    public void setLikedDislikedRecord(AbstractRecord likedDislikedRecord) {
        this.likedDislikedRecord = likedDislikedRecord;
    }

    @Override
    public String toString() {
        return "RecordLikeDislike{" +
                "likedDislikedRecord=" + likedDislikedRecord +
                '}';
    }
}
