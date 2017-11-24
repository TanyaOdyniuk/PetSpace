package com.netcracker.UI;


import com.netcracker.model.StubUser;
import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Odyniuk on 06/11/2017.
 */

@SpringUI
@Theme("valo")
public class StubVaadinUI extends UI {

    private final StubUserEditor stubUserEditor;

    private final Grid<StubUser> grid;

    private final Button addNewBtn;

    private final HorizontalLayout topPanel;

    private final VerticalLayout leftPanel;

    @Autowired
    public StubVaadinUI(StubUserEditor stubUserEditor) {
        this.stubUserEditor = stubUserEditor;
        this.grid = new Grid<>(StubUser.class);
        this.addNewBtn = new Button("New user", VaadinIcons.PLUS);
        this.topPanel = new StubTopBar();
        this.leftPanel = new StubLeftBar();
    }

    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout mainLayout = new VerticalLayout();
        HorizontalLayout primaryAreaLayout = new HorizontalLayout();
        VerticalLayout addUsersLayout = new VerticalLayout();

        addUsersLayout.addComponentsAndExpand(addNewBtn, grid, stubUserEditor);
        addUsersLayout.setExpandRatio(addUsersLayout.getComponent(0), 1.0f);
        addUsersLayout.setExpandRatio(addUsersLayout.getComponent(1), 15.0f);
        addUsersLayout.setExpandRatio(addUsersLayout.getComponent(2), 15.0f);

        primaryAreaLayout.addComponentsAndExpand(leftPanel, addUsersLayout);
        primaryAreaLayout.setExpandRatio(primaryAreaLayout.getComponent(0), 2.0f);
        primaryAreaLayout.setExpandRatio(primaryAreaLayout.getComponent(1), 9.0f);

        mainLayout.addComponentsAndExpand(topPanel, primaryAreaLayout);
        mainLayout.setExpandRatio(mainLayout.getComponent(0), 1.0f);
        mainLayout.setExpandRatio(mainLayout.getComponent(1), 9.0f);
        setContent(mainLayout);

        addNewBtn.setHeight(30, Unit.PIXELS);
        grid.setHeight(300, Unit.PIXELS);
        grid.setColumns("id", "firstName", "lastName");
        stubUserEditor.setHeight(250, Unit.PIXELS);
        addNewBtn.addClickListener(clickEvent -> stubUserEditor.editUser(new StubUser(++StubUser.objectCount, "", "")));

        grid.asSingleSelect().addValueChangeListener(e -> stubUserEditor.editUser(e.getValue()));

        stubUserEditor.setChangeHandler(() -> {
            stubUserEditor.setVisible(false);
            listCustomers();
        });

        listCustomers();


        UI.getCurrent().setErrorHandler(new DefaultErrorHandler() {
            @Override
            public void error(com.vaadin.server.ErrorEvent event) {
                Notification notification = new Notification("Error", "Error in UI", Notification.Type.ERROR_MESSAGE);
                notification.show(Page.getCurrent());
            }
        });

    }

    private void listCustomers() {
        List<StubUser> users = Arrays.asList(StubConstants.REST_TEMPLATE.getForObject(StubConstants.RESOURCE_URL, StubUser[].class));
        grid.setItems(users);
    }

}