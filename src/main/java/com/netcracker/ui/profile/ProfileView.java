package com.netcracker.ui.profile;

import com.netcracker.model.comment.WallRecordComment;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.record.AbstractRecord;
import com.netcracker.model.record.WallRecord;
import com.netcracker.model.user.Profile;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.StubVaadinUI;
import com.netcracker.ui.friendlist.FriendListUI;
import com.netcracker.ui.pet.MyPetsListUI;
import com.netcracker.ui.pet.PetPageUI;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.event.MouseEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@SpringComponent
@UIScope
public class ProfileView extends VerticalLayout {
    private final Profile profile;
    private final BigInteger profileId;
    private final List<Profile> friendList;
    private final List<Pet> petList;
    private final List<WallRecord> wallRecordsList;
    private List<WallRecordComment> currentWallRecordComments;
    private Window newWallRecordWindow;
    private int browserHeight;
    private int browserWidth;

    @Autowired
    public ProfileView(BigInteger profileId) {
        super();
        this.profileId = profileId;
        profile = CustomRestTemplate.getInstance().
                customGetForObject("/profile/" + profileId, Profile.class);
        friendList = Arrays.asList(CustomRestTemplate.getInstance().
                customGetForObject("/friends/" + profileId, Profile[].class));
        petList = Arrays.asList(CustomRestTemplate.getInstance().
                customGetForObject("/pets/" + profileId, Pet[].class));
        wallRecordsList = Arrays.asList(CustomRestTemplate.getInstance().
                customGetForObject("/wallrecords/" + profileId, WallRecord[].class));
        wallRecordsList.sort(Comparator.comparing(AbstractRecord::getRecordDate));

        //Creating matryoshka layout
        HorizontalLayout mainLayout = new HorizontalLayout();
        browserHeight = UI.getCurrent().getPage().getBrowserWindowHeight();
        browserWidth = UI.getCurrent().getPage().getBrowserWindowWidth();
        mainLayout.setHeight(browserHeight - 250, Unit.PIXELS);
        mainLayout.setWidth(browserWidth - 450, Unit.PIXELS);
        Panel leftPartPanel = new Panel();
        leftPartPanel.setHeight("100%");
        leftPartPanel.setWidth(250, Unit.PIXELS);
        Panel rightPartPanel = new Panel();
        rightPartPanel.setHeight("100%");
        //rightPartPanel.setHeight(750, Unit.PIXELS);
        //rightPartPanel.setWidth(100, Unit.PERCENTAGE);
        VerticalLayout leftPartLayout = new VerticalLayout();
        leftPartLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        VerticalLayout rightPartLayout = new VerticalLayout();
        rightPartLayout.setSpacing(true);

        //Elements for left part
        Image avatarImage = new Image();
        avatarImage.setHeight(250, Unit.PIXELS);
        avatarImage.setWidth(250, Unit.PIXELS);
        avatarImage.setSource(new ExternalResource(profile.getProfileAvatar()));
        avatarImage.setDescription("Profile avatar");
        Button addToFriendsButton = new Button("Add friend", VaadinIcons.SMILEY_O);
        addToFriendsButton.setHeight(50, Unit.PIXELS);
        addToFriendsButton.setWidth("100%");

        Panel petsPanel = new Panel();
        petsPanel.setWidth("100%");
        Button profilePets = PageElements.createClickedLabel(profile.getProfileName() + "`s pets:");
        profilePets.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new MyPetsListUI(profileId));
            }
        });
        VerticalLayout petsLayout = new VerticalLayout();
        int petColumns = 3;
        int petRows = 1;
        if (petList.size() != 0) {
            petRows = petList.size() / petColumns;
            if (petList.size() % petColumns != 0) {
                petRows++;
            }
        }
        GridLayout petsGrid = new GridLayout(petColumns, petRows);
        petsGrid.setSpacing(true);
        for (Pet singlePet : petList) {
            Image petMiniImage = new Image();
            petMiniImage.setHeight(55, Unit.PIXELS);
            petMiniImage.setWidth(55, Unit.PIXELS);
            petMiniImage.setSource(new ExternalResource(singlePet.getPetAvatar()));
            petMiniImage.setDescription(singlePet.getPetName());
            petMiniImage.addClickListener((MouseEvents.ClickListener) clickEvent ->
                    ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new PetPageUI(singlePet.getObjectId())));
            petsGrid.addComponent(petMiniImage);
        }
        petsLayout.addComponents(profilePets, petsGrid);
        petsPanel.setContent(petsLayout);

        Panel friendsPanel = new Panel();
        friendsPanel.setWidth("100%");
        Button profileFriends = PageElements.createClickedLabel(profile.getProfileName() + "`s friends:");
        profileFriends.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new FriendListUI(profileId));
            }
        });
        VerticalLayout friendsLayout = new VerticalLayout();
        int friendColumns = 3;
        int friendRows = 1;
        if (friendList.size() != 0) {
            friendRows = friendList.size() / friendColumns;
            if (friendList.size() % friendColumns != 0) {
                friendRows++;
            }
        }
        GridLayout friendsGrid = new GridLayout(friendColumns, friendRows);
        friendsGrid.setSpacing(true);
        for (Profile singleFriend : friendList) {
            Image friendMiniImage = new Image();
            friendMiniImage.setHeight(55, Unit.PIXELS);
            friendMiniImage.setWidth(55, Unit.PIXELS);
            friendMiniImage.setSource(new ExternalResource(singleFriend.getProfileAvatar()));
            friendMiniImage.setDescription(singleFriend.getProfileName() + " " + singleFriend.getProfileSurname());
            friendMiniImage.addClickListener((MouseEvents.ClickListener) clickEvent ->
                    ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(singleFriend.getObjectId())));
            friendsGrid.addComponent(friendMiniImage);
        }
        friendsLayout.addComponents(profileFriends, friendsGrid);
        friendsPanel.setContent(friendsLayout);

        //Elements for right part
        Panel nameAndBalancePanel = new Panel();
        VerticalLayout nameAndBalanceLayout = new VerticalLayout();
        nameAndBalanceLayout.addComponents(
                new Label(profile.getProfileName() + " " + profile.getProfileSurname(), ContentMode.PREFORMATTED),
                new Label("Balance: " + profile.getProfileCurrencyBalance())
        );
        nameAndBalancePanel.setContent(nameAndBalanceLayout);

        Panel simpleInfoPanel = new Panel();
        VerticalLayout simpleInfoLayout = new VerticalLayout();
        HorizontalLayout interestsLayout = new HorizontalLayout();
        interestsLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        interestsLayout.addComponent(new Label("Interests: "));
        for (String singleInterest : profile.getProfileHobbies()) {
            interestsLayout.addComponent(new Button(singleInterest));
        }
        HorizontalLayout favBreedsLayout = new HorizontalLayout();
        favBreedsLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        favBreedsLayout.addComponent(new Label("Favourite breeds: "));
        for (String singleFavBreed : profile.getProfileFavouriteBreeds()) {
            favBreedsLayout.addComponent(new Button(singleFavBreed));
        }
        simpleInfoLayout.addComponents(
                new Label("Age: " + profile.getProfileAge()),
                interestsLayout,
                favBreedsLayout
        );
        simpleInfoPanel.setContent(simpleInfoLayout);

        Panel photosPanel = new Panel();
        photosPanel.setWidth("100%");
        VerticalLayout photosLayout = new VerticalLayout();
        int photosColumns = 5, photosRows = 3;
        GridLayout photosGrid = new GridLayout(photosColumns, photosRows);
        photosGrid.setSpacing(true);
        Resource photoResource = new ExternalResource("https://image.ibb.co/d9Oc9b/2004039.png");
        Image photosImage = new Image();
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

        //Profile wall with records
        Panel wallPanel = new Panel();
        wallPanel.setWidth("100%");
        VerticalLayout wallLayout = new VerticalLayout();
        HorizontalLayout wallHeaderLayout = new HorizontalLayout();
        VerticalLayout newWallRecordLayout = new VerticalLayout();
        newWallRecordLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        Button addNewWallRecordButton = new Button("Add new record", VaadinIcons.PLUS);
        addNewWallRecordButton.setWidth(180, Unit.PIXELS);
        addNewWallRecordButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                getNewWallRecord();
                UI.getCurrent().addWindow(newWallRecordWindow);
            }
        });
        wallHeaderLayout.addComponents(
                new Label("Records on " + profile.getProfileName() + "`s wall:"),
                /*addWallRecordPopupView*/ addNewWallRecordButton);
        wallHeaderLayout.setComponentAlignment(addNewWallRecordButton, Alignment.MIDDLE_RIGHT);
        wallLayout.addComponent(wallHeaderLayout);

        for (int i = wallRecordsList.size(); i > 0; i--) {
            WallRecord currentWallRecord = wallRecordsList.get(i-1);
            Profile currentWallRecordAuthor = CustomRestTemplate.getInstance().
                    customGetForObject("/wallrecords/author/" + currentWallRecord.getObjectId(), Profile.class);
            Panel singleWallRecordPanel = new Panel();
            VerticalLayout singleWallRecordLayout = new VerticalLayout();

            HorizontalLayout recordInfoLayout = new HorizontalLayout();
            Image recordAuthorAvatar = new Image();
            recordAuthorAvatar.setSource(new ExternalResource(currentWallRecordAuthor.getProfileAvatar()));
            recordAuthorAvatar.setDescription(currentWallRecordAuthor.getProfileName() + " " +
                    currentWallRecordAuthor.getProfileSurname());
            recordAuthorAvatar.setHeight(100, Unit.PIXELS);
            recordAuthorAvatar.setWidth(100, Unit.PIXELS);
            Label recordName = new Label("Record from " + recordAuthorAvatar.getDescription());
            //recordName.setWidth(400, Unit.PIXELS);
            String recordDateString;
            try {
                recordDateString = currentWallRecord.getRecordDate().toString();
            } catch (NullPointerException e) {
                recordDateString = "null!";
            }
            Label recordDate = new Label(recordDateString);
            recordInfoLayout.addComponentsAndExpand(recordAuthorAvatar, recordName, recordDate);
            recordInfoLayout.setComponentAlignment(recordName, Alignment.TOP_CENTER);
            recordInfoLayout.setComponentAlignment(recordDate, Alignment.TOP_RIGHT);

            HorizontalLayout recordLikesLayout = new HorizontalLayout();
            Button likeRecordButton = new Button(String.valueOf((int) ((Math.random() - 0.3) * 100 / 1)), VaadinIcons.THUMBS_UP);
            likeRecordButton.setWidth(50, Unit.PIXELS);
            Button showRecordCommentsButton = new Button(
                    String.valueOf((int) (Math.random() * 10 / 1)) + " comments:",
                    VaadinIcons.COMMENT);
            showRecordCommentsButton.setWidth(100, Unit.PIXELS);
            recordLikesLayout.addComponentsAndExpand(
                    likeRecordButton,
                    showRecordCommentsButton);
            recordLikesLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

            Panel allCommentsPanel = new Panel();
            VerticalLayout allCommentsLayout = new VerticalLayout();
/*            List<WallRecordComment> allCommentsList = Arrays.asList(CustomRestTemplate.getInstance().customGetForObject(
                    "/wallrecords/comments/" + currentWallRecord.getObjectId(), WallRecordComment[].class));
            allCommentsList.sort(Comparator.comparing(WallRecordComment::getCommentDate));
            for (int j = allCommentsList.size(); j > 0; j--) {
                WallRecordComment currentWallRecordComment = allCommentsList.get(j - 1);
                Profile currentWallRecordCommentAuthor = CustomRestTemplate.getInstance().
                        customGetForObject("/wallrecords/comments/author/" + currentWallRecordComment.getObjectId(), Profile.class);
                allCommentsLayout.addComponentsAndExpand(new Label(currentWallRecordComment.getCommentText()));
            }*/
            allCommentsPanel.setContent(allCommentsLayout);

            singleWallRecordLayout.addComponents(
                    recordInfoLayout,
                    new Label(currentWallRecord.getRecordText(), ContentMode.PREFORMATTED),
                    recordLikesLayout,
                    allCommentsPanel);
            singleWallRecordPanel.setContent(singleWallRecordLayout);
            wallLayout.addComponent(singleWallRecordPanel);
        }
        wallPanel.setContent(wallLayout);

        //Filling matryoshka layout
        leftPartLayout.addComponents(avatarImage, addToFriendsButton, petsPanel, friendsPanel);
        rightPartLayout.addComponents(nameAndBalancePanel, simpleInfoPanel, photosPanel, wallPanel);
        leftPartPanel.setContent(leftPartLayout);
        rightPartPanel.setContent(rightPartLayout);
        mainLayout.addComponent(leftPartPanel);
        mainLayout.addComponentsAndExpand(rightPartPanel);
        addComponents(mainLayout);
    }

    private void getNewWallRecord() {
        newWallRecordWindow = new Window();
        newWallRecordWindow.setWidth("400px");
        newWallRecordWindow.setHeight("250px");
        newWallRecordWindow.setCaption("Creating new wall record:");
        newWallRecordWindow.setModal(true);
        VerticalLayout windowContent = new VerticalLayout();
        HorizontalLayout addPhotoRecordButtonsLayout = new HorizontalLayout();

        TextArea wallRecordText = new TextArea();
        wallRecordText.setWidth("100%");

        Button addWallRecordButton = new Button("Add record");
        addWallRecordButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                addWallRecordButton.setComponentError(null);
                WallRecord newWallRecord = new WallRecord();
                newWallRecord.setRecordDate(Timestamp.valueOf(
                        new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()));
                newWallRecord.setRecordText(wallRecordText.getValue());
                //recordAuthor = getCurrentUser.getProfile(); 21 is stub
                newWallRecord.setRecordAuthor(CustomRestTemplate.getInstance().
                        customGetForObject("/profile/" + 21, Profile.class));
                //newWallRecord.setRecordAuthor(profile);
                createWallRecord(newWallRecord);
                Notification.show("Wall record added successfully!");
                newWallRecordWindow.close();
                ((StubVaadinUI)UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(profileId));
            }
        });

        Button cancelAddingNewWallRecord = new Button("Cancel", click -> newWallRecordWindow.close());
        addPhotoRecordButtonsLayout.addComponentsAndExpand(addWallRecordButton, cancelAddingNewWallRecord);
        windowContent.addComponents(wallRecordText, addPhotoRecordButtonsLayout);

        newWallRecordWindow.setContent(windowContent);
        newWallRecordWindow.center();
    }

    private void createWallRecord(WallRecord wallRecord) {
        HttpEntity<WallRecord> request = new HttpEntity<>(wallRecord);
        CustomRestTemplate.getInstance().customPostForObject("/wallrecords/add", request, WallRecord.class);
    }
}
