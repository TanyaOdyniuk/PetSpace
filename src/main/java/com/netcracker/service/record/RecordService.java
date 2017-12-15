package com.netcracker.service.record;

import com.netcracker.model.record.StubWallRecord;
import com.netcracker.model.user.Profile;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public interface RecordService {

    //Получить список записей на стене профиля по его ID
    List<StubWallRecord> getProfileWallRecords(BigInteger profileID);

    //Получить профиль автора из записи на стене
    Profile getWallRecordAuthor(BigInteger wallRecordID);
}
