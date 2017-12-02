package com.netcracker.service.profile;

import com.netcracker.model.service.Service;
import com.netcracker.model.user.Profile;
import com.netcracker.model.user.User;

import java.math.BigInteger;
import java.util.List;

@org.springframework.stereotype.Service
public interface ProfileService {
    List<Profile> viewProfile(BigInteger profileID);

    void deleteProfile(User user);

    void privacySettings(Profile profile);

    void editProfile(Profile profile);

    void bindServices(Service service);
}
