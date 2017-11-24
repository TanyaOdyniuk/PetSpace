package com.netcracker.service.autorization;

import com.netcracker.model.user.Profile;

public interface AutorizationService {

//    После нажатия на кнопку “Sign In” система должна сверить пароль, соответствующий в базе данных
//    электронному адресу, введенному пользователем в поле “Email” с паролем, введенным пользователем в поле “Password”
    Profile checkCredentials(String enteredPassword, String email);

//    Система должна предоставлять возможность восстановления пароля через форму восстановления
//    пароля с полем для заполнения “Email” и кнопкой “Reset password”
    String passwordRecovery(String email);

}

