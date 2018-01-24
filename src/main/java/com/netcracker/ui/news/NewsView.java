package com.netcracker.ui.news;

import com.netcracker.model.BaseEntity;
import com.netcracker.model.comment.AbstractComment;
import com.netcracker.model.comment.WallRecordComment;
import com.netcracker.model.like.AbstractLike;
import com.netcracker.model.like.CommentLike;
import com.netcracker.model.like.RecordLike;
import com.netcracker.model.record.AbstractRecord;
import com.netcracker.model.record.GroupRecord;
import com.netcracker.model.record.WallRecord;
import com.netcracker.model.user.Profile;
import com.netcracker.service.util.RestResponsePage;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.StubPagingBar;
import com.netcracker.ui.StubVaadinUI;
import com.netcracker.ui.profile.ProfileView;
import com.netcracker.ui.util.CustomRestTemplate;
import com.netcracker.ui.util.VaadinValidationBinder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.*;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class NewsView extends VerticalLayout {
    private static final Resource likeOn = VaadinIcons.THUMBS_UP;
    private static final Resource likeOff = VaadinIcons.THUMBS_UP_O;
    private static final Resource dislikeOn = VaadinIcons.THUMBS_DOWN;
    private static final Resource dislikeOff = VaadinIcons.THUMBS_DOWN_O;
    private final Profile profile;
    private final BigInteger profileId;
    private StubPagingBar pagingLayout;
    private Panel wallPanel;
    private RestResponsePage<WallRecord> wallRecordsList;
    private RestResponsePage<GroupRecord> groupRecordsList;
    private Window updateWallRecordWindow;
    private Window updateCommentWindow;
    private int browserHeight;
    private int browserWidth;
    private String stubAvatar = "https://goo.gl/6eEoWo";
    private boolean isFriendsNews = true;

    public NewsView(BigInteger profileID) {
        super();
        profileId = profileID;
        profile = CustomRestTemplate.getInstance().
                customGetForObject("/profile/" + profileId, Profile.class);
        //Creating matryoshka layout
        HorizontalLayout mainLayout = new HorizontalLayout();
        browserHeight = UI.getCurrent().getPage().getBrowserWindowHeight();
        browserWidth = UI.getCurrent().getPage().getBrowserWindowWidth();
        mainLayout.setHeight(browserHeight - 250, Sizeable.Unit.PIXELS);
        mainLayout.setWidth(browserWidth - 450, Sizeable.Unit.PIXELS);
        Panel leftPartPanel = new Panel();
        leftPartPanel.setHeight("100%");
        leftPartPanel.setWidth(250, Sizeable.Unit.PIXELS);
        Panel rightPartPanel = new Panel();
        rightPartPanel.setHeight("100%");

        VerticalLayout leftPartLayout = new VerticalLayout();
        leftPartLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        VerticalLayout rightPartLayout = new VerticalLayout();
        rightPartLayout.setSpacing(true);
        Panel pagingPanel = new Panel();

        //Elements for left part
        Button showNewsOfFriendsButton = new Button("Show news of friends", VaadinIcons.NEWSPAPER);
        showNewsOfFriendsButton.setHeight(50, Unit.PIXELS);
        showNewsOfFriendsButton.setWidth("100%");
        showNewsOfFriendsButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                isFriendsNews = true;
                getFriendsRecordsList(1);
                wallPanel.setContent(getWallRecordsLayout());
                getPagingLayout(pagingPanel, isFriendsNews);
            }
        });

        Button showNewsOfGroupsButton = new Button("Show news of groups", VaadinIcons.NEWSPAPER);
        showNewsOfGroupsButton.setHeight(50, Unit.PIXELS);
        showNewsOfGroupsButton.setWidth("100%");
        showNewsOfGroupsButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                isFriendsNews = false;
                getGroupsRecordsList(1);
                wallPanel.setContent(getGroupRecordsLayout());
                getPagingLayout(pagingPanel, isFriendsNews);
            }
        });
        //Elements for right part
        getFriendsRecordsList(1);
        //добавить проверку если пустой, то не добавлять
        getPagingLayout(pagingPanel, isFriendsNews); //вылетает нул поинтер


        //Profile wall with records
        wallPanel = new Panel();
        wallPanel.setWidth("100%");
        wallPanel.setContent(getWallRecordsLayout());

        //Filling matryoshka layout
        leftPartLayout.addComponents(showNewsOfFriendsButton, showNewsOfGroupsButton);

        rightPartLayout.addComponents(pagingPanel, wallPanel);
        leftPartPanel.setContent(leftPartLayout);
        rightPartPanel.setContent(rightPartLayout);
        mainLayout.addComponent(leftPartPanel);
        mainLayout.addComponentsAndExpand(rightPartPanel);
        addComponents(mainLayout);
    }

    private void getFriendsRecordsList(int pageNumber) {
        ResponseEntity<RestResponsePage<WallRecord>> pageResponseEntity =
                CustomRestTemplate.getInstance().customExchangeForParametrizedTypes("/news/friends/" + profileId + "/" + pageNumber, HttpMethod.GET,
                        null, new ParameterizedTypeReference<RestResponsePage<WallRecord>>() {
                        });
        wallRecordsList = pageResponseEntity.getBody();
        List<WallRecord> advertisements = wallRecordsList.getContent();
        if (advertisements.isEmpty()) {
            Notification.show("No news  were found");
        }
    }
    private void getGroupsRecordsList(int pageNumber){
        ResponseEntity<RestResponsePage<GroupRecord>> pageResponseEntity =
                CustomRestTemplate.getInstance().customExchangeForParametrizedTypes("/news/groups/" + profileId + "/" + pageNumber, HttpMethod.GET,
                        null, new ParameterizedTypeReference<RestResponsePage<GroupRecord>>() {
                        });
        groupRecordsList = pageResponseEntity.getBody();
        List<GroupRecord> advertisements = groupRecordsList.getContent();
        if (advertisements.isEmpty()) {
            Notification.show("No news  were found");
        }
        //ОБРАБОТКА РЕЗУЛЬТАТА
    }
    private void getData(int page, boolean isFriendsNews) {
        if(isFriendsNews){
            getFriendsRecordsList(page);
            wallPanel.setContent(getWallRecordsLayout());
        } else{
            getGroupsRecordsList(page);
            wallPanel.setContent(getGroupRecordsLayout());
        }
        ((TextField) pagingLayout.getComponent(3)).setValue(String.valueOf(page));
    }

    private void getPagingLayout(Panel pagingPanel, boolean isFriendsNews) {
        if (pagingLayout != null) {
            pagingPanel.setContent(null);
        }
        int pageCount;
        if(isFriendsNews){
            pageCount = (int) wallRecordsList.getTotalElements();
        } else {
            pageCount = (int) groupRecordsList.getTotalElements();
        }
        if (pageCount > 1) {
            pagingLayout = new StubPagingBar(pageCount);

            ((Button) pagingLayout.getComponent(0)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    Integer page = (Integer) ((Button) pagingLayout.getComponent(0)).getData();
                    getData(page, isFriendsNews);
                    pagingLayout.currentPageNumber = 1;
                }
            });
            ((Button) pagingLayout.getComponent(6)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    Integer page = (Integer) ((Button) pagingLayout.getComponent(6)).getData();
                    getData(page, isFriendsNews);
                    pagingLayout.currentPageNumber = page;
                }
            });
            ((Button) pagingLayout.getComponent(1)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    --pagingLayout.currentPageNumber;
                    if (pagingLayout.currentPageNumber < 1) {
                        pagingLayout.currentPageNumber = pageCount;
                    }
                    getData(pagingLayout.currentPageNumber, isFriendsNews);
                }
            });
            ((Button) pagingLayout.getComponent(5)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    ++pagingLayout.currentPageNumber;
                    if (pagingLayout.currentPageNumber > pageCount) {
                        pagingLayout.currentPageNumber = 1;
                    }
                    getData(pagingLayout.currentPageNumber, isFriendsNews);
                }
            });

            ((TextField) pagingLayout.getComponent(3)).addShortcutListener(new ShortcutListener("Enter", ShortcutAction.KeyCode.ENTER, null) {
                @Override
                public void handleAction(Object o, Object o1) {
                    BinderValidationStatus<VaadinValidationBinder> status = pagingLayout.pageNumberFieldBinder.validate();
                    if (!status.hasErrors()) {
                        pagingLayout.currentPageNumber = Integer.valueOf(((TextField) pagingLayout.getComponent(3)).getValue());
                        getData(pagingLayout.currentPageNumber, isFriendsNews);
                    }
                }
            });
            pagingPanel.setContent(pagingLayout);
        }
    }

    private VerticalLayout getWallRecordsLayout() {
        VerticalLayout wallLayout = new VerticalLayout();
        HorizontalLayout wallHeaderLayout = new HorizontalLayout();

        int wallRecordsListSize = wallRecordsList.getContent().size();
        String labelCaptionStart = wallRecordsListSize + " record";
        if (wallRecordsListSize != 1) {
            labelCaptionStart += "s";
        }
        wallHeaderLayout.addComponents(
                new Label(labelCaptionStart + " of your friends"));
        wallLayout.addComponent(wallHeaderLayout);
        //WallRecords
        for (int i = 0; i < wallRecordsList.getContent().size(); i++) {
            WallRecord currentWallRecord = wallRecordsList.getContent().get(i);
            List<WallRecordComment> commentsList = getRecordComments(currentWallRecord.getObjectId());
            Profile commentator = currentWallRecord.getRecordAuthor();
            Profile wallOwner = currentWallRecord.getWallOwner();

            Panel singleWallRecordPanel = new Panel();
            VerticalLayout singleWallRecordLayout = new VerticalLayout();

            HorizontalLayout recordInfoLayout = new HorizontalLayout();
            String commentatorAvatar = commentator.getProfileAvatar();
            Image recordAuthorAvatar = new Image();
            recordAuthorAvatar.setSource(new ExternalResource(commentatorAvatar == null ? stubAvatar : commentatorAvatar));
            String description = "from " + commentator.getProfileName() + " " + commentator.getProfileSurname() + " on ";
            if (commentator.equals(wallOwner)) {
                description += "his/her";
            } else {
                description += wallOwner.getProfileName() + " " + wallOwner.getProfileSurname();
            }
            description += " wall";
            recordAuthorAvatar.setDescription(description);
            recordAuthorAvatar.setHeight(100, Sizeable.Unit.PIXELS);
            recordAuthorAvatar.setWidth(100, Sizeable.Unit.PIXELS);
            Label recordName = new Label("Record " + recordAuthorAvatar.getDescription());

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
            likeRecordButton.setWidth(70, Sizeable.Unit.PIXELS);
            Button dislikeRecordButton = new Button();
            dislikeRecordButton.setWidth(70, Sizeable.Unit.PIXELS);
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
                    addComment(currentWallRecord);
                    UI.getCurrent().addWindow(updateCommentWindow);
                }
            });
            countAndShowLikes(currentWallRecord, likeRecordButton, dislikeRecordButton);
            recordLikeAndCommentsLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
            recordLikeAndCommentsLayout.addComponents(likeRecordButton, dislikeRecordButton);

            //Comments
            Panel allCommentsPanel = new Panel();
            allCommentsPanel.setVisible(true);
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
                            commentatorMiniImage.setHeight(55, Sizeable.Unit.PIXELS);
                            commentatorMiniImage.setWidth(55, Sizeable.Unit.PIXELS);
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
                            likeCommentButton.setWidth(70, Sizeable.Unit.PIXELS);
                            Button dislikeCommentButton = new Button();
                            likeCommentButton.setWidth(70, Sizeable.Unit.PIXELS);
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
                            if (currentComment.getCommentAuthor().getObjectId().equals(profileId)) {
                                Button editCommentButton = new Button("Edit", VaadinIcons.EDIT);
                                editCommentButton.setWidth(100, Sizeable.Unit.PIXELS);
                                editCommentButton.addClickListener(new AbstractClickListener() {
                                    @Override
                                    public void buttonClickListener() {
                                        editWallRecordComment(currentComment, currentWallRecord);
                                        UI.getCurrent().addWindow(updateCommentWindow);
                                    }
                                });
                                commentLikeLayout.addComponents(editCommentButton, new Button("Delete"));
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
                    }
                });
                recordLikeAndCommentsLayout.addComponent(showCommentsButton);
            }
            if (currentWallRecord.getRecordAuthor().getObjectId().equals(profileId)) {
                Button editWallRecordButton = new Button("Edit", VaadinIcons.EDIT);
                editWallRecordButton.setWidth(100, Sizeable.Unit.PIXELS);
                editWallRecordButton.addClickListener(new AbstractClickListener() {
                    @Override
                    public void buttonClickListener() {
                        editWallRecord(currentWallRecord);
                        UI.getCurrent().addWindow(updateWallRecordWindow);
                    }
                });
                recordLikeAndCommentsLayout.addComponents(editWallRecordButton, new Button("Delete"));
            }
            singleWallRecordLayout.addComponents(
                    recordInfoLayout,
                    new Label(currentWallRecord.getRecordText(), ContentMode.PREFORMATTED),
                    recordLikeAndCommentsLayout,
                    allCommentsPanel);
            singleWallRecordPanel.setContent(singleWallRecordLayout);
            wallLayout.addComponent(singleWallRecordPanel);
        }
        return wallLayout;
    }
    /*доделать, т.к. пока нет функционала групп*/
    private VerticalLayout getGroupRecordsLayout(){
        VerticalLayout groupLayout = new VerticalLayout();
        HorizontalLayout groupHeaderLayout = new HorizontalLayout();

        int groupRecordsListSize = groupRecordsList.getContent().size();
        String labelCaptionStart = groupRecordsListSize + " record";
        if (groupRecordsListSize != 1) {
            labelCaptionStart += "s";
        }
        groupHeaderLayout.addComponents(
                new Label(labelCaptionStart + " of your groups"));
        groupLayout.addComponent(groupHeaderLayout);
        //GroupRecords
        for (int i = groupRecordsList.getContent().size(); i > 0; i--) {
            GroupRecord currentGroupRecord = groupRecordsList.getContent().get(i - 1);
            /*
            List<GroupRecordComment> commentsList = getGroupRecordComments(currentGroupRecord.getObjectId());
            Profile commentator = currentGroupRecord.getRecordAuthor();
            Profile wallOwner = currentGroupRecord.getWallOwner();

            Panel singleWallRecordPanel = new Panel();
            VerticalLayout singleWallRecordLayout = new VerticalLayout();

            HorizontalLayout recordInfoLayout = new HorizontalLayout();
            String commentatorAvatar = commentator.getProfileAvatar();
            Image recordAuthorAvatar = new Image();
            recordAuthorAvatar.setSource(new ExternalResource(commentatorAvatar == null ? stubAvatar : commentatorAvatar));
            String description = "from " + commentator.getProfileName() + " " + commentator.getProfileSurname() + " on ";
            if (commentator.equals(wallOwner)) {
                description += "his/her";
            } else {
                description += wallOwner.getProfileName() + " " + wallOwner.getProfileSurname();
            }
            description += " wall";
            recordAuthorAvatar.setDescription(description);
            recordAuthorAvatar.setHeight(100, Sizeable.Unit.PIXELS);
            recordAuthorAvatar.setWidth(100, Sizeable.Unit.PIXELS);
            Label recordName = new Label("Record " + recordAuthorAvatar.getDescription());

            String recordDateString;
            try {
                recordDateString = currentGroupRecord.getRecordDate().toString();
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
            likeRecordButton.setWidth(70, Sizeable.Unit.PIXELS);
            Button dislikeRecordButton = new Button();
            dislikeRecordButton.setWidth(70, Sizeable.Unit.PIXELS);
            likeRecordButton.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    checkAndAddLike(currentGroupRecord, false, likeRecordButton, dislikeRecordButton);
                }
            });
            dislikeRecordButton.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    checkAndAddLike(currentGroupRecord, true, likeRecordButton, dislikeRecordButton);
                }
            });
            Button addCommentButton = new Button("Add new comment", VaadinIcons.PLUS);
            addCommentButton.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    addComment(currentGroupRecord);
                    UI.getCurrent().addWindow(updateCommentWindow);
                }
            });
            countAndShowLikes(currentGroupRecord, likeRecordButton, dislikeRecordButton);
            recordLikeAndCommentsLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
            recordLikeAndCommentsLayout.addComponents(likeRecordButton, dislikeRecordButton);

            //Comments
            Panel allCommentsPanel = new Panel();
            allCommentsPanel.setVisible(true);
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
                            commentatorMiniImage.setHeight(55, Sizeable.Unit.PIXELS);
                            commentatorMiniImage.setWidth(55, Sizeable.Unit.PIXELS);
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
                            likeCommentButton.setWidth(70, Sizeable.Unit.PIXELS);
                            Button dislikeCommentButton = new Button();
                            likeCommentButton.setWidth(70, Sizeable.Unit.PIXELS);
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
                            if (currentComment.getCommentAuthor().getObjectId().equals(profileID)) {
                                Button editCommentButton = new Button("Edit", VaadinIcons.EDIT);
                                editCommentButton.setWidth(100, Sizeable.Unit.PIXELS);
                                editCommentButton.addClickListener(new AbstractClickListener() {
                                    @Override
                                    public void buttonClickListener() {
                                        editWallRecordComment(currentComment, currentGroupRecord);
                                        UI.getCurrent().addWindow(updateCommentWindow);
                                    }
                                });
                                commentLikeLayout.addComponents(editCommentButton, new Button("Delete"));
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
                    }
                });
                recordLikeAndCommentsLayout.addComponent(showCommentsButton);
            }
            if (currentGroupRecord.getRecordAuthor().getObjectId().equals(profileID)) {
                Button editWallRecordButton = new Button("Edit", VaadinIcons.EDIT);
                editWallRecordButton.setWidth(100, Sizeable.Unit.PIXELS);
                editWallRecordButton.addClickListener(new AbstractClickListener() {
                    @Override
                    public void buttonClickListener() {
                        editWallRecord(currentGroupRecord);
                        UI.getCurrent().addWindow(updateWallRecordWindow);
                    }
                });
                recordLikeAndCommentsLayout.addComponents(editWallRecordButton, new Button("Delete"));
            }
            singleWallRecordLayout.addComponents(
                    recordInfoLayout,
                    new Label(currentGroupRecord.getRecordText(), ContentMode.PREFORMATTED),
                    recordLikeAndCommentsLayout,
                    allCommentsPanel);
            singleWallRecordPanel.setContent(singleWallRecordLayout);
            groupLayout.addComponent(singleWallRecordPanel);*/
        }
        return groupLayout;
    }

    private void countAndShowLikes(BaseEntity entity, Button likeRecordButton, Button dislikeRecordButton) {
        List<AbstractLike> likesList = null;
        Class cc = entity.getClass();
        if (AbstractRecord.class.isAssignableFrom(cc)) {
            likesList = getRecordLikes(entity.getObjectId());
        } else if (AbstractComment.class.isAssignableFrom(cc)) {
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
                if (rc.getParentId().equals(profileId)) {
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
        Class cc = entity.getClass();
        if (AbstractRecord.class.isAssignableFrom(cc)) {
            likesList = getRecordLikes(entity.getObjectId());
        } else if (AbstractComment.class.isAssignableFrom(cc)) {
            likesList = getCommentLikes(entity.getObjectId());
        }
        outer:
        {
            for (AbstractLike al : likesList) {
                boolean isCurrentDislike = Boolean.valueOf(al.getIsDislike());
                if (al.getParentId().equals(profileId)) {
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


    private List<WallRecordComment> getRecordComments(BigInteger wallRecordID) {
        List<WallRecordComment> result = Arrays.asList(CustomRestTemplate.getInstance().customGetForObject(
                "/comments/" + wallRecordID, WallRecordComment[].class));
        //отсортировать не так
        result.sort(Comparator.comparing(WallRecordComment::getCommentDate));
        return result;
    }

    private List<AbstractLike> getRecordLikes(BigInteger wallRecordID) {
        return Arrays.asList(CustomRestTemplate.getInstance().customGetForObject(
                "/likes/record/" + wallRecordID, AbstractLike[].class));
    }

    private List<AbstractLike> getCommentLikes(BigInteger commentID) {
        return Arrays.asList(CustomRestTemplate.getInstance().customGetForObject(
                "/likes/comment/" + commentID, AbstractLike[].class));
    }

    private void addComment(WallRecord currentWallRecord) {
        updateCommentWindow = new Window();
        updateCommentWindow.setWidth("400px");
        updateCommentWindow.setHeight("250px");
        updateCommentWindow.setCaption("Creating new comment:");
        updateCommentWindow.setModal(true);
        VerticalLayout windowContent = new VerticalLayout();
        HorizontalLayout addCommentButtonsLayout = new HorizontalLayout();

        TextArea commentText = new TextArea();
        commentText.setWidth("100%");

        Button add = new Button("Add comment");
        add.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                add.setComponentError(null);
                WallRecordComment comment = new WallRecordComment("Comment",
                        "From " + /*currentProfile.getProfileSurname()*/ " lalallala" + " to wall record№" + currentWallRecord.getObjectId());
                comment.setCommentDate(Timestamp.valueOf(
                        new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()));
                comment.setCommentText(commentText.getValue());
                comment.setCommentAuthor(profile);
                comment.setCommentedWallRecord(currentWallRecord);
                editWallRecordComment(null, currentWallRecord);
                updateCommentWindow.close();
                ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(profileId));
            }
        });

        Button cancel = new Button("Cancel", click -> updateCommentWindow.close());
        addCommentButtonsLayout.addComponentsAndExpand(add, cancel);
        windowContent.addComponents(commentText, addCommentButtonsLayout);

        updateCommentWindow.setContent(windowContent);
        updateCommentWindow.center();
    }

    private void addLike(BaseEntity entity, boolean isDislike) {
        Class cc = entity.getClass();
        if (AbstractRecord.class.isAssignableFrom(cc)) {
            addWallRecordLike((AbstractRecord) entity, isDislike);
        } else if (AbstractComment.class.isAssignableFrom(cc)) {
            addCommentLike((AbstractComment) entity, isDislike);
        }
    }

    private void editWallRecord(WallRecord currentRecord) {
        updateWallRecordWindow = new Window();
        updateWallRecordWindow.setWidth("400px");
        updateWallRecordWindow.setHeight("250px");
        updateWallRecordWindow.setCaption("Edit wall record");
        updateWallRecordWindow.setModal(true);
        VerticalLayout windowContent = new VerticalLayout();
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        TextArea text = new TextArea();
        text.setWidth("100%");
        text.setValue(currentRecord.getRecordText());
        Button edit = new Button("Edit record");
        edit.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                edit.setComponentError(null);
                currentRecord.setRecordText(text.getValue());
                updateWallRecord(currentRecord);
                Notification.show("Record edited!");
                updateWallRecordWindow.close();
                ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new NewsView(profileId));
            }
        });
        buttonsLayout.addComponentsAndExpand(edit);
        windowContent.addComponent(text);

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
        updateCommentWindow.setCaption("New wall record comment");
        updateCommentWindow.setModal(true);
        VerticalLayout windowContent = new VerticalLayout();
        HorizontalLayout buttonsLayout = new HorizontalLayout();

        if (currentComment == null) {
            TextArea text = new TextArea();
            text.setWidth("100%");
            Button add = new Button("Add comment");
            add.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    add.setComponentError(null);
                    WallRecordComment comment = new WallRecordComment("WallRecordComment",
                            "From " + profile.getProfileSurname() + " to wall record№" + currentWallRecord.getObjectId());
                    comment.setCommentDate(Timestamp.valueOf(
                            new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()));
                    comment.setCommentText(text.getValue());
                    comment.setCommentAuthor(profile);
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
            TextArea text = new TextArea();
            text.setWidth("100%");
            text.setValue(currentComment.getCommentText());
            Button edit = new Button("Edit comment");
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
                "From " + profile.getProfileSurname() + " to wall record№" + currentRecord.getObjectId());
        like.setIsDislike(Boolean.toString(isDislike));
        like.setParentId(profile.getObjectId());
        like.setLikedRecord(currentRecord);
        createWallRecordLike(like);
    }

    private void addCommentLike(AbstractComment currentComment, boolean isDislike) {
        CommentLike like = new CommentLike(isDislike ? "Dislike" : "Like",
                "From " + profile.getProfileSurname() + " to wall record№" + currentComment.getObjectId());
        like.setIsDislike(Boolean.toString(isDislike));
        like.setParentId(profile.getObjectId());
        like.setLikedDislikedComment(currentComment);
        createCommentLike(like);
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

    private void deleteLike(BigInteger likeID) {
        CustomRestTemplate.getInstance().customGetForObject("/likes/delete/" + likeID, Void.class);
    }
}
