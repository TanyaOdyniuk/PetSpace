package com.netcracker.ui.groups;

import com.netcracker.asserts.ObjectAssert;
import com.netcracker.asserts.PetDataAssert;
import com.netcracker.model.group.Group;
import com.netcracker.model.record.AbstractRecord;
import com.netcracker.model.record.GroupRecord;
import com.netcracker.model.user.Profile;
import com.netcracker.ui.*;
import com.netcracker.ui.profile.ProfileView;
import com.netcracker.ui.util.CustomRestTemplate;
import com.netcracker.ui.util.upload.ImageUpload;
import com.netcracker.ui.util.upload.UploadableComponent;
import com.vaadin.event.MouseEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.io.File;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class GroupUI extends VerticalLayout implements UploadableComponent {
    private HorizontalLayout headerLayout;
    private HorizontalLayout contentLayout;
    private Window newGroupWindow;

    private Image avatar;
    private Boolean isFileResource;
    private String avatarPath;
    private Group group;
    private Window deleteGroupWindow;

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
        PageElements.setDefaultImageSource(avatar, curGroup.getGroupAvatar());
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
        PageElements.setDefaultImageSource(adminImage, admin.getProfileAvatar());
        adminImage.setWidth("52px");
        adminImage.setHeight("45px");
        adminImage.addClickListener((MouseEvents.ClickListener) clickEvent ->
                ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(admin.getObjectId())));
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
        getDeleteConfirmationWindow(groupId, profileId);
        deleteGroup.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                UI.getCurrent().addWindow(deleteGroupWindow);
            }
        });
        subscribeButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                CustomRestTemplate.getInstance().customGetForObject(
                        "/groups/subscribe/" + groupId + "/" + profileId, Void.class);
                ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new GroupUI(groupId));
            }
        });
        leaveGroupButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                CustomRestTemplate.getInstance().customGetForObject(
                        "/groups/leaveGroup/" + groupId + "/" + profileId, Void.class);
                ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new MyGroupsListUI(profileId));
            }
        });
        mainRightLayout.addComponents(subscribersPanel, adminPanel);
        mainRightPanel.setContent(mainRightLayout);
        contentLayout.addComponents(mainLeftPanel, mainRightPanel);
        addComponents(headerLayout, contentLayout);
    }

    private void editGroup(Group group) {
        this.group = group;
        this.isFileResource = false;
        this.avatar = new Image();
        newGroupWindow = new Window();
        newGroupWindow.setModal(true);
        newGroupWindow.setWidth("450px");
        newGroupWindow.setHeight("495px");
        newGroupWindow.setCaption("Group change:");
        VerticalLayout windowContent = new VerticalLayout();
        HorizontalLayout addAlbumButtonsLayout = new HorizontalLayout();

        GridLayout avatarLayout = new GridLayout(2, 1);
        VerticalLayout avatarContext = new VerticalLayout();
        TextField avatarField = PageElements.createTextField("Avatar", "Avatar's URL");
        String groupAvatarSource = group.getGroupAvatar();
        PageElements.setDefaultImageSource(avatar, groupAvatarSource);
        avatar.setHeight("200px");
        avatar.setWidth("200px");

        Button avatarSelect = new Button("Set URL");
        avatarSelect.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                avatarSelect.setComponentError(null);
                updateImage(avatarField.getValue(), avatar);
            }
        });
        Upload uploadAvatar = new ImageUpload(UIConstants.PATH_TO_PHOTOS,
                group.getObjectId() == null ? getCurrentUserProfileId() : group.getObjectId(), this);
        avatarSelect.setWidth("100%");
        uploadAvatar.setWidth("100%");

        avatarContext.addComponents(avatarField, avatarSelect, uploadAvatar);
        avatarContext.setComponentAlignment(avatarSelect, Alignment.MIDDLE_CENTER);
        avatarContext.setComponentAlignment(uploadAvatar, Alignment.MIDDLE_CENTER);

        avatarLayout.addComponent(avatar, 0, 0);
        avatarLayout.addComponent(avatarContext, 1, 0);

        TextField groupName = PageElements.createTextField("Change the name:", group.getGroupName(), false);
        groupName.setWidth("100%");
        TextField description = PageElements.createTextField("Change the group description:", group.getGroupDescription(), false);
        description.setWidth("100%");

        Button editGroupButton = new Button("Edit group");
        groupName.setValue(group.getGroupName());
        description.setValue(group.getGroupDescription() == null ? "" : group.getGroupDescription());
        editGroupButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                editGroupButton.setComponentError(null);

                if(avatarPath == null)
                    avatarPath = group.getGroupAvatar();
                group.setGroupAvatar(avatarPath);
                group.setGroupName(groupName.getValue());
                group.setGroupDescription(description.getValue());
                updateGroup(group);
                newGroupWindow.close();
                Notification.show("You have just edited the group!");
                ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new GroupUI(group.getObjectId()));
            }
        });
        Button cancelAddingNewAlbum = new Button("Cancel", click -> newGroupWindow.close());
        addAlbumButtonsLayout.addComponentsAndExpand(editGroupButton, cancelAddingNewAlbum);
        windowContent.addComponents(avatarLayout, groupName, description, addAlbumButtonsLayout);

        newGroupWindow.setContent(windowContent);
        newGroupWindow.center();
    }

    private GridLayout getPhotosGrid(List<Profile> list) {
        GridLayout photosGrid = new GridLayout(3, (list.size() / 3) + 1);
        photosGrid.setSpacing(true);
        for (Profile user : list) {
            Panel singlePhotoPanel = new Panel();
            Image singlePhotoImage = new Image();
            PageElements.setProfileImageSource(singlePhotoImage, user.getProfileAvatar());
            singlePhotoImage.setWidth("52px");
            singlePhotoImage.setHeight("45px");
            singlePhotoImage.addClickListener((MouseEvents.ClickListener) clickEvent ->
                    ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(user.getObjectId())));
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
        ObjectAssert.isNullOrEmpty(group.getGroupAvatar());
        if (!isFileResource)
            avatarPath = PetDataAssert.assertAvatarURL(avatarPath);
        ObjectAssert.isNullOrEmpty(group.getGroupName());
        HttpEntity<Group> request = new HttpEntity<>(group);
        CustomRestTemplate.getInstance().customPostForObject("/groups/update", request, Group.class);
    }

    @Override
    public void updateImage(File imageFile) {
        avatar.setSource(new FileResource(imageFile));
        isFileResource = true;
        avatarPath = imageFile.getPath();
    }

    private void updateImage(String imageURL, Image imageToUpdate) {
        imageURL = PetDataAssert.assertAvatarURL(imageURL);
        group.setGroupAvatar(imageURL);
        imageToUpdate.setSource(new ExternalResource(imageURL));
        this.isFileResource = false;
        this.avatarPath = imageURL;
    }

    private BigInteger getCurrentUserProfileId() {
        SecurityContext o = (SecurityContext) VaadinSession.getCurrent().getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        String login = o.getAuthentication().getPrincipal().toString();
        return CustomRestTemplate.getInstance().customPostForObject("/user/profileId", login, BigInteger.class);
    }

    private void getDeleteConfirmationWindow(BigInteger groupId, BigInteger profileId){
        deleteGroupWindow = new Window();
        deleteGroupWindow.setCaption("Confirm the action");
        deleteGroupWindow.setDraggable(false);
        deleteGroupWindow.setResizable(false);
        deleteGroupWindow.setModal(true);
        deleteGroupWindow.center();
        deleteGroupWindow.setWidth("300px");
        deleteGroupWindow.setHeight("150px");

        VerticalLayout mainLayout = new VerticalLayout();
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        mainLayout.setWidth("100%");
        buttonsLayout.setWidth("100%");

        Button yesButton = new Button("YES", VaadinIcons.CHECK);
        Button noButton = new Button("NO", VaadinIcons.CLOSE_BIG);
        yesButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                CustomRestTemplate.getInstance().customGetForObject("/groups/delete/" + groupId, Void.class);
                Notification.show("Your group was successfully deleted!", Notification.Type.TRAY_NOTIFICATION);
                deleteGroupWindow.close();
                ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new MyGroupsListUI(profileId));
            }
        });
        noButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                deleteGroupWindow.close();
            }
        });
        Label confirmText = PageElements.createStandartLabel("Are you sure to delete the group?");
        buttonsLayout.addComponents(yesButton, noButton);
        buttonsLayout.setComponentAlignment(yesButton, Alignment.MIDDLE_LEFT);
        buttonsLayout.setComponentAlignment(noButton, Alignment.MIDDLE_RIGHT);
        yesButton.setWidth("125px");
        noButton.setWidth("125px");

        mainLayout.addComponents(confirmText, buttonsLayout);
        mainLayout.setComponentAlignment(confirmText, Alignment.MIDDLE_CENTER);
        deleteGroupWindow.setContent(mainLayout);
    }
}
