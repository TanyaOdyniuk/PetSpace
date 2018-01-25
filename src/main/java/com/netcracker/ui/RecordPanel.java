package com.netcracker.ui;

import com.netcracker.model.BaseEntity;
import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.album.PhotoAlbum;
import com.netcracker.model.comment.AbstractComment;
import com.netcracker.model.comment.GroupRecordComment;
import com.netcracker.model.comment.WallRecordComment;
import com.netcracker.model.group.Group;
import com.netcracker.model.like.AbstractLike;
import com.netcracker.model.like.CommentLike;
import com.netcracker.model.like.RecordLike;
import com.netcracker.model.record.AbstractRecord;
import com.netcracker.model.record.GroupRecord;
import com.netcracker.model.record.WallRecord;
import com.netcracker.model.user.Profile;
import com.netcracker.ui.gallery.GalleryUI;
import com.netcracker.ui.groups.GroupUI;
import com.netcracker.ui.news.NewsView;
import com.netcracker.ui.profile.ProfileView;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.event.MouseEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

//Panel for creating records, only for wall and group records
public class RecordPanel extends Panel {

    private static final Resource likeOn = VaadinIcons.THUMBS_UP;
    private static final Resource likeOff = VaadinIcons.THUMBS_UP_O;
    private static final Resource dislikeOn = VaadinIcons.THUMBS_DOWN;
    private static final Resource dislikeOff = VaadinIcons.THUMBS_DOWN_O;
    private Window updateWallRecordWindow;
    private Window confirmDeleteWindow;
    private String stubAvatar = "https://goo.gl/6eEoWo";
    private Profile currentProfile;
    private BigInteger currentProfileId;
    private final BaseEntity reloadTo;
    private boolean isFromNews;
    private int pageNumber;
    private boolean isFriendNews;

