package com.netcracker.ui.pet;

import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.pet.Pet;
import com.netcracker.service.petprofile.PetProfileService;
import com.netcracker.service.petprofile.impl.PetProfileServiceImpl;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.StubVaadinUI;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.event.MouseEvents;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

@SpringComponent
@UIScope
public class AllPetsListUI extends Panel {

    @Autowired
    public AllPetsListUI(){
        super();
        setWidth("100%");
        setHeight("100%");
        VerticalLayout petRecordsLayout = new VerticalLayout();
        List<Pet> petList = getAllPets();
        for (Pet pet: petList) {
            HorizontalLayout petRecord = new HorizontalLayout();
            VerticalLayout petInfoLayout = new VerticalLayout();
            Image petAvatar = new Image();
            petAvatar.setHeight(250, Unit.PIXELS);
            petAvatar.setWidth(250, Unit.PIXELS);
            petAvatar.setSource(new ExternalResource(pet.getPetAvatar()));
            petAvatar.setDescription("Pet avatar");
            petAvatar.addClickListener((MouseEvents.ClickListener) clickEvent -> ((StubVaadinUI)UI.getCurrent()).changePrimaryAreaLayout(new PetPageUI(pet.getObjectId())));

            Label petNameSign = PageElements.createLabel(3, "gray", "Кличка питомца");
            Label petName = PageElements.createStandartLabel(PageElements.htmlTabulation + pet.getPetName());
            Label petInfoSign = PageElements.createLabel(3, "gray", "Информация о питомце");
            Label petInfo = PageElements.createStandartLabel(PageElements.htmlTabulation + pet.getPetSpecificParam());

            //PET INFO
            petInfoLayout.addComponents(petNameSign, petName, petInfoSign, petInfo);

            //INFO + AVATAR
            petRecord.addComponents(petAvatar, petInfoLayout);

            petRecordsLayout.addComponents(petRecord);
        }
        setContent(petRecordsLayout);
    }

    private List<Pet> getAllPets(){
        List<Pet> petList = Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/pets/", Pet[].class));
        return petList;
    }
}
