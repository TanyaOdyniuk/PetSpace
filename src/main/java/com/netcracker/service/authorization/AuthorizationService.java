package com.netcracker.service.authorization;

import com.netcracker.model.user.Profile;
import com.netcracker.model.user.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface AuthorizationService {

//    Система должна предоставлять возможность восстановления пароля через форму восстановления
//    пароля с полем для заполнения “Email” и кнопкой “Reset password”
    User passwordRecovery(String email);

    User authenticate(String email, String password, Collection<? extends GrantedAuthority> authorities);
}

