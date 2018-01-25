package com.netcracker.ui.gallery;

import com.netcracker.asserts.ObjectAssert;
import com.netcracker.model.record.PhotoRecord;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.MainUI;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.event.MouseEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.*;
import org.springframework.http.HttpEntity;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class GalleryUI extends VerticalLayout {
    private HorizontalGallery horizontalGallery;
    private VerticalLayout photosLayout;
    private Window newPhotoRecordWindow;
    private BigInteger albumId;

    public GalleryUI(BigInteger albumId) {
        this.albumId = albumId;
        addStyleName("v-scrollable");
        setHeight("100%");
        List<PhotoRecord> photos = getPhotosFromAlbum(albumId);

        photosLayout = new VerticalLayout();
        GridLayout photosGrid = new GridLayout(3, (photos.size() / 3) + 1);
        photosGrid.setSpacing(true);

        for (int i = 0; i < photos.size(); i++) {
            Panel singlePhotoPanel = new Panel();
            Image singlePhotoImage = new Image();
            singlePhotoImage.setSource(new ExternalResource(photos.get(i).getPhoto()));
            singlePhotoImage.setWidth(300, Unit.PIXELS);
            singlePhotoImage.setHeight(320, Unit.PIXELS);
            Integer clickPhotoIndex = i;
            singlePhotoImage.addClickListener(new MouseEvents.ClickListener() {
                @Override
                public void click(MouseEvents.ClickEvent clickEvent) {
                     horizontalGallery = new HorizontalGallery(photos, clickPhotoIndex);
                    horizontalGallery.setPositionX(250);
                    horizontalGallery.setPositionY(15);
                    UI.getCurrent().addWindow(horizontalGallery);
                }
            });
            singlePhotoPanel.setContent(singlePhotoImage);
            photosGrid.addComponent(singlePhotoPanel);
        }

        getNewPhotoRecord();
        Button addNewPhotoRecordButton = new Button("Add photo", VaadinIcons.PLUS);
        addNewPhotoRecordButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                UI.getCurrent().addWindow(newPhotoRecordWindow);
            }
        });
        photosLayout.addComponents(addNewPhotoRecordButton, photosGrid);
        addComponents(photosLayout);
    }


    private void getNewPhotoRecord() {
        newPhotoRecordWindow = new Window();
        newPhotoRecordWindow.setWidth("400px");
        newPhotoRecordWindow.setHeight("250px");
        newPhotoRecordWindow.setCaption("Creating new photo:");
        VerticalLayout windowContent = new VerticalLayout();
        HorizontalLayout addPhotoRecordButtonsLayout = new HorizontalLayout();
        TextField photoLink = PageElements.createTextField("Enter photos link:", "photos link", true);
        photoLink.setWidth("100%");
        TextField description = PageElements.createTextField("Enter photos description:", "photos description", false);
        description.setWidth("100%");
        Button addPhotoRecordButton = new Button("Add photo");
        addPhotoRecordButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                addPhotoRecordButton.setComponentError(null);
                createPhotoRecord(photoLink.getValue(), description.getValue(), albumId);
                newPhotoRecordWindow.close();
                Notification.show("You have just added a new photo!");
            }
        });
        Button cancelAddingNewPhoto = new Button("Cancel", click -> newPhotoRecordWindow.close());
        addPhotoRecordButtonsLayout.addComponentsAndExpand(addPhotoRecordButton, cancelAddingNewPhoto);
        windowContent.addComponents(photoLink, description, addPhotoRecordButtonsLayout);
        newPhotoRecordWindow.setContent(windowContent);
        newPhotoRecordWindow.center();
    }

    private List<PhotoRecord> getPhotosFromAlbum(BigInteger albumId) {
        return Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/gallery/" + albumId, PhotoRecord[].class));
    }

    private void createPhotoRecord(String photoLink, String description, BigInteger albumId) {
        ObjectAssert.isNullOrEmpty(photoLink);
        PhotoRecord createdPhoto = new PhotoRecord();
        createdPhoto.setPhoto(photoLink);
        createdPhoto.setRecordText(description);
        createdPhoto.setRecordDate(Timestamp.valueOf(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()));
        HttpEntity<PhotoRecord> photo = new HttpEntity<>(createdPhoto);
        CustomRestTemplate.getInstance()
                .customPostForObject("/gallery/" + albumId + "/add", photo, PhotoRecord.class);
        ((MainUI)UI.getCurrent()).changePrimaryAreaLayout(new GalleryUI(albumId));
    }
}
