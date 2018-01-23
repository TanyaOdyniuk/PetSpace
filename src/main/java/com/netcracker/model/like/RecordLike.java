package com.netcracker.model.like;

import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.record.AbstractRecord;
import com.netcracker.model.record.RecordConstant;

@ObjectType(LikeConstant.LDL_TYPE)
public class RecordLike extends AbstractLike {
    @Reference(value = RecordConstant.REC_LDLREF, isParentChild = 0)
    private AbstractRecord likedRecord;

    public RecordLike() {
    }

    public RecordLike(String name) {
        super(name);
    }

    public RecordLike(String name, String description) {
        super(name, description);
    }

    public AbstractRecord getLikedRecord() {
        return likedRecord;
    }

    public void setLikedRecord(AbstractRecord likedRecord) {
        this.likedRecord = likedRecord;
    }

    @Override
    public String toString() {
        return "RecordLike{" +
                "likedRecord=" + likedRecord +
                '}';
    }
}
