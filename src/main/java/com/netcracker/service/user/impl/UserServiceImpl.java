package com.netcracker.service.user.impl;

import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.pet.PetConstant;
import com.netcracker.model.user.Profile;
import com.netcracker.model.user.User;
import com.netcracker.model.user.UsersProfileConstant;
import com.netcracker.service.search.SearchService;
import com.netcracker.service.user.UserService;
import com.netcracker.service.util.PageCounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Value("${userlist.pageCapacity}")
    String userListPageCapacity;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private EntityManagerService entityManagerService;

    @Autowired
    private PageCounterService pageCounterService;

    public User getCurrentUser(String login) {
        return userDetailsService.loadUserByUsername(login);
    }

    private String getAllUsersQuery = "SELECT OBJECT_ID FROM OBJECTS WHERE OBJECT_TYPE_ID = '403'";

    @Override
    public List<Profile> getAllUsers(int page) {
        QueryDescriptor descriptor = new QueryDescriptor();
        descriptor.addPagingDescriptor(page, Integer.valueOf(userListPageCapacity));
        return entityManagerService.getObjectsBySQL((getAllUsersQuery), Profile.class, descriptor);
    }

    @Override
    public int getAllUsersPageCount() {
        Integer usersPageCapacity = new Integer(userListPageCapacity);
        return pageCounterService.getPageCount(usersPageCapacity, entityManagerService.getBySqlCount(getAllUsersQuery));
    }

    @Override
    public int getFoundUsersPageCount(List<Profile> foundUsers) {
        Integer usersPageCapacity = new Integer(userListPageCapacity);
        return pageCounterService.getPageCount(usersPageCapacity, foundUsers.size());
    }


}
