package com.netcracker.service.user;

import com.netcracker.model.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<User> getUsers();
    User getUserByLogin(String login);
}
