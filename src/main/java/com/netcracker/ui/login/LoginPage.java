package com.netcracker.ui.login;

import com.netcracker.error.ErrorMessage;
import com.netcracker.service.autorization.AuthorizationService;
import com.netcracker.ui.AbstractClickListener;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

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
                    authorizationService.authenticate(emailField.getValue(), passwordField.getValue());
                } catch (BadCredentialsException | InternalAuthenticationServiceException e) {
                    Notification.show("Wrong email or password!");
                    emailField.clear();
                    passwordField.clear();
                }
//                setErrorMessage(ErrorMessage.VALIDATION_NAME);
//                getPage().setLocation("/testpage");
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

    private VerticalLayout genForgetPasswordLink(){
        VerticalLayout layoutContent = new VerticalLayout();
        layoutContent.setMargin(false);
        layoutContent.setSpacing(false);
        layoutContent.setSizeFull();
        Link logoLink = new Link("Forgot your password?", new ExternalResource("/passwordRecovery"));
        logoLink.setIcon(VaadinIcons.COGS);
        logoLink.setCaptionAsHtml(true);

        layoutContent.addComponent(logoLink);
        layoutContent.setComponentAlignment(logoLink, Alignment.BOTTOM_CENTER);
        return layoutContent;
    }
}
