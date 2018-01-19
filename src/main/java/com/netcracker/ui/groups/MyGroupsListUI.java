package com.netcracker.ui.groups;

import com.netcracker.asserts.ObjectAssert;
import com.netcracker.model.group.Group;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.StubVaadinUI;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.event.MouseEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.http.HttpEntity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringComponent
@UIScope
public class MyGroupsListUI extends Panel {
    private VerticalLayout mainLayout = new VerticalLayout();
    private Window newGroupWindow;
    private BigInteger profileId;
    private VerticalLayout allGroupsLayout;
    private Label amountOfMyGroups;

    public MyGroupsListUI(BigInteger profileId) {
        this.profileId = profileId;
        addStyleName("v-scrollable");
        setHeight("100%");
        List<Group> groupsList = getMyGroupsList(profileId);

        //HEADER: all and find group
        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidth("100%");
        headerLayout.setHeight("45px");
        getNewGroupWindow();
        Button headerButton = new Button("Create group", VaadinIcons.PLUS);
        headerButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                UI.getCurrent().addWindow(newGroupWindow);
            }
        });
        amountOfMyGroups = new Label("My groups " + groupsList.size());
        headerLayout.addComponents(amountOfMyGroups, headerButton);
        headerLayout.setComponentAlignment(amountOfMyGroups, Alignment.MIDDLE_LEFT);
        headerLayout.setComponentAlignment(headerButton, Alignment.MIDDLE_RIGHT);


        HorizontalLayout findGroupLayout = new HorizontalLayout();
        findGroupLayout.setWidth("100%");
        findGroupLayout.setHeight("45px");
        TextField findGroupField = PageElements.createTextField(null, "Find group by the topic...", false);
        findGroupField.setWidth("100%");
        Button findGroupButton = new Button("Find");
        findGroupButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                List<Group> foundGroups = new ArrayList<>();
                for (Group group : groupsList) {
                    if (findGroupField.getValue().equals(group.getGroupName())) {
                        foundGroups.add(group);
                    }
                }
                if (foundGroups.size() != 0) {
                    mainLayout.removeComponent(allGroupsLayout);
                    showGroups(foundGroups);
                    amountOfMyGroups.setValue("All selected groups " + foundGroups.size());
                } else if (findGroupField.getValue().isEmpty()) {
                    Notification.show("You should enter group's name!");
                } else {
                    Notification.show("No groups with this name!");
                }
            }
        });

        findGroupLayout.addComponents(findGroupField, findGroupButton);
        mainLayout.addComponents(headerLayout, findGroupLayout);
        mainLayout.setComponentAlignment(findGroupLayout, Alignment.TOP_CENTER);
        //--------------------HEADER: all and find group

        showGroups(groupsList);
        setContent(mainLayout);
    }

    private void showGroups(List<Group> groupsList) {
        HorizontalLayout everyGroupLayout;
        VerticalLayout infoGroupLayout;
        allGroupsLayout = new VerticalLayout();
        for (int i = 0; i < groupsList.size(); i++) {
            everyGroupLayout = new HorizontalLayout();
            Image groupAvatar = new Image();
            groupAvatar.setSource(new ExternalResource(groupsList.get(i).getGroupAvatar()));
            groupAvatar.setWidth("120px");
            groupAvatar.setHeight("120px");
            int finalI = i;
            groupAvatar.addClickListener((MouseEvents.ClickListener) clickEvent -> ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new GroupUI(groupsList.get(finalI).getObjectId())));
            infoGroupLayout = new VerticalLayout();
            infoGroupLayout.addComponents(new Label(groupsList.get(i).getGroupName(), ContentMode.PREFORMATTED),
                    new Label("72630 subscribers"));
            everyGroupLayout.addComponents(groupAvatar, infoGroupLayout);
            allGroupsLayout.addComponent(everyGroupLayout);
            if (finalI != (groupsList.size() - 1))
                allGroupsLayout.addComponent(PageElements.getSeparator());
        }
        mainLayout.addComponents(allGroupsLayout);
    }

    private void getNewGroupWindow() {
        newGroupWindow = new Window();
        newGroupWindow.setModal(true);
        newGroupWindow.setWidth("400px");
        newGroupWindow.setHeight("320px");
        newGroupWindow.setCaption("Creating a new group:");
        VerticalLayout windowContent = new VerticalLayout();
        HorizontalLayout addAlbumButtonsLayout = new HorizontalLayout();

        TextField groupName = PageElements.createTextField("Enter group name:", "group name", true);
        groupName.setWidth("100%");
        TextField groupAvatar = PageElements.createTextField("Enter group avatar link:", "group avatar", false);
        groupName.setWidth("100%");
        TextField description = PageElements.createTextField("Enter group description:", "group description", false);
        description.setWidth("100%");

        Button addGroupButton = new Button("Create group");
        addGroupButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                addGroupButton.setComponentError(null);
                createGroup(groupName.getValue(), groupAvatar.getValue(), description.getValue(), profileId);
                newGroupWindow.close();
                Notification.show("You have just created a new group!");
            }
        });
        Button cancelAddingNewAlbum = new Button("Cancel", click -> newGroupWindow.close());
        addAlbumButtonsLayout.addComponentsAndExpand(addGroupButton, cancelAddingNewAlbum);
        windowContent.addComponents(groupName, groupAvatar, description, addAlbumButtonsLayout);

        newGroupWindow.setContent(windowContent);
        newGroupWindow.center();
    }

    private void createGroup(String groupName, String groupAvatar, String description, BigInteger profileId) {
        ObjectAssert.isNullOrEmpty(groupAvatar);
        Group createdGroup = new Group(groupName, description, groupAvatar);
        HttpEntity<Group> group = new HttpEntity<>(createdGroup);
        CustomRestTemplate.getInstance().customPostForObject("/groupList/" + profileId + "/add", group, Group.class);
        ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new MyGroupsListUI(profileId));
    }

    private List<Group> getMyGroupsList(BigInteger profileId) {
        return Arrays.asList(CustomRestTemplate.getInstance().
                customGetForObject("/groupList/" + profileId, Group[].class));
    }
}
