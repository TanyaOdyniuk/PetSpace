package com.netcracker.model.record;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.like.RecordLikeDislike;

import java.util.Date;
import java.util.List;

@ObjectType(value = 402)
public abstract class AbstractRecord extends BaseEntity {

    @Attribute(value = 407)
    private String recordText;
    @Attribute(value = 408)
    private Date recordDate;
    //TODO SERVICE TO GET LIKES
    @Attribute(value = 409)
    private List<RecordLikeDislike> recordLikes;
    //TODO SERVICE TO GET DISLIKES
    @Attribute(value = 409)
    private List<RecordLikeDislike> recordDislikes;

    public AbstractRecord() {
    }

    public AbstractRecord(String name) {
        super(name);
    }

    public AbstractRecord(String name, String description) {
        super(name, description);
    }

    public String getRecordText() {
        return recordText;
    }

    public void setRecordText(String recordText) {
        this.recordText = recordText;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public List<RecordLikeDislike> getRecordLikes() {
        return recordLikes;
    }

    public void setRecordLikes(List<RecordLikeDislike> recordLikes) {
        this.recordLikes = recordLikes;
    }

    public List<RecordLikeDislike> getRecordDislikes() {
        return recordDislikes;
    }

    public void setRecordDislikes(List<RecordLikeDislike> recordDislikes) {
        this.recordDislikes = recordDislikes;
    }

    @Override
    public String toString() {
        return "AbstractRecord{" +
                "recordText='" + recordText + '\'' +
                ", recordDate=" + recordDate +
                ", recordLikes=" + recordLikes +
                ", recordDislikes=" + recordDislikes +
                '}';
    }
}
