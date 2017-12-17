package com.netcracker.ui.pet;

import com.netcracker.model.pet.Pet;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.StubVaadinUI;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.event.MouseEvents;
import com.vaadin.icons.VaadinIcons;
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
public class MyPetsListUI extends VerticalLayout {

    private BigInteger profileId;

    @Autowired
    public MyPetsListUI(BigInteger profileId){
        super();
        this.profileId = profileId;

        this.addStyleName("v-scrollable");
        this.setHeight("100%");

        Panel mainPanel = new Panel();
        /*mainPanel.setWidth("100%");
        mainPanel.setHeight(750, Unit.PIXELS);*/
        VerticalLayout petRecordsLayout = new VerticalLayout();
        Button addNewPet = new Button("Добавить нового питомца", VaadinIcons.PLUS);
        addNewPet.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                ((StubVaadinUI)UI.getCurrent()).changePrimaryAreaLayout(new PetFormUI());
            }
        });
        List<Pet> petList = getProfilePets(profileId);
        if(petList == null){
            Label noPetsLabel = PageElements.createLabel(5, "У вас ещё нет питомца!");
            mainPanel.setContent(noPetsLabel);
            addComponents(addNewPet, mainPanel);
            return;
        }
        for (Pet pet: petList) {
            HorizontalLayout petRecord = new HorizontalLayout();
            VerticalLayout petInfoLayout = new VerticalLayout();
            Image petAvatar = new Image();
            petAvatar.setHeight(250, Unit.PIXELS);
            petAvatar.setWidth(250, Unit.PIXELS);
            petAvatar.setSource(new ExternalResource(pet.getPetAvatar()));
            petAvatar.setDescription("Pet avatar");
            petAvatar.addClickListener((MouseEvents.ClickListener) clickEvent -> ((StubVaadinUI)UI.getCurrent()).changePrimaryAreaLayout(new PetPageUI(pet.getObjectId())));

            Label petNameSign = PageElements.createGrayLabel("Кличка питомца");
            Button petName = PageElements.createClickedLabel(pet.getPetName());
            petName.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new PetPageUI(pet.getObjectId()));
                }
            });
            Label petInfo = PageElements.createCheckedValueLabel(pet.getPetSpecificParam());
            petInfo.setCaption("Информация о питомце");

            //PET INFO
            petInfoLayout.addComponents(petNameSign, petName, petInfo);

            //INFO + AVATAR
            petRecord.addComponents(petAvatar, petInfoLayout);

            petRecordsLayout.addComponents(petRecord, PageElements.getSeparator());
        }
        mainPanel.setContent(petRecordsLayout);
        addComponents(addNewPet, mainPanel);
    }

    private List<Pet> getProfilePets(BigInteger profileId){
        List<Pet> petList = Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/pets/" + profileId, Pet[].class));
        return petList;
    }
}