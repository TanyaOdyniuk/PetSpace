package com.netcracker.ui.gallery;

import com.netcracker.asserts.ObjectAssert;
import com.netcracker.asserts.PetDataAssert;
import com.netcracker.model.record.PhotoRecord;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.MainUI;
import com.netcracker.ui.UIConstants;
import com.netcracker.ui.util.CustomRestTemplate;
import com.netcracker.ui.util.upload.ImageUpload;
import com.netcracker.ui.util.upload.UploadableComponent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.io.File;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.Date;

public class PhotoRecordFormUI extends Window implements UploadableComponent {
    private Image photo;
    private Boolean isFileResource;
    private String photoPath;

    PhotoRecordFormUI(BigInteger albumId) {
        super();
        this.isFileResource = false;

        setModal(true);
        setWidth("446px");
        setHeight("420px");
        setCaption("Creating new photo:");
        VerticalLayout windowContent = new VerticalLayout();

        GridLayout photoLayout = new GridLayout(2, 1);
        VerticalLayout photoContext = new VerticalLayout();
        TextField photoField = PageElements.createTextField("Photo", "Photo's URL");
        photo = PageElements.getNoImage();
        photo.setHeight("200px");
        photo.setWidth("200px");

        Button photoSelect = new Button("Set URL");
        photoSelect.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                photoSelect.setComponentError(null);
                updateImage(photoField.getValue(), photo);
            }
        });
        Upload uploadAvatar = new ImageUpload(UIConstants.PATH_TO_PHOTOS,
                albumId == null ? getCurrentUserProfileId() : albumId, this);
        photoSelect.setWidth("100%");
        uploadAvatar.setWidth("100%");

        photoContext.addComponents(photoField, photoSelect, uploadAvatar);
        photoContext.setComponentAlignment(photoSelect, Alignment.MIDDLE_CENTER);
        photoContext.setComponentAlignment(uploadAvatar, Alignment.MIDDLE_CENTER);

        photoLayout.addComponent(photo, 0, 0);
        photoLayout.addComponent(photoContext, 1, 0);


        HorizontalLayout addPhotoRecordButtonsLayout = new HorizontalLayout();
        TextField description = PageElements.createTextField("Enter photos description:", "photos description", false);
        description.setWidth("100%");
        Button addPhotoRecordButton = new Button("Add photo");
        addPhotoRecordButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                addPhotoRecordButton.setComponentError(null);
                createPhotoRecord(photoPath, description.getValue(), albumId);
                close();
                Notification.show("You have just added a new photo!");
            }
        });
        Button cancelAddingNewPhoto = new Button("Cancel", click -> close());
        addPhotoRecordButtonsLayout.addComponentsAndExpand(addPhotoRecordButton, cancelAddingNewPhoto);
        windowContent.addComponents(photoLayout, description, addPhotoRecordButtonsLayout);
        setContent(windowContent);
        center();
    }

    private void createPhotoRecord(String photoLink, String description, BigInteger albumId) {
        ObjectAssert.isNullOrEmpty(photoLink);

        if (!isFileResource)
            photoLink = PetDataAssert.assertAvatarURL(photoLink);

        PhotoRecord createdPhoto = new PhotoRecord();
        createdPhoto.setPhoto(photoLink);
        createdPhoto.setRecordText(description);
        createdPhoto.setRecordDate(Timestamp.valueOf(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()));

        HttpEntity<PhotoRecord> photo = new HttpEntity<>(createdPhoto);
        CustomRestTemplate.getInstance()
                .customPostForObject("/gallery/" + albumId + "/add", photo, PhotoRecord.class);
        ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new GalleryUI(albumId));
    }

    private void updateImage(String imageURL, Image imageToUpdate) {
        imageURL = PetDataAssert.assertAvatarURL(imageURL);
        imageToUpdate.setSource(new ExternalResource(imageURL));
        isFileResource = false;
        photoPath = imageURL;
    }

    @Override
    public void updateImage(File imageFile) {
        photo.setSource(new FileResource(imageFile));
        isFileResource = true;
        photoPath = imageFile.getPath();
    }

    private BigInteger getCurrentUserProfileId() {
        SecurityContext o = (SecurityContext) VaadinSession.getCurrent().getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        String login = o.getAuthentication().getPrincipal().toString();
        return CustomRestTemplate.getInstance().customPostForObject("/user/profileId", login, BigInteger.class);
    }
}
