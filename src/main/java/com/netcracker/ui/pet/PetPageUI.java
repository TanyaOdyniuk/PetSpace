package com.netcracker.ui.pet;

import com.netcracker.model.album.PhotoAlbum;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.record.PhotoRecord;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

@SpringComponent
@UIScope
public class PetPageUI extends VerticalLayout {

    private BigInteger petId;

    @Autowired
    public PetPageUI(BigInteger petId) {
        this.petId = petId;
        setSizeFull();
        setSpacing(true);
        Pet pet = getPet(petId);

        HorizontalLayout mainLayout = new HorizontalLayout();
        VerticalLayout avatarLayout = new VerticalLayout();
        VerticalLayout rightPagePart = new VerticalLayout();
        VerticalLayout infoLayout = new VerticalLayout();

        Panel rightPagePanel = new Panel();
        rightPagePanel.setHeight(750, Unit.PIXELS);
        rightPagePanel.setWidth(875, Unit.PIXELS);

        Panel avatarPanel = new Panel();
        avatarPanel.setWidth("250px");
        avatarPanel.setHeight("100%");

        Panel infoPanel = new Panel();
        infoPanel.setWidth("100%");
        infoPanel.setHeight("100%");

        Panel galleryPanel = new Panel();
        galleryPanel.setWidth("100%");
        galleryPanel.setHeight("100%");

        Image petAvatar = new Image();
        petAvatar.setHeight(250, Unit.PIXELS);
        petAvatar.setWidth(250, Unit.PIXELS);
        petAvatar.setSource(new ExternalResource(pet.getPetAvatar()));
        petAvatar.setDescription("Pet avatar");

        avatarPanel.setContent(petAvatar);
        avatarLayout.addComponents(avatarPanel);

        Label petName = PageElements.createLabel(4 , "black", pet.getPetName());

        Label petSpeciesSign = PageElements.createGrayLabel("Вид");
        Label petSpecies = new Label(pet.getPetSpecies().getSpeciesName());

        Label petBreedSign = PageElements.createGrayLabel("Порода");
        Label petBreed = new Label(pet.getPetBreed());

        Label petAgeSign = PageElements.createGrayLabel("Возраст");
        Label petAge = new Label(pet.getPetAge() + " лет");

        Label petOwnerSign = PageElements.createGrayLabel("Владелец");
        Label petOwner = new Label(pet.getPetOwner().getProfileName() + " " + pet.getPetOwner().getProfileSurname());

        Label additionInfo = PageElements.createGrayLabel("Дополнительаня информация");

        Label petWeightSign = PageElements.createGrayLabel("Вес");
        Label petWeight = new Label(pet.getPetWeight() + " кг");

        Label petHeightSign = PageElements.createGrayLabel("Рост");
        Label petHeight = new Label(pet.getPetHeight() + " м");

        Label petSpecParamSign = PageElements.createGrayLabel("Дополнительные данные");
        Label petSpecParam = new Label(pet.getPetSpecificParam());

        infoLayout.addComponentsAndExpand(petName, PageElements.getSeparator(), petSpeciesSign, petSpecies, petBreedSign, petBreed,
                petAgeSign, petAge, petOwnerSign, petOwner, additionInfo, PageElements.getSeparator(), petWeightSign, petWeight, petHeightSign,
                petHeight, petSpecParamSign, petSpecParam);

        infoPanel.setContent(infoLayout);
        infoPanel.setHeight("100%");
        infoPanel.setWidth("100%");

        PhotoAlbum album = pet.getPetPhotoAlbums().get(0);
        HorizontalLayout photosLayout = new HorizontalLayout();
        if(album != null){
            List<PhotoRecord> photos = album.getPhotoRecords();
            for (PhotoRecord record: photos) {
                Image photo = new Image(record.getPhoto());
                photosLayout.addComponentsAndExpand(photo);
            }
        } else {
            for(int i = 0; i <4; i++){
                Image emptyPhoto = new Image("", new ExternalResource("https://assets2.bus.com/assets/camaleon_cms/image-not-found-4a963b95bf081c3ea02923dceaeb3f8085e1a654fc54840aac61a57a60903fef.png"));
                photosLayout.addComponentsAndExpand(emptyPhoto);
            }
        }

        galleryPanel.setContent(photosLayout);
        rightPagePart.addComponents(infoPanel, galleryPanel);
        rightPagePanel.setContent(rightPagePart);
        mainLayout.addComponents(avatarPanel, rightPagePanel);
        /*mainLayout.setComponentAlignment(avatarPanel, Alignment.MIDDLE_LEFT);
        mainLayout.setComponentAlignment(rightPagePanel, Alignment.MIDDLE_RIGHT);*/
        //mainPanel.setContent(mainLayout);
        addComponents(mainLayout);
    }

    private Pet getPet(BigInteger petId){
        Pet pet = CustomRestTemplate.getInstance().customGetForObject(
                "/pet/" + petId, Pet.class);
        return pet;
    }
}
