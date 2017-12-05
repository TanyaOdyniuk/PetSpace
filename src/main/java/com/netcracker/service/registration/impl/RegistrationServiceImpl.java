package com.netcracker.service.registration.impl;

import com.netcracker.dao.managerapi.ManagerAPI;
import com.netcracker.model.user.Profile;
import com.netcracker.model.user.User;
import com.netcracker.service.registration.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    @Value("${currency.registration.newuser}")
    String registrationBonusProp;
    @Value("${currency.registration.invitedby}")
    String invitedByBonusProp;

    @Autowired
    ManagerAPI managerAPI;

    @Override
    public Profile registrateUser(User user) {
        increaseBalanceAtStart(user);
        Profile profile = managerAPI.create(user.getProfile());
        user.setProfile(profile);
        managerAPI.create(user);
        return profile;
    }

    @Override
    public void invitedByUser(String userLogin) {
        BigDecimal invitedByBonus = new BigDecimal(invitedByBonusProp);
        //find user
        //increase user balance
        //update user
    }

    @Override
    public void increaseBalanceAtStart(User user) {
        BigDecimal registrationBonus = new BigDecimal(registrationBonusProp);
        user.getProfile().setProfileCurrencyBalance(registrationBonus);
    }

    @Override
    public void increaseBalanceMonthly() {

    }
}
