package com.netcracker.model.record;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.status.Status;

import java.sql.Timestamp;

public class AbstractRecord extends BaseEntity {

    @Attribute(RecordConstant.REC_INFO)
    private String recordText;
    @Attribute(RecordConstant.REC_DATE)
    private Timestamp recordDate;
    @Reference(value = RecordConstant.REC_STATE, isParentChild = 0)
    private Status recordState;

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

    public Timestamp getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Timestamp recordDate) {
        this.recordDate = recordDate;
    }

    public Status getRecordState() {
        return recordState;
    }

    public void setRecordState(Status recordState) {
        this.recordState = recordState;
    }

    @Override
    public String toString() {
        return "AbstractRecord{" +
                "recordText='" + recordText + '\'' +
                ", recordDate=" + recordDate +
                ", recordState=" + recordState +
                '}';
    }
}
