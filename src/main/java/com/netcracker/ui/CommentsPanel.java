package com.netcracker.ui;

import com.netcracker.model.BaseEntity;
import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.album.PhotoAlbum;
import com.netcracker.model.comment.*;
import com.netcracker.model.group.Group;
import com.netcracker.model.like.AbstractLike;
import com.netcracker.model.like.CommentLike;
import com.netcracker.model.like.RecordLike;
import com.netcracker.model.record.AbstractRecord;
import com.netcracker.model.record.GroupRecord;
import com.netcracker.model.record.PhotoRecord;
import com.netcracker.model.record.WallRecord;
import com.netcracker.model.user.Profile;
import com.netcracker.ui.bulletinboard.AdvertisementView;
import com.netcracker.ui.gallery.AlbumsUI;
import com.netcracker.ui.groups.GroupUI;
import com.netcracker.ui.news.NewsView;
import com.netcracker.ui.profile.ProfileView;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.event.MouseEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class CommentsPanel<T extends AbstractComment> extends Panel {

    private static final Resource likeOn = VaadinIcons.THUMBS_UP;
    private static final Resource likeOff = VaadinIcons.THUMBS_UP_O;
    private static final Resource dislikeOn = VaadinIcons.THUMBS_DOWN;
    private static final Resource dislikeOff = VaadinIcons.THUMBS_DOWN_O;
    private Window updateCommentWindow;
    private Window confirmDeleteWindow;
    private String stubAvatar = "https://goo.gl/6eEoWo";
    private Profile currentProfile;
    private BigInteger currentProfileId;
    private final BaseEntity reloadTo;
    private final Class<T> c;
    private List<T> commentsList;
    private boolean isFromNews = false;
    private int pageNumber = 1;
    private boolean isFriendNews = true;

    public CommentsPanel(BaseEntity currentRecord, Class<T> commentsType, AbstractComponentContainer parentComponent, BaseEntity reloadPageTo, int pageNumber, boolean isFriendNews){
        this(currentRecord, commentsType, parentComponent, reloadPageTo);
        isFromNews = true;
        this.isFriendNews = isFriendNews;
        this.pageNumber = pageNumber;
    }

    public CommentsPanel(BaseEntity currentRecord, Class<T> commentsType, AbstractComponentContainer parentComponent, BaseEntity reloadPageTo) {
        super();
        this.reloadTo = reloadPageTo;
        c = commentsType;
        setCurrentProfile();
        VerticalLayout allCommentsLayout = new VerticalLayout();
        setVisible(false);

        if (WallRecordComment.class.equals(c)) {
            commentsList = (List<T>) getWallRecordComments(currentRecord.getObjectId());
        } else if (GroupRecordComment.class.equals(c)) {
            commentsList = (List<T>) getGroupRecordComments(currentRecord.getObjectId());
        } else if (PhotoRecordComment.class.equals(c)) {
            commentsList = (List<T>) getPhotoRecordComments(currentRecord.getObjectId());
        } else if (AdvertisementComment.class.equals(c)) {
            commentsList = (List<T>) getAdvertisementComments(currentRecord.getObjectId());
        }

        int commentsListSize = commentsList.size();

        for (int j = 0; j < commentsListSize; j++) {
            AbstractComment currentComment = commentsList.get(j);
            Panel currentCommentPanel = new Panel();
            VerticalLayout currentCommentLayout = new VerticalLayout();
            HorizontalLayout commentDateAndAvatarLayout = new HorizontalLayout();
            Profile commentator = CustomRestTemplate.getInstance().
                    customGetForObject("/comments/author/" + currentComment.getObjectId(), Profile.class);
            String authorNameAndSurname = commentator.getProfileName() + " " + commentator.getProfileSurname();
            String singleFriendAvatar = commentator.getProfileAvatar();
            Image commentatorMiniImage = new Image();
            commentatorMiniImage.setHeight(55, Unit.PIXELS);
            commentatorMiniImage.setWidth(55, Unit.PIXELS);
            commentatorMiniImage.setSource(new ExternalResource(singleFriendAvatar == null ? stubAvatar : singleFriendAvatar));
            commentatorMiniImage.setDescription(authorNameAndSurname);
            commentatorMiniImage.addClickListener((MouseEvents.ClickListener) clickEvent ->
                    ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(commentator.getObjectId())));
            commentDateAndAvatarLayout.addComponents(
                    commentatorMiniImage,
                    new Label("Comment from " + authorNameAndSurname),
                    new Label(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(currentComment.getCommentDate()))
            );
            HorizontalLayout commentLikeLayout = new HorizontalLayout();
            Button likeCommentButton = new Button();
            likeCommentButton.setWidth(70, Unit.PIXELS);
            Button dislikeCommentButton = new Button();
            likeCommentButton.setWidth(70, Unit.PIXELS);
            likeCommentButton.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    checkAndChangeLike(currentComment, false, likeCommentButton, dislikeCommentButton);
                }
            });
            dislikeCommentButton.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    checkAndChangeLike(currentComment, true, likeCommentButton, dislikeCommentButton);
                }
            });
            countAndShowLikes(currentComment, likeCommentButton, dislikeCommentButton);
            commentLikeLayout.addComponents(likeCommentButton, dislikeCommentButton);
            if (commentator.getObjectId().equals(currentProfileId)) {
                Button editCommentButton = new Button("Edit", VaadinIcons.EDIT);
                editCommentButton.setWidth(100, Unit.PIXELS);
                editCommentButton.addClickListener(new AbstractClickListener() {
                    @Override
                    public void buttonClickListener() {
                        editRecordComment(currentComment, currentRecord);
                        UI.getCurrent().addWindow(updateCommentWindow);
                    }
                });
                Button deleteWallRecordCommentButton = new Button("Delete", VaadinIcons.DEL_A);
                deleteWallRecordCommentButton.addClickListener(new AbstractClickListener() {
                    @Override
                    public void buttonClickListener() {
                        confirmDelete(currentComment);
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
            currentCommentPanel.focus();
        }

        Button addCommentButton = new Button("Add new comment", VaadinIcons.PLUS);
        addCommentButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                editRecordComment(null, currentRecord);
                UI.getCurrent().addWindow(updateCommentWindow);
            }
        });
        allCommentsLayout.addComponent(addCommentButton);

        if (commentsListSize == 0) {
            setVisible(true);
        } else {
            String buttonCaption = "Show/hide " + commentsListSize + " comment";
            if (commentsListSize != 1) {
                buttonCaption += "s";
            }
            Button showCommentsButton = new Button(buttonCaption, VaadinIcons.COMMENT_O);
            showCommentsButton.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {

                    setVisible(!isVisible());
                    showCommentsButton.setIcon(
                            showCommentsButton.getIcon().equals(VaadinIcons.COMMENT_O) ?
                                    VaadinIcons.COMMENT : VaadinIcons.COMMENT_O);
                    addCommentButton.focus();
                }
            });
            parentComponent.addComponent(showCommentsButton);
        }
        setContent(allCommentsLayout);
    }

    private void countAndShowLikes(BaseEntity entity, Button likeRecordButton, Button dislikeRecordButton) {
        List<AbstractLike> likesList = null;
        Class c = entity.getClass();
        if (AbstractRecord.class.isAssignableFrom(c)) {
            likesList = getRecordLikes(entity.getObjectId());
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

    private void checkAndChangeLike(BaseEntity entity, boolean isDislike, Button likeRecordButton, Button dislikeRecordButton) {
        List<AbstractLike> likesList = null;
        Class c = entity.getClass();
        if (AbstractRecord.class.isAssignableFrom(c)) {
            likesList = getRecordLikes(entity.getObjectId());
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

    private void addLike(BaseEntity entity, boolean isDislike) {
        Class c = entity.getClass();
        if (AbstractRecord.class.isAssignableFrom(c)) {
            addRecordLike((AbstractRecord) entity, isDislike);
        } else if (AbstractComment.class.isAssignableFrom(c)) {
            addCommentLike((AbstractComment) entity, isDislike);
        }
    }

    private void editRecordComment(AbstractComment currentComment, BaseEntity currentRecord) {
        updateCommentWindow = new Window();
        updateCommentWindow.setWidth("400px");
        updateCommentWindow.setHeight("250px");
        updateCommentWindow.setModal(true);
        VerticalLayout windowContent = new VerticalLayout();
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        TextArea textArea = new TextArea();
        textArea.setWidth("100%");
        Class c = currentRecord.getClass();

        if(currentComment == null) {
            updateCommentWindow.setCaption("New comment");
            Button add = new Button("Add");
            add.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    add.setComponentError(null);
                    String text = textArea.getValue();

                    if (WallRecord.class.equals(c)) {
                        WallRecordComment comment = new WallRecordComment("WallRecordComment",
                                "From " + currentProfile.getProfileSurname() + " to wall record №" + currentRecord.getObjectId());
                        comment.setCommentedWallRecord((WallRecord) currentRecord);
                        createWallRecordComment((WallRecordComment) setAbstractFields(comment, text));
                    } else if (GroupRecord.class.equals(c)) {
                        GroupRecordComment comment = new GroupRecordComment("GroupRecordComment",
                                "From " + currentProfile.getProfileSurname() + " to group record №" + currentRecord.getObjectId());
                        comment.setCommentedGroupRecord((GroupRecord) currentRecord);
                        createGroupRecordComment((GroupRecordComment) setAbstractFields(comment, text));
                    } else if (PhotoRecord.class.equals(c)) {
                        PhotoRecordComment comment = new PhotoRecordComment("PhotoRecordComment",
                                "From " + currentProfile.getProfileSurname() + " to photo record №" + currentRecord.getObjectId());
                        comment.setCommentedPhotoRecord((PhotoRecord) currentRecord);
                        createPhotoRecordComment((PhotoRecordComment) setAbstractFields(comment, text));
                    } else if (Advertisement.class.equals(c)) {
                        AdvertisementComment comment = new AdvertisementComment("AdvertisementComment",
                                "From " + currentProfile.getProfileSurname() + " to advertisement №" + currentRecord.getObjectId());
                        comment.setCommentedAdvertisement((Advertisement) currentRecord);
                        createAdvertisementComment((AdvertisementComment) setAbstractFields(comment, text));
                    }

                    Notification.show("Comment added!");
                    updateCommentWindow.close();
                    checkAndReload();
                }
            });
            buttonsLayout.addComponentsAndExpand(add);
        } else {
            updateCommentWindow.setCaption("Edit comment");
            textArea.setValue(currentComment.getCommentText());
            Button edit = new Button("Edit");
            edit.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    edit.setComponentError(null);
                    currentComment.setCommentText(textArea.getValue());
                    if (WallRecord.class.equals(c)) {
                        updateWallRecordComment((WallRecordComment) currentComment);
                    } else if (GroupRecord.class.equals(c)) {
                        updateGroupRecordComment((GroupRecordComment) currentComment);
                    } else if (PhotoRecord.class.equals(c)) {
                        updatePhotoRecordComment((PhotoRecordComment) currentComment);
                    } else if (Advertisement.class.equals(c)) {
                        updateAdvertisementComment((AdvertisementComment) currentComment);
                    }
                    Notification.show("Comment edited!");
                    updateCommentWindow.close();
                    checkAndReload();
                }
            });
            buttonsLayout.addComponentsAndExpand(edit);
        }
        Button cancel = new Button("Cancel", click -> updateCommentWindow.close());
        buttonsLayout.addComponentsAndExpand(cancel);
        windowContent.addComponents(textArea, buttonsLayout);

        updateCommentWindow.setContent(windowContent);
        updateCommentWindow.center();
    }

    private AbstractComment setAbstractFields(AbstractComment comment, String text){
        comment.setCommentAuthor(currentProfile);
        comment.setCommentDate(Timestamp.valueOf(
                new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()));
        comment.setCommentText(text);
        return comment;
    }

    private void addRecordLike(AbstractRecord currentRecord, boolean isDislike) {
        RecordLike like = new RecordLike(isDislike ? "Dislike" : "Like",
                "From " + currentProfile.getProfileSurname() + " to record №" + currentRecord.getObjectId());
        like.setIsDislike(Boolean.toString(isDislike));
        like.setParentId(currentProfile.getObjectId());
        like.setLikedRecord(currentRecord);
        createRecordLike(like);
    }

    private void addCommentLike(AbstractComment currentComment, boolean isDislike) {
        CommentLike like = new CommentLike(isDislike ? "Dislike" : "Like",
                "From " + currentProfile.getProfileSurname() + " to comment №" + currentComment.getObjectId());
        like.setIsDislike(Boolean.toString(isDislike));
        like.setParentId(currentProfile.getObjectId());
        like.setLikedDislikedComment(currentComment);
        createCommentLike(like);
    }

    private void confirmDelete(BaseEntity entity){
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
                    deleteRecord((AbstractRecord) entity);
                    Notification.show("Record deleted!");
                } else if (AbstractComment.class.isAssignableFrom(c)) {
                    deleteComment((AbstractComment) entity);
                    Notification.show("Comment deleted!");
                }
