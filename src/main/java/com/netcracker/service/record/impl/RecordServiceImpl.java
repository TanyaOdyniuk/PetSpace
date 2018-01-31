package com.netcracker.service.record.impl;

import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.model.comment.AbstractComment;
import com.netcracker.model.comment.WallRecordComment;
import com.netcracker.model.group.Group;
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

import static com.netcracker.dao.manager.query.Query.IGNORING_DELETED_ELEMENTS_IN_REF;

@Service
public class RecordServiceImpl implements RecordService {
    @Autowired
    private EntityManagerService entityManagerService;
    @Autowired
    private StatusService statusService;

    @Override
    public List<WallRecord> getWallRecords(BigInteger profileID) {
        String sqlQuery = "SELECT OBJECT_ID FROM OBJREFERENCE " +
                "WHERE REFERENCE = " + profileID + " AND ATTRTYPE_ID = " + RecordConstant.REC_WALLOWNER +
               " and " + IGNORING_DELETED_ELEMENTS_IN_REF;
        return entityManagerService.getObjectsBySQL(sqlQuery, WallRecord.class, new QueryDescriptor());
    }

    @Override
    public List<GroupRecord> getGroupRecords(BigInteger groupID) {
        String sqlQuery = "SELECT OBJECT_ID FROM OBJREFERENCE " +
                "WHERE REFERENCE = " + groupID + " AND ATTRTYPE_ID = " + GroupConstant.GR_RECORDS +
                " and " + IGNORING_DELETED_ELEMENTS_IN_REF;
        return entityManagerService.getObjectsBySQL(sqlQuery, GroupRecord.class, new QueryDescriptor());
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
        List<Profile> currentWallRecordOwner = entityManagerService.getObjectsBySQL(sqlQuery, Profile.class, new QueryDescriptor());
        return currentWallRecordOwner.get(0);
    }

    @Override
    public Group getGroupRecordOwner(BigInteger groupRecordID) {
        String sqlQuery = "SELECT REFERENCE FROM OBJREFERENCE " +
                "WHERE OBJECT_ID = " + groupRecordID + " AND ATTRTYPE_ID = " + GroupConstant.GR_RECORDS;
        List<Group> currentGroupRecordOwner = entityManagerService.getObjectsBySQL(sqlQuery, Group.class, new QueryDescriptor());
        return currentGroupRecordOwner.get(0);
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
    public void deleteRecord(AbstractRecord record) {
/*        List<AbstractComment> list = commentService.getWallRecordComments(wallRecord.getObjectId());
        for (AbstractComment ac : list){
            entityManagerService.delete(ac.getObjectId(), -1);
        }*/
        entityManagerService.delete(record.getObjectId(), -1);
    }
}