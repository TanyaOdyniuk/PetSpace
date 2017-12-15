package com.netcracker.service.profile;

import com.netcracker.model.record.StubWallRecord;
import com.netcracker.model.user.Profile;
import com.netcracker.model.user.User;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public interface ProfileService {

    //Получить профиль по его id
    Profile viewProfile(BigInteger profileID);

    void deleteProfile(User user);

    void privacySettings(Profile profile);

    void editProfile(Profile profile);

    void bindServices(Service service);
}
