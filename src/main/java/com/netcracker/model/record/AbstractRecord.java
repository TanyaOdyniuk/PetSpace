package com.netcracker.model.record;

import com.netcracker.model.Model;

public class AbstractRecord extends Model {

    private String recordText;

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

    @Override
    public String toString() {
        return "AbstractRecord{" +
                "recordText='" + recordText + '\'' +
                '}';
    }
}
