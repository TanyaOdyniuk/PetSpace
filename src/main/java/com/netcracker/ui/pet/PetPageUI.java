package com.netcracker.ui.pet;

import com.netcracker.model.pet.Pet;
import com.netcracker.model.pet.PetSpecies;
import com.netcracker.model.user.Profile;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.StubVaadinUI;
import com.netcracker.ui.profile.ProfileView;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.*;

import java.math.BigInteger;

public class PetPageUI extends VerticalLayout {

    private BigInteger petId;
    private PetSpecies petSpecies;
    private Pet pet;

    public PetPageUI(BigInteger petId) {
        this.petId = petId;
        this.petSpecies = getSpecies();
        this.pet = getPet(petId);
        setSizeFull();
        setSpacing(true);
        Profile owner = CustomRestTemplate.getInstance().customGetForObject("/profile/" + pet.getPetOwner().getObjectId(), Profile.class);

        HorizontalLayout mainLayout = new HorizontalLayout();
        VerticalLayout leftPageLayout = new VerticalLayout();
        VerticalLayout rightPagePart = new VerticalLayout();
        VerticalLayout avatarLayout = new VerticalLayout();
        VerticalLayout infoLayout = new VerticalLayout();

        Panel rightPagePanel = new Panel();
        rightPagePanel.setHeight(750, Unit.PIXELS);
        rightPagePanel.setWidth(875, Unit.PIXELS);

        Panel avatarPanel = new Panel();
        avatarPanel.setWidth("252px");
        avatarPanel.setHeight("100%");

        Panel infoPanel = new Panel();
        infoPanel.setWidth("100%");
        infoPanel.setHeight("100%");

        Panel galleryPanel = new Panel();
        galleryPanel.setWidth("100%");
        galleryPanel.setHeight("100%");

        Image petAvatar = new Image();
        if (pet.getPetAvatar() != null)
            petAvatar.setSource(new ExternalResource(pet.getPetAvatar()));
        else
            petAvatar = PageElements.getNoImage();
        petAvatar.setHeight(225, Unit.PIXELS);
        petAvatar.setWidth(225, Unit.PIXELS);
        petAvatar.setDescription("Pet avatar");

        Button editPage = PageElements.createClickedLabel("Update pet's profile");
        editPage.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                PetEditFormUI sub = new PetEditFormUI(pet);
                UI.getCurrent().addWindow(sub);
            }
        });

        Button deletePage = PageElements.createClickedLabel("Delete page");
        deletePage.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                Notification.show("Тут можно будет удалить\nстраницу, если ты владелец", Notification.Type.TRAY_NOTIFICATION);
            }
        });

        Button albums = PageElements.createClickedLabel("Albums");
        albums.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                Notification.show("Здесь будут альбомы!\nМожет быть :)", Notification.Type.TRAY_NOTIFICATION);
            }
        });

        leftPageLayout.addComponents(petAvatar, PageElements.getSeparator(), editPage, deletePage, PageElements.getSeparator(), albums);
        avatarPanel.setContent(leftPageLayout);
        avatarLayout.addComponents(avatarPanel);

        Label petName = PageElements.createLabel(5, pet.getPetName());

        Label petSpecies = PageElements.createCheckedValueLabel(this.petSpecies.getSpeciesName());
        petSpecies.setCaption("Species");

        Label petBreed = PageElements.createCheckedValueLabel(pet.getPetBreed());
        petBreed.setCaption("Breed");

        Label petAge;
        if (pet.getPetAge() == 1)
            petAge = PageElements.createCheckedValueLabel(pet.getPetAge(), "year");
        else
            petAge = PageElements.createCheckedValueLabel(pet.getPetAge(), "years");

        petAge.setCaption("Age");

        Label petOwnerSign = PageElements.createLabel(2, "Owner");

        Button petOwner = PageElements.createClickedLabel(owner.getProfileName() + " " + owner.getProfileSurname());
        petOwner.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(owner.getObjectId()));
            }
        });


        Label additionInfo = PageElements.createGrayLabel("Additional information");

        Label petWeight = PageElements.createCheckedValueLabel(pet.getPetWeight(), "kg");
        petWeight.setCaption("Weight");

        Label petHeight = PageElements.createCheckedValueLabel(pet.getPetHeight(), "m");
        petHeight.setCaption("Height");

        Label petSpecParam = PageElements.createCheckedValueLabel(pet.getPetSpecificParam());
        petSpecParam.setCaption("Other info");

        infoLayout.addComponentsAndExpand(petName, PageElements.getSeparator(), petSpecies, petBreed,
                petAge, petOwnerSign, petOwner, additionInfo, PageElements.getSeparator(), petWeight,
                petHeight, petSpecParam);

        infoPanel.setContent(infoLayout);
        infoPanel.setHeight("100%");
        infoPanel.setWidth("100%");

        //PhotoAlbum album = pet.getPetPhotoAlbums().get(0);
        HorizontalLayout photosLayout = new HorizontalLayout();
        /*if (album != null) {
            List<PhotoRecord> photos = album.getPhotoRecords();
            for (PhotoRecord record : photos) {
                Image photo = new Image(record.getPhoto());
                photosLayout.addComponentsAndExpand(photo);
            }
        } else {*/
        for (int i = 0; i < 4; i++) {
            Image emptyPhoto = PageElements.getNoImage();
            photosLayout.addComponentsAndExpand(emptyPhoto);
        }
        //}

        galleryPanel.setContent(photosLayout);
        rightPagePart.addComponents(infoPanel, galleryPanel);
        rightPagePanel.setContent(rightPagePart);
        mainLayout.addComponents(avatarPanel, rightPagePanel);
        addComponents(mainLayout);
    }

    private Pet getPet(BigInteger petId) {
        return CustomRestTemplate.getInstance().customGetForObject(
                "/pet/" + petId, Pet.class);
    }

    private PetSpecies getSpecies(){
        return CustomRestTemplate.getInstance().customGetForObject(
                        "/pet/" + petId + "/species" , PetSpecies.class);
    }


}
