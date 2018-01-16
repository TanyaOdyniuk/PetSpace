package com.netcracker.service.user;

import com.netcracker.model.user.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User getCurrentUser(String login);
}
