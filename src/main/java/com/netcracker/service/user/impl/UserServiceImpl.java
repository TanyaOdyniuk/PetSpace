package com.netcracker.service.user.impl;

import com.netcracker.dao.managerapi.ManagerAPI;
import com.netcracker.model.user.User;
import com.netcracker.model.user.UsersProfileConstant;
import com.netcracker.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    ManagerAPI managerAPI;

    @Override
    public List<User> getUsers() {
        return managerAPI.getAll(BigInteger.valueOf(UsersProfileConstant.USER_TYPE), User.class);
    }

    @Override
    public User getUserByLogin(String login) {
        return null;
    }
}
