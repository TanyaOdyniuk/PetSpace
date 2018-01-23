package com.netcracker.service.record;

import com.netcracker.model.comment.WallRecordComment;
import com.netcracker.model.record.WallRecord;
import com.netcracker.model.user.Profile;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public interface RecordService {

    //Get list of all records on profile`s wall by his ID
    List<WallRecord> getProfileWallRecords(BigInteger profileID);

    //Get author`s profile from certain wall record
    Profile getWallRecordAuthor(BigInteger wallRecordID);

    //Get owner`s profile from certain wall record
    Profile getWallRecordOwner(BigInteger wallRecordID);

    //Create new wall record (with already filled fields)
    WallRecord createWallRecord(WallRecord wallRecord);

    //Update existing wall record (with already filled fields)
    void updateWallRecord(WallRecord wallRecord);
}
