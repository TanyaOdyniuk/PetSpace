package com.netcracker.ui.pet;

import com.netcracker.model.album.PhotoAlbum;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.pet.PetSpecies;
import com.netcracker.model.record.PhotoRecord;
import com.netcracker.model.user.Profile;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.StubVaadinUI;
import com.netcracker.ui.profile.ProfileView;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;

@SpringComponent
@UIScope
public class PetPageUI extends VerticalLayout {

    private BigInteger petId;
    private PetSpecies petSpecies;
    private Pet pet;

    @Autowired
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
        petAvatar.setHeight(250, Unit.PIXELS);
        petAvatar.setWidth(250, Unit.PIXELS);
        petAvatar.setDescription("Pet avatar");

        Button editPage = PageElements.createClickedLabel("Редактировать информацию");
        editPage.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                PetEditFormUI sub = new PetEditFormUI(pet);
                UI.getCurrent().addWindow(sub);
            }
        });

        Button deletePage = PageElements.createClickedLabel("Удалить страницу");
        deletePage.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                Notification.show("Тут можно будет удалить\nстраницу, если ты владелец", Notification.Type.TRAY_NOTIFICATION);
            }
        });

        Button albums = PageElements.createClickedLabel("Альбомы");
        albums.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                Notification.show("Здесь будут альбомы!\nМожет быть :)", Notification.Type.TRAY_NOTIFICATION);
            }
        });

        Button argue = PageElements.createClickedLabel("Пожаловаться");
        argue.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                Notification.show("Здесь будут жалобы!\nНо это не точно :)", Notification.Type.TRAY_NOTIFICATION);
            }
        });

        leftPageLayout.addComponents(petAvatar, PageElements.getSeparator(), editPage, deletePage, PageElements.getSeparator(), argue, albums);
        avatarPanel.setContent(leftPageLayout);
        avatarLayout.addComponents(avatarPanel);

        Label petName = PageElements.createLabel(5, pet.getPetName());

        Label petSpecies = PageElements.createCheckedValueLabel(this.petSpecies.getSpeciesName());
        petSpecies.setCaption("Вид");

        Label petBreed = PageElements.createCheckedValueLabel(pet.getPetBreed());
        petBreed.setCaption("Порода");

        Label petAge;
        if (pet.getPetAge() % 10 == 1)
            petAge = PageElements.createCheckedValueLabel(pet.getPetAge(), "год");
        else if (pet.getPetAge() % 10 == 2 ||
                pet.getPetAge() % 10 == 3 ||
                pet.getPetAge() % 10 == 4)
            petAge = PageElements.createCheckedValueLabel(pet.getPetAge(), "года");
        else
            petAge = PageElements.createCheckedValueLabel(pet.getPetAge(), "лет");

        petAge.setCaption("Возраст");

        Label petOwnerSign = PageElements.createLabel(2, "Владелец");

        Button petOwner = PageElements.createClickedLabel(owner.getProfileName() + " " + owner.getProfileSurname());
        petOwner.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(owner.getObjectId()));
            }
        });


        Label additionInfo = PageElements.createGrayLabel("Дополнительная информация");

        Label petWeight = PageElements.createCheckedValueLabel(pet.getPetWeight(), "кг");
        petWeight.setCaption("Вес");

        Label petHeight = PageElements.createCheckedValueLabel(pet.getPetHeight(), "м");
        petHeight.setCaption("Рост");

        Label petSpecParam = PageElements.createCheckedValueLabel(pet.getPetSpecificParam());
        petSpecParam.setCaption("Особые данные");

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
        Pet pet = CustomRestTemplate.getInstance().customGetForObject(
                "/pet/" + petId, Pet.class);
        return pet;
    }

    private PetSpecies getSpecies() {
        PetSpecies species =
                CustomRestTemplate.getInstance().customGetForObject(
                        "/pet/" + petId + "/species", PetSpecies.class);
        return species;
    }


}
