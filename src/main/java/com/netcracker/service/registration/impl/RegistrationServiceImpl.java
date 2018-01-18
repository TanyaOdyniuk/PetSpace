package com.netcracker.service.registration.impl;

import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.error.exceptions.UserNotValidException;
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
    private String registrationBonusProp;
    @Value("${currency.registration.invitedby}")
    private String invitedByBonusProp;
    @Autowired
    private EntityManagerService entityManagerService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private StatusService statusService;
    @Override
    public User registerUser(User user) {
        increaseBalanceAtStart(user);
        Profile toDBProfile = user.getProfile();
        toDBProfile.setProfileStatus(statusService.getActiveStatus());
        toDBProfile.setName("Profile " + toDBProfile.getProfileSurname());
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
        user.setName("User " + fromDBProfile.getProfileSurname());
        User result = entityManagerService.create(user);
        result.setUserAuthorities(null);
        return result;
    }

    @Override
    public void invitedByUser(String userLogin) {
        BigDecimal invitedByBonus = new BigDecimal(invitedByBonusProp);
        User invitingUser = userDetailsService.loadUserByUsername(userLogin);
        if(invitingUser != null){
            Profile invitingProfile = profileService.viewProfile(invitingUser.getProfile().getObjectId());
            invitingProfile.setProfileCurrencyBalance(invitingProfile.getProfileCurrencyBalance().add(invitedByBonus));
            entityManagerService.update(invitingProfile);
        } else{
            throw new UserNotValidException("User with email: " + userLogin + " is not exist");
        }

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
