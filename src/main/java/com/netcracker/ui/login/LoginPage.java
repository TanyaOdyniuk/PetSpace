package com.netcracker.ui.login;

import com.netcracker.dao.managerapi.ManagerAPI;
import com.netcracker.error.ErrorMessage;
import com.netcracker.model.user.User;
import com.netcracker.service.user.impl.UserDetailsServiceImpl;
import com.netcracker.ui.AbstractClickListener;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Theme("valo")
@SpringUI(path = "login")
@Title("PetSpace LOGIN")
public class LoginPage extends UI {

    @Autowired
    DaoAuthenticationProvider daoAuthenticationProvider;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    private static final String VIEW_NAME = "Login Form";
    private Window loginWindow;
    private TextField emailField;
    private PasswordField passwordField;
    private ManagerAPI manager;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        loginWindow = new Window();
        Panel panel = new Panel(VIEW_NAME);
        panel.setContent(getContentLayout());
        panel.setSizeUndefined();
        loginWindow.setContent(panel);
        loginWindow.center();
        loginWindow.setClosable(false);
        loginWindow.setResizable(false);
        loginWindow.setResponsive(false);
        loginWindow.setDraggable(false);
        loginWindow.setWindowMode(WindowMode.NORMAL);
        UI.getCurrent().addWindow(loginWindow);
    }

    private HorizontalLayout getEmailLayout() {
        HorizontalLayout emailLayout = new HorizontalLayout();
        emailField = new TextField("Email Address");
        emailField.setPlaceholder("example@ex.com");
        emailField.setRequiredIndicatorVisible(true);
        emailLayout.addComponent(emailField);
        return emailLayout;
    }

    private HorizontalLayout getPasswordLayout() {
        HorizontalLayout passwordLayout = new HorizontalLayout();
        passwordField = new PasswordField("Password");
        passwordField.setRequiredIndicatorVisible(true);
        passwordLayout.addComponent(passwordField);
        return passwordLayout;
    }

    private HorizontalLayout getButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        Button submitButton = new Button("Submit");
        Button cancelButton = new Button("Cancel");
        submitButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                authenticate();
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
                buttons);
        contentLayout.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);
        return contentLayout;
    }

    private void authenticate() {
        Authentication auth = new UsernamePasswordAuthenticationToken(emailField.getValue(), passwordField.getValue());
        try {
            Authentication authenticated = daoAuthenticationProvider.authenticate(auth);
            SecurityContextHolder.getContext().setAuthentication(authenticated);
            User user = userDetailsService.findUserByUsername(emailField.getValue());
            if (user == null) {
                Notification.show("Wrong email or password!");
            } else {
                if (userDetailsService.hasRole("ROLE_USER")) {
                    getPage().setLocation("/user");
                } else if (userDetailsService.hasRole("ROLE_ADMIN")) {
                    getPage().setLocation("/admin");
                }
            }

        } catch (BadCredentialsException | InternalAuthenticationServiceException e) {
            Notification.show("Wrong email or password!");
            emailField.clear();
            passwordField.clear();
        }
    }
}
