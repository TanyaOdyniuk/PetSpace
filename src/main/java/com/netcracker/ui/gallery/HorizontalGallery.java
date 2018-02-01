package com.netcracker.ui.gallery;

import com.netcracker.model.album.PhotoAlbum;
import com.netcracker.model.comment.PhotoRecordComment;
import com.netcracker.model.record.PhotoRecord;
import com.netcracker.model.user.Profile;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.CommentsPanel;
import com.netcracker.ui.MainUI;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class HorizontalGallery extends VerticalLayout {
    private Panel panel;
    private HorizontalLayout galleryLayout;
    private Image image;
    private HorizontalLayout imageLayout;
    private Integer index;

    public HorizontalGallery(BigInteger photoRecordId) {

        HorizontalLayout main = new HorizontalLayout();
        panel = new Panel();
        galleryLayout = new HorizontalLayout();
        imageLayout = new HorizontalLayout();

        List<PhotoRecord> list = getPhotosFromAlbumByPhotoRecord(photoRecordId);
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

        galleryLayout.setComponentAlignment(arrowLeft, Alignment.MIDDLE_LEFT);
        galleryLayout.setComponentAlignment(arrowRight, Alignment.MIDDLE_RIGHT);

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.addComponent(galleryLayout);


        mainLayout.addComponents(new Label("Description: " + currentRecord.getRecordText()),
                new Label("Date: " + currentRecord.getRecordDate().toString()));

        if(getCurrentProfile().getObjectId().equals(getAlbumAuthor(currentRecord.getObjectId()).getObjectId())){
            HorizontalLayout manage = new HorizontalLayout();
            Button edit = new Button("Edit", VaadinIcons.EDIT);
            //to be done
            Button delete = new Button("Delete", VaadinIcons.DEL_A);
            //to be done
            manage.addComponents(edit, delete);
            mainLayout.addComponent(manage);
        }

        HorizontalLayout likesAndComments = new HorizontalLayout();
        Button like = new Button("Like");
        Button dislike = new Button("Dislike");
        likesAndComments.addComponents(like, dislike);
        Panel commentsLikes = new CommentsPanel<>(currentRecord, PhotoRecordComment.class, likesAndComments, currentRecord);

        VerticalLayout rightPart = new VerticalLayout();
        rightPart.addComponent(likesAndComments);
        rightPart.addComponentsAndExpand(commentsLikes);

        mainLayout.setSizeFull();
        panel.setContent(mainLayout);
        main.addComponents(panel, rightPart);
        addComponent(main);
    }

    private List<PhotoRecord> getPhotosFromAlbumByPhotoRecord(BigInteger recordId) {
        return Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/gallery/fromrecord/" + recordId, PhotoRecord[].class));
    }

    private Profile getAlbumAuthor(BigInteger recordId) {
        return CustomRestTemplate.getInstance().customGetForObject(
                "/gallery/profileId/" + recordId, Profile.class);
    }

    private Profile getCurrentProfile() {
        SecurityContext o = (SecurityContext) VaadinSession.getCurrent().getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        String login = o.getAuthentication().getPrincipal().toString();
        BigInteger currentProfileId = CustomRestTemplate.getInstance().customPostForObject("/user/profileId", login, BigInteger.class);
        return CustomRestTemplate.getInstance().
                customGetForObject("/profile/" + currentProfileId, Profile.class);
    }
}



