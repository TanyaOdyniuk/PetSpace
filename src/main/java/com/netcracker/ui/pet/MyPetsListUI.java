package com.netcracker.ui.pet;

import com.netcracker.model.pet.Pet;
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
public class MyPetsListUI extends Panel {

    private BigInteger profileId;

    @Autowired
    public MyPetsListUI(BigInteger profileId){
        super();
        setWidth("100%");
        setHeight("100%");
        VerticalLayout petRecordsLayout = new VerticalLayout();
        List<Pet> petList = getProfilePets(profileId);
        for (Pet pet: petList) {
            HorizontalLayout petRecord = new HorizontalLayout();
            VerticalLayout petInfoLayout = new VerticalLayout();
            Image petAvatar = new Image();
            petAvatar.setHeight(250, Unit.PIXELS);
            petAvatar.setWidth(250, Unit.PIXELS);
            petAvatar.setSource(new ExternalResource(pet.getPetAvatar()));
            petAvatar.setDescription("Pet avatar");

            Link petName = new Link(pet.getPetName(), new ExternalResource("https://vaadin.com/"));
            petName.setDescription("Здесь должна быть ссылка на питомца :)");
            Label petInfo = new Label(pet.getPetSpecificParam());

            //PET INFO
            petInfoLayout.addComponents(petName, petInfo);

            //INFO + AVATAR
            petRecord.addComponents(petAvatar, petInfoLayout);

            petRecordsLayout.addComponents(petRecord);
        }
        setContent(petRecordsLayout);
    }

    private List<Pet> getProfilePets(BigInteger profileId){
        List<Pet> petList = Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/pets/" + profileId, Pet[].class));
        return petList;
    }
}