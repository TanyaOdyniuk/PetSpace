package com.netcracker.ui.login;

import com.netcracker.error.ErrorMessage;
import com.netcracker.model.user.UserAuthority;
import com.netcracker.service.autorization.AuthorizationService;
import com.netcracker.ui.AbstractClickListener;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

@Theme("valo")
@SpringUI(path = "loginform")
@Title("PetSpace LOGIN")
public class LoginPage extends UI {

    @Autowired
    AuthorizationService authorizationService;

    private static final String VIEW_NAME = "Login Form";
    private Window loginWindow;
    private TextField emailField;
    private PasswordField passwordField;

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
                try {
                    Collection<GrantedAuthority> authorities = new ArrayList<>();
                    UserAuthority role = new UserAuthority();
                    role.setAuthority("ROLE_USER");
                    authorities.add(role);
                    ArrayList<String> requestParams = new ArrayList<>();
                    requestParams.add(emailField.getValue());
                    requestParams.add(passwordField.getValue());
                    for (GrantedAuthority authority : authorities) {
                        requestParams.add(authority.getAuthority());
                    }
                    /*HttpEntity<ArrayList<String>> request = new HttpEntity<>(requestParams);
                    //HttpHeaders headers = new HttpHeaders();
                   // headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                    User user = CustomRestTemplate.getInstance()
                            .customExchange("/loginform", HttpMethod.POST, request, User.class).getBody();
                    if (user == null) {
                        Notification.show("Wrong email or password!");
                    } else {
                        LoginPage.getCurrent().getPage().setLocation("/testpage");
                    }*/

                    authorizationService.authenticate(emailField.getValue(), passwordField.getValue(), authorities);
                } catch (BadCredentialsException | InternalAuthenticationServiceException e) {
                    Notification.show("Wrong email or password!");
                    emailField.clear();
                    passwordField.clear();
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
                genForgetPasswordLink(),
                buttons);
        contentLayout.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);
        return contentLayout;
    }

    private Window genPasswordRecoveryWindow() {
        Window passWindow = new Window("Recover your password");
        VerticalLayout windowContent = new VerticalLayout();
        TextField email = new TextField("Your email:");
        Button okButton = new Button("Ok");

        windowContent.addComponent(email);
        windowContent.addComponent(okButton);

        okButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                if (email.getValue().equals("")) {
                    Notification.show("Enter your email PLZ");
                } else {
                    authorizationService.passwordRecovery(emailField.getValue());
                    passWindow.close();
                }
            }
        });
        passWindow.setContent(windowContent);
        passWindow.center();
        passWindow.setClosable(false);
        passWindow.setResizable(false);
        passWindow.setResponsive(false);
        passWindow.setDraggable(false);
        passWindow.setWindowMode(WindowMode.NORMAL);
        return passWindow;
    }

    private VerticalLayout genForgetPasswordLink() {
        VerticalLayout layoutContent = new VerticalLayout();
        layoutContent.setMargin(false);
        layoutContent.setSpacing(false);
        layoutContent.setSizeFull();
        Button forgetPass = new Button("I forgot my password");
        forgetPass.setStyleName(ValoTheme.BUTTON_LINK);
        forgetPass.setIcon(VaadinIcons.COGS);
        layoutContent.addComponent(forgetPass);
        layoutContent.setComponentAlignment(forgetPass, Alignment.BOTTOM_CENTER);
        forgetPass.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                addWindow(genPasswordRecoveryWindow());
            }
        });
        return layoutContent;
    }
}
