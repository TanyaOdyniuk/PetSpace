package com.netcracker.service.registration;

import com.netcracker.model.user.User;
import javafx.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface RegistrationService {
    User registerUser(User user);

    void invitedByUser(String userLogin);

    void increaseBalanceAtStart(User user);

    void increaseBalanceMonthly();
}
