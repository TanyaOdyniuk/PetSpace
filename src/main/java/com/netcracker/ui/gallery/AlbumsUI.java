package com.netcracker.ui.gallery;

import com.netcracker.model.album.PhotoAlbum;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.StubVaadinUI;
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
public class AlbumsUI extends Panel{
//    private final List<PhotoAlbum> albums;

    public static GalleryUI galleryUI;

    @Autowired
    public AlbumsUI(BigInteger petId) {
        super();
        setWidth("100%");
        setHeight("100%");
//        setSizeFull();
//        albums = Arrays.asList(CustomRestTemplate.getInstance().
//                customGetForObject("/albums/" + albumsId, PhotoAlbum[].class));
        List<PhotoAlbum> albums = getAlbumList(petId);
        VerticalLayout albumLayout = new VerticalLayout();
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
//            galleryUI = new GalleryUI(BigInteger.valueOf(26));

            albumLayout.addComponents(albumName);
        }
        setContent(albumLayout);



//        Panel albumsPanel = new Panel();
//        VerticalLayout layout = new VerticalLayout();
//        layout.addComponents(new Label("Your albums"));
//
//        for (PhotoAlbum album : albums) {
//            Link link = new Link(album.getPhotoAlbumName(),new ExternalResource("http://localhost:8888/gallery"));
////            Label label = new Label(album.getPhotoAlbumName());
//            layout.addComponent(link);
//        }
//        albumsPanel.setContent(layout);
//        addComponentsAndExpand(albumsPanel);
    }

    private List<PhotoAlbum> getAlbumList(BigInteger petId){
        List<PhotoAlbum> albumList = Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/albums/" + petId, PhotoAlbum[].class));
        return albumList;
    }

}
