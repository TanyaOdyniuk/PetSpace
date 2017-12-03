package com.netcracker.service.registration;

import com.netcracker.model.user.Profile;
import com.netcracker.model.user.User;
import org.springframework.stereotype.Service;

@Service
public interface RegistrationService {
    Profile registrateUser(User user);

    void invitedByUser(String userLogin);

    void increaseBalanceAtStart(User user);

    void increaseBalanceMonthly();
}
