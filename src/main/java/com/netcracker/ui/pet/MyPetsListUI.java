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
import elemental.css.CSSStyleDeclaration;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

@SpringComponent
@UIScope
public class MyPetsListUI extends VerticalLayout {

    private BigInteger profileId;
    private int browserHeight;

    @Autowired
    public MyPetsListUI(BigInteger profileId) {
        super();
        this.profileId = profileId;
        this.browserHeight = UI.getCurrent().getPage().getBrowserWindowHeight();

        this.addStyleName("v-scrollable");
        this.setHeightUndefined();

        Panel mainPanel = new Panel();
        mainPanel.setWidth("100%");
        mainPanel.setHeight(browserHeight - 300, Unit.PIXELS);
        VerticalLayout petRecordsLayout = new VerticalLayout();
        Button addNewPet = new Button("Добавить нового питомца", VaadinIcons.PLUS);
        addNewPet.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                PetEditFormUI sub = new PetEditFormUI(new Pet());
                UI.getCurrent().addWindow(sub);
            }
        });
        List<Pet> petList = getProfilePets(profileId);
        if (petList.size() == 0) {
            Label noPetsLabel = PageElements.createLabel(5, "У вас ещё нет питомца!");
            petRecordsLayout.addComponent(noPetsLabel);
            petRecordsLayout.setComponentAlignment(noPetsLabel, Alignment.MIDDLE_CENTER);
            mainPanel.setContent(petRecordsLayout);
            addComponents(addNewPet, mainPanel);
            return;
        }
        for (Pet pet : petList) {
            HorizontalLayout petRecord = new HorizontalLayout();
            VerticalLayout petInfoLayout = new VerticalLayout();
            Image petAvatar = new Image();
            if(pet.getPetAvatar() != null)
                petAvatar.setSource(new ExternalResource(pet.getPetAvatar()));
            else
                petAvatar = PageElements.getNoImage();
            petAvatar.setHeight(250, Unit.PIXELS);
            petAvatar.setWidth(250, Unit.PIXELS);
            petAvatar.setDescription("Pet avatar");
            petAvatar.addClickListener((MouseEvents.ClickListener) clickEvent -> ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new PetPageUI(pet.getObjectId())));

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

    private List<Pet> getProfilePets(BigInteger profileId) {
        List<Pet> petList = Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/pets/" + profileId, Pet[].class));
        return petList;
    }
}