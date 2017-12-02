package com.netcracker.ui.pet;

import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.pet.Pet;
import com.netcracker.service.petprofile.PetProfileService;
import com.netcracker.service.petprofile.impl.PetProfileServiceImpl;
import com.netcracker.ui.util.CustomRestTemplate;
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
public class AllPetsListUI extends VerticalLayout {

    @Autowired
    public AllPetsListUI(){
        super();
        setWidth("100%");
        VerticalLayout petRecordsLayout = new VerticalLayout();
        petRecordsLayout.setSpacing(true);
        List<Pet> petList = getAllPets();
        for (Pet pet: petList) {
            HorizontalLayout petRecord = new HorizontalLayout();
            VerticalLayout petInfoLayout = new VerticalLayout();
            //TextField petAvatar = new TextField("This is image " + i);
            Image petAvatar = new Image();
            petAvatar.setHeight(150, Unit.POINTS);
            petAvatar.setWidth(150, Unit.POINTS);
            petAvatar.setSource(new ExternalResource(pet.getPetAvatar()));
            petAvatar.setDescription("Pet avatar");

            petAvatar.setHeight("60%");

            Label petName = new Label(pet.getPetName());
            Label petInfo = new Label(pet.getPetSpecificParam());

            //PET INFO
            petInfoLayout.setSpacing(true);
            petInfoLayout.addComponentsAndExpand(petName, petInfo);

            //INFO + AVATAR
            petRecord.setSpacing(true);

            petRecord.addComponentsAndExpand(petAvatar, petInfoLayout);
            petRecordsLayout.addComponentsAndExpand(petRecord);
        }
        addComponents(petRecordsLayout);
    }

    private List<Pet> getAllPets(){
        List<Pet> petList = Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/pets", Pet[].class));
        return petList;
    }


    public void setListVisible(){
        setVisible(true);
    }
}
