package com.netcracker.service.registration.impl;

import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.model.user.Profile;
import com.netcracker.model.user.User;
import com.netcracker.model.user.UserAuthority;
import com.netcracker.model.user.UsersProfileConstant;
import com.netcracker.service.profile.ProfileService;
import com.netcracker.service.registration.RegistrationService;
import com.netcracker.service.status.StatusService;
import com.netcracker.service.user.impl.UserDetailsServiceImpl;
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
    EntityManagerService entityManagerService;
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    ProfileService profileService;
    @Autowired
    StatusService statusService;
    @Override
    public User registerUser(User user) {
        increaseBalanceAtStart(user);
        Profile toDBProfile = user.getProfile();
        toDBProfile.setProfileStatus(statusService.getActiveStatus());
        Profile fromDBProfile = entityManagerService.create(toDBProfile);
        user.setProfile(fromDBProfile);
        List<UserAuthority> authority = entityManagerService.getObjectsBySQL(
                "SELECT obj.object_id " +
                        "FROM Objects obj, ATTRIBUTES atr " +
                        "where obj.OBJECT_ID = atr.OBJECT_ID " +
                        "and obj.object_type_id = " + UsersProfileConstant.USERTYPE_TYPE +
                        " and atr.attrtype_id = " + UsersProfileConstant.USERTYPE_NAME +
                        " AND atr.value = 'ROLE_USER'", UserAuthority.class,
                        new QueryDescriptor());
        user.setUserAuthorities(authority);
        return entityManagerService.create(user);
    }

    @Override
    public void invitedByUser(String userLogin) {
        BigDecimal invitedByBonus = new BigDecimal(invitedByBonusProp);
        User invitingUser = userDetailsService.loadUserByUsername(userLogin);
        Profile invitingProfile = profileService.viewProfile(invitingUser.getProfile().getObjectId());
        invitingProfile.setProfileCurrencyBalance(invitingProfile.getProfileCurrencyBalance().add(invitedByBonus));
        entityManagerService.update(invitingProfile);
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
