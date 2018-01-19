package com.netcracker.ui.gallery;

import com.netcracker.asserts.ObjectAssert;
import com.netcracker.model.album.PhotoAlbum;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.record.PhotoRecord;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.StubVaadinUI;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import org.springframework.http.HttpEntity;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AlbumsUI extends HorizontalLayout{
    private Panel panel;
    private VerticalLayout albumLayout;
    private Window newAlbumWindow;
    private Grid<PhotoAlbum> grid;
    private List<PhotoAlbum> albums;

    public AlbumsUI(BigInteger profileId) {
        super();
        addStyleName("v-scrollable");
        setHeight("100%");

        albums = getAlbumList(profileId);
        List<BigInteger> ids = new ArrayList<>();
        for(PhotoAlbum id : albums) {
            ids.add(id.getObjectId());
        }

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

        if(albums.size() != 0) {
            getGrid();
            albumLayout.addComponent(grid);
            int height = (albums.size()) * 80;
            if(albums.size() > 1){
                height = height - albums.size() * 20;
            }
            grid.setHeight(height,Unit.PIXELS);
        }
//        OPEN GALLERY
//        for(PhotoAlbum album : albums){
//            Button albumName = PageElements.createClickedLabel(album.getPhotoAlbumName());
//            albumName.addClickListener(new AbstractClickListener() {
//                @Override
//                public void buttonClickListener() {
//                    ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new GalleryUI(album.getObjectId()));
//                }
//            });
//            albumLayout.addComponents(albumName);
//        }
        panel.setContent(albumLayout);
        addComponent(panel);
    }

    private void getGrid() {
        grid = new Grid<>();
        grid.setWidth("650px");
//        List<PhotoRecord> lastRecords = getLastPhotos();

        Grid.Column topicColumn = grid.addColumn(al ->
                al.getPhotoAlbumName()).setCaption("Album name").setWidth(200).setSortable(false);
        Grid.Column descriptionColumn = grid.addColumn(al ->
                al.getPhotoAlbumDesc()).setCaption("Album description").setWidth(450).setSortable(false);

        grid.setItems(albums);
        grid.addItemClickListener(album ->
                ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new GalleryUI(album.getItem().getObjectId())));
    }

    private void getNewAlbumWindow(){
        newAlbumWindow = new Window();
        newAlbumWindow.setModal(true);
        newAlbumWindow.setWidth("400px");
        newAlbumWindow.setHeight("320px");
        newAlbumWindow.setCaption("Creating new album:");
        VerticalLayout windowContent = new VerticalLayout();
        HorizontalLayout addAlbumButtonsLayout = new HorizontalLayout();

        List<Pet> allPetsUserList = getProfilePets(BigInteger.valueOf(25));
        ComboBox<Pet> petsComboBox = new ComboBox("Select the pet for the new album:");
        petsComboBox.setItems(allPetsUserList);
        petsComboBox.setItemCaptionGenerator(Pet::getPetName);
        petsComboBox.setEmptySelectionAllowed(false);
        petsComboBox.setRequiredIndicatorVisible(true);
        petsComboBox.setWidth("100%");
        petsComboBox.setTextInputAllowed(false);

        TextField albumName = PageElements.createTextField("Enter album's name:", "album's name", true);
        albumName.setWidth("100%");
        TextField description = PageElements.createTextField("Enter album's description:", "album's description", false);
        description.setWidth("100%");

        Button addAlbumButton = new Button("Add album");
        addAlbumButton.addClickListener(new AbstractClickListener() {
                    @Override
                    public void buttonClickListener() {
                        addAlbumButton.setComponentError(null);
                        createAlbum(albumName.getValue(),description.getValue(), petsComboBox.getValue().getObjectId());
                        newAlbumWindow.close();
                    }
                });
        Button cancelAddingNewAlbum = new Button("Cancel", click -> newAlbumWindow.close());
        addAlbumButtonsLayout.addComponentsAndExpand(addAlbumButton, cancelAddingNewAlbum);
        windowContent.addComponents(petsComboBox, albumName, description, addAlbumButtonsLayout);

        newAlbumWindow.setContent(windowContent);
        newAlbumWindow.center();
    }

    private List<Pet> getProfilePets(BigInteger profileId) {
        return Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/pets/" + profileId, Pet[].class));
    }

    private List<PhotoAlbum> getAlbumList(BigInteger profileId){
        return Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/albums/" + profileId , PhotoAlbum[].class));
    }

    private void createAlbum(String albumName, String description, BigInteger petId) {
        ObjectAssert.isNullOrEmpty(albumName);
        PhotoAlbum createdAlbum = new PhotoAlbum();
        createdAlbum.setPhotoAlbumName(albumName);
        createdAlbum.setPhotoAlbumDesc(description);
        createdAlbum.setPhotoAlbumDate(Timestamp.valueOf(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()));
        HttpEntity<PhotoAlbum> albumEntity = new HttpEntity<>(createdAlbum);
        createdAlbum = CustomRestTemplate.getInstance()
                .customPostForObject("/albums/"+ petId +"/add", albumEntity, PhotoAlbum.class);
        Notification.show("Album successfully added!");
        ((StubVaadinUI)UI.getCurrent()).changePrimaryAreaLayout(new GalleryUI(createdAlbum.getObjectId()));
    }

    private List<PhotoRecord> getLastPhotos(){
        List<PhotoRecord> list = Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject("/albums/lastPhotos", PhotoRecord[].class));
        return list;
    }
}
