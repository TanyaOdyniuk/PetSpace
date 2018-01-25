package com.netcracker.ui.groups;

import com.netcracker.model.group.Group;
import com.netcracker.model.record.AbstractRecord;
import com.netcracker.model.record.GroupRecord;
import com.netcracker.model.user.Profile;
import com.netcracker.ui.*;
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
import org.springframework.http.HttpEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;
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
        VerticalLayout groupRecordsLayout = new VerticalLayout();
        HorizontalLayout wallListAddRecordayout = new HorizontalLayout();
        wallListAddRecordayout.setWidth("705px");
        Button addRecordButton = new AddRecordButton(curGroup);
        List<GroupRecord> groupRecords = getWallRecords(groupId);
        wallListAddRecordayout.addComponents(new Label("Wall " + groupRecords.size() + " records"), addRecordButton);
        wallListAddRecordayout.setComponentAlignment(addRecordButton, Alignment.TOP_RIGHT);
        groupRecordsLayout.addComponents(wallListAddRecordayout);
        for(GroupRecord currentRecord : groupRecords) {
            Panel singleRecord = new RecordPanel(currentRecord, curGroup, false, 0, false);
            groupRecordsLayout.addComponent(singleRecord);
        }
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
        Button deleteGroup = new Button("Delete the group");
        if (profileId.equals(admin.getObjectId())) {
            mainRightLayout.removeComponent(subscribeButton);
            mainRightLayout.removeComponent(leaveGroupButton);
            mainRightLayout.addComponent(editGroup);
            mainRightLayout.setComponentAlignment(editGroup, Alignment.TOP_CENTER);
            mainRightLayout.addComponent(deleteGroup);
            mainRightLayout.setComponentAlignment(deleteGroup, Alignment.TOP_CENTER);
        }
        editGroup.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                editGroup(curGroup);
                UI.getCurrent().addWindow(newGroupWindow);
            }
        });
        deleteGroup.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                CustomRestTemplate.getInstance().customGetForObject("/groups/delete/" + groupId, Void.class);
                ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new MyGroupsListUI(profileId));
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
                "Change the name:", group.getGroupName(), false);
        groupName.setWidth("100%");
        TextField groupAvatar = PageElements.createTextField(
                "Change the avatar:", group.getGroupAvatar(), false);
        groupName.setWidth("100%");
        TextField description = PageElements.createTextField(
                "Change the group description:", group.getGroupDescription(), false);
        description.setWidth("100%");

        Button editGroupButton = new Button("Edit group");
        editGroupButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                editGroupButton.setComponentError(null);
                groupName.setValue(group.getGroupName());
                groupAvatar.setValue(group.getGroupAvatar());
                description.setValue(group.getGroupDescription());
                group.setGroupName(groupName.getValue());
                group.setGroupAvatar(groupAvatar.getValue());
                group.setGroupDescription(description.getValue());
                updateGroup(group);
                newGroupWindow.close();
                Notification.show("You have just edited the group!");
            }
        });
        Button cancelAddingNewAlbum = new Button("Cancel", click -> newGroupWindow.close());
        addAlbumButtonsLayout.addComponentsAndExpand(editGroupButton, cancelAddingNewAlbum);
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

    private List<GroupRecord> getWallRecords(BigInteger groupId) {
        List<GroupRecord> result = Arrays.asList(CustomRestTemplate.getInstance().
                customGetForObject("/records/group/" + groupId, GroupRecord[].class));
        result.sort(Comparator.comparing(AbstractRecord::getRecordDate));
        return result;
    }

    private void updateGroup(Group group) {
        HttpEntity<Group> request = new HttpEntity<>(group);
        CustomRestTemplate.getInstance().customPostForObject("/groups/update", request, Group.class);
    }
}
