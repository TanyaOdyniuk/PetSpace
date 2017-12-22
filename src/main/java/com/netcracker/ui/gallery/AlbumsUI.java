package com.netcracker.ui.gallery;

import com.netcracker.asserts.ObjectAssert;
import com.netcracker.model.album.PhotoAlbum;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.StubVaadinUI;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

@SpringComponent
@UIScope
public class AlbumsUI extends  HorizontalLayout{
    public static GalleryUI galleryUI;
    Panel panel;
    VerticalLayout albumLayout;

    @Autowired
    public AlbumsUI(BigInteger petId) {
        super();
        addStyleName("v-scrollable");
        setHeight("100%");

        List<PhotoAlbum> albums = getAlbumList(petId);
        panel = new Panel();
        albumLayout = new VerticalLayout();

        VerticalLayout newAlbumLayout = new VerticalLayout();
        newAlbumLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        PopupView addAlbumPopupView = new PopupView(null, newAlbumLayout);

        addAlbumPopupView.addPopupVisibilityListener(event -> {
            if (event.isPopupVisible()) {
                newAlbumLayout.removeAllComponents();
                HorizontalLayout addAlbumButtonsLayout = new HorizontalLayout();
                TextField albumName = new TextField();
                TextArea description = new TextArea();
                Button addNotNullAlbum = new Button("Add album");
                addNotNullAlbum.addClickListener(new AbstractClickListener() {
                    @Override
                    public void buttonClickListener() {
                        addNotNullAlbum.setComponentError(null);
                        createAlbum(albumName.getValue(),description.getValue());
                        addAlbumPopupView.setPopupVisible(false);
                        Notification.show("You just added a new album!");
                    }
                });

                Button cancelAddingNewAlbum = new Button("Cancel", click ->
                        addAlbumPopupView.setPopupVisible(false));
                addAlbumButtonsLayout.addComponentsAndExpand(addNotNullAlbum, cancelAddingNewAlbum);
                newAlbumLayout.addComponentsAndExpand(
                        new Label("Enter album's name:"), albumName,new Label("Enter album's description:") ,description, addAlbumButtonsLayout);
            }
        });

        Button addAlbumButton = new Button("Add album", click ->
                addAlbumPopupView.setPopupVisible(true));
        addAlbumButton.setIcon(VaadinIcons.PLUS);

        albumLayout.addComponents(addAlbumButton, addAlbumPopupView);
        albumLayout.setComponentAlignment(addAlbumButton, Alignment.MIDDLE_RIGHT);
        albumLayout.setComponentAlignment(addAlbumPopupView, Alignment.MIDDLE_RIGHT);

        //MOVE TO GALLERY
        for(PhotoAlbum album : albums){
            Button albumName = PageElements.createClickedLabel(album.getPhotoAlbumName());
            albumName.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    galleryUI = new GalleryUI(album.getObjectId());
                    ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(galleryUI);
                }
            });
            albumLayout.addComponents(albumName);
        }
        panel.setContent(albumLayout);
        addComponent(panel);
    }

    private List<PhotoAlbum> getAlbumList(BigInteger petId){
        List<PhotoAlbum> albumList = Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/albums/" + petId, PhotoAlbum[].class));
        return albumList;
    }

    private void createAlbum(String albumName, String description) {
        ObjectAssert.isNullOrEmpty(albumName);
        PhotoAlbum createdAlbum = new PhotoAlbum();
        createdAlbum.setPhotoAlbumName(albumName);
        createdAlbum.setPhotoAlbumDesc(description);
        HttpEntity<PhotoAlbum> album = new HttpEntity<>(createdAlbum);
        PhotoAlbum dbAlbum = CustomRestTemplate.getInstance()
                .customPostForObject("/albums/add", album, PhotoAlbum.class);
        if(dbAlbum != null){
            Notification.show("You have just added a new album");
        }
    }
}
