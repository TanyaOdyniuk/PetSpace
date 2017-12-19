package com.netcracker.ui.gallery;

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
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

@SpringComponent
@UIScope
public class GalleryUI extends VerticalLayout {
    static HorizontalGallery horizontalGallery;

    @Autowired
    public GalleryUI(BigInteger albumId) {
        addStyleName("v-scrollable");
        setHeight("100%");
        List<PhotoRecord> photos = getPhotosFromAlbum(albumId);

        VerticalLayout photosLayout = new VerticalLayout();
        GridLayout photosGrid = new GridLayout(3,(photos.size()/3)+1);
        photosGrid.setSpacing(true);

        for (int i = 0; i < photos.size(); i++) {
            Panel singlePhotoPanel = new Panel();
            Image singlePhotoImage = new Image();
            singlePhotoImage.setSource(new ExternalResource(photos.get(i).getPhoto()));
            String buffLinkOfPhoto = photos.get(i).getPhoto();
            singlePhotoImage.setWidth(300,Unit.PIXELS);
            singlePhotoImage.setHeight(320,Unit.PIXELS);

            Integer cleckPhotoIndex = i;

            singlePhotoImage.addClickListener(new MouseEvents.ClickListener() {
                @Override
                public void click(MouseEvents.ClickEvent clickEvent) {
                     horizontalGallery = new HorizontalGallery(photos, cleckPhotoIndex);
                    addComponentsAndExpand(horizontalGallery);
                }
            });
            singlePhotoPanel.setContent(singlePhotoImage);
            photosGrid.addComponent(singlePhotoPanel);
        }

        Button addNewPhoto = new Button("Download a photo", VaadinIcons.PLUS);
        addNewPhoto.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
//                ((StubVaadinUI)UI.getCurrent()).changePrimaryAreaLayout(new NewPhotoUI());
            }
        });

        photosLayout.addComponents(addNewPhoto, photosGrid);
        photosLayout.setComponentAlignment(addNewPhoto, Alignment.TOP_RIGHT);
        addComponentsAndExpand(photosLayout);
    }


    private List<PhotoRecord> getPhotosFromAlbum(BigInteger albumId){
        List<PhotoRecord> photos = Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/gallery/" + albumId, PhotoRecord[].class));
        return photos;
    }
}
