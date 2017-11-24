package com.netcracker.service.profile;

import com.netcracker.model.service.Service;
import com.netcracker.model.user.Profile;
import com.netcracker.model.user.User;

public interface OwnerProfileService {
    Profile mainPageLoad(User login);

    void deleteProfile(User user);

    void privacySettings(Profile profile);

    void editProfile(Profile profile);

    void bindServices(Service service);
}
