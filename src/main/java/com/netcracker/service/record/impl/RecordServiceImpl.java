package com.netcracker.service.record.impl;

import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.model.record.RecordConstant;
import com.netcracker.model.record.StubWallRecord;
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
    public List<StubWallRecord> getProfileWallRecords(BigInteger profileID) {
        String sqlQuery = "SELECT REFERENCE FROM OBJREFERENCE " +
                "WHERE OBJECT_ID = " + profileID + " AND ATTRTYPE_ID = " + UsersProfileConstant.PROFILE_WALLREC;
        return entityManagerService.getObjectsBySQL(sqlQuery, StubWallRecord.class, new QueryDescriptor());
    }

    @Override
    public Profile getWallRecordAuthor(BigInteger wallRecordID) {
        String sqlQuery = "SELECT REFERENCE FROM OBJREFERENCE " +
                "WHERE OBJECT_ID = " + wallRecordID + " AND ATTRTYPE_ID = " + RecordConstant.REC_AUTOR;
        List<Profile> currentWallRecordAuthor = entityManagerService.getObjectsBySQL(sqlQuery, Profile.class, new QueryDescriptor());
        return currentWallRecordAuthor.get(0);
    }

    @Override
    public StubWallRecord createPetProfile(StubWallRecord wallRecord) {
        StubWallRecord newWallRecord = entityManagerService.create(wallRecord);
        Profile author = entityManagerService.getById(BigInteger.valueOf(48), Profile.class);
        newWallRecord.setRecordAuthor(author);
        Profile receiver = entityManagerService.getById(BigInteger.valueOf(1), Profile.class);
        List<StubWallRecord> wallRecordsList = Arrays.asList(CustomRestTemplate.getInstance().
                customGetForObject("/wallrecords/" + receiver.getObjectId(), StubWallRecord[].class));
        wallRecordsList.add(newWallRecord);
        //receiver.setProfileWallRecords(wallRecordsList);
        entityManagerService.update(receiver);
        return newWallRecord;
    }
}
