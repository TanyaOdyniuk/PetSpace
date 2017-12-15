package com.netcracker.model.record;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.comment.WallComment;
import com.netcracker.model.like.RecordLikeDislike;
import com.netcracker.model.user.Profile;

import java.sql.Date;
import java.util.List;

@ObjectType(RecordConstant.REC_TYPE)
public class StubWallRecord extends BaseEntity{
    @Attribute(RecordConstant.REC_INFO)
    private String recordText;
    @Attribute(RecordConstant.REC_DATE)
    private Date recordDate;
    //TODO SERVICE TO GET LIKES
    @Reference(RecordConstant.REC_LDLREF)
    private List<RecordLikeDislike> recordLikes;
    //TODO SERVICE TO GET DISLIKES
    @Reference(RecordConstant.REC_LDLREF)
    private List<RecordLikeDislike> recordDislikes;
    @Reference(RecordConstant.REC_AUTOR)
    private Profile recordAuthor;
    @Reference(RecordConstant.REC_COMREF)
    private List<WallComment> wallComments;

    public StubWallRecord() {
    }

    public StubWallRecord(String name) {
        super(name);
    }

    public StubWallRecord(String name, String description) {
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
        return "StubWallRecord{" +
                "recordText='" + recordText + '\'' +
                ", recordDate=" + recordDate +
                ", recordLikes=" + recordLikes +
                ", recordDislikes=" + recordDislikes +
                ", recordAuthor=" + recordAuthor +
                ", wallComments=" + wallComments +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", objectId=" + objectId +
                ", parentId=" + parentId +
                ", objectTypeId=" + objectTypeId +
                '}';
    }
}
