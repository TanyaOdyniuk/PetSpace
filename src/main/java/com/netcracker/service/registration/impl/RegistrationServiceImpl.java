package com.netcracker.service.registration.impl;

import com.netcracker.dao.managerapi.ManagerAPI;
import com.netcracker.model.user.Profile;
import com.netcracker.model.user.User;
import com.netcracker.model.user.UserAuthority;
import com.netcracker.model.user.UsersProfileConstant;
import com.netcracker.service.registration.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    @Value("${currency.registration.newuser}")
    String registrationBonusProp;
    @Value("${currency.registration.invitedby}")
    String invitedByBonusProp;

    @Autowired
    ManagerAPI managerAPI;

    @Override
    public User registerUser(User user) {
        increaseBalanceAtStart(user);
        Profile profile = managerAPI.create(user.getProfile());
        user.setProfile(profile);
        List<UserAuthority> authority = managerAPI.getObjectsBySQL(
                "SELECT obj.object_id " +
                        "FROM Objects obj, ATTRIBUTES atr " +
                        "where obj.OBJECT_ID = atr.OBJECT_ID " +
                        "and obj.object_type_id = " + UsersProfileConstant.USER_TYPE +
                        " and atr.attrtype_id = " + UsersProfileConstant.USERTYPE_NAME +
                        " AND atr.value = 'ROLE_USER'", UserAuthority.class);
        user.setUserAuthorities(authority);
        return managerAPI.create(user);
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
