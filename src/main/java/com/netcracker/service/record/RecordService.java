package com.netcracker.service.record;

import com.netcracker.model.record.WallRecord;
import com.netcracker.model.user.Profile;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public interface RecordService {

    //Получить список записей на стене профиля по его ID
    List<WallRecord> getProfileWallRecords(BigInteger profileID);

    //Получить профиль автора из записи на стене
    Profile getWallRecordAuthor(BigInteger wallRecordID);

    //Создать запись на стене
    WallRecord createWallRecord(WallRecord wallRecord);
}
