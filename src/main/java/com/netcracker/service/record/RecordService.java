package com.netcracker.service.record;

import com.netcracker.model.comment.WallRecordComment;
import com.netcracker.model.record.AbstractRecord;
import com.netcracker.model.record.GroupRecord;
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
    Profile getRecordAuthor(BigInteger recordID);

    //Get owner`s profile from certain wall record
    Profile getWallRecordOwner(BigInteger wallRecordID);

    //Create new wall record (with already filled fields)
    WallRecord createWallRecord(WallRecord record);

    //Create new group record (with already filled fields)
    GroupRecord createGroupRecord(GroupRecord record);

    //Update existing wall record (with already filled fields)
    void updateWallRecord(WallRecord record);

    //Update existing group record (with already filled fields)
    void updateGroupRecord(GroupRecord record);

    //Delete wall record
    void deleteWallRecord(AbstractRecord wallRecord);
}
