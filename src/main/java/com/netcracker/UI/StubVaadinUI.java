package com.netcracker.UI;

import com.netcracker.model.StubUser;
import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Odyniuk on 06/11/2017.
 */

@SpringUI
@Theme("valo")
public class StubVaadinUI extends UI {

    private final StubUserEditor stubUserEditor;

    private final Grid<StubUser> grid;

    private final Button addNewBtn;

    @Autowired
    public StubVaadinUI(StubUserEditor stubUserEditor) {
        this.stubUserEditor = stubUserEditor;
        this.grid = new Grid<>(StubUser.class);
        this.addNewBtn = new Button("New user", VaadinIcons.PLUS);
    }

    @Override
    protected void init(VaadinRequest request) {

        VerticalLayout mainLayout = new VerticalLayout();
        HorizontalLayout primaryAreaLayout = new HorizontalLayout();
        VerticalLayout addUsersLayout = new VerticalLayout();
        addUsersLayout.addComponentsAndExpand(addNewBtn, grid, stubUserEditor);
        addUsersLayout.getComponent(0).setHeight("10%");
        primaryAreaLayout.addComponentsAndExpand(initLeftPanel(), addUsersLayout);
        mainLayout.addComponentsAndExpand(initTopPanel(), primaryAreaLayout);
        setContent(mainLayout);

        grid.setHeight(300, Unit.PIXELS);
        grid.setColumns("id", "firstName", "lastName");
        addNewBtn.addClickListener(clickEvent -> stubUserEditor.editUser(new StubUser(++StubUser.objectCount, "", "")));

        grid.asSingleSelect().addValueChangeListener(e -> stubUserEditor.editUser(e.getValue()));

        stubUserEditor.setChangeHandler(() -> {
            stubUserEditor.setVisible(false);
            listCustomers();
        });

        listCustomers();
    }

    private void listCustomers() {
        List<StubUser> users = Arrays.asList(StubConstants.REST_TEMPLATE.getForObject(StubConstants.RESOURCE_URL, StubUser[].class));
        grid.setItems(users);
    }

    private HorizontalLayout initTopPanel() {
        HorizontalLayout topPanelLayout = new HorizontalLayout();
        topPanelLayout.setWidth("100%");
        topPanelLayout.setSpacing(false);
        HashMap<String, VaadinIcons> buttonsMap = new HashMap<>(5);
        buttonsMap.put("Main page", VaadinIcons.REFRESH);
        buttonsMap.put("News", VaadinIcons.DOCTOR);
        buttonsMap.put("Users", VaadinIcons.ARCHIVE);
        buttonsMap.put("Pets", VaadinIcons.HOSPITAL);
        buttonsMap.put("Bulletin board", VaadinIcons.YOUTUBE);
        for (Map.Entry<String, VaadinIcons> entry : buttonsMap.entrySet()) {
            Button button = new Button(entry.getKey(), entry.getValue());
            button.setWidth("100%");
            if (entry.getKey().equals("Main page")) {
                button.addStyleName(ValoTheme.BUTTON_DANGER);
            } else {
                button.addStyleName(ValoTheme.BUTTON_HUGE);
            }
            topPanelLayout.addComponentsAndExpand(button);
            button.addClickListener(clickEvent -> {
                Notification notification = new Notification("No " + entry.getKey() + " yet!!!",
                        Notification.Type.HUMANIZED_MESSAGE);
                notification.setPosition(Position.MIDDLE_CENTER);
                notification.show(Page.getCurrent());
            });
        }
        return topPanelLayout;
    }

    private VerticalLayout initLeftPanel() {
        VerticalLayout leftPanelLayout = new VerticalLayout();
        leftPanelLayout.setWidth("20%");
        leftPanelLayout.setSpacing(false);

        HashMap<String, VaadinIcons> buttonsMap = new HashMap<>(6);
        buttonsMap.put("Profile", VaadinIcons.ABACUS);
        buttonsMap.put("Pets", VaadinIcons.BOMB);
        buttonsMap.put("Friends", VaadinIcons.BAN);
        buttonsMap.put("Photos", VaadinIcons.PENCIL);
        buttonsMap.put("Adverts", VaadinIcons.SPOON);
        buttonsMap.put("Settings", VaadinIcons.MOON);
        for (Map.Entry<String, VaadinIcons> entry : buttonsMap.entrySet()) {
            Button button = new Button(entry.getKey(), entry.getValue());
            button.setWidth("100%");
            button.addStyleName(ValoTheme.BUTTON_QUIET);
            leftPanelLayout.addComponentsAndExpand(button);
            button.addClickListener(clickEvent -> {
                Notification notification = new Notification("No " + entry.getKey() + " yet!!!",
                        Notification.Type.HUMANIZED_MESSAGE);
                notification.setPosition(Position.MIDDLE_CENTER);
                notification.show(Page.getCurrent());
            });
        }

        return leftPanelLayout;
    }
}