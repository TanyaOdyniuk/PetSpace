package com.netcracker.ui.gallery;

import com.netcracker.model.record.PhotoRecord;
import com.netcracker.ui.AbstractClickListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.*;
import java.util.List;

public class HorizontalGallery extends HorizontalLayout {
    Panel panel;
    HorizontalLayout galleryLayout;
    Image image;
    HorizontalLayout imageLayout;
    Integer index;

    public HorizontalGallery(List<PhotoRecord> list, Integer i){
        panel = new Panel();
        galleryLayout = new HorizontalLayout();
        imageLayout = new HorizontalLayout();
        index = i;

        image = new Image();
        image.setSource(new ExternalResource(list.get(index).getPhoto()));
        image.setDescription(list.get(index).getDescription());
        panel.setWidth(400,Unit.PIXELS);
        panel.setHeight(500,Unit.PIXELS);


        Button arrowLeft = new Button();
        arrowLeft.setWidth(10,Unit.PIXELS);
        arrowLeft.setHeight(40,Unit.PIXELS);
        arrowLeft.setIcon(VaadinIcons.ARROW_LEFT);
        arrowLeft.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                index--;
                if(index < 0)
                    index = list.size() - 1;
                image.setSource(new ExternalResource(list.get(index).getPhoto()));
                imageLayout.addComponents(image);
            }
        });


        Button arrowRight = new Button();
        arrowRight.setWidth(10,Unit.PIXELS);
        arrowRight.setHeight(40,Unit.PIXELS);
        arrowRight.setIcon(VaadinIcons.ARROW_RIGHT);
        arrowRight.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                index++;
                if(index == list.size())
                    index = 0;
                image.setSource(new ExternalResource(list.get(index).getPhoto()));
                imageLayout.addComponents(image);
            }
        });

        Button closeHG = new Button();
        closeHG.setIcon(VaadinIcons.CLOSE);
        closeHG.setWidth(3,Unit.PIXELS);
        closeHG.setHeight(25,Unit.PIXELS);
        closeHG.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                AlbumsUI.galleryUI.removeComponent(GalleryUI.horizontalGallery);
            }
        });

        imageLayout.addComponent(image);
        galleryLayout.addComponents(arrowLeft, imageLayout, arrowRight, closeHG);

        galleryLayout.setComponentAlignment(arrowLeft, Alignment.MIDDLE_LEFT);
        galleryLayout.setComponentAlignment(arrowRight, Alignment.MIDDLE_RIGHT);
        galleryLayout.setComponentAlignment(closeHG, Alignment.TOP_RIGHT);

        panel.setContent(galleryLayout);
        addComponentsAndExpand(panel);
    }
}