    public RecordPanel(AbstractRecord currentRecord, BaseEntity reloadTo, boolean isFromNews, int pageNumber, boolean isFriendNews) {
        super();
        this.reloadTo = reloadTo;
        this.isFromNews = isFromNews;
        this.isFriendNews = isFriendNews;
        this.pageNumber = pageNumber;
        setCurrentProfile();
        VerticalLayout singleRecordLayout = new VerticalLayout();
        Profile recordAuthor = CustomRestTemplate.getInstance().
                customGetForObject("/records/author/" + currentRecord.getObjectId(), Profile.class);

        HorizontalLayout recordInfoLayout = new HorizontalLayout();
        String commentatorAvatar = recordAuthor.getProfileAvatar();
        Image recordAuthorAvatar = new Image();
        recordAuthorAvatar.setSource(new ExternalResource(commentatorAvatar == null ? stubAvatar : commentatorAvatar));
        recordAuthorAvatar.setDescription(createDescription(isFromNews, currentRecord, recordAuthor));
        recordAuthorAvatar.setHeight(100, Sizeable.Unit.PIXELS);
        recordAuthorAvatar.setWidth(100, Sizeable.Unit.PIXELS);
        recordAuthorAvatar.addClickListener((MouseEvents.ClickListener) clickEvent ->
                ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(recordAuthor.getObjectId())));
        Label recordName = new Label("Record from " + recordAuthorAvatar.getDescription());
        Label recordDate = new Label(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(currentRecord.getRecordDate()));
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
                checkAndChangeLike(currentRecord, false, likeRecordButton, dislikeRecordButton);
            }
        });
        dislikeRecordButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                checkAndChangeLike(currentRecord, true, likeRecordButton, dislikeRecordButton);
            }
        });

        countAndShowLikes(currentRecord, likeRecordButton, dislikeRecordButton);
        recordLikeAndCommentsLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        recordLikeAndCommentsLayout.addComponents(likeRecordButton, dislikeRecordButton);

        Panel allCommentsPanel = null;
        //Comments
        Class c = currentRecord.getClass();
        if (WallRecord.class.equals(c)) {
            if (isFromNews) {
                allCommentsPanel = new CommentsPanel<>(currentRecord, WallRecordComment.class, recordLikeAndCommentsLayout, reloadTo, pageNumber, isFriendNews);
            } else {
                allCommentsPanel = new CommentsPanel<>(currentRecord, WallRecordComment.class, recordLikeAndCommentsLayout, reloadTo);
            }
        } else if (GroupRecord.class.equals(c)) {
            if (isFromNews) {
                allCommentsPanel = new CommentsPanel<>(currentRecord, GroupRecordComment.class, recordLikeAndCommentsLayout, reloadTo, pageNumber, isFriendNews);
            } else {
                allCommentsPanel = new CommentsPanel<>(currentRecord, GroupRecordComment.class, recordLikeAndCommentsLayout, reloadTo);
            }
        }

        if (recordAuthor.getObjectId().equals(currentProfileId)) {
            Button editRecordButton = new Button("Edit", VaadinIcons.EDIT);
            editRecordButton.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    editRecord(currentRecord, null);
                    UI.getCurrent().addWindow(updateWallRecordWindow);
                }
            });
            Button deleteRecordButton = new Button("Delete", VaadinIcons.DEL);
            deleteRecordButton.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    confirmDelete(currentRecord);
                    UI.getCurrent().addWindow(confirmDeleteWindow);
                }
            });
            recordLikeAndCommentsLayout.addComponents(editRecordButton, deleteRecordButton);
        }
        singleRecordLayout.addComponents(
                recordInfoLayout,
                new Label(currentRecord.getRecordText(), ContentMode.PREFORMATTED),
                recordLikeAndCommentsLayout,
                allCommentsPanel);
        setContent(singleRecordLayout);
    }

    private String createDescription(boolean isFromNews, AbstractRecord currentRecord, Profile author) {
        String result = author.getProfileName() + " " + author.getProfileSurname();
        if (isFromNews) {
            result += " on ";
            if (currentRecord instanceof WallRecord) {
                Profile recordOwner = CustomRestTemplate.getInstance().customGetForObject("/records/owner/" + currentRecord.getObjectId(), Profile.class);
                if (recordOwner.getObjectId().equals(author.getObjectId())) {
                    result += "own";
                } else {
                    result += recordOwner.getProfileName() + " " + recordOwner.getProfileSurname();
                }
            } else if(currentRecord instanceof GroupRecord) {
                result += ((GroupRecord) currentRecord).getParentGroup().getGroupName();
            }
            result += " wall";
        }
        return result;
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

    private void editRecord(AbstractRecord currentRecord, BaseEntity recordType) {
        updateWallRecordWindow = new Window();
        updateWallRecordWindow.setWidth("400px");
        updateWallRecordWindow.setHeight("250px");
        updateWallRecordWindow.setModal(true);

        VerticalLayout windowContent = new VerticalLayout();
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        TextArea textArea = new TextArea();
        textArea.setWidth("100%");
        Timestamp date = Timestamp.valueOf(
                new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

        if (currentRecord == null) {
            updateWallRecordWindow.setCaption("New record");
            Button add = new Button("Add");
            add.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    add.setComponentError(null);
                    Class c = recordType.getClass();
                    if (Profile.class.equals(c)) {
                        Profile receiver = (Profile) recordType;
                        WallRecord record = new WallRecord("WallRecord",
                                "From " + currentProfile.getProfileSurname() + " to " + receiver.getProfileSurname());
                        record.setRecordDate(date);
                        record.setRecordText(textArea.getValue());
                        record.setRecordAuthor(currentProfile);
                        record.setWallOwner(receiver);
                        createWallRecord(record);
                    } else if (Group.class.equals(c)) {
                        Group receiver = (Group) recordType;
                        GroupRecord record = new GroupRecord("GroupRecord",
                                "From " + currentProfile.getProfileSurname() + " to " + receiver.getGroupName());
                        record.setRecordDate(date);
                        record.setRecordText(textArea.getValue());
                        record.setRecordAuthor(currentProfile);
                        record.setParentGroup(receiver);
                        createGroupRecord(record);
                    }
                    Notification.show("Record added!");
                    updateWallRecordWindow.close();
                    checkAndReload();
                }
            });
            buttonsLayout.addComponentsAndExpand(add);
        } else {
            updateWallRecordWindow.setCaption("Edit record");
            textArea.setValue(currentRecord.getRecordText());
            Button edit = new Button("Edit");
            edit.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    edit.setComponentError(null);
                    currentRecord.setRecordText(textArea.getValue());
                    if (currentRecord.getClass().equals(WallRecord.class))
                        updateWallRecord((WallRecord) currentRecord);
                    else if (currentRecord.getClass().equals(GroupRecord.class))
                        updateGroupRecord((GroupRecord) currentRecord);
                    Notification.show("Record edited!");
                    updateWallRecordWindow.close();
                    checkAndReload();
                }
            });
            buttonsLayout.addComponentsAndExpand(edit);
        }
        Button cancel = new Button("Cancel", click -> updateWallRecordWindow.close());
        buttonsLayout.addComponentsAndExpand(cancel);
        windowContent.addComponents(textArea, buttonsLayout);

        updateWallRecordWindow.setContent(windowContent);
        updateWallRecordWindow.center();
    }

    private void checkAndReload() {
        if (isFromNews) {
            reloadPage(reloadTo, pageNumber, isFriendNews);
        } else {
            reloadPage(reloadTo);
        }
    }

    private List<AbstractLike> getRecordLikes(BigInteger recordID) {
        return Arrays.asList(CustomRestTemplate.getInstance().customGetForObject(
                "/likes/record/" + recordID, AbstractLike[].class));
    }

    private List<AbstractLike> getCommentLikes(BigInteger commentID) {
        return Arrays.asList(CustomRestTemplate.getInstance().customGetForObject(
                "/likes/comment/" + commentID, AbstractLike[].class));
    }

    private void createWallRecord(WallRecord record) {
        HttpEntity<WallRecord> request = new HttpEntity<>(record);
        CustomRestTemplate.getInstance().customPostForObject("/records/wall/add", request, WallRecord.class);
    }

    private void createGroupRecord(GroupRecord record) {
        HttpEntity<GroupRecord> request = new HttpEntity<>(record);
        CustomRestTemplate.getInstance().customPostForObject("/records/group/add", request, GroupRecord.class);
    }

    private void confirmDelete(BaseEntity entity) {
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

    private void createRecordLike(RecordLike like) {
        HttpEntity<RecordLike> request = new HttpEntity<>(like);
        CustomRestTemplate.getInstance().customPostForObject("/likes/record/add", request, RecordLike.class);
    }

    private void createCommentLike(CommentLike like) {
        HttpEntity<CommentLike> request = new HttpEntity<>(like);
        CustomRestTemplate.getInstance().customPostForObject("/likes/comment/add", request, CommentLike.class);
    }

    private void updateWallRecord(WallRecord record) {
        HttpEntity<WallRecord> request = new HttpEntity<>(record);
        CustomRestTemplate.getInstance().customPostForObject("/records/wall/update", request, WallRecord.class);
    }

    private void updateGroupRecord(GroupRecord record) {
        HttpEntity<GroupRecord> request = new HttpEntity<>(record);
        CustomRestTemplate.getInstance().customPostForObject("/records/group/update", request, GroupRecord.class);
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

    private void reloadPage(BaseEntity reloadTo, int pageNumber, boolean isFriendNews) {
        ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new NewsView(reloadTo.getObjectId(), pageNumber, isFriendNews));
    }

    private void reloadPage(BaseEntity reloadTo) {
        BigInteger destinationID = reloadTo.getObjectId();
        Class c = reloadTo.getClass();
        if (Profile.class.equals(c)) {
            ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(destinationID));
        } else if (Group.class.equals(c)) {
            ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new GroupUI(destinationID));
        } else if (PhotoAlbum.class.equals(c)) {
            ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new GalleryUI(destinationID));
        } else if (Advertisement.class.equals(c)) {
            ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(destinationID));
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
