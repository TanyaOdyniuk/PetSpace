package com.netcracker.service.record.impl;

import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.model.comment.AbstractComment;
import com.netcracker.model.comment.WallRecordComment;
import com.netcracker.model.group.GroupConstant;
import com.netcracker.model.record.AbstractRecord;
import com.netcracker.model.record.GroupRecord;
import com.netcracker.model.record.RecordConstant;
import com.netcracker.model.record.WallRecord;
import com.netcracker.model.user.Profile;
import com.netcracker.service.comment.CommentService;
import com.netcracker.service.record.RecordService;
import com.netcracker.service.status.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class RecordServiceImpl implements RecordService {
    @Autowired
    EntityManagerService entityManagerService;
    @Autowired
    private StatusService statusService;
    @Autowired
    CommentService commentService;

    @Override
    public List<WallRecord> getProfileWallRecords(BigInteger profileID) {
        String sqlQuery = "SELECT OBJECT_ID FROM OBJREFERENCE " +
                "WHERE REFERENCE = " + profileID + " AND ATTRTYPE_ID = " + RecordConstant.REC_WALLOWNER;
        return entityManagerService.getObjectsBySQL(sqlQuery, WallRecord.class, new QueryDescriptor());
    }

    @Override
    public Profile getRecordAuthor(BigInteger recordID) {
        String sqlQuery = "SELECT REFERENCE FROM OBJREFERENCE " +
                "WHERE OBJECT_ID = " + recordID + " AND ATTRTYPE_ID IN (" + RecordConstant.REC_AUTOR + ", " + GroupConstant.GR_AUTOR + ")";
        List<Profile> currentWallRecordAuthor = entityManagerService.getObjectsBySQL(sqlQuery, Profile.class, new QueryDescriptor());
        return currentWallRecordAuthor.get(0);
    }

    @Override
    public Profile getWallRecordOwner(BigInteger wallRecordID) {
        String sqlQuery = "SELECT REFERENCE FROM OBJREFERENCE " +
                "WHERE OBJECT_ID = " + wallRecordID + " AND ATTRTYPE_ID = " + RecordConstant.REC_WALLOWNER;
        List<Profile> currentWallRecordAuthor = entityManagerService.getObjectsBySQL(sqlQuery, Profile.class, new QueryDescriptor());
        return currentWallRecordAuthor.get(0);
    }

    @Override
    public WallRecord createWallRecord(WallRecord record) {
        record.setRecordState(statusService.getActiveStatus());
        return entityManagerService.create(record);
    }

    @Override
    public GroupRecord createGroupRecord(GroupRecord record) {
        record.setRecordState(statusService.getActiveStatus());
        return entityManagerService.create(record);
    }

    @Override
    public void updateWallRecord(WallRecord record) {
        entityManagerService.update(record);
    }

    @Override
    public void updateGroupRecord(GroupRecord record) {
        entityManagerService.update(record);
    }

    @Override
    public void deleteWallRecord(AbstractRecord wallRecord) {
/*        List<AbstractComment> list = commentService.getWallRecordComments(wallRecord.getObjectId());
        for (AbstractComment ac : list){
            entityManagerService.delete(ac.getObjectId(), -1);
        }*/
        entityManagerService.delete(wallRecord.getObjectId(), -1);
    }
}