package com.netcracker.ui.profile;

import com.netcracker.asserts.PetDataAssert;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.comment.AbstractComment;
import com.netcracker.model.comment.WallRecordComment;
import com.netcracker.model.like.AbstractLike;
import com.netcracker.model.like.CommentLike;
import com.netcracker.model.like.RecordLike;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.record.AbstractRecord;
import com.netcracker.model.record.WallRecord;
import com.netcracker.model.user.Profile;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.StubVaadinUI;
import com.netcracker.ui.friendlist.FriendListUI;
import com.netcracker.ui.messages.NewMessageWindowUI;
import com.netcracker.ui.pet.MyPetsListUI;
import com.netcracker.ui.pet.PetPageUI;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.event.MouseEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.io.File;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.*;

public class ProfileView extends VerticalLayout {
    private static final Resource likeOn = VaadinIcons.THUMBS_UP;
    private static final Resource likeOff = VaadinIcons.THUMBS_UP_O;
    private static final Resource dislikeOn = VaadinIcons.THUMBS_DOWN;
    private static final Resource dislikeOff = VaadinIcons.THUMBS_DOWN_O;
    private final Profile profile;
    private final BigInteger profileId;
    private final List<Profile> friendList;
    private final List<Pet> petList;
    private final List<WallRecord> wallRecordsList;
    private Window updateWallRecordWindow;
    private Window updateCommentWindow;
    private Window confirmDeleteWindow;
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
        String profileAvatar = profile.getProfileAvatar();
        avatarImage.setSource(new ExternalResource(profileAvatar == null ? stubAvatar : profileAvatar));
        avatarImage.setDescription("Profile avatar");
        Button addToFriendsButton = new Button("Add friend", VaadinIcons.SMILEY_O);
        addToFriendsButton.setHeight(50, Unit.PIXELS);
        addToFriendsButton.setWidth("100%");

