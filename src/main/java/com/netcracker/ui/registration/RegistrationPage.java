package com.netcracker.ui.registration;

import com.netcracker.error.ErrorMessage;
import com.netcracker.model.user.Profile;
import com.netcracker.model.user.User;
import com.netcracker.model.user.UserAuthority;
import com.netcracker.service.authorization.AuthorizationService;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.netcracker.ui.validation.UiValidationConstants.*;

@Theme("valo")
@SpringUI(path = "registration")
@Title("PetSpace REGISTRATION")
public class RegistrationPage extends UI {

    @Autowired
    AuthorizationService authorizationService;

    private static final String VIEW_NAME = "Registration Form";
    private Window regWindow;
    private Binder<User> userBinder;
    private Binder<Profile> profileBinder;
    private TextField emailField;
    private TextField friendField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private TextField userNameField;
    private TextField userSurnameField;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        userBinder = new Binder<>(User.class);
        profileBinder = new Binder<>(Profile.class);
        regWindow = new Window();
        Panel panel = new Panel(VIEW_NAME);
        panel.setContent(getContentLayout());
        panel.setSizeUndefined();
        regWindow.setContent(panel);
        regWindow.center();
        regWindow.setClosable(false);
        regWindow.setResizable(false);
        regWindow.setResponsive(false);
        regWindow.setDraggable(false);
        regWindow.setWindowMode(WindowMode.NORMAL);
        UI.getCurrent().addWindow(regWindow);
    }

    private HorizontalLayout getEmailLayout() {
        HorizontalLayout emailLayout = new HorizontalLayout();
        emailField = new TextField("Email Address");
        emailField.setPlaceholder("example@ex.com");
        emailField.setRequiredIndicatorVisible(true);
        userBinder.forField(emailField)
                .asRequired(CHECK_FULLNESS)
                .withValidator(new EmailValidator(CHECK_EMAIL))
                .bind(User::getLogin, User::setLogin);
        friendField = new TextField("Invited by");

        emailLayout.addComponent(emailField);
        emailLayout.addComponent(friendField);
        return emailLayout;
    }

    private HorizontalLayout getPasswordLayout() {
        HorizontalLayout passwordLayout = new HorizontalLayout();
        passwordField = new PasswordField("Password");
        passwordField.setRequiredIndicatorVisible(true);
        userBinder.forField(passwordField)
                .asRequired(CHECK_FULLNESS)
                .withValidator(new StringLengthValidator(CHECK_PASS_LEN, MIN_PASS_CHAR_COUNT, MAX_PASS_CHAR_COUNT))
                .bind(User::getPassword, User::setPassword);

        confirmPasswordField = new PasswordField("Confirm Password");
        confirmPasswordField.setRequiredIndicatorVisible(true);
        userBinder.forField(confirmPasswordField)
                .asRequired(CHECK_FULLNESS)
                .withValidator(new StringLengthValidator(CHECK_PASS_LEN, MIN_PASS_CHAR_COUNT, MAX_PASS_CHAR_COUNT))
                .withValidator(passInfo -> passInfo.equals(passwordField.getValue()), CHECK_EQ_PASS)
                .bind(User::getPassword, User::setPassword);

        passwordLayout.addComponent(passwordField);
        passwordLayout.addComponent(confirmPasswordField);
        return passwordLayout;
    }

    private HorizontalLayout getUserNameLayout() {
        HorizontalLayout userNameLayout = new HorizontalLayout();
        userNameField = new TextField("Your name");
        userNameField.setPlaceholder("John");
        userNameField.setRequiredIndicatorVisible(true);
        profileBinder.forField(userNameField)
                .asRequired(CHECK_FULLNESS)
                .withValidator(
                        name -> name.length() >= MIN_CHAR_COUNT,
                        CHECK_LENGTH)
                .bind(Profile::getProfileName, Profile::setProfileName);
        userSurnameField = new TextField("Your surname");
        userSurnameField.setPlaceholder("Doe");
        userSurnameField.setRequiredIndicatorVisible(true);
        profileBinder.forField(userSurnameField)
                .asRequired(CHECK_FULLNESS)
                .withValidator(
                        surname -> surname.length() >= MIN_CHAR_COUNT,
                        CHECK_LENGTH)
                .bind(Profile::getProfileSurname, Profile::setProfileSurname);
        userNameLayout.addComponent(userNameField);
        userNameLayout.addComponent(userSurnameField);

        return userNameLayout;
    }

    private HorizontalLayout getButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        Button submitButton = new Button("Submit");
        Button cancelButton = new Button("Cancel");
        submitButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                if (userBinder.validate().isOk() && profileBinder.validate().isOk()) {
                    setErrorMessage("User with your email is already exist\n\nor incorrect email of person, who invited you");

                    Profile profile = new Profile();
                    profile.setProfileName(userNameField.getValue());
                    profile.setProfileSurname(userSurnameField.getValue());

                    User newUser = new User();
                    newUser.setLogin(emailField.getValue());
                    newUser.setPassword(passwordField.getValue());
                    newUser.setProfile(profile);
                    String invitedBy = friendField.getValue();
                    if (!invitedBy.isEmpty()) {
                        HttpEntity<String> increaseBalanceRequest = new HttpEntity<>(invitedBy);
                        CustomRestTemplate.getInstance().customExchange("/user/increasebalance", HttpMethod.PUT, increaseBalanceRequest, String.class);
                    }
                    HttpEntity<User> createRequest = new HttpEntity<>(newUser);
                    HttpHeaders headers = new HttpHeaders();
                    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                    CustomRestTemplate.getInstance()
                            .customExchange("/user", HttpMethod.POST, createRequest, User.class);
                    Collection<GrantedAuthority> authorities = new ArrayList<>();
                    UserAuthority role = new UserAuthority();
                    role.setAuthority("ROLE_USER");
                    authorities.add(role);
                    authorizationService.authenticate(newUser.getLogin(), newUser.getPassword(), authorities);
                }

            }
        });

        cancelButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                setErrorMessage(ErrorMessage.VALIDATION_NAME);
                getPage().setLocation("/authorization");
            }
        });
        buttonLayout.addComponents(submitButton, cancelButton);
        return buttonLayout;
    }

    private VerticalLayout getContentLayout() {
        VerticalLayout contentLayout = new VerticalLayout();
        HorizontalLayout buttons = getButtonLayout();
        contentLayout.addComponents(getEmailLayout(),
                getPasswordLayout(),
                getUserNameLayout(),
                buttons);
        contentLayout.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);

        return contentLayout;
    }
}
