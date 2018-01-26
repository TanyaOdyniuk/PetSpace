package com.netcracker.ui.gallery;

import com.netcracker.model.album.PhotoAlbum;
import com.netcracker.model.comment.PhotoRecordComment;
import com.netcracker.model.record.PhotoRecord;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.CommentsPanel;
import com.netcracker.ui.PageElements;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;

import java.util.List;

class HorizontalGallery extends Window {
    private Panel panel;
    private HorizontalLayout galleryLayout;
    private Image image;
    private HorizontalLayout imageLayout;
    private Integer index;

    HorizontalGallery(List<PhotoRecord> list, Integer i) {
        setModal(true);
        setResizable(false);
        center();
        panel = new Panel();
        galleryLayout = new HorizontalLayout();
        imageLayout = new HorizontalLayout();
        index = i;

        PhotoRecord currentRecord = list.get(index);
        image = new Image();
        PageElements.setImageSource(image, currentRecord.getPhoto());
        image.setDescription(currentRecord.getDescription());

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
                PageElements.setImageSource(image, list.get(index).getPhoto());
                image.setDescription(list.get(index).getDescription());
                imageLayout.addComponents(image);
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
                PageElements.setImageSource(image, list.get(index).getPhoto());
                image.setDescription(list.get(index).getDescription());
                imageLayout.addComponents(image);
            }
        });


        imageLayout.addComponent(image);

        galleryLayout.addComponents(arrowLeft, imageLayout, arrowRight);

        galleryLayout.setComponentAlignment(arrowLeft, Alignment.MIDDLE_LEFT);
        galleryLayout.setComponentAlignment(arrowRight, Alignment.MIDDLE_RIGHT);

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.addComponent(galleryLayout);

        Panel commentsLikes = new CommentsPanel<>(currentRecord, PhotoRecordComment.class, mainLayout, new PhotoAlbum());
        mainLayout.addComponent(commentsLikes);

        Panel galleryLayoutPanel = new Panel();
        galleryLayoutPanel.setContent(mainLayout);
        panel.setContent(mainLayout);
        setContent(panel);
    }
}



