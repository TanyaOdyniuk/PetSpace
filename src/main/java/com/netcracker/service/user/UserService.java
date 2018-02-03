package com.netcracker.service.user;

import com.netcracker.model.user.Profile;
import com.netcracker.model.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User getCurrentUser(String login);

    List<Profile> getAllUsers(int page);

    int getAllUsersPageCount();

    int getFoundUsersPageCount(List<Profile> foundUsers);
}
