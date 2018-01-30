package com.netcracker.ui.gallery;

import com.netcracker.model.record.PhotoRecord;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.event.MouseEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class GalleryUI extends VerticalLayout{
    private HorizontalGallery horizontalGallery;
    private VerticalLayout photosLayout;

    public GalleryUI(BigInteger albumId) {
        addStyleName("v-scrollable");
        setHeight("100%");
        List<PhotoRecord> photos = getPhotosFromAlbum(albumId);

        photosLayout = new VerticalLayout();
        GridLayout photosGrid = new GridLayout(3, (photos.size() / 3) + 1);
        photosGrid.setSpacing(true);

        for (int i = 0; i < photos.size(); i++) {
            Panel singlePhotoPanel = new Panel();
            Image singlePhotoImage = new Image();
            PageElements.setDefaultImageSource(singlePhotoImage, photos.get(i).getPhoto());
            singlePhotoImage.setWidth(300, Unit.PIXELS);
            singlePhotoImage.setHeight(320, Unit.PIXELS);
            Integer clickPhotoIndex = i;
            singlePhotoImage.addClickListener((MouseEvents.ClickListener) clickEvent -> {
                 horizontalGallery = new HorizontalGallery(photos, clickPhotoIndex);
                UI.getCurrent().addWindow(horizontalGallery);
            });
            singlePhotoPanel.setContent(singlePhotoImage);
            photosGrid.addComponent(singlePhotoPanel);
        }

        Button addNewPhotoRecordButton = new Button("Add photo", VaadinIcons.PLUS);
        addNewPhotoRecordButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                UI.getCurrent().addWindow(new PhotoRecordFormUI(albumId));
            }
        });
        photosLayout.addComponents(addNewPhotoRecordButton, photosGrid);
        addComponents(photosLayout);
    }

    private List<PhotoRecord> getPhotosFromAlbum(BigInteger albumId) {
        return Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/gallery/" + albumId, PhotoRecord[].class));
    }
}
