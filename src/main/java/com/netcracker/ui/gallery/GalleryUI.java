package com.netcracker.ui.gallery;

import com.netcracker.asserts.ObjectAssert;
import com.netcracker.model.record.PhotoRecord;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.StubVaadinUI;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.event.MouseEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringComponent
@UIScope
public class GalleryUI extends VerticalLayout {
    static HorizontalGallery horizontalGallery;
    static VerticalLayout photosLayout;

    @Autowired
    public GalleryUI(BigInteger albumId) {
        addStyleName("v-scrollable");
        setHeight("100%");
        List<PhotoRecord> photos = getPhotosFromAlbum(albumId);

        photosLayout = new VerticalLayout();
        GridLayout photosGrid = new GridLayout(3,(photos.size()/3)+1);
        photosGrid.setSpacing(true);

        for (int i = 0; i < photos.size(); i++) {
            Panel singlePhotoPanel = new Panel();
            Image singlePhotoImage = new Image();
            singlePhotoImage.setSource(new ExternalResource(photos.get(i).getPhoto()));
            singlePhotoImage.setWidth(300,Unit.PIXELS);
            singlePhotoImage.setHeight(320,Unit.PIXELS);
            Integer clickPhotoIndex = i;
            singlePhotoImage.addClickListener(new MouseEvents.ClickListener() {
                @Override
                public void click(MouseEvents.ClickEvent clickEvent) {
                    horizontalGallery = new HorizontalGallery(photos, clickPhotoIndex);
                    addComponentsAndExpand(horizontalGallery);
                    photosLayout.detach();
                }
            });
            singlePhotoPanel.setContent(singlePhotoImage);
            photosGrid.addComponent(singlePhotoPanel);
        }

        //POPUP NEW PHOTO
        VerticalLayout newPhotoLayout = new VerticalLayout();
        newPhotoLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        PopupView addPhotoRecordView = new PopupView(/*"Add album",*/null, newPhotoLayout);
        addPhotoRecordView.addPopupVisibilityListener(event -> {
            if (event.isPopupVisible()) {
                newPhotoLayout.removeAllComponents();
                HorizontalLayout addPhotoButtonsLayout = new HorizontalLayout();
                TextField photoLink = new TextField();
                TextArea description = new TextArea();
                Button addNotNullPhoto = new Button("Add photo");
                addNotNullPhoto.addClickListener(new AbstractClickListener() {
                    @Override
                    public void buttonClickListener() {
                        addNotNullPhoto.setComponentError(null);
                        createPhotoRecord(photoLink.getValue(), description.getValue() , albumId);
                        addPhotoRecordView.setPopupVisible(false);
                        Notification.show("You have just added a new photo!");
                    }
                });

                Button cancelAddingNewAlbum = new Button("Cancel", click ->
                        addPhotoRecordView.setPopupVisible(false));
                addPhotoButtonsLayout.addComponentsAndExpand(addNotNullPhoto, cancelAddingNewAlbum);
                newPhotoLayout.addComponentsAndExpand(
                        new Label("Enter the link to the photo:"), photoLink, new Label("Enter photos description:") ,description, addPhotoButtonsLayout);
            }
        });//---------------------------------POPUP NEW PHOTO

        Button addPhotoButton = new Button("Add photo", click ->
                addPhotoRecordView.setPopupVisible(true));
        addPhotoButton.setIcon(VaadinIcons.PLUS);

        photosLayout.addComponents(addPhotoButton, addPhotoRecordView, photosGrid);
//        photosLayout.setComponentAlignment(/*addNewPhoto*/button, Alignment.TOP_RIGHT);

        addComponentsAndExpand(photosLayout);
    }



    private List<PhotoRecord> getPhotosFromAlbum(BigInteger albumId){
        List<PhotoRecord> photos = Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/gallery/" + albumId, PhotoRecord[].class));
        return photos;
    }

    private void createPhotoRecord(String photoLink, String description, BigInteger albumId) {
        ObjectAssert.isNullOrEmpty(photoLink);
        PhotoRecord createdPhoto = new PhotoRecord();
        createdPhoto.setPhoto(photoLink);
        createdPhoto.setDescription(description);
        createdPhoto.setPhotoUploadDate(new java.sql.Date(new Date().getTime()));
        HttpEntity<PhotoRecord> photo = new HttpEntity<>(createdPhoto);
        PhotoRecord dbAlbum = CustomRestTemplate.getInstance()
                .customPostForObject("/gallery/"+ albumId +"/add", photo, PhotoRecord.class);
//        if(dbAlbum != null){
//            Notification.show("You just added a new photo");
//        }
    }
}
