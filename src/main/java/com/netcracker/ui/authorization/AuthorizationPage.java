package com.netcracker.ui.authorization;

import com.netcracker.ui.AbstractClickListener;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;

@Theme("valo")
@SpringUI(path = "authorization")
@Title("PetSpace AUTH")
public class AuthorizationPage extends UI{

    private Window regWindow;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        regWindow = new Window();
        regWindow.setContent(getStartWindow());
        regWindow.center();
        regWindow.setClosable(false);
        regWindow.setResizable(false);
        regWindow.setResponsive(false);
        regWindow.setDraggable(false);
        regWindow.setWindowMode(WindowMode.NORMAL);
        UI.getCurrent().addWindow(regWindow);
    }

    private VerticalLayout genLayoutContent() {
        VerticalLayout layoutContent = new VerticalLayout();
        layoutContent.setMargin(true);
        layoutContent.setSpacing(true);
        layoutContent.setSizeFull();
        return layoutContent;
    }

    private VerticalLayout getStartWindow() {
        VerticalLayout form = new VerticalLayout();
        form.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        form.setMargin(true);
        form.setSpacing(true);
        form.setWidth(520, Unit.PIXELS);

        form.addComponent(genHeader());
        form.addComponent(genButtons());
        form.addComponent(genBottom());

        return form;
    }

    private HorizontalLayout genHeader() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(false);
        horizontalLayout.setMargin(false);
        horizontalLayout.setWidth(100, Unit.PERCENTAGE);

        Label welcome = new Label("<h4>Welcome to PetSpace!</h4>", ContentMode.HTML);
        horizontalLayout.addComponent(welcome);
        horizontalLayout.setComponentAlignment(welcome, Alignment.MIDDLE_CENTER);
        horizontalLayout.setExpandRatio(welcome, 0.7f);

        return horizontalLayout;
    }

    private HorizontalLayout genButtons() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(false);
        horizontalLayout.setMargin(false);
        horizontalLayout.setWidth(100, Unit.PERCENTAGE);

        Button login = new Button("Log in");
        horizontalLayout.addComponent(login);
        horizontalLayout.setComponentAlignment(login, Alignment.BOTTOM_CENTER);
        login.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                setErrorMessage("");
                getPage().setLocation("/login");
            }
        });

        Button signup = new Button("Sign up");
        horizontalLayout.addComponent(signup);
        signup.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                setErrorMessage("");
                getPage().setLocation("/registration");
            }
        });
        horizontalLayout.setComponentAlignment(signup, Alignment.BOTTOM_CENTER);

        return horizontalLayout;
    }


    private HorizontalLayout genBottom() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(false);
        horizontalLayout.setMargin(false);
        horizontalLayout.setWidth(100, Unit.PERCENTAGE);

        Label label = new Label();
        horizontalLayout.addComponent(label);
        horizontalLayout.setExpandRatio(label, 0.55f);

        return horizontalLayout;
    }
}