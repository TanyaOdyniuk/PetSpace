package com.netcracker.service.profile.impl;

import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.model.user.Profile;
import com.netcracker.model.user.User;
import com.netcracker.service.profile.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class ProfileServiceImpl implements ProfileService {
    @Autowired
    EntityManagerService entityManagerService;

    @Override
    public Profile viewProfile(BigInteger profileID) {
        return entityManagerService.getById(profileID, Profile.class);
    }

    @Override
    public void deleteProfile(User user) {

    }

    @Override
    public void privacySettings(Profile profile) {

    }

    @Override
    public void editProfile(Profile profile) {

    }

    @Override
    public void bindServices(Service service) {

    }
}
