package com.netcracker.ui.news;

import com.netcracker.model.record.GroupRecord;
import com.netcracker.model.record.WallRecord;
import com.netcracker.model.user.Profile;
import com.netcracker.service.util.RestResponsePage;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.RecordPanel;
import com.netcracker.ui.StubPagingBar;
import com.netcracker.ui.util.CustomRestTemplate;
import com.netcracker.ui.util.VaadinValidationBinder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.*;
import com.vaadin.ui.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;
import java.util.List;

public class NewsView extends VerticalLayout {
    private final Profile profile;
    private final BigInteger profileId;
    private StubPagingBar pagingLayout;
    private Panel wallPanel;
    private RestResponsePage<WallRecord> wallRecordsList;
    private RestResponsePage<GroupRecord> groupRecordsList;
    private int browserHeight;
    private int browserWidth;
    private boolean isFriendsNews;
    private int pageNumber;
    public NewsView(BigInteger profileId, int pageNumb, boolean isFriendNews) {
        super();
        this.profileId = profileId;
        this.isFriendsNews = isFriendNews;
        this.pageNumber = pageNumb;
        profile = CustomRestTemplate.getInstance().
                customGetForObject("/profile/" + this.profileId, Profile.class);

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
                pageNumber = 1;
                getFriendsRecordsList(pageNumber);
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
                pageNumber = 1;
                getGroupsRecordsList(pageNumber);
                wallPanel.setContent(getGroupRecordsLayout());
                getPagingLayout(pagingPanel, isFriendsNews);
            }
        });
        //Elements for right part
        if(isFriendsNews){
            getFriendsRecordsList(pageNumber);
        } else {
            getGroupsRecordsList(pageNumber);
        }

        getPagingLayout(pagingPanel, isFriendsNews);

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

    private void getGroupsRecordsList(int pageNumber) {
        ResponseEntity<RestResponsePage<GroupRecord>> pageResponseEntity =
                CustomRestTemplate.getInstance().customExchangeForParametrizedTypes("/news/groups/" + profileId + "/" + pageNumber, HttpMethod.GET,
                        null, new ParameterizedTypeReference<RestResponsePage<GroupRecord>>() {
                        });
        groupRecordsList = pageResponseEntity.getBody();
        List<GroupRecord> advertisements = groupRecordsList.getContent();
        if (advertisements.isEmpty()) {
            Notification.show("No news  were found");
        }
    }

    private void getData(int page, boolean isFriendsNews) {
        if (isFriendsNews) {
            getFriendsRecordsList(page);
            wallPanel.setContent(getWallRecordsLayout());
        } else {
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
        if (isFriendsNews) {
            pageCount = (int) wallRecordsList.getTotalElements();
        } else {
            pageCount = (int) groupRecordsList.getTotalElements();
        }
        if (pageCount > 1) {
            pagingLayout = new StubPagingBar(pageCount, pageNumber);

            ((Button) pagingLayout.getComponent(0)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    Integer page = (Integer) ((Button) pagingLayout.getComponent(0)).getData();
                    getData(page, isFriendsNews);
                    pagingLayout.currentPageNumber = 1;
                    pageNumber = pagingLayout.currentPageNumber;
                }
            });
            ((Button) pagingLayout.getComponent(6)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    Integer page = (Integer) ((Button) pagingLayout.getComponent(6)).getData();
                    getData(page, isFriendsNews);
                    pagingLayout.currentPageNumber = page;
                    pageNumber = pagingLayout.currentPageNumber;
                }
            });
            ((Button) pagingLayout.getComponent(1)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    --pagingLayout.currentPageNumber;
                    if (pagingLayout.currentPageNumber < 1) {
                        pagingLayout.currentPageNumber = pageCount;
                    }
                    pageNumber = pagingLayout.currentPageNumber;
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
                    pageNumber = pagingLayout.currentPageNumber;
                    getData(pagingLayout.currentPageNumber, isFriendsNews);
                }
            });

            ((TextField) pagingLayout.getComponent(3)).addShortcutListener(new ShortcutListener("Enter", ShortcutAction.KeyCode.ENTER, null) {
                @Override
                public void handleAction(Object o, Object o1) {
                    BinderValidationStatus<VaadinValidationBinder> status = pagingLayout.pageNumberFieldBinder.validate();
                    if (!status.hasErrors()) {
                        pagingLayout.currentPageNumber = Integer.valueOf(((TextField) pagingLayout.getComponent(3)).getValue());
                        pageNumber = pagingLayout.currentPageNumber;
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
            Panel singleWallRecordPanel = new RecordPanel(currentWallRecord, profile, true,
                    pageNumber, isFriendsNews);
            wallLayout.addComponent(singleWallRecordPanel);
        }
        return wallLayout;
    }

    /*доделать, т.к. пока нет функционала групп*/
    private VerticalLayout getGroupRecordsLayout() {
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
}
