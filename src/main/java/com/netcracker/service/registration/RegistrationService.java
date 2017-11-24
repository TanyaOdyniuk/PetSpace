package com.netcracker.service.registration;

import com.netcracker.model.user.Profile;
import com.netcracker.model.user.User;

public interface RegistrationService {
    Profile registrateUser(User user);

    void invitedByUser(String userLogin);

    void increaseBalanceAtStart(User user);

    void increaseBalanceMonthly();
}
