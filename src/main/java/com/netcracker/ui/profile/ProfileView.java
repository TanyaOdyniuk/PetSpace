package com.netcracker.ui.profile;

import com.netcracker.model.pet.Pet;
import com.netcracker.model.record.AbstractRecord;
import com.netcracker.model.record.WallRecord;
import com.netcracker.model.request.FriendRequest;
import com.netcracker.model.status.Status;
import com.netcracker.model.status.StatusConstant;
import com.netcracker.model.user.Profile;
import com.netcracker.ui.*;
import com.netcracker.ui.friendlist.FriendListUI;
import com.netcracker.ui.messages.NewMessageWindowUI;
import com.netcracker.ui.pet.MyPetsListUI;
import com.netcracker.ui.pet.PetPageUI;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.event.MouseEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.math.BigInteger;
import java.util.*;

public class ProfileView extends VerticalLayout {
    private final Profile profile;
    private final BigInteger profileId;
    private final List<Profile> friendList;
    private final List<Pet> petList;
    private final List<WallRecord> wallRecordsList;
    private final Button addToFriendsButton;
    private int browserHeight;
    private int browserWidth;
    private String stubAvatar = "https://goo.gl/6eEoWo";
    private Profile currentProfile;
    private BigInteger currentProfileId;

    public ProfileView(BigInteger profileID) {
        super();
        profileId = profileID;
        profile = CustomRestTemplate.getInstance().
                customGetForObject("/profile/" + profileId, Profile.class);
        setCurrentProfile();
        friendList = Arrays.asList(CustomRestTemplate.getInstance().
                customGetForObject("/friends/" + profileId, Profile[].class));
        petList = Arrays.asList(CustomRestTemplate.getInstance().
                customGetForObject("/pets/" + profileId, Pet[].class));
        wallRecordsList = getWallRecords(profileId);

        HorizontalLayout mainLayout = new HorizontalLayout();
        browserHeight = UI.getCurrent().getPage().getBrowserWindowHeight();
        browserWidth = UI.getCurrent().getPage().getBrowserWindowWidth();

        Panel leftPartPanel = new Panel();
        leftPartPanel.setHeight("100%");
        leftPartPanel.setWidth(250, Unit.PIXELS);
        Panel rightPartPanel = new Panel();
        rightPartPanel.setHeight("100%");

        VerticalLayout leftPartLayout = new VerticalLayout();
        leftPartLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        VerticalLayout rightPartLayout = new VerticalLayout();
        rightPartLayout.setSpacing(true);

        //Elements for left part
        Image avatarImage = new Image();
        avatarImage.setHeight(250, Unit.PIXELS);
        avatarImage.setWidth(250, Unit.PIXELS);
        PageElements.setProfileImageSource(avatarImage, profile.getProfileAvatar());
        avatarImage.setDescription("Profile avatar");

        addToFriendsButton = new Button();
        Button sendDirectMessage = new Button();
        Status profilesStatus = checkProfilesStatus(currentProfileId, profileId);
        if (!currentProfileId.equals(profileId)) {
            sendDirectMessage.setCaption("Send message");
            sendDirectMessage.setIcon(VaadinIcons.ENVELOPE_O);
            sendDirectMessage.setHeight(50, Unit.PIXELS);
            sendDirectMessage.setWidth("100%");

            sendDirectMessage.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    NewMessageWindowUI sub = new NewMessageWindowUI(currentProfileId, profileId);
                    UI.getCurrent().addWindow(sub);
                }
            });

            if (profilesStatus == null || profilesStatus.getStatusName().equalsIgnoreCase(StatusConstant.IS_NOT_FRIEND)) {
                addToFriendsButton.setCaption("Add friend");
                addToFriendsButton.setIcon(VaadinIcons.USERS);
                addToFriendsButton.addClickListener(new AbstractClickListener() {
                    @Override
                    public void buttonClickListener() {
                        sendRequest();
                        addToFriendsButton.setEnabled(false);
                        addToFriendsButton.setCaption("Request was sent");
                    }
                });
            } else if (profilesStatus.getStatusName().equalsIgnoreCase(StatusConstant.IS_PENDING)) {
                addToFriendsButton.setCaption("Request was sent");
                addToFriendsButton.setIcon(VaadinIcons.USERS);
                addToFriendsButton.setEnabled(false);
            }
            addToFriendsButton.setHeight(50, Unit.PIXELS);
            addToFriendsButton.setWidth("100%");
        }

        Panel petsPanel = new Panel();
        petsPanel.setWidth("100%");
        Button profilePets = PageElements.createClickedLabel(profile.getProfileName() + "`s pets:");
        profilePets.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new MyPetsListUI(profileId));
            }
        });
        VerticalLayout petsLayout = new VerticalLayout();
        GridLayout petsGrid;
        int petColumns = 3;
        int petListSize = petList.size();
        if (petListSize != 0) {
            int petRows = petListSize / petColumns;
            if (petListSize % petColumns != 0) {
                petRows++;
            }
            petsGrid = new GridLayout(petColumns, petRows);
            petsGrid.setSpacing(true);
            for (Pet singlePet : petList) {
                Image petMiniImage = new Image();
                String petAvatar = singlePet.getPetAvatar();
                PageElements.setPetImageSource(petMiniImage, petAvatar);
                petMiniImage.setHeight(55, Unit.PIXELS);
                petMiniImage.setWidth(55, Unit.PIXELS);
                petMiniImage.setDescription(singlePet.getPetName());
                petMiniImage.addClickListener((MouseEvents.ClickListener) clickEvent ->
                        ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new PetPageUI(singlePet.getObjectId())));
                petsGrid.addComponent(petMiniImage);
            }
        } else {
            petsGrid = new GridLayout(1, 1);
            petsGrid.addComponent(new Label("No pets yet ;("));
        }
        petsLayout.addComponents(profilePets, petsGrid);
        petsPanel.setContent(petsLayout);

        Panel friendsPanel = new Panel();
        friendsPanel.setWidth("100%");
        Button profileFriends = PageElements.createClickedLabel(profile.getProfileName() + "`s friends:");
        profileFriends.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new FriendListUI(profileId));
            }
        });
        VerticalLayout friendsLayout = new VerticalLayout();
        GridLayout friendsGrid;
        int friendColumns = 3;
        int friendListSize = friendList.size();
        if (friendListSize != 0) {
            int friendRows = friendListSize / friendColumns;
            if (friendListSize % friendColumns != 0) {
                friendRows++;
            }
            friendsGrid = new GridLayout(friendColumns, friendRows);
            friendsGrid.setSpacing(true);
            for (Profile singleFriend : friendList) {
                String singleFriendAvatar = singleFriend.getProfileAvatar();
                Image friendMiniImage = new Image();
                friendMiniImage.setHeight(55, Unit.PIXELS);
                friendMiniImage.setWidth(55, Unit.PIXELS);
                PageElements.setProfileImageSource(friendMiniImage, singleFriendAvatar);
                friendMiniImage.setDescription(singleFriend.getProfileName() + " " + singleFriend.getProfileSurname());
                friendMiniImage.addClickListener((MouseEvents.ClickListener) clickEvent ->
                        ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(singleFriend.getObjectId())));
                friendsGrid.addComponent(friendMiniImage);
            }
        } else {
            friendsGrid = new GridLayout(1, 1);
            friendsGrid.addComponent(new Label("No friends yet ;("));
        }
        friendsLayout.addComponents(profileFriends, friendsGrid);
        friendsPanel.setContent(friendsLayout);

        //Elements for right part
        Panel nameAndManagePanel = new Panel();
        HorizontalLayout nameAndBalanceLayout = new HorizontalLayout();
        nameAndBalanceLayout.addComponents(
                new Label(profile.getProfileName() + " " + profile.getProfileSurname())/*,
                new Label("Balance: " + profile.getProfileCurrencyBalance())*/
        );
        if (profileID.equals(currentProfileId)) {
            nameAndBalanceLayout.addComponents(
                    new Label("(it`s your page)"), getEditProfileButton());
        }
        nameAndBalanceLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        nameAndManagePanel.setContent(nameAndBalanceLayout);
        nameAndManagePanel.setHeight(45, Unit.PIXELS);

        Panel simpleInfoPanel = new Panel();
        VerticalLayout simpleInfoLayout = new VerticalLayout();
        HorizontalLayout interestsLayout = new HorizontalLayout();
        interestsLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        interestsLayout.addComponent(new Label("Interests: "));
        List<String> profileHobbies = profile.getProfileHobbies();
        if (profileHobbies == null) {
            interestsLayout.addComponent(new Label("not specified."));
        } else {
            for (String singleInterest : profile.getProfileHobbies()) {
                if (singleInterest != null)
                    interestsLayout.addComponent(new Label(singleInterest));
            }
        }
        HorizontalLayout favBreedsLayout = new HorizontalLayout();
        favBreedsLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        favBreedsLayout.addComponent(new Label("Favourite breeds: "));
        List<String> profileFavBreeds = profile.getProfileFavouriteBreeds();
        if (profileFavBreeds == null) {
            favBreedsLayout.addComponent(new Label("not specified."));
        } else {
            for (String singleFavBreed : profile.getProfileFavouriteBreeds()) {
                if (singleFavBreed != null)
                    favBreedsLayout.addComponent(new Label(singleFavBreed));
            }
        }
        simpleInfoLayout.addComponents(
                new Label("Age: " + (profile.getProfileAge() == null ? "not specified." : profile.getProfileAge())),
                interestsLayout,
                favBreedsLayout
        );
        simpleInfoPanel.setContent(simpleInfoLayout);

        //Profile wall with records
        Panel wallPanel = new Panel();
        wallPanel.setWidth("100%");
        VerticalLayout wallLayout = new VerticalLayout();
        HorizontalLayout wallHeaderLayout = new HorizontalLayout();
        VerticalLayout addWallRecordLayout = new VerticalLayout();
        addWallRecordLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        Button addWallRecordButton = new AddRecordButton(profile);

        int wallRecordsListSize = wallRecordsList.size();
        String labelCaptionStart = wallRecordsListSize + " record";
        if (wallRecordsListSize != 1) {
            labelCaptionStart += "s";
        }
        wallHeaderLayout.addComponents(
                new Label(labelCaptionStart + " on " + profile.getProfileName() + "`s wall:"), addWallRecordButton);
        wallHeaderLayout.setComponentAlignment(addWallRecordButton, Alignment.MIDDLE_RIGHT);
        wallLayout.addComponent(wallHeaderLayout);
        //WallRecords+Comments
        for (int i = wallRecordsListSize; i > 0; i--) {
            WallRecord currentRecord = wallRecordsList.get(i - 1);
            Panel singleWallRecordPanel = new RecordPanel(currentRecord, profile, false, 0, false);

            wallLayout.addComponent(singleWallRecordPanel);
        }
        wallPanel.setContent(wallLayout);

        if (!currentProfileId.equals(profileId)) {
            if (profilesStatus == null || !profilesStatus.getStatusName().equalsIgnoreCase(StatusConstant.IS_FRIEND))
                leftPartLayout.addComponents(avatarImage, addToFriendsButton, sendDirectMessage, petsPanel, friendsPanel);
            else {
                Label friendsLabel = new Label("You're " + profile.getProfileName() + "'s friend");
                Button removeFromFriends = new Button();
                removeFromFriends.setCaption("Delete friend");
                removeFromFriends.setIcon(VaadinIcons.USERS);
                removeFromFriends.setHeight(50, Unit.PIXELS);
                removeFromFriends.setWidth("100%");
                removeFromFriends.addClickListener(new AbstractClickListener() {
                    @Override
                    public void buttonClickListener() {
                        deleteFromFriends(currentProfileId, profileID);
                        Notification.show("Friend was deleted!");
                        ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(profileID));
                    }
                });
                leftPartLayout.addComponents(avatarImage, friendsLabel, removeFromFriends, sendDirectMessage, petsPanel, friendsPanel);
            }
        } else
            leftPartLayout.addComponents(avatarImage, petsPanel, friendsPanel);

        rightPartLayout.addComponents(nameAndBalanceLayout, simpleInfoPanel, wallPanel);
        leftPartPanel.setContent(leftPartLayout);
        rightPartPanel.setContent(rightPartLayout);
        mainLayout.addComponent(leftPartPanel);
        mainLayout.addComponentsAndExpand(rightPartPanel);
        addComponents(mainLayout);
    }

    private Button getEditProfileButton() {
        Button result = new Button("Edit profile");
        result.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                UI.getCurrent().addWindow(new ProfileEditUI(currentProfile));
            }
        });
        return result;
    }

    private Button getDeleteProfileButton() {
        Button result = new Button("Delete profile");
        result.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                Notification.show("Not today!");
            }
        });
        return result;
    }

    private Panel getLastPhotos() {
        Panel photosPanel = new Panel();
        photosPanel.setWidth("100%");
        VerticalLayout photosLayout = new VerticalLayout();
        int photosColumns = 5, photosRows = 3;
        GridLayout photosGrid = new GridLayout(photosColumns, photosRows);
        photosGrid.setSpacing(true);
        Resource photoResource = new ExternalResource("https://image.ibb.co/d9Oc9b/2004039.png");
        for (int i = 0; i < photosColumns * photosRows; i++) {
            Panel singlePhotoPanel = new Panel();
            Image singlePhotoImage = new Image();
            singlePhotoImage.setSource(photoResource);
            singlePhotoImage.setDescription("Photo " + (i + 1));
            singlePhotoPanel.setContent(singlePhotoImage);
            photosGrid.addComponent(singlePhotoPanel);
        }
        photosLayout.addComponentsAndExpand(new Label("Last photos"), photosGrid);
        photosPanel.setContent(photosLayout);
        return photosPanel;
    }

    private List<WallRecord> getWallRecords(BigInteger profileId) {
        List<WallRecord> result = Arrays.asList(CustomRestTemplate.getInstance().
                customGetForObject("/records/wall/" + profileId, WallRecord[].class));
        result.sort(Comparator.comparing(AbstractRecord::getRecordDate));
        return result;
    }

    private void sendRequest() {
        FriendRequest request = new FriendRequest();
        request.setReqTo(profile);
        request.setReqFrom(currentProfile);
        CustomRestTemplate.getInstance().customPostForObject("/request/send", request, FriendRequest.class);
    }

    private void deleteFromFriends(BigInteger currentProfileId, BigInteger profileIdToDelete) {
        CustomRestTemplate.getInstance().customGetForObject("/request/" + currentProfileId + "/delete/" + profileIdToDelete, Void.class);
    }

    private Status checkProfilesStatus(BigInteger currentProfileId, BigInteger profileIdToCheck) {
        return CustomRestTemplate.getInstance().customGetForObject("/request/" + currentProfileId + "/check/" + profileIdToCheck, Status.class);
    }

    private void setCurrentProfile() {
        SecurityContext o = (SecurityContext) VaadinSession.getCurrent().getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        String login = o.getAuthentication().getPrincipal().toString();
        BigInteger currentProfileId = CustomRestTemplate.getInstance().customPostForObject("/user/profileId", login, BigInteger.class);
        currentProfile = CustomRestTemplate.getInstance().
                customGetForObject("/profile/" + currentProfileId, Profile.class);
        this.currentProfileId = currentProfile.getObjectId();
    }
}
