package com.netcracker.ui.pet;

import com.netcracker.model.pet.Pet;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.server.ExternalResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@SpringComponent
@UIScope
public class MyPetsListUI extends VerticalLayout {

    @Autowired
    public MyPetsListUI(){
        super();
        setWidth("100%");
        VerticalLayout petRecordsLayout = new VerticalLayout();
        petRecordsLayout.setSizeUndefined();
        petRecordsLayout.setSpacing(true);
        List<Pet> petList = getMyPets();
        for (Pet pet: petList) {
            HorizontalLayout petRecord = new HorizontalLayout();
            VerticalLayout petInfoLayout = new VerticalLayout();
            //TextField petAvatar = new TextField("This is image " + i);
            Image petAvatar = new Image();
            petAvatar.setHeight(150, Unit.PIXELS);
            petAvatar.setWidth(150, Unit.PIXELS);
            petAvatar.setSource(new ExternalResource(pet.getPetAvatar()));
            petAvatar.setDescription("Pet avatar");

            petAvatar.setHeight("60%");

            /*Label petName = new Label(pet.getPetName());*/
            Link petName = new Link(pet.getPetName(),
                    new ExternalResource("http://vaadin.com/"));
            Label petInfo = new Label(pet.getPetSpecificParam());

            //PET INFO
            petInfoLayout.setSpacing(true);
            //petInfoLayout.addComponentsAndExpand(petName, petInfo);
            petInfoLayout.addComponents(petName, petInfo);

            //INFO + AVATAR
            petRecord.setSpacing(true);

            /*petRecord.addComponentsAndExpand(petAvatar, petInfoLayout);
            petRecordsLayout.addComponentsAndExpand(petRecord);*/
            petRecord.addComponents(petAvatar, petInfoLayout);
            petRecordsLayout.addComponents(petRecord);
        }
        addComponents(petRecordsLayout);
    }

    private List<Pet> getMyPets(){
        List<Pet> petList = Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/pets", Pet[].class));
        return petList;
    }

}
