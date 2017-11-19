package com.netcracker.model.record;

import com.netcracker.model.BaseEntity;

import java.util.Date;

public abstract class AbstractRecord extends BaseEntity {

    private String recordText;
    private Date recordDate;

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

    @Override
    public String toString() {
        return "AbstractRecord{" +
                "recordText='" + recordText + '\'' +
                ", recordDate=" + recordDate +
                '}';
    }
}
