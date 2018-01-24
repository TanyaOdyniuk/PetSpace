package com.netcracker.ui.groups;

import com.netcracker.model.group.Group;
import com.netcracker.model.user.Profile;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.StubVaadinUI;
import com.netcracker.ui.profile.ProfileView;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.event.MouseEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

@SpringComponent
@UIScope
public class GroupUI extends VerticalLayout {
    private HorizontalLayout headerLayout;
    private HorizontalLayout contentLayout;
    private String stubAvatar = "https://goo.gl/6eEoWo";
    private Window newGroupWindow;

    public GroupUI(BigInteger groupId) {
        addStyleName("v-scrollable");
        setHeight("100%");

        Group curGroup = getGroup(groupId);

        headerLayout = new HorizontalLayout();
        contentLayout = new HorizontalLayout();
        //HEADER
        Panel infoGroupPanel = new Panel();
        infoGroupPanel.setWidth("730px");
        infoGroupPanel.setHeight("215px");

        VerticalLayout infoLayout = new VerticalLayout();
        HorizontalLayout descrLayuot = new HorizontalLayout();
        Label descriptionWordLabel = new Label("Description:  ");
        Label descriptionLabel = new Label(curGroup.getGroupDescription());
        descriptionLabel.setWidth("590px");
        descrLayuot.addComponents(descriptionWordLabel, descriptionLabel);

        infoLayout.addComponents(new Label(curGroup.getGroupName(), ContentMode.PREFORMATTED), descrLayuot);
        infoGroupPanel.setContent(infoLayout);
        Image avatar = new Image();
        avatar.setWidth("240px");
        avatar.setHeight("215px");
        avatar.setSource(new ExternalResource(curGroup.getGroupAvatar()));
        headerLayout.addComponents(infoGroupPanel, avatar);
        //-----------------HEADER

        //MAIN
        Panel mainLeftPanel = new Panel();
        mainLeftPanel.setWidth("730px");
        mainLeftPanel.setHeightUndefined();
        Panel groupRecord = new Panel();
        groupRecord.setWidth("705px");
        groupRecord.setHeight("300px");
        Panel groupRecord1 = new Panel();
        groupRecord1.setWidth("705px");
        groupRecord1.setHeight("300px");
        Panel groupRecord2 = new Panel();
        groupRecord2.setWidth("705px");
        groupRecord2.setHeight("300px");

        VerticalLayout groupRecordsLayout = new VerticalLayout();
        HorizontalLayout wallListAddRecordayout = new HorizontalLayout();
        wallListAddRecordayout.setWidth("705px");
        Button addRecordButton = new Button("Add record", VaadinIcons.PLUS);
        wallListAddRecordayout.addComponents(new Label("Wall 396 records"), addRecordButton);
        wallListAddRecordayout.setComponentAlignment(addRecordButton, Alignment.TOP_RIGHT);
        groupRecordsLayout.addComponents(wallListAddRecordayout, groupRecord, groupRecord1, groupRecord2);
        mainLeftPanel.setContent(groupRecordsLayout);


        Panel mainRightPanel = new Panel();
        mainRightPanel.setWidth("240px");
        //SUBSCRIBED FRIENDS
//        List<Profile> friends = new ArrayList<>(
//                Arrays.asList(new Profile(), new Profile(), new Profile(), new Profile(), new Profile()));
//        for (int i = 0; i < 5; i++)
//            friends.get(i).setProfileAvatar("https://goo.gl/h9Dg5U");
//        Panel friendSubscribersPanel = new Panel();
//        friendSubscribersPanel.setWidth("215px");
//        friendSubscribersPanel.setHeight("110px");
//        GridLayout friendsGrid = getPhotosGrid(friends);
//        VerticalLayout friendsLayout = new VerticalLayout();
//        friendsLayout.addComponents(new Label("Your friends already here"), friendsGrid);
//        friendSubscribersPanel.setContent(friendsLayout);
        //-----------------SUBSCRIBED FRIENDS

        //SUBSCRIBERS
        List<Profile> subscribers = getGroupSubscribers(groupId);
        Panel subscribersPanel = new Panel();
        subscribersPanel.setWidth("215px");
        subscribersPanel.setHeight("200px");
        GridLayout subscribersGrid = getPhotosGrid(subscribers);
        VerticalLayout subscribersLayout = new VerticalLayout();
        subscribersLayout.addComponents(new Label("Subscribers"), subscribersGrid);
        subscribersPanel.setContent(subscribersLayout);
        //-----------------SUBSCRIBERS

        //ADMIN
        Profile admin = getGroupAdmin(groupId);
        Panel adminPanel = new Panel();
        adminPanel.setWidth("215px");
        adminPanel.setHeight("110px");
        Image adminImage = new Image();
        adminImage.setSource(new ExternalResource(
                admin.getProfileAvatar() == null ? stubAvatar : admin.getProfileAvatar()));
        adminImage.setWidth("52px");
        adminImage.setHeight("45px");
        adminImage.addClickListener((MouseEvents.ClickListener) clickEvent ->
                ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(admin.getObjectId())));
        VerticalLayout adminLayout = new VerticalLayout();
        adminLayout.addComponents(new Label("Admin"), adminImage);
        adminPanel.setContent(adminLayout);
        //-----------------ADMIN

