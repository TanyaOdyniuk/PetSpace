package com.netcracker.ui.pet;

import com.netcracker.model.pet.Pet;
import com.netcracker.model.pet.PetSpecies;
import com.netcracker.model.user.Profile;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.MainUI;
import com.netcracker.ui.gallery.AlbumsUI;
import com.netcracker.ui.profile.ProfileView;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.math.BigInteger;

public class PetPageUI extends VerticalLayout {

    private BigInteger petId;
    private PetSpecies petSpecies;
    private Pet pet;

    public PetPageUI(BigInteger petId) {
        this.petId = petId;
        this.petSpecies = getSpecies();
        this.pet = getPet(petId);

        int browserHeight = UI.getCurrent().getPage().getBrowserWindowHeight();
        int browserWidth = UI.getCurrent().getPage().getBrowserWindowWidth();

        setSizeFull();
        setSpacing(true);

        BigInteger currentUserProfileId = getCurrentUserProfileId();

        Profile owner = CustomRestTemplate.getInstance().customGetForObject("/profile/" + pet.getPetOwner().getObjectId(), Profile.class);

        HorizontalLayout mainLayout = new HorizontalLayout();
        VerticalLayout leftPageLayout = new VerticalLayout();
        VerticalLayout rightPagePart = new VerticalLayout();
        VerticalLayout avatarLayout = new VerticalLayout();
        VerticalLayout infoLayout = new VerticalLayout();

        Panel rightPagePanel = new Panel();
        rightPagePanel.setHeight(browserHeight - 250, Unit.PIXELS);
        rightPagePanel.setWidth(browserWidth * 0.7f - 252, Unit.PIXELS);

        Panel avatarPanel = new Panel();
        avatarPanel.setWidth("252px");
        avatarPanel.setHeight("100%");

        Panel infoPanel = new Panel();
        infoPanel.setWidth("100%");
        infoPanel.setHeight("100%");

        Image petAvatar = new Image();
        String petAvatarSource = pet.getPetAvatar();
        PageElements.setPetImageSource(petAvatar, petAvatarSource);
        petAvatar.setHeight(225, Unit.PIXELS);
        petAvatar.setWidth(225, Unit.PIXELS);
        petAvatar.setDescription("Pet avatar");

        leftPageLayout.addComponents(petAvatar, PageElements.getSeparator());

        if (owner.getObjectId().equals(currentUserProfileId)) {
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
                    PetDeleteConfirmationWindow sub = new PetDeleteConfirmationWindow(pet.getObjectId(), owner.getObjectId());
                    UI.getCurrent().addWindow(sub);
                }
            });
            editPage.setWidth("100%");
            deletePage.setWidth("100%");

            editPage.setIcon(VaadinIcons.EDIT);
            deletePage.setIcon(VaadinIcons.TRASH);

            leftPageLayout.addComponents(editPage, deletePage, PageElements.getSeparator());
        }

        Button albums = PageElements.createClickedLabel("Owner's albums");
        albums.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new AlbumsUI(owner.getObjectId()));
            }
        });

        albums.setWidth("100%");
        albums.setIcon(VaadinIcons.PICTURE);

        leftPageLayout.addComponents(albums);
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
                ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(owner.getObjectId()));
            }
        });
        petOwner.setHeight("25px");


        Label additionInfo = PageElements.createGrayLabel("Additional information");

        Label petWeight = PageElements.createCheckedValueLabel(pet.getPetWeight(), "kg");
        petWeight.setCaption("Weight");

        Label petHeight = PageElements.createCheckedValueLabel(pet.getPetHeight(), "m");
        petHeight.setCaption("Height");

        Label petSpecParam = PageElements.createCheckedValueLabel(pet.getPetSpecificParam());
        petSpecParam.setCaption("Other info");

        infoLayout.addComponents(petName, PageElements.getSeparator(), petSpecies, petBreed,
                petAge, petOwnerSign, petOwner, additionInfo, PageElements.getSeparator(), petWeight,
                petHeight, petSpecParam);

        infoPanel.setContent(infoLayout);
        infoPanel.setHeight("100%");
        infoPanel.setWidth("100%");

        rightPagePart.addComponent(infoPanel);
        rightPagePanel.setContent(rightPagePart);
        mainLayout.addComponents(avatarPanel, rightPagePanel);
        addComponents(mainLayout);
    }

    private Pet getPet(BigInteger petId) {
        return CustomRestTemplate.getInstance().customGetForObject(
                "/pet/" + petId, Pet.class);
    }

    private PetSpecies getSpecies() {
        return CustomRestTemplate.getInstance().customGetForObject(
                "/pet/" + petId + "/species", PetSpecies.class);
    }

    private BigInteger getCurrentUserProfileId() {
        SecurityContext o = (SecurityContext) VaadinSession.getCurrent().getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        String login = o.getAuthentication().getPrincipal().toString();
        return CustomRestTemplate.getInstance().customPostForObject("/user/profileId", login, BigInteger.class);
    }
}
