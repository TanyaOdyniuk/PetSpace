package com.netcracker.ui;

import com.netcracker.error.ErrorMessage;
import com.netcracker.error.handler.ClientExceptionHandler;
import com.netcracker.model.StubUser;
import com.netcracker.ui.bulletinboard.BulletinBoardListContent;
import com.netcracker.ui.gallery.GalleryListContent;
import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.List;


@SpringUI
@Theme("valo")
public class StubVaadinUI extends UI implements Button.ClickListener {

    //"STATIC" PARTS OF ui
    private final HorizontalLayout topPanel;

    private final VerticalLayout leftPanel;

    private StubUserEditor stubUserEditor;

    private Grid<StubUser> grid;

    private Button addNewBtn;


    private final VerticalLayout mainLayout;

    private final HorizontalLayout primaryAreaLayout;

    private final VerticalLayout addUsersLayout;

    @Autowired
    public StubVaadinUI(StubUserEditor stubUserEditor) {
        this.stubUserEditor = stubUserEditor;
        grid = new Grid<>(StubUser.class);
        addNewBtn = new Button("New user", VaadinIcons.PLUS);
        topPanel = new StubTopBar(this);
        leftPanel = new StubLeftBar(this);
        mainLayout = new VerticalLayout();
        primaryAreaLayout = new HorizontalLayout();
        addUsersLayout = new VerticalLayout();
    }

    @Override
    protected void init(VaadinRequest request) {
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
        grid.setHeight(200, Unit.PIXELS);
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
                ClientExceptionHandler.handle(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR), ErrorMessage.ERROR_500);
            }
        });
    }

    private void listCustomers() {
        List<StubUser> users = Arrays.asList(StubConstants.REST_TEMPLATE.getForObject(StubConstants.RESOURCE_URL, StubUser[].class));
        grid.setItems(users);
    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        String clikedButtonCaption = clickEvent.getButton().getCaption();
        if("My adverts".equals(clikedButtonCaption) || "Bulletin board".equals(clikedButtonCaption)){
            primaryAreaLayout.removeComponent(primaryAreaLayout.getComponent(1));
            primaryAreaLayout.addComponentsAndExpand(new BulletinBoardListContent());
            primaryAreaLayout.setExpandRatio(primaryAreaLayout.getComponent(0), 2.0f);
            primaryAreaLayout.setExpandRatio(primaryAreaLayout.getComponent(1), 9.0f);
        } else if("My albums".equals(clikedButtonCaption)){
//            primaryAreaLayout.removeComponent(primaryAreaLayout.getComponent(1));
            primaryAreaLayout.addComponentsAndExpand(new GalleryListContent());
            primaryAreaLayout.setExpandRatio(primaryAreaLayout.getComponent(2), 2.0f);
//            primaryAreaLayout.setExpandRatio(primaryAreaLayout.getComponent(1), 9.0f);
        }
    }
}