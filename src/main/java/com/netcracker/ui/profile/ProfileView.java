package com.netcracker.ui.profile;

import com.netcracker.model.user.Profile;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.util.Date;

@SpringComponent
@UIScope
public class ProfileView extends VerticalLayout {
    private final Profile profile;

    @Autowired
    public ProfileView(BigInteger profileId) {
        super();
        setSpacing(true);
        setSizeFull();
        profile = CustomRestTemplate.getInstance().customGetForObject("/profile/" + profileId, Profile.class);

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
        avatarImage.setHeight(250, Unit.PIXELS);
        avatarImage.setWidth(250, Unit.PIXELS);
        avatarImage.setSource(new ExternalResource(profile.getProfileAvatar()));
        avatarImage.setDescription("Profile avatar");
        Button addToFriendsButton = new Button("Add friend", VaadinIcons.SMILEY_O);
        addToFriendsButton.setHeight(50, Unit.PIXELS);
        addToFriendsButton.setWidth("100%");

        Panel petsPanel = new Panel();
        petsPanel.setWidth("100%");
        VerticalLayout petsLayout = new VerticalLayout();
        int columns = 3, rows = 2;
        GridLayout petsGrid = new GridLayout(columns, rows);
        for (int i = 0; i < columns * rows; i++) {
            petsGrid.addComponent(new Button("" + (i + 1), VaadinIcons.SHIFT));
        }
        petsLayout.addComponents(new Label("Pets"), petsGrid);
        petsPanel.setContent(petsLayout);

        Panel friendsPanel = new Panel();
        friendsPanel.setWidth("100%");
        VerticalLayout friendsLayout = new VerticalLayout();
        columns = 3;
        rows = 3;
        GridLayout friendsGrid = new GridLayout(columns, rows);
        for (int i = 0; i < columns * rows; i++) {
            friendsGrid.addComponent(new Button("" + (i + 1), VaadinIcons.USER_HEART));
        }
        friendsLayout.addComponents(new Label("Friends"), friendsGrid);
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
        simpleInfoLayout.addComponents(
                new Label("Age: " + profile.getProfileAge()),
                new Label("Interests: " + profile.getProfileHobbies()),
                new Label("Favourite breeds: " + profile.getProfileFavouriteBreeds())
        );
        simpleInfoPanel.setContent(simpleInfoLayout);

        Panel photosPanel = new Panel();
        photosPanel.setWidth("100%");
        VerticalLayout photosLayout = new VerticalLayout();
        columns = 5;
        rows = 3;
        GridLayout photosGrid = new GridLayout(columns, rows);
        photosGrid.setSpacing(true);
        Resource photoResource = new ExternalResource("https://image.ibb.co/d9Oc9b/2004039.png");
        Image photosImage = new Image();
        for (int i = 0; i < columns * rows; i++) {
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
        for(int i = 0; i < recordsCount; i++){
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
            Label recordLikeCount = new Label(String.valueOf((int)((Math.random() - 0.3) * 100 / 1)));
            Button commentRecordButton = new Button("Comment", VaadinIcons.COMMENT);
            Label recordCommentCount = new Label(String.valueOf((int)(Math.random() * 10 / 1)));
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
