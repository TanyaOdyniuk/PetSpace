package com.netcracker.ui.groups;

import com.netcracker.asserts.ObjectAssert;
import com.netcracker.model.group.Group;
import com.netcracker.model.group.GroupType;
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
import java.util.Arrays;
import java.util.List;

@SpringComponent
@UIScope
public class MyGroupsListUI extends Panel {
    VerticalLayout mainLayout = new VerticalLayout();
    Window newGroupWindow;
    BigInteger profileId;

    public MyGroupsListUI(BigInteger profileId){
        this.profileId = profileId;
        addStyleName("v-scrollable");
        setHeight("100%");
        List<Group> groupsList = getMyGroupsList(profileId);

        //HEADER: all and find group
        Panel headerPanel = new Panel();
        headerPanel.setWidth("100%");
        headerPanel.setHeight("48px");
        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidth("100%");
        headerLayout.setHeight("45px");
        getNewGroupWindow();
        Button headerButton = new Button("Create group",VaadinIcons.PLUS);
        headerButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                UI.getCurrent().addWindow(newGroupWindow);
            }
        });
        Label amountOfMyGroups = new Label("All groups 88");
        headerLayout.addComponents(amountOfMyGroups, headerButton);
        headerLayout.setComponentAlignment(amountOfMyGroups, Alignment.MIDDLE_LEFT);
        headerLayout.setComponentAlignment(headerButton, Alignment.MIDDLE_RIGHT);
        headerPanel.setContent(headerLayout);

        HorizontalLayout findGroupLayout = new HorizontalLayout();
        findGroupLayout.setWidth("100%");
        findGroupLayout.setHeight("45px");
        TextField findGroupField = PageElements.createTextField(null, "Find group by the topic...", false);
        findGroupField.setWidth("100%");
        Button findGroupButton = new Button("Find");
        findGroupLayout.addComponents(findGroupField, findGroupButton);

        mainLayout.addComponents(headerPanel, findGroupLayout);
        mainLayout.setComponentAlignment(findGroupLayout, Alignment.TOP_CENTER);
        //--------------------HEADER: all and find group

        HorizontalLayout everyGroupLayout;
        VerticalLayout infoGroupLayout;
        for(Group group : groupsList){
            everyGroupLayout = new HorizontalLayout();
            Image groupAvatar = new Image();
            groupAvatar.setSource(new ExternalResource(group.getGroupAvatar()));
            groupAvatar.setWidth("120px");
            groupAvatar.setHeight("120px");
            groupAvatar.addClickListener((MouseEvents.ClickListener) clickEvent ->
                    ((StubVaadinUI)UI.getCurrent()).changePrimaryAreaLayout(new GroupUI(group.getObjectId())));
            infoGroupLayout = new VerticalLayout();
            infoGroupLayout.addComponents(new Label(group.getGroupName(), ContentMode.PREFORMATTED),
                    new Label("72630 describers"));
            everyGroupLayout.addComponents(groupAvatar, infoGroupLayout);
            mainLayout.addComponents(everyGroupLayout,PageElements.getSeparator());
        }
        setContent(mainLayout);
    }

    private void getNewGroupWindow(){
        newGroupWindow = new Window();
        newGroupWindow.setWidth("400px");
        newGroupWindow.setHeight("395px");
        newGroupWindow.setCaption("Creating a new group:");
        VerticalLayout windowContent = new VerticalLayout();
        HorizontalLayout addAlbumButtonsLayout = new HorizontalLayout();

        TextField groupName = PageElements.createTextField("Enter group name:", "group name", true);
        groupName.setWidth("100%");
        TextField groupAvatar = PageElements.createTextField("Enter group avatar link:", "group avatar", false);
        groupName.setWidth("100%");
        TextField description = PageElements.createTextField("Enter group description:", "group description", false);
        description.setWidth("100%");

        List<GroupType> groupTypesList = getAllGroupTypes();
        ComboBox<GroupType> groupTypeComboBox = new ComboBox("Type");
        groupTypeComboBox.setItems(groupTypesList);
        groupTypeComboBox.setItemCaptionGenerator(GroupType::getGroupType);

        groupTypeComboBox.setEmptySelectionAllowed(false);
        groupTypeComboBox.setRequiredIndicatorVisible(true);
        groupTypeComboBox.setWidth("100%");
        groupTypeComboBox.setTextInputAllowed(false);

        Button addGroupButton = new Button("Create group");
        addGroupButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                addGroupButton.setComponentError(null);
                createGroup(groupName.getValue(), groupAvatar.getValue(), description.getValue(), groupTypeComboBox.getValue(), profileId);
                newGroupWindow.close();
                Notification.show("You have just created a new group!");
            }
        });
        Button cancelAddingNewAlbum = new Button("Cancel", click -> newGroupWindow.close());
        addAlbumButtonsLayout.addComponentsAndExpand(addGroupButton, cancelAddingNewAlbum);
        windowContent.addComponents(groupName, groupAvatar, description, groupTypeComboBox, addAlbumButtonsLayout);

        newGroupWindow.setContent(windowContent);
        newGroupWindow.center();
    }

    private void createGroup(String groupName, String groupAvatar, String description, GroupType groupType, BigInteger profileId) {
        ObjectAssert.isNullOrEmpty(groupAvatar);
        Group createdGroup = new Group();
        createdGroup.setGroupName(groupName);
        createdGroup.setGroupAvatar(groupAvatar);
        createdGroup.setGroupDescription(description);
        createdGroup.setGroupType(groupType);
        HttpEntity<Group> group = new HttpEntity<>(createdGroup);
        CustomRestTemplate.getInstance().customPostForObject("/groupList/"+ profileId +"/add", group, Group.class);
    }

    private List<GroupType> getAllGroupTypes() {
        return Arrays.asList(CustomRestTemplate.getInstance().customGetForObject(
                        "groupList/allGroupTypes", GroupType[].class));
    }

    private List<Group> getMyGroupsList(BigInteger profileId) {
        return Arrays.asList(CustomRestTemplate.getInstance().
                customGetForObject("/groupList/" + profileId, Group[].class));
    }
}
