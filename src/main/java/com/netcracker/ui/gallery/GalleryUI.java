package com.netcracker.ui.gallery;

import com.netcracker.model.record.PhotoRecord;
import com.netcracker.model.user.Profile;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.MainUI;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.event.MouseEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class GalleryUI extends VerticalLayout{
    private HorizontalGallery horizontalGallery;

    public GalleryUI(BigInteger albumId) {
        super();

        Panel mainPanel = new Panel();
        mainPanel.addStyleName("v-scrollable");
        mainPanel.setHeight("100%");
        List<PhotoRecord> photos = getPhotosFromAlbum(albumId);

        VerticalLayout photosLayout = new VerticalLayout();
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

        BigInteger suggestId = getCurrentUserProfileId();
        BigInteger isId = getUserProfileIdOfAlbum(albumId);

        Button deleteAlbum = new Button("Delete the album");
        deleteAlbum.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                if(isId.equals(suggestId))
                    CustomRestTemplate.getInstance().customGetForObject("/albums/delete/" + albumId, Void.class);
                ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new AlbumsUI(isId));
            }
        });

        HorizontalLayout buttonsLayuot = new HorizontalLayout();
        if(isId.equals(suggestId))
            buttonsLayuot.addComponents(addNewPhotoRecordButton, deleteAlbum);
        buttonsLayuot.setHeight("60px");
        photosLayout.addComponents(buttonsLayuot, photosGrid);
        mainPanel.setContent(photosLayout);
        addComponent(mainPanel);
    }

    private BigInteger getUserProfileIdOfAlbum(BigInteger albumId){
        Profile p = CustomRestTemplate.getInstance().customGetForObject("/albums/profileId/" + albumId, Profile.class);
        return p.getObjectId();
    }

    private BigInteger getCurrentUserProfileId() {
        SecurityContext o = (SecurityContext) VaadinSession.getCurrent().getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        String login = o.getAuthentication().getPrincipal().toString();
        return CustomRestTemplate.getInstance().customPostForObject("/user/profileId", login, BigInteger.class);
    }

    private List<PhotoRecord> getPhotosFromAlbum(BigInteger albumId) {
        return Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/gallery/" + albumId, PhotoRecord[].class));
    }
}
