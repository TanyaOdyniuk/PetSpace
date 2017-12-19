package com.netcracker.ui.gallery;

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
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

@SpringComponent
@UIScope
public class AlbumsUI extends  HorizontalLayout{
    public static GalleryUI galleryUI;
    public static NewAlbumUI newAlbumUI;
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
        Button addNewAlbum = new Button("Create new album", VaadinIcons.PLUS);
        addNewAlbum.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
//                ((StubVaadinUI)UI.getCurrent()).changePrimaryAreaLayout(new NewAlbumUI());
//                 newAlbumUI = new NewAlbumUI();
//                addComponentsAndExpand(newAlbumUI);
            }
        });
        albumLayout.addComponent(addNewAlbum);
        for(PhotoAlbum album : albums){
            HorizontalLayout petRecord = new HorizontalLayout();
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
}