/*                ComponentContainer parent = (ComponentContainer) currentPanel.getParent();
                parent.removeComponent(currentPanel);*/
                confirmDeleteWindow.close();
                checkAndReload();
            }
        });

        Button cancel = new Button("Cancel", click -> confirmDeleteWindow.close());
        buttonsLayout.addComponentsAndExpand(delete, cancel);
        windowContent.addComponents(new Label("Are you sure?"), buttonsLayout);
        confirmDeleteWindow.setContent(windowContent);
        confirmDeleteWindow.center();
    }

    private List<WallRecordComment> getWallRecordComments(BigInteger recordID) {
        List<WallRecordComment> result = Arrays.asList(CustomRestTemplate.getInstance().customGetForObject(
                "/comments/wall/" + recordID, WallRecordComment[].class));
        result.sort(Comparator.comparing(AbstractComment::getCommentDate));
        return result;
    }

    private List<GroupRecordComment> getGroupRecordComments(BigInteger recordID) {
        List<GroupRecordComment> result = Arrays.asList(CustomRestTemplate.getInstance().customGetForObject(
                "/comments/group/" + recordID, GroupRecordComment[].class));
        result.sort(Comparator.comparing(AbstractComment::getCommentDate));
        return result;
    }

    private List<PhotoRecordComment> getPhotoRecordComments(BigInteger recordID) {
        List<PhotoRecordComment> result = Arrays.asList(CustomRestTemplate.getInstance().customGetForObject(
                "/comments/photo/" + recordID, PhotoRecordComment[].class));
        result.sort(Comparator.comparing(AbstractComment::getCommentDate));
        return result;
    }

    private List<AdvertisementComment> getAdvertisementComments(BigInteger recordID) {
        return Arrays.asList(CustomRestTemplate.getInstance().customGetForObject(
                "/comments/ad/" + recordID, AdvertisementComment[].class));
    }

    private List<AbstractLike> getRecordLikes(BigInteger recordID) {
        return Arrays.asList(CustomRestTemplate.getInstance().customGetForObject(
                "/likes/record/" + recordID, AbstractLike[].class));
    }

    private List<AbstractLike> getCommentLikes(BigInteger commentID) {
        return Arrays.asList(CustomRestTemplate.getInstance().customGetForObject(
                "/likes/comment/" + commentID, AbstractLike[].class));
    }

    private void createWallRecordComment(WallRecordComment comment) {
        HttpEntity<WallRecordComment> request = new HttpEntity<>(comment);
        CustomRestTemplate.getInstance().customPostForObject("/comments/wall/add", request, WallRecordComment.class);
    }

    private void createGroupRecordComment(GroupRecordComment comment) {
        HttpEntity<GroupRecordComment> request = new HttpEntity<>(comment);
        CustomRestTemplate.getInstance().customPostForObject("/comments/group/add", request, GroupRecordComment.class);
    }

    private void createPhotoRecordComment(PhotoRecordComment comment) {
        HttpEntity<PhotoRecordComment> request = new HttpEntity<>(comment);
        CustomRestTemplate.getInstance().customPostForObject("/comments/photo/add", request, PhotoRecordComment.class);
    }

    private void createAdvertisementComment(AdvertisementComment comment) {
        HttpEntity<AdvertisementComment> request = new HttpEntity<>(comment);
        CustomRestTemplate.getInstance().customPostForObject("/comments/ad/add", request, AdvertisementComment.class);
    }

    private void createRecordLike(RecordLike like) {
        HttpEntity<RecordLike> request = new HttpEntity<>(like);
        CustomRestTemplate.getInstance().customPostForObject("/likes/record/add", request, RecordLike.class);
    }

    private void createCommentLike(CommentLike like) {
        HttpEntity<CommentLike> request = new HttpEntity<>(like);
        CustomRestTemplate.getInstance().customPostForObject("/likes/comment/add", request, CommentLike.class);
    }

    private void updateWallRecordComment(WallRecordComment comment) {
        HttpEntity<WallRecordComment> request = new HttpEntity<>(comment);
        CustomRestTemplate.getInstance().customPostForObject("/comments/wall/update", request, WallRecordComment.class);
    }

    private void updateGroupRecordComment(GroupRecordComment comment) {
        HttpEntity<GroupRecordComment> request = new HttpEntity<>(comment);
        CustomRestTemplate.getInstance().customPostForObject("/comments/group/update", request, GroupRecordComment.class);
    }

    private void updatePhotoRecordComment(PhotoRecordComment comment) {
        HttpEntity<PhotoRecordComment> request = new HttpEntity<>(comment);
        CustomRestTemplate.getInstance().customPostForObject("/comments/photo/update", request, PhotoRecordComment.class);
    }

    private void updateAdvertisementComment(AdvertisementComment comment) {
        HttpEntity<AdvertisementComment> request = new HttpEntity<>(comment);
        CustomRestTemplate.getInstance().customPostForObject("/comments/ad/update", request, AdvertisementComment.class);
    }

    private void deleteRecord(AbstractRecord record) {
        HttpEntity<AbstractRecord> request = new HttpEntity<>(record);
        CustomRestTemplate.getInstance().customPostForObject("/records/delete", request, AbstractRecord.class);
    }

    private void deleteComment(AbstractComment comment) {
        HttpEntity<AbstractComment> request = new HttpEntity<>(comment);
        CustomRestTemplate.getInstance().customPostForObject("/comments/delete", request, AbstractComment.class);
    }

    private void deleteLike(BigInteger likeID) {
        CustomRestTemplate.getInstance().customGetForObject("/likes/delete/" + likeID, Void.class);
    }

    private void checkAndReload(){
        if(isFromNews){
            reloadPage(reloadTo, pageNumber, isFriendNews);
        } else{
            reloadPage(reloadTo);
        }
    }
    private void reloadPage(BaseEntity reloadTo, int pageNumber, boolean isFriendNews) {
        ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new NewsView(reloadTo.getObjectId(), pageNumber, isFriendNews));
    }
    private void reloadPage(BaseEntity reloadTo){
        BigInteger destinationID = reloadTo.getObjectId();
        Class c = reloadTo.getClass();
        if(Profile.class.equals(c)) {
            ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(destinationID));
        } else if(Group.class.equals(c)) {
            ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new GroupUI(destinationID));
        } else if(PhotoAlbum.class.equals(c)) {
            //((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new GalleryUI(destinationID));
            ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new AlbumsUI(currentProfileId));
        } else if(Advertisement.class.equals(c)) {
            ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new AdvertisementView((Advertisement) reloadTo));
        }
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