        VerticalLayout mainRightLayout = new VerticalLayout();
        Button subscribeButton = new Button("Subscribe", VaadinIcons.ENTER_ARROW);
        Button leaveGroupButton = new Button("Leave", VaadinIcons.SIGN_OUT);
        SecurityContext curUser = (SecurityContext) VaadinSession.getCurrent().getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        String login = curUser.getAuthentication().getPrincipal().toString();
        BigInteger profileId = CustomRestTemplate.getInstance().customPostForObject("/user/profileId", login, BigInteger.class);
        List<Profile> subscrs = getGroupSubscribers(groupId);
        if (subscrs.size() == 0) {
            mainRightLayout.addComponent(subscribeButton);
            mainRightLayout.setComponentAlignment(subscribeButton, Alignment.TOP_CENTER);
        }
        for (Profile user : subscrs) {
            if (!profileId.equals(user.getObjectId())) {
                mainRightLayout.addComponent(subscribeButton);
                mainRightLayout.setComponentAlignment(subscribeButton, Alignment.TOP_CENTER);
            } else {
                mainRightLayout.replaceComponent(subscribeButton, leaveGroupButton);
                break;
            }
        }
        Button editGroup = new Button("Edit the group");
        if (profileId.equals(admin.getObjectId())) {
            mainRightLayout.removeComponent(subscribeButton);
            mainRightLayout.removeComponent(leaveGroupButton);
            mainRightLayout.addComponent(editGroup);
            mainRightLayout.setComponentAlignment(editGroup, Alignment.TOP_CENTER);
        }
        editGroup.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                editGroup(curGroup);
                UI.getCurrent().addWindow(newGroupWindow);
            }
        });
        subscribeButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                CustomRestTemplate.getInstance().customGetForObject(
                        "/groups/subscribe/" + groupId + "/" + profileId, Void.class);
                ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new GroupUI(groupId));
            }
        });
        leaveGroupButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                CustomRestTemplate.getInstance().customGetForObject(
                        "/groups/leaveGroup/" + groupId + "/" + profileId, Void.class);
                ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new MyGroupsListUI(profileId));
            }
        });
        mainRightLayout.addComponents(subscribersPanel, adminPanel);
        mainRightPanel.setContent(mainRightLayout);
        contentLayout.addComponents(mainLeftPanel, mainRightPanel);
        addComponents(headerLayout, contentLayout);
    }

    private void editGroup(Group group) {
        newGroupWindow = new Window();
        newGroupWindow.setModal(true);
        newGroupWindow.setWidth("400px");
        newGroupWindow.setHeight("320px");
        newGroupWindow.setCaption("Group change:");
        VerticalLayout windowContent = new VerticalLayout();
        HorizontalLayout addAlbumButtonsLayout = new HorizontalLayout();

        TextField groupName = PageElements.createTextField(
                "Change the name:", "old: " + group.getGroupName(), false);
        groupName.setWidth("100%");
        TextField groupAvatar = PageElements.createTextField(
                "Change the avatar:", "group avatar", false);
        groupName.setWidth("100%");
        TextField description = PageElements.createTextField(
                "Change the group description:", "old: " + group.getGroupDescription().substring(0,40) + "...", false);
        description.setWidth("100%");

        Button addGroupButton = new Button("Edit group");
        addGroupButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                addGroupButton.setComponentError(null);
//                createGroup(groupName.getValue(), groupAvatar.getValue(), description.getValue(), profileId);
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

    private GridLayout getPhotosGrid(List<Profile> list) {
        GridLayout photosGrid = new GridLayout(3, (list.size() / 3) + 1);
        photosGrid.setSpacing(true);
        for (Profile user : list) {
            Panel singlePhotoPanel = new Panel();
            Image singlePhotoImage = new Image();
            String curAvatar = user.getProfileAvatar();
            singlePhotoImage.setSource(new ExternalResource(curAvatar == null ? stubAvatar : curAvatar));
            singlePhotoImage.setWidth("52px");
            singlePhotoImage.setHeight("45px");
            singlePhotoImage.addClickListener((MouseEvents.ClickListener) clickEvent ->
                    ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(user.getObjectId())));
            singlePhotoPanel.setContent(singlePhotoImage);
            photosGrid.addComponent(singlePhotoPanel);
        }
        return photosGrid;
    }

    private Group getGroup(BigInteger groupId) {
        return CustomRestTemplate.getInstance().customGetForObject("/groups/" + groupId, Group.class);
    }

    private List<Profile> getGroupSubscribers(BigInteger groupId) {
        return Arrays.asList(CustomRestTemplate.getInstance().
                customGetForObject("/groups/" + groupId + "/subscribers", Profile[].class));
    }

    private Profile getGroupAdmin(BigInteger groupId) {
        return CustomRestTemplate.getInstance().
                customGetForObject("/groups/" + groupId + "/admin", Profile.class);
    }
}