        Button sendDirectMessage = new Button();
        if (!currentProfileId.equals(profileId)) {
            sendDirectMessage = new Button("Send message", VaadinIcons.ENVELOPE_O);
            sendDirectMessage.setHeight(50, Unit.PIXELS);
            sendDirectMessage.setWidth("100%");

            sendDirectMessage.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    NewMessageWindowUI sub = new NewMessageWindowUI(currentProfileId/*SENDER*/, profileId/*RECEIVER*/);
                    UI.getCurrent().addWindow(sub);
                }
            });
        }

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
                if (petAvatar != null) {
                    if (PetDataAssert.isAvatarURL(petAvatar))
                        petMiniImage.setSource(new ExternalResource(petAvatar));
                    else
                        petMiniImage.setSource(new FileResource(new File(petAvatar)));
                } else
                    petMiniImage = PageElements.getNoImage();
                petMiniImage.setHeight(55, Unit.PIXELS);
                petMiniImage.setWidth(55, Unit.PIXELS);
                petMiniImage.setSource(new ExternalResource(singlePet.getPetAvatar()));
                petMiniImage.setDescription(singlePet.getPetName());
                petMiniImage.addClickListener((MouseEvents.ClickListener) clickEvent ->
                        ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new PetPageUI(singlePet.getObjectId())));
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
                ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new FriendListUI(profileId));
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
                friendMiniImage.setSource(new ExternalResource(singleFriendAvatar == null ? stubAvatar : singleFriendAvatar));
                friendMiniImage.setDescription(singleFriend.getProfileName() + " " + singleFriend.getProfileSurname());
                friendMiniImage.addClickListener((MouseEvents.ClickListener) clickEvent ->
                        ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(singleFriend.getObjectId())));
                friendsGrid.addComponent(friendMiniImage);
            }
        } else {
            friendsGrid = new GridLayout(1, 1);
            friendsGrid.addComponent(new Label("No friends yet ;("));
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
        List<String> profileHobbies = profile.getProfileHobbies();
        if (profileHobbies == null) {
            interestsLayout.addComponent(new Label("not specified."));
        } else {
            for (String singleInterest : profile.getProfileHobbies()) {
                interestsLayout.addComponent(new Button(singleInterest));
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
                favBreedsLayout.addComponent(new Button(singleFavBreed));
            }
        }
        simpleInfoLayout.addComponents(
                new Label("Age: " + (profile.getProfileAge() == null ? "not specified." : profile.getProfileAge())),
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
        VerticalLayout addWallRecordLayout = new VerticalLayout();
        addWallRecordLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        Button addWallRecordButton = new Button("Add new record", VaadinIcons.PLUS);
        addWallRecordButton.setWidth(180, Unit.PIXELS);
        addWallRecordButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                editWallRecord(null);
                UI.getCurrent().addWindow(updateWallRecordWindow);
            }
        });
        int wallRecordsListSize = wallRecordsList.size();
        String labelCaptionStart = wallRecordsListSize + " record";
        if (wallRecordsListSize != 1) {
            labelCaptionStart += "s";
        }
        wallHeaderLayout.addComponents(
                new Label(labelCaptionStart + " on " + profile.getProfileName() + "`s wall:"), addWallRecordButton);
        wallHeaderLayout.setComponentAlignment(addWallRecordButton, Alignment.MIDDLE_RIGHT);
        wallLayout.addComponent(wallHeaderLayout);
        //WallRecords
        for (int i = wallRecordsListSize; i > 0; i--) {
            WallRecord currentWallRecord = wallRecordsList.get(i - 1);
            Profile commentator = CustomRestTemplate.getInstance().
                    customGetForObject("/wallrecords/author/" + currentWallRecord.getObjectId(), Profile.class);
            Panel singleWallRecordPanel = new Panel();
            VerticalLayout singleWallRecordLayout = new VerticalLayout();

            HorizontalLayout recordInfoLayout = new HorizontalLayout();
            String commentatorAvatar = commentator.getProfileAvatar();
            Image recordAuthorAvatar = new Image();
            recordAuthorAvatar.setSource(new ExternalResource(commentatorAvatar == null ? stubAvatar : commentatorAvatar));
            recordAuthorAvatar.setDescription(commentator.getProfileName()
                    + " " + commentator.getProfileSurname());
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
            recordInfoLayout.addComponent(recordAuthorAvatar);
            recordInfoLayout.addComponentsAndExpand(recordName, recordDate);
            recordInfoLayout.setComponentAlignment(recordName, Alignment.TOP_CENTER);
            recordInfoLayout.setComponentAlignment(recordDate, Alignment.TOP_RIGHT);
            HorizontalLayout recordLikeAndCommentsLayout = new HorizontalLayout();
            Button likeRecordButton = new Button();
            likeRecordButton.setWidth(70, Unit.PIXELS);
            Button dislikeRecordButton = new Button();
            dislikeRecordButton.setWidth(70, Unit.PIXELS);
            likeRecordButton.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    checkAndAddLike(currentWallRecord, false, likeRecordButton, dislikeRecordButton);
                }
            });
            dislikeRecordButton.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    checkAndAddLike(currentWallRecord, true, likeRecordButton, dislikeRecordButton);
                }
            });
            Button addCommentButton = new Button("Add new comment", VaadinIcons.PLUS);
            addCommentButton.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    editWallRecordComment(null, currentWallRecord);
                    UI.getCurrent().addWindow(updateCommentWindow);
                }
            });
            countAndShowLikes(currentWallRecord, likeRecordButton, dislikeRecordButton);
            recordLikeAndCommentsLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
            recordLikeAndCommentsLayout.addComponents(likeRecordButton, dislikeRecordButton);

            //Comments
            Panel allCommentsPanel = new Panel();
            allCommentsPanel.setVisible(false);
            List<WallRecordComment> commentsList = getWallRecordComments(currentWallRecord.getObjectId());
            int commentsListSize = commentsList.size();
            if (commentsListSize == 0) {
                recordLikeAndCommentsLayout.addComponent(addCommentButton);
            } else {
                String buttonCaption = "Show/hide " + commentsListSize + " comment";
                if (commentsListSize != 1) {
                    buttonCaption += "s";
                }
                Button showCommentsButton = new Button(buttonCaption, VaadinIcons.COMMENT_O);
                showCommentsButton.addClickListener(new AbstractClickListener() {
                    @Override
                    public void buttonClickListener() {
                        VerticalLayout allCommentsLayout = new VerticalLayout();
                        for (int j = commentsListSize; j > 0; j--) {
                            WallRecordComment currentComment = commentsList.get(j - 1);
                            Panel currentCommentPanel = new Panel();
                            VerticalLayout currentCommentLayout = new VerticalLayout();
                            HorizontalLayout commentDateAndAvatarLayout = new HorizontalLayout();
                            Profile currentCommentAuthor = CustomRestTemplate.getInstance().
                                    customGetForObject("/comments/author/" + currentComment.getObjectId(), Profile.class);
                            String authorNameAndSurname = currentCommentAuthor.getProfileName() + " " + currentCommentAuthor.getProfileSurname();
                            String singleFriendAvatar = currentCommentAuthor.getProfileAvatar();
                            Image commentatorMiniImage = new Image();
                            commentatorMiniImage.setHeight(55, Unit.PIXELS);
                            commentatorMiniImage.setWidth(55, Unit.PIXELS);
                            commentatorMiniImage.setSource(new ExternalResource(singleFriendAvatar == null ? stubAvatar : singleFriendAvatar));
                            commentatorMiniImage.setDescription(authorNameAndSurname);
                            commentatorMiniImage.addClickListener((MouseEvents.ClickListener) clickEvent ->
                                    ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(currentCommentAuthor.getObjectId())));
                            commentDateAndAvatarLayout.addComponents(
                                    commentatorMiniImage,
                                    new Label("Comment from " + authorNameAndSurname),
                                    new Label(currentComment.getCommentDate().toString())
                            );
                            HorizontalLayout commentLikeLayout = new HorizontalLayout();
                            Button likeCommentButton = new Button();
                            likeCommentButton.setWidth(70, Unit.PIXELS);
                            Button dislikeCommentButton = new Button();
                            likeCommentButton.setWidth(70, Unit.PIXELS);
                            likeCommentButton.addClickListener(new AbstractClickListener() {
                                @Override
                                public void buttonClickListener() {
                                    checkAndAddLike(currentComment, false, likeCommentButton, dislikeCommentButton);
                                }
                            });
                            dislikeCommentButton.addClickListener(new AbstractClickListener() {
                                @Override
                                public void buttonClickListener() {
                                    checkAndAddLike(currentComment, true, likeCommentButton, dislikeCommentButton);
                                }
                            });
                            countAndShowLikes(currentComment, likeCommentButton, dislikeCommentButton);
                            commentLikeLayout.addComponents(likeCommentButton, dislikeCommentButton);
                            if (currentComment.getCommentAuthor().getObjectId().equals(currentProfileId)) {
                                Button editCommentButton = new Button("Edit", VaadinIcons.EDIT);
                                editCommentButton.setWidth(100, Unit.PIXELS);
                                editCommentButton.addClickListener(new AbstractClickListener() {
                                    @Override
                                    public void buttonClickListener() {
                                        editWallRecordComment(currentComment, currentWallRecord);
                                        UI.getCurrent().addWindow(updateCommentWindow);
                                    }
                                });
                                Button deleteWallRecordCommentButton = new Button("Delete", VaadinIcons.DEL_A);
                                deleteWallRecordCommentButton.addClickListener(new AbstractClickListener() {
                                    @Override
                                    public void buttonClickListener() {
                                        confirmDelete(currentComment, currentCommentPanel);
                                        UI.getCurrent().addWindow(confirmDeleteWindow);
                                    }
                                });
                                commentLikeLayout.addComponents(editCommentButton, deleteWallRecordCommentButton);
                            }
                            currentCommentLayout.addComponents(
                                    commentDateAndAvatarLayout,
                                    new Label(currentComment.getCommentText()),
                                    commentLikeLayout
                            );
                            currentCommentPanel.setContent(currentCommentLayout);
                            allCommentsLayout.addComponent(currentCommentPanel);
                        }
                        allCommentsLayout.addComponent(addCommentButton);
                        allCommentsPanel.setContent(allCommentsLayout);
                        allCommentsPanel.setVisible(!allCommentsPanel.isVisible());
                        showCommentsButton.setIcon(
                                showCommentsButton.getIcon().equals(VaadinIcons.COMMENT_O) ?
                                        VaadinIcons.COMMENT : VaadinIcons.COMMENT_O);
                        addCommentButton.focus();
                    }
                });

                recordLikeAndCommentsLayout.addComponent(showCommentsButton);
            }
            if(currentWallRecord.getRecordAuthor().getObjectId().equals(currentProfileId)){
                Button editWallRecordButton = new Button("Edit", VaadinIcons.EDIT);
                editWallRecordButton.addClickListener(new AbstractClickListener() {
                    @Override
                    public void buttonClickListener() {
                        editWallRecord(currentWallRecord);
                        UI.getCurrent().addWindow(updateWallRecordWindow);
                    }
                });
                Button deleteWallRecordButton = new Button("Delete", VaadinIcons.DEL);
                deleteWallRecordButton.addClickListener(new AbstractClickListener() {
                    @Override
                    public void buttonClickListener() {
                        confirmDelete(currentWallRecord, singleWallRecordPanel);
                        UI.getCurrent().addWindow(confirmDeleteWindow);
                    }
                });
                recordLikeAndCommentsLayout.addComponents(editWallRecordButton, deleteWallRecordButton);
            }
            singleWallRecordLayout.addComponents(
                    recordInfoLayout,
                    new Label(currentWallRecord.getRecordText(), ContentMode.PREFORMATTED),
                    recordLikeAndCommentsLayout,
                    allCommentsPanel);
            singleWallRecordPanel.setContent(singleWallRecordLayout);
            wallLayout.addComponent(singleWallRecordPanel);
        }
        wallPanel.setContent(wallLayout);

        //Filling matryoshka layout
        if (!currentProfileId.equals(profileId))
            leftPartLayout.addComponents(avatarImage, addToFriendsButton, sendDirectMessage, petsPanel, friendsPanel);
        else
            leftPartLayout.addComponents(avatarImage, addToFriendsButton, petsPanel, friendsPanel);
        rightPartLayout.addComponents(nameAndBalancePanel, simpleInfoPanel, photosPanel, wallPanel);
        leftPartPanel.setContent(leftPartLayout);
        rightPartPanel.setContent(rightPartLayout);
        mainLayout.addComponent(leftPartPanel);
        mainLayout.addComponentsAndExpand(rightPartPanel);
        addComponents(mainLayout);
    }

    private void countAndShowLikes(BaseEntity entity, Button likeRecordButton, Button dislikeRecordButton) {
        List<AbstractLike> likesList = null;
        Class c = entity.getClass();
        if (AbstractRecord.class.isAssignableFrom(c)) {
            likesList = getWallRecordLikes(entity.getObjectId());
        } else if (AbstractComment.class.isAssignableFrom(c)) {
            likesList = getCommentLikes(entity.getObjectId());
        }
        likeRecordButton.setIcon(likeOff);
        dislikeRecordButton.setIcon(dislikeOff);
        int dislikesCount = 0;
        boolean ownLikeFound = false;
        for (AbstractLike rc : likesList) {
            if (rc.getIsDislike().equals("true")) {
                dislikesCount++;
            }
            if (!ownLikeFound) {
                if (rc.getParentId().equals(currentProfileId)) {
                    ownLikeFound = true;
                    if (rc.getIsDislike().equals("true")) {
                        dislikeRecordButton.setIcon(dislikeOn);
                    } else {
                        likeRecordButton.setIcon(likeOn);
                    }
                }
            }
        }
        likeRecordButton.setCaption(String.valueOf(likesList.size() - dislikesCount));
        dislikeRecordButton.setCaption(String.valueOf(dislikesCount));
    }

    private void checkAndAddLike(BaseEntity entity, boolean isDislike, Button likeRecordButton, Button dislikeRecordButton) {
        List<AbstractLike> likesList = null;
        Class c = entity.getClass();
        if (AbstractRecord.class.isAssignableFrom(c)) {
            likesList = getWallRecordLikes(entity.getObjectId());
        } else if (AbstractComment.class.isAssignableFrom(c)) {
            likesList = getCommentLikes(entity.getObjectId());
        }
        outer:
        {
            for (AbstractLike al : likesList) {
                boolean isCurrentDislike = Boolean.valueOf(al.getIsDislike());
                if (al.getParentId().equals(currentProfileId)) {
                    deleteLike(al.getObjectId());
                    changeButtons(isCurrentDislike, likeRecordButton, dislikeRecordButton);
                    if (isCurrentDislike != isDislike) {
                        addLike(entity, isDislike);
                        changeButtons(isDislike, likeRecordButton, dislikeRecordButton);
                        break outer;
                    }
                    break outer;
                }
            }
            addLike(entity, isDislike);
            changeButtons(isDislike, likeRecordButton, dislikeRecordButton);
        }
    }

    private void changeButtons(boolean isDislike, Button likeRecordButton, Button dislikeRecordButton) {
        if (isDislike) {
            reverseButton(dislikeRecordButton);
        } else {
            reverseButton(likeRecordButton);
        }
    }

    private void reverseButton(Button button) {
        Resource icon = button.getIcon();
        int count = Integer.parseInt(button.getCaption());
        button.setIcon(
                icon.equals(likeOn) ?
                        likeOff : icon.equals(likeOff) ?
                            likeOn : icon.equals(dislikeOn) ?
                                dislikeOff : icon.equals(dislikeOff) ?
                                    dislikeOn : dislikeOff
        );
        button.setCaption(String.valueOf((icon.equals(likeOn) || icon.equals(dislikeOn)) ? --count : ++count));
    }

    private List<WallRecord> getWallRecords(BigInteger profileId) {
        List<WallRecord> result = Arrays.asList(CustomRestTemplate.getInstance().
                customGetForObject("/wallrecords/" + profileId, WallRecord[].class));
        result.sort(Comparator.comparing(AbstractRecord::getRecordDate));
        return result;
    }

    private List<WallRecordComment> getWallRecordComments(BigInteger wallRecordID) {
        List<WallRecordComment> result = Arrays.asList(CustomRestTemplate.getInstance().customGetForObject(
                "/comments/" + wallRecordID, WallRecordComment[].class));
        result.sort(Comparator.comparing(WallRecordComment::getCommentDate));
        return result;
    }

    private List<AbstractLike> getWallRecordLikes(BigInteger wallRecordID) {
        return Arrays.asList(CustomRestTemplate.getInstance().customGetForObject(
                "/likes/record/" + wallRecordID, AbstractLike[].class));
    }

    private List<AbstractLike> getCommentLikes(BigInteger commentID) {
        return Arrays.asList(CustomRestTemplate.getInstance().customGetForObject(
                "/likes/comment/" + commentID, AbstractLike[].class));
    }

    private void addLike(BaseEntity entity, boolean isDislike) {
        Class c = entity.getClass();
        if (AbstractRecord.class.isAssignableFrom(c)) {
            addWallRecordLike((AbstractRecord) entity, isDislike);
        } else if (AbstractComment.class.isAssignableFrom(c)) {
            addCommentLike((AbstractComment) entity, isDislike);
        }
    }

    private void editWallRecord(WallRecord currentRecord) {
        updateWallRecordWindow = new Window();
        updateWallRecordWindow.setWidth("400px");
        updateWallRecordWindow.setHeight("250px");
        updateWallRecordWindow.setModal(true);
        VerticalLayout windowContent = new VerticalLayout();
        HorizontalLayout buttonsLayout = new HorizontalLayout();

        if(currentRecord == null) {
            updateWallRecordWindow.setCaption("New record");
            TextArea text = new TextArea();
            text.setWidth("100%");
            Button add = new Button("Add");
            add.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    add.setComponentError(null);
                    WallRecord record = new WallRecord("WallRecord",
                            "From " + currentProfile.getProfileSurname() + " to " + profile.getProfileSurname());
                    record.setRecordDate(Timestamp.valueOf(
                            new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()));
                    record.setRecordText(text.getValue());
                    record.setRecordAuthor(currentProfile);
                    record.setWallOwner(profile);
                    createWallRecord(record);
                    Notification.show("Record added!");
                    updateWallRecordWindow.close();
                    ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(profileId));
                }
            });
            buttonsLayout.addComponentsAndExpand(add);
            windowContent.addComponent(text);
        } else {
            updateWallRecordWindow.setCaption("Edit record");
            TextArea text = new TextArea();
            text.setWidth("100%");
            text.setValue(currentRecord.getRecordText());
            Button edit = new Button("Edit");
            edit.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    edit.setComponentError(null);
                    currentRecord.setRecordText(text.getValue());
                    updateWallRecord(currentRecord);
                    Notification.show("Record edited!");
                    updateWallRecordWindow.close();
                    ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(profileId));
                }
            });
            buttonsLayout.addComponentsAndExpand(edit);
            windowContent.addComponent(text);
        }
        Button cancel = new Button("Cancel", click -> updateWallRecordWindow.close());
        buttonsLayout.addComponentsAndExpand(cancel);
        windowContent.addComponent(buttonsLayout);

        updateWallRecordWindow.setContent(windowContent);
        updateWallRecordWindow.center();
    }

    private void editWallRecordComment(WallRecordComment currentComment, WallRecord currentWallRecord) {
        updateCommentWindow = new Window();
        updateCommentWindow.setWidth("400px");
        updateCommentWindow.setHeight("250px");
        updateCommentWindow.setModal(true);
        VerticalLayout windowContent = new VerticalLayout();
        HorizontalLayout buttonsLayout = new HorizontalLayout();

        if(currentComment == null) {
            updateCommentWindow.setCaption("New comment");
            TextArea text = new TextArea();
            text.setWidth("100%");
            Button add = new Button("Add");
            add.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    add.setComponentError(null);
                    WallRecordComment comment = new WallRecordComment("WallRecordComment",
                            "From " + currentProfile.getProfileSurname() + " to wall record№" + currentWallRecord.getObjectId());
                    comment.setCommentDate(Timestamp.valueOf(
                            new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()));
                    comment.setCommentText(text.getValue());
                    comment.setCommentAuthor(currentProfile);
                    comment.setCommentedWallRecord(currentWallRecord);
                    createWallRecordComment(comment);
                    Notification.show("Comment added!");
                    updateCommentWindow.close();
                    ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(profileId));
                }
            });
            buttonsLayout.addComponentsAndExpand(add);
            windowContent.addComponent(text);
        } else {
            updateCommentWindow.setCaption("Edit comment");
            TextArea text = new TextArea();
            text.setWidth("100%");
            text.setValue(currentComment.getCommentText());
            Button edit = new Button("Edit");
            edit.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    edit.setComponentError(null);
                    currentComment.setCommentText(text.getValue());
                    updateWallRecordComment(currentComment);
                    Notification.show("Comment edited!");
                    updateCommentWindow.close();
                    ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(profileId));
                }
            });
            buttonsLayout.addComponentsAndExpand(edit);
            windowContent.addComponent(text);
        }
        Button cancel = new Button("Cancel", click -> updateCommentWindow.close());
        buttonsLayout.addComponentsAndExpand(cancel);
        windowContent.addComponent(buttonsLayout);

        updateCommentWindow.setContent(windowContent);
        updateCommentWindow.center();
    }

    private void addWallRecordLike(AbstractRecord currentRecord, boolean isDislike) {
        RecordLike like = new RecordLike(isDislike ? "Dislike" : "Like",
                "From " + currentProfile.getProfileSurname() + " to wall record№" + currentRecord.getObjectId());
        like.setIsDislike(Boolean.toString(isDislike));
        like.setParentId(currentProfile.getObjectId());
        like.setLikedRecord(currentRecord);
        createWallRecordLike(like);
    }

    private void addCommentLike(AbstractComment currentComment, boolean isDislike) {
        CommentLike like = new CommentLike(isDislike ? "Dislike" : "Like",
                "From " + currentProfile.getProfileSurname() + " to wall record№" + currentComment.getObjectId());
        like.setIsDislike(Boolean.toString(isDislike));
        like.setParentId(currentProfile.getObjectId());
        like.setLikedDislikedComment(currentComment);
        createCommentLike(like);
    }

    private void confirmDelete(BaseEntity entity, Panel currentPanel){
        confirmDeleteWindow = new Window();
        confirmDeleteWindow.setModal(true);
        VerticalLayout windowContent = new VerticalLayout();
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        Button delete = new Button("Delete");
        delete.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                Class c = entity.getClass();
                if (AbstractRecord.class.isAssignableFrom(c)) {
                    deleteWallRecord((WallRecord) entity);
                    Notification.show("Record deleted!");
                } else if (AbstractComment.class.isAssignableFrom(c)) {
                    deleteWallRecordComment((WallRecordComment) entity);
                    Notification.show("Comment deleted!");
                }
                ComponentContainer parent = (ComponentContainer) currentPanel.getParent();
                parent.removeComponent(currentPanel);
                confirmDeleteWindow.close();
                ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(profileId));
            }
        });

        Button cancel = new Button("Cancel", click -> confirmDeleteWindow.close());
        buttonsLayout.addComponentsAndExpand(delete, cancel);
        windowContent.addComponents(new Label("Are you sure?"), buttonsLayout);
        confirmDeleteWindow.setContent(windowContent);
        confirmDeleteWindow.center();
    }

    private void createWallRecord(WallRecord record) {
        HttpEntity<WallRecord> request = new HttpEntity<>(record);
        CustomRestTemplate.getInstance().customPostForObject("/wallrecords/add", request, WallRecord.class);
    }

    private void createWallRecordComment(WallRecordComment comment) {
        HttpEntity<WallRecordComment> request = new HttpEntity<>(comment);
        CustomRestTemplate.getInstance().customPostForObject("/comments/add", request, WallRecordComment.class);
    }

    private void createWallRecordLike(RecordLike like) {
        HttpEntity<RecordLike> request = new HttpEntity<>(like);
        CustomRestTemplate.getInstance().customPostForObject("/likes/record/add", request, RecordLike.class);
    }

    private void createCommentLike(CommentLike like) {
        HttpEntity<CommentLike> request = new HttpEntity<>(like);
        CustomRestTemplate.getInstance().customPostForObject("/likes/comment/add", request, CommentLike.class);
    }

    private void updateWallRecord(WallRecord record) {
        HttpEntity<WallRecord> request = new HttpEntity<>(record);
        CustomRestTemplate.getInstance().customPostForObject("/wallrecords/update", request, WallRecord.class);
    }

    private void updateWallRecordComment(WallRecordComment comment) {
        HttpEntity<WallRecordComment> request = new HttpEntity<>(comment);
        CustomRestTemplate.getInstance().customPostForObject("/comments/update", request, WallRecordComment.class);
    }

    private void deleteWallRecord(WallRecord record) {
        HttpEntity<WallRecord> request = new HttpEntity<>(record);
        CustomRestTemplate.getInstance().customPostForObject("/wallrecords/delete", request, WallRecord.class);
    }

    private void deleteWallRecordComment(WallRecordComment comment) {
        HttpEntity<WallRecordComment> request = new HttpEntity<>(comment);
        CustomRestTemplate.getInstance().customPostForObject("/comments/delete", request, WallRecordComment.class);
    }

    private void deleteLike(BigInteger likeID) {
        CustomRestTemplate.getInstance().customGetForObject("/likes/delete/" + likeID, Void.class);
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
