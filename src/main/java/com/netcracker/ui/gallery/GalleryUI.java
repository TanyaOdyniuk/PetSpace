package com.netcracker.ui.gallery;

import com.netcracker.model.record.PhotoRecord;
import com.netcracker.ui.util.CustomRestTemplate;
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
    private final List<PhotoRecord> photos;

    @Autowired
    public GalleryUI(BigInteger albumId) {
        super();
        setSpacing(true);
        setSizeFull();
        photos = Arrays.asList(CustomRestTemplate.getInstance().
                customGetForObject("/gallery/" + albumId, PhotoRecord[].class));

        Panel galleryPanel = new Panel();
        VerticalLayout photosLayout = new VerticalLayout();
        GridLayout photosGrid = new GridLayout(3,(photos.size()/3)+1);
        photosGrid.setSpacing(true);

        for (int i = 0; i < photos.size(); i++) {
            Panel singlePhotoPanel = new Panel();
            Image singlePhotoImage = new Image();
            singlePhotoImage.setSource(new ExternalResource(photos.get(i).getPhoto()));
            singlePhotoImage.setWidth("300px");
            singlePhotoImage.setHeight("320px");
            singlePhotoPanel.setContent(singlePhotoImage);
            photosGrid.addComponent(singlePhotoPanel);
        }
        photosLayout.addComponents(new Label("album's name"), photosGrid);

        galleryPanel.setContent(photosLayout);
        addComponentsAndExpand(galleryPanel);
    }
}
