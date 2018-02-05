package com.netcracker.ui.gallery;

import com.netcracker.asserts.ObjectAssert;
import com.netcracker.model.album.PhotoAlbum;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.user.Profile;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.MainUI;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AlbumsUI extends VerticalLayout {
    private Window newAlbumWindow;
    private Grid<PhotoAlbum> grid;
    private List<PhotoAlbum> albums;
    private Profile profile;
    private BigInteger profileId;
    private Profile currentProfile;
    private BigInteger currentProfileId;

    public AlbumsUI(BigInteger profileId) {
        super();
        this.profileId = profileId;
        profile = getProfile(this.profileId);
        setCurrentProfile();

        albums = getAlbumList(this.profileId);
        Panel mainPanel = new Panel();
        VerticalLayout albumLayout = new VerticalLayout();

        albumLayout.addComponent(new Label(profile.getProfileName() + " "
                + profile.getProfileSurname() + " albums"));

        getNewAlbumWindow();
        Button addNewAlbumButton = new Button("Add album", VaadinIcons.PLUS);
        addNewAlbumButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                UI.getCurrent().addWindow(newAlbumWindow);
            }
        });
        if (currentProfileId.equals(this.profileId)) {
            ((Label) albumLayout.getComponent(0)).setValue(
                    ((Label) albumLayout.getComponent(0)).getValue() + " (your albums)");
            albumLayout.addComponent(addNewAlbumButton);
        }

        if (albums.size() != 0) {
            getGrid();
            albumLayout.addComponent(grid);
            int height = (albums.size()) * 80;
            if (albums.size() > 1) {
                height = height - albums.size() * 20;
            }
            grid.setHeight(height, Unit.PIXELS);
        }
        mainPanel.setContent(albumLayout);
        addComponent(mainPanel);
    }

    private void getGrid() {
        grid = new Grid<>();
        grid.setWidth("900px");

        grid.addColumn(al ->
                getPetByAlbum(al.getObjectId()).getPetName()).setCaption("Assigned to").setWidth(200).setSortable(true);
        grid.addColumn(PhotoAlbum::getPhotoAlbumName).setCaption("Album name").setWidth(200).setSortable(true);
        grid.addColumn(PhotoAlbum::getPhotoAlbumDesc).setCaption("Album description").setWidth(250).setSortable(true);
        grid.addColumn(PhotoAlbum::getPhotoAlbumDate).setCaption("Creation date").setWidth(250).setSortable(true);

        grid.setItems(albums);
        grid.addItemClickListener(album ->
                ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new GalleryUI(album.getItem().getObjectId())));
    }

    private void getNewAlbumWindow() {
        newAlbumWindow = new Window();
        newAlbumWindow.setModal(true);
        newAlbumWindow.setResizable(false);
        newAlbumWindow.setWidth("400px");
        newAlbumWindow.setHeight("320px");
        newAlbumWindow.setCaption("Creating new album:");
        VerticalLayout windowContent = new VerticalLayout();
        HorizontalLayout addAlbumButtonsLayout = new HorizontalLayout();

        List<Pet> allPetsUserList = getProfilePets(profileId);
        ComboBox<Pet> petsComboBox = new ComboBox<>("Select the pet for the new album:");
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
                if (!petsComboBox.isEmpty()) {
                    BigInteger petID = petsComboBox.getValue().getObjectId();
                    createAlbum(albumName.getValue(), description.getValue(), petID);
                    newAlbumWindow.close();
                } else
                    Notification.show("You should select you pet!");
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

    private List<PhotoAlbum> getAlbumList(BigInteger profileId) {
        return Arrays.asList(
                CustomRestTemplate.getInstance()
                        .customGetForObject("/albums/" + profileId, PhotoAlbum[].class));
    }

    private Pet getPetByAlbum(BigInteger petId) {
        return CustomRestTemplate.getInstance()
                .customGetForObject("/albums/pet/" + petId, Pet.class);
    }

    private void createAlbum(String albumName, String description, BigInteger petId) {
        ObjectAssert.isNullOrEmpty(albumName);
        PhotoAlbum createdAlbum = new PhotoAlbum();
        createdAlbum.setPhotoAlbumName(albumName);
        createdAlbum.setPhotoAlbumDesc(description);
        createdAlbum.setPhotoAlbumDate(Timestamp.valueOf(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()));
        HttpEntity<PhotoAlbum> albumEntity = new HttpEntity<>(createdAlbum);
        createdAlbum = CustomRestTemplate.getInstance()
                .customPostForObject("/albums/" + petId + "/add", albumEntity, PhotoAlbum.class);
        Notification.show("Album successfully added!");
        ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new GalleryUI(createdAlbum.getObjectId()));
    }

    private Profile getProfile(BigInteger profileId) {
        return CustomRestTemplate.getInstance().
                customGetForObject("/profile/" + profileId, Profile.class);
    }

    private void setCurrentProfile() {
        SecurityContext o = (SecurityContext) VaadinSession.getCurrent().getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        String login = o.getAuthentication().getPrincipal().toString();
        BigInteger currentProfileId = CustomRestTemplate.getInstance().customPostForObject("/user/profileId", login, BigInteger.class);
        currentProfile = CustomRestTemplate.getInstance().
                customGetForObject("/profile/" + currentProfileId, Profile.class);
        this.currentProfileId = currentProfile.getObjectId();
    }
}
