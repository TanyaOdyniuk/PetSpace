package com.netcracker.service.profile;

import com.netcracker.model.user.Profile;
import com.netcracker.model.user.User;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public interface ProfileService {

    //Get profile by its objectID
    Profile viewProfile(BigInteger profileID);

    //Update profile by its objectID
    void updateProfile(Profile profile);

    //Delete profile by its objectID
    void deleteProfile(User user);

    //void privacySettings(Profile profile);

    //void bindServices(Service service);
}
