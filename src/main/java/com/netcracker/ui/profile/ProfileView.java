package com.netcracker.ui.profile;

import com.netcracker.model.pet.Pet;
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

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringComponent
@UIScope
public class ProfileView extends VerticalLayout {
    private final Profile profile;
    private final List<Profile> friendList;
    private final List<Pet> petList;

    @Autowired
    public ProfileView(BigInteger profileId) {
        super();
        setSpacing(true);
        setSizeFull();
        profile = CustomRestTemplate.getInstance().customGetForObject("/profile/" + profileId, Profile.class);
        friendList = Arrays.asList(CustomRestTemplate.getInstance().customGetForObject("/friends/" + profileId, Profile[].class));
        petList = Arrays.asList(CustomRestTemplate.getInstance().customGetForObject("/pets/" + profileId, Pet[].class));

        //Creating matryoshka layout
        HorizontalLayout mainLayout = new HorizontalLayout();
        Panel leftPartPanel = new Panel();
        leftPartPanel.setHeight("100%");
        leftPartPanel.setWidth(250, Unit.PIXELS);
        Panel rightPartPanel = new Panel();
        rightPartPanel.setHeight(750, Unit.PIXELS);
        rightPartPanel.setWidth(875, Unit.PIXELS);
        VerticalLayout leftPartLayout = new VerticalLayout();
        VerticalLayout rightPartLayout = new VerticalLayout();
        rightPartLayout.setSpacing(true);

        //Elements for left part
        Image avatarImage = new Image();
        avatarImage.setHeight(240, Unit.PIXELS);
        avatarImage.setWidth(240, Unit.PIXELS);
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
        photosLayout.addComponents(new Label("Last photos"), photosGrid);
        photosPanel.setContent(photosLayout);

        Panel wallPanel = new Panel();
        wallPanel.setWidth("100%");
        VerticalLayout wallLayout = new VerticalLayout();
        wallLayout.addComponent(new Label("Wall records"));
        int recordsCount = 5;
        //all values simulated!
        for (int i = 0; i < recordsCount; i++) {
            Panel singleWallRecordPanel = new Panel();
            VerticalLayout singleWallRecordLayout = new VerticalLayout();

            HorizontalLayout recordInfoLayout = new HorizontalLayout();
            Image recordAuthorAvatar = new Image();
            recordAuthorAvatar.setSource(new ExternalResource(profile.getProfileAvatar()));
            recordAuthorAvatar.setDescription(profile.getProfileName() + " " + profile.getProfileSurname());
            recordAuthorAvatar.setHeight(100, Unit.PIXELS);
            recordAuthorAvatar.setWidth(100, Unit.PIXELS);
            Label recordName = new Label("Record from " + profile.getProfileName() + " " + profile.getProfileSurname());
            recordName.setWidth(400, Unit.PIXELS);
            Label recordDate = new Label(new Date().toString());
            recordInfoLayout.addComponents(recordAuthorAvatar, recordName, recordDate);

            HorizontalLayout recordLikesLayout = new HorizontalLayout();
            Button likeRecordButton = new Button("Like", VaadinIcons.STAR);
            Label recordLikeCount = new Label(String.valueOf((int) ((Math.random() - 0.3) * 100 / 1)));
            Button commentRecordButton = new Button("Comment", VaadinIcons.COMMENT);
            Label recordCommentCount = new Label(String.valueOf((int) (Math.random() * 10 / 1)));
            Button showRecordCommentsButton = new Button("Show comments", VaadinIcons.COMMENT_O);
            recordLikesLayout.addComponents(likeRecordButton, recordLikeCount, commentRecordButton, recordCommentCount, showRecordCommentsButton);

            singleWallRecordLayout.addComponents(recordInfoLayout, new Label("Some\n useful\n info\n here", ContentMode.PREFORMATTED), recordLikesLayout);
            singleWallRecordPanel.setContent(singleWallRecordLayout);
            wallLayout.addComponent(singleWallRecordPanel);
        }
        wallPanel.setContent(wallLayout);

        //Filling matryoshka layout
        leftPartLayout.addComponents(avatarImage, addToFriendsButton, petsPanel, friendsPanel);
        rightPartLayout.addComponents(nameAndBalancePanel, simpleInfoPanel, photosPanel, wallPanel);
        leftPartPanel.setContent(leftPartLayout);
        rightPartPanel.setContent(rightPartLayout);
        mainLayout.addComponents(leftPartPanel, rightPartPanel);
        addComponents(mainLayout);
    }
}
