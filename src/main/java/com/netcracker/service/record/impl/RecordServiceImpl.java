package com.netcracker.service.record.impl;

import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.model.record.RecordConstant;
import com.netcracker.model.record.WallRecord;
import com.netcracker.model.user.Profile;
import com.netcracker.model.user.UsersProfileConstant;
import com.netcracker.service.record.RecordService;
import com.netcracker.ui.util.CustomRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class RecordServiceImpl implements RecordService {
    @Autowired
    EntityManagerService entityManagerService;

    @Override
    public List<WallRecord> getProfileWallRecords(BigInteger profileID) {
        String sqlQuery = "SELECT OBJECT_ID FROM OBJREFERENCE " +
                "WHERE REFERENCE = " + profileID + " AND ATTRTYPE_ID = " + UsersProfileConstant.PROFILE_WALLREC;
        return entityManagerService.getObjectsBySQL(sqlQuery, WallRecord.class, new QueryDescriptor());
    }

    @Override
    public Profile getWallRecordAuthor(BigInteger wallRecordID) {
        String sqlQuery = "SELECT OBJECT_ID FROM OBJREFERENCE " +
                "WHERE REFERENCE = " + wallRecordID + " AND ATTRTYPE_ID = " + RecordConstant.REC_AUTOR;
        List<Profile> currentWallRecordAuthor = entityManagerService.getObjectsBySQL(sqlQuery, Profile.class, new QueryDescriptor());
        return currentWallRecordAuthor.get(0);
    }

    @Override
    public WallRecord createWallRecord(WallRecord wallRecord) {
        BigInteger cutAuthorObjectID = wallRecord.getRecordAuthor().getObjectId();
        wallRecord.setRecordAuthor(null);
        WallRecord createdWallRecord = entityManagerService.create(wallRecord);
        Profile receiver = entityManagerService.getById(BigInteger.valueOf(1), Profile.class);
        List<WallRecord> wallRecordsList = new ArrayList<>(receiver.getProfileWallRecords());
        wallRecordsList.add(createdWallRecord);
        receiver.setProfileWallRecords(wallRecordsList);
        entityManagerService.update(receiver);
        Profile cutAuthor = new Profile();
        cutAuthor.setObjectId(cutAuthorObjectID);
        createdWallRecord.setRecordAuthor(cutAuthor);
        //entityManagerService.update(createdWallRecord);
        return createdWallRecord;
    }
}