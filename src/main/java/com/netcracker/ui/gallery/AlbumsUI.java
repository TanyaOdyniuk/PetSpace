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

public class AlbumsUI extends  HorizontalLayout{
    private static GalleryUI galleryUI;
    private Panel panel;
    private VerticalLayout albumLayout;
    private Window newAlbumWindow;
    private BigInteger petId;

    public AlbumsUI(BigInteger petId) {
        super();
        this.petId = petId;
        addStyleName("v-scrollable");
        setHeight("100%");

        List<PhotoAlbum> albums = getAlbumList(petId);
        panel = new Panel();
        albumLayout = new VerticalLayout();

        getNewAlbumWindow();
        Button addNewAlbumButton = new Button("Add album", VaadinIcons.PLUS);
        addNewAlbumButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                UI.getCurrent().addWindow(newAlbumWindow);
            }
        });
        albumLayout.addComponent(addNewAlbumButton);

        //OPEN GALLERY
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

    private void getNewAlbumWindow(){
        newAlbumWindow = new Window();
        newAlbumWindow.setWidth("400px");
        newAlbumWindow.setHeight("250px");
        newAlbumWindow.setCaption("Creating new album:");
        VerticalLayout windowContent = new VerticalLayout();
        HorizontalLayout addAlbumButtonsLayout = new HorizontalLayout();

        TextField albumName = PageElements.createTextField("Enter album's name:", "album's name", true);
        albumName.setWidth("100%");
        TextField description = PageElements.createTextField("Enter album's description:", "album's description", false);
        description.setWidth("100%");

        Button addAlbumButton = new Button("Add album");
        addAlbumButton.addClickListener(new AbstractClickListener() {
                    @Override
                    public void buttonClickListener() {
                        addAlbumButton.setComponentError(null);
                        createAlbum(albumName.getValue(),description.getValue(), petId);
                        newAlbumWindow.close();
                        Notification.show("You just added a new album!");
                    }
                });
        Button cancelAddingNewAlbum = new Button("Cancel", click -> newAlbumWindow.close());
        addAlbumButtonsLayout.addComponentsAndExpand(addAlbumButton, cancelAddingNewAlbum);
        windowContent.addComponents(albumName, description, addAlbumButtonsLayout);

        newAlbumWindow.setContent(windowContent);
        newAlbumWindow.center();
    }

    private List<PhotoAlbum> getAlbumList(BigInteger petId){
        return Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/albums/" + petId, PhotoAlbum[].class));
    }

    private void createAlbum(String albumName, String description, BigInteger petId) {
        ObjectAssert.isNullOrEmpty(albumName);
        PhotoAlbum createdAlbum = new PhotoAlbum();
        createdAlbum.setPhotoAlbumName(albumName);
        createdAlbum.setPhotoAlbumDesc(description);
        HttpEntity<PhotoAlbum> album = new HttpEntity<>(createdAlbum);
        PhotoAlbum dbAlbum = CustomRestTemplate.getInstance()
                .customPostForObject("/albums/"+ /*petId*/ 22 +"/add", album, PhotoAlbum.class);
    }
}
