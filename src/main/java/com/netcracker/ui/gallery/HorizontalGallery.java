package com.netcracker.ui.gallery;

import com.netcracker.model.BaseEntity;
import com.netcracker.model.album.PhotoAlbum;
import com.netcracker.model.comment.AbstractComment;
import com.netcracker.model.comment.PhotoRecordComment;
import com.netcracker.model.like.AbstractLike;
import com.netcracker.model.like.CommentLike;
import com.netcracker.model.like.RecordLike;
import com.netcracker.model.record.AbstractRecord;
import com.netcracker.model.record.PhotoRecord;
import com.netcracker.model.user.Profile;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.CommentsPanel;
import com.netcracker.ui.MainUI;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class HorizontalGallery extends VerticalLayout {
    private static final Resource likeOn = VaadinIcons.THUMBS_UP;
    private static final Resource likeOff = VaadinIcons.THUMBS_UP_O;
    private static final Resource dislikeOn = VaadinIcons.THUMBS_DOWN;
    private static final Resource dislikeOff = VaadinIcons.THUMBS_DOWN_O;
    private Panel leftPanel;
    private HorizontalLayout galleryLayout;
    private Image image;
    private BigInteger currentAlbumId;
    private HorizontalLayout imageLayout;
    private Window updateWallRecordWindow;
    private Window confirmDeleteWindow;
    private Integer index;
    private Profile currentProfile;
    private BigInteger currentProfileId;

    public HorizontalGallery(BigInteger photoRecordId) {
        setCurrentProfile();

        HorizontalLayout main = new HorizontalLayout();
        leftPanel = new Panel();
        galleryLayout = new HorizontalLayout();
        imageLayout = new HorizontalLayout();

        currentAlbumId = getCurrentAlbumId(photoRecordId);
        List<PhotoRecord> list = getAlbumPhotos(currentAlbumId);
        for (int i = 0; i < list.size(); i++) {
            if (photoRecordId.equals(list.get(i).getObjectId())) {
                index = i;
                break;
            }
        }

        PhotoRecord currentRecord = list.get(index);
        image = new Image();
        PageElements.setDefaultImageSource(image, currentRecord.getPhoto());
        image.setDescription(currentRecord.getDescription());
        image.setHeight(400, Unit.PIXELS);
        image.setWidth(400, Unit.PIXELS);

        Button arrowLeft = new Button();
        arrowLeft.setWidth(10, Unit.PIXELS);
        arrowLeft.setHeight(40, Unit.PIXELS);
        arrowLeft.setIcon(VaadinIcons.ARROW_LEFT);
        arrowLeft.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                index--;
                if (index < 0)
                    index = list.size() - 1;
                ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new HorizontalGallery(list.get(index).getObjectId()));
            }
        });

        Button arrowRight = new Button();
        arrowRight.setWidth(10, Unit.PIXELS);
        arrowRight.setHeight(40, Unit.PIXELS);
        arrowRight.setIcon(VaadinIcons.ARROW_RIGHT);
        arrowRight.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                index++;
                if (index == list.size())
                    index = 0;
                ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new HorizontalGallery(list.get(index).getObjectId()));
            }
        });

        imageLayout.addComponent(image);
        galleryLayout.addComponents(arrowLeft, imageLayout, arrowRight);
        galleryLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        galleryLayout.setComponentAlignment(arrowLeft, Alignment.MIDDLE_LEFT);
        galleryLayout.setComponentAlignment(arrowRight, Alignment.MIDDLE_RIGHT);

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        mainLayout.addComponents(galleryLayout, PageElements.getSeparator());

        if (currentRecord.getRecordText() != null)
            mainLayout.addComponent(new Label("Description: " + currentRecord.getRecordText()));
        mainLayout.addComponent(new Label("Date: " + currentRecord.getRecordDate().toString()));

        if (currentProfileId.equals(getAlbumAuthor(currentRecord.getObjectId()).getObjectId())) {
            HorizontalLayout manage = new HorizontalLayout();
            Button edit = new Button("Edit", VaadinIcons.EDIT);
            edit.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    editRecord(currentRecord);
                    UI.getCurrent().addWindow(updateWallRecordWindow);
                }
            });
            Button delete = new Button("Delete", VaadinIcons.DEL_A);
            delete.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    confirmDelete(currentRecord);
                    UI.getCurrent().addWindow(confirmDeleteWindow);
                }
            });
            manage.addComponents(edit, delete);
            mainLayout.addComponents(PageElements.getSeparator(), manage);
        }

        HorizontalLayout likesAndComments = new HorizontalLayout();
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
        likesAndComments.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        likesAndComments.addComponents(likeRecordButton, dislikeRecordButton);

        Panel commentsLikes = new CommentsPanel<>(currentRecord, PhotoRecordComment.class, likesAndComments, currentRecord);
        VerticalLayout rightUpperPart = new VerticalLayout();
        rightUpperPart.addComponents(new Label("Comments and likes"), PageElements.getSeparator());
        rightUpperPart.addComponents(likesAndComments);
        VerticalLayout rightLowerPart = new VerticalLayout();
        rightLowerPart.addComponentsAndExpand(commentsLikes);

        VerticalLayout rightPart = new VerticalLayout();
        rightPart.addComponent(rightUpperPart);
        rightPart.addComponentsAndExpand(rightLowerPart);
        Panel rightPanel = new Panel();
        rightPanel.setContent(rightPart);
        rightPanel.setSizeFull();

        leftPanel.setContent(mainLayout);
        main.addComponents(leftPanel);
        main.addComponents(rightPanel);
        main.setExpandRatio(main.getComponent(0), 2.0f);
        main.setExpandRatio(main.getComponent(1), 3.5f);
        addComponent(main);
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

    private void editRecord(AbstractRecord currentRecord) {
        updateWallRecordWindow = new Window();
        updateWallRecordWindow.setWidth("400px");
        updateWallRecordWindow.setHeight("250px");
        updateWallRecordWindow.setModal(true);

        VerticalLayout windowContent = new VerticalLayout();
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        TextArea textArea = new TextArea();
        textArea.setWidth("100%");

        updateWallRecordWindow.setCaption("Edit record");
        textArea.setValue(currentRecord.getRecordText() == null ? "" : currentRecord.getRecordText());
        Button edit = new Button("Edit");
        edit.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                edit.setComponentError(null);
                currentRecord.setRecordText(textArea.getValue());
                updatePhotoRecord((PhotoRecord) currentRecord);
                Notification.show("Record edited!");
                updateWallRecordWindow.close();
                ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new HorizontalGallery(currentRecord.getObjectId()));
            }
        });
        buttonsLayout.addComponentsAndExpand(edit);

        Button cancel = new Button("Cancel", click -> updateWallRecordWindow.close());
        buttonsLayout.addComponentsAndExpand(cancel);
        windowContent.addComponents(textArea, buttonsLayout);

        updateWallRecordWindow.setContent(windowContent);
        updateWallRecordWindow.center();
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

    private void createRecordLike(RecordLike like) {
        HttpEntity<RecordLike> request = new HttpEntity<>(like);
        CustomRestTemplate.getInstance().customPostForObject("/likes/record/add", request, RecordLike.class);
    }

    private void createCommentLike(CommentLike like) {
        HttpEntity<CommentLike> request = new HttpEntity<>(like);
        CustomRestTemplate.getInstance().customPostForObject("/likes/comment/add", request, CommentLike.class);
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
                }
                confirmDeleteWindow.close();
                ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new GalleryUI(currentAlbumId));
            }
        });

        Button cancel = new Button("Cancel", click -> confirmDeleteWindow.close());
        buttonsLayout.addComponentsAndExpand(delete, cancel);
        windowContent.addComponents(new Label("Are you sure?"), buttonsLayout);
        confirmDeleteWindow.setContent(windowContent);
        confirmDeleteWindow.center();
    }

    private List<PhotoRecord> getAlbumPhotos(BigInteger albumId) {
        return Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/gallery/" + albumId, PhotoRecord[].class));
    }

    private List<AbstractLike> getRecordLikes(BigInteger recordID) {
        return Arrays.asList(CustomRestTemplate.getInstance().customGetForObject(
                "/likes/record/" + recordID, AbstractLike[].class));
    }

    private List<AbstractLike> getCommentLikes(BigInteger commentID) {
        return Arrays.asList(CustomRestTemplate.getInstance().customGetForObject(
                "/likes/comment/" + commentID, AbstractLike[].class));
    }

    private Profile getAlbumAuthor(BigInteger recordId) {
        return CustomRestTemplate.getInstance().customGetForObject(
                "/gallery/profileId/" + recordId, Profile.class);
    }

    private BigInteger getCurrentAlbumId(BigInteger photoRecordId) {
        return CustomRestTemplate.getInstance().customGetForObject(
                "/albums/contains/" + photoRecordId, PhotoAlbum.class).getObjectId();
    }

    private void updatePhotoRecord(PhotoRecord record) {
        HttpEntity<PhotoRecord> request = new HttpEntity<>(record);
        CustomRestTemplate.getInstance().customPostForObject("/records/photo/update", request, PhotoRecord.class);
    }

    private void deleteRecord(AbstractRecord record) {
        HttpEntity<AbstractRecord> request = new HttpEntity<>(record);
        CustomRestTemplate.getInstance().customPostForObject("/records/delete", request, AbstractRecord.class);
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



