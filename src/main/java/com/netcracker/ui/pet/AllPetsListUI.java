package com.netcracker.ui.pet;

import com.netcracker.asserts.PetDataAssert;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.user.Profile;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.StubVaadinUI;
import com.netcracker.ui.profile.ProfileView;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.event.MouseEvents;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.ui.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class AllPetsListUI extends VerticalLayout {

    private int browserHeight;

    public AllPetsListUI() {
        super();
        this.browserHeight = UI.getCurrent().getPage().getBrowserWindowHeight();

        Panel mainPanel = new Panel();
        mainPanel.setWidth("100%");
        mainPanel.setHeight(browserHeight - 250, Unit.PIXELS);
        VerticalLayout petRecordsLayout = new VerticalLayout();
        List<Pet> petList = getAllPets();
        for (Pet pet : petList) {
            HorizontalLayout petRecord = new HorizontalLayout();
            VerticalLayout petInfoLayout = new VerticalLayout();
            Image petAvatar = new Image();
            petAvatar.setHeight(250, Unit.PIXELS);
            petAvatar.setWidth(250, Unit.PIXELS);
            String petAvatarSource = pet.getPetAvatar();
            if (petAvatarSource != null) {
                if (PetDataAssert.isAvatarURL(petAvatarSource))
                    petAvatar.setSource(new ExternalResource(petAvatarSource));
                else
                    petAvatar.setSource(new FileResource(new File(petAvatarSource)));
            } else
                petAvatar = PageElements.getNoImage();
            petAvatar.setDescription("Pet avatar");
            petAvatar.addClickListener((MouseEvents.ClickListener) clickEvent -> ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new PetPageUI(pet.getObjectId())));

            Profile buff = pet.getPetOwner();
            Profile owner = CustomRestTemplate.getInstance().customGetForObject("/profile/" + buff.getObjectId(), Profile.class);

            Label petNameSign = PageElements.createGrayLabel("Pet's name");
            Button petName = PageElements.createClickedLabel(pet.getPetName());
            petName.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new PetPageUI(pet.getObjectId()));
                }
            });
            Label petOwnerSign = PageElements.createGrayLabel("Owner");
            Button petOwner = PageElements.createClickedLabel(owner.getProfileName() + " " + owner.getProfileSurname());
            petOwner.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(owner.getObjectId()));
                }
            });

            Label petInfo = PageElements.createCheckedValueLabel(pet.getPetSpecificParam());
            petInfo.setCaption("Information about pet");

            //PET INFO
            petInfoLayout.addComponents(petNameSign, petName, petOwnerSign, petOwner, petInfo);

            //INFO + AVATAR
            petRecord.addComponents(petAvatar, petInfoLayout);

            petRecordsLayout.addComponents(petRecord, PageElements.getSeparator());
        }
        mainPanel.setContent(petRecordsLayout);
        addComponent(mainPanel);
    }

    private List<Pet> getAllPets() {
        return Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/pets/", Pet[].class));
    }
}
