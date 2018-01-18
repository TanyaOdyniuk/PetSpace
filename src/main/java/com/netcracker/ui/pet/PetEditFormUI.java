package com.netcracker.ui.pet;

import com.netcracker.asserts.PetDataAssert;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.pet.PetSpecies;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.StubVaadinUI;
import com.netcracker.ui.UIConstants;
import com.netcracker.ui.util.CustomRestTemplate;
import com.netcracker.ui.util.UploadWindow;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class PetEditFormUI extends Window {

    private Pet pet;
    private Window windowToSend;
    private Image avatar;
    private String avatarPath;
    private Boolean isFileResource;

    PetEditFormUI(Pet pet) {
        super();
        this.pet = pet;
        this.windowToSend = this;
        this.isFileResource = false;
        setCaption("Information about pet");
        VerticalLayout mainLayout = new VerticalLayout();

        GridLayout avatarLayout = new GridLayout(2, 1);
        VerticalLayout avatarContext = new VerticalLayout();
        String petAvatarSource = pet.getPetAvatar();
        avatar = new Image();
        TextField avatarField = PageElements.createTextField("Avatar", "Avatar's URL");
        if (petAvatarSource != null) {
            if (PetDataAssert.isAvatarURL(petAvatarSource)) {
                avatar.setSource(new ExternalResource(petAvatarSource));
                avatarField.setValue(pet.getPetAvatar());
            }
            else
                avatar.setSource(new FileResource(new File(petAvatarSource)));
        } else
            avatar = PageElements.getNoImage();
        avatar.setHeight("200px");
        avatar.setWidth("200px");

        avatarField.setWidth("100%");


        Button avatarSelect = new Button("Set URL");
        avatarSelect.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                avatarSelect.setComponentError(null);
                updateAvatar(avatarField.getValue(), avatar);
            }
        });

        Button uploadAvatar = new Button("Upload");
        uploadAvatar.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                UploadWindow sub = new UploadWindow(UIConstants.PATH_TO_AVATAR_PET, windowToSend);
                UI.getCurrent().addWindow(sub);
            }
        });

        avatarSelect.setWidth("185px");
        uploadAvatar.setWidth("185px");

        avatarContext.addComponents(avatarField, avatarSelect, uploadAvatar);

        avatarLayout.addComponent(avatar, 0, 0);
        avatarLayout.addComponent(avatarContext, 1, 0);

        mainLayout.addComponent(avatarLayout);

        GridLayout infoLayout = new GridLayout(2, 3);
        infoLayout.setWidth("100%");
        infoLayout.setSpacing(true);
        //FIELDS
        TextField petNameField = PageElements.createTextField("Pet's name", "Pet's name", true);
        petNameField.setWidth("100%");
        petNameField.setValue(pet.getPetName() == null ? "" : pet.getPetName());

        List<PetSpecies> speciesList = getSpecies();
        ComboBox<PetSpecies> speciesTypeSelect = new ComboBox<>("Species", speciesList);
        speciesTypeSelect.setItemCaptionGenerator(PetSpecies::getSpeciesName);

        speciesTypeSelect.setEmptySelectionAllowed(false);
        speciesTypeSelect.setRequiredIndicatorVisible(true);
        speciesTypeSelect.setWidth("100%");
        speciesTypeSelect.setTextInputAllowed(false);
        speciesTypeSelect.setValue(pet.getPetSpecies() == null ? speciesList.get(0) : pet.getPetSpecies());

        TextField breedTypeField = PageElements.createTextField("Pet's breed", "Pet's breed");
        breedTypeField.setWidth("100%");
        breedTypeField.setValue(pet.getPetBreed() == null ? "" : pet.getPetBreed());

        TextField ageField = PageElements.createTextField("Age", "Age (full years)", true);
        ageField.setWidth("100%");
        ageField.setValue(pet.getPetAge() == null ? "" : pet.getPetAge().toString());

        TextField weightField = PageElements.createTextField("Weight", "Weight (kg)");
        weightField.setWidth("100%");
        weightField.setValue(pet.getPetWeight() == null ? "" : pet.getPetWeight().toString());

        TextField heightField = PageElements.createTextField("Height", "Height (m)");
        heightField.setWidth("100%");
        heightField.setValue(pet.getPetHeight() == null ? "" : pet.getPetHeight().toString());

        infoLayout.addComponent(petNameField, 0, 0);
        infoLayout.addComponent(speciesTypeSelect, 1, 0);
        infoLayout.addComponent(breedTypeField, 0, 1);
        infoLayout.addComponent(ageField, 1, 1);
        infoLayout.addComponent(weightField, 0, 2);
        infoLayout.addComponent(heightField, 1, 2);

        mainLayout.addComponent(infoLayout);

        TextArea specParamField = new TextArea("Other information");
        specParamField.setWidth("100%");
        specParamField.setValue(pet.getPetSpecificParam() == null ? "" : pet.getPetSpecificParam());

        Button addPet;
        if (pet.getObjectId() == null) {
            addPet = new Button("Add pet");
            addPet.setWidth("100%");
            addPet.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    addPet.setComponentError(null);
                    createPet(avatarPath, petNameField.getValue(), ageField.getValue(), speciesTypeSelect.getValue(), breedTypeField.getValue(),
                            weightField.getValue(), heightField.getValue(), specParamField.getValue());
                }
            });
        } else {
            addPet = new Button("Update information");
            addPet.setWidth("100%");
            addPet.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    addPet.setComponentError(null);
                    updatePet(avatarPath, petNameField.getValue(), ageField.getValue(), speciesTypeSelect.getValue(), breedTypeField.getValue(),
                            weightField.getValue(), heightField.getValue(), specParamField.getValue());
                }
            });
        }

        mainLayout.addComponents(specParamField, addPet);

        setContent(mainLayout);
        center();
    }

    public void updateAvatar(File imageSource) {
        avatar.setSource(new FileResource(imageSource));
        this.isFileResource = true;
        this.avatarPath = imageSource.getPath();
    }

    private void updateAvatar(String imageURL, Image imageToUpdate) {
        imageURL = PetDataAssert.assertAvatarURL(imageURL);
        pet.setPetAvatar(imageURL);
        imageToUpdate.setSource(new ExternalResource(imageURL));
        this.isFileResource = false;
        this.avatarPath = imageURL;
    }

    private List<PetSpecies> getSpecies() {
        return Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/species", PetSpecies[].class));
    }

    private void createPet(String avatarPath, String petName, String petAge, PetSpecies petSpecies, String petBreed,
                           String petWeight, String petHeight, String specificParameters) {

        if (!isFileResource)
            avatarPath = PetDataAssert.assertAvatarURL(avatarPath);
        PetDataAssert.assertName(petName);

        Integer age = PetDataAssert.assertAge(petAge);
        Double weight = PetDataAssert.assertWeight(petWeight);
        Double height = PetDataAssert.assertHeight(petHeight);

        Pet createdPet = new Pet(avatarPath, petName, age, petSpecies, petBreed,
                weight, height, specificParameters);

        SecurityContext o = (SecurityContext) VaadinSession.getCurrent().getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        String login = o.getAuthentication().getPrincipal().toString();

        HttpHeaders headers = new HttpHeaders();
        headers.add("login", login);

        HttpEntity<Pet> petEntity = new HttpEntity<>(createdPet, headers);

        createdPet = CustomRestTemplate.getInstance()
                .customPostForObject("/pet/add", petEntity, Pet.class);
        Notification.show("Pet was successfully added!");
        this.close();
        ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new PetPageUI(createdPet.getObjectId()));
    }

    private void updatePet(String avatarPath, String petName, String petAge, PetSpecies petSpecies, String petBreed,
                           String petWeight, String petHeight, String specificParameters) {

        if (!isFileResource)
            avatarPath = PetDataAssert.assertAvatarURL(avatarPath);
        PetDataAssert.assertName(petName);

        Integer age = PetDataAssert.assertAge(petAge);
        Double weight = PetDataAssert.assertWeight(petWeight);
        Double height = PetDataAssert.assertHeight(petHeight);

        pet.setPetAvatar(avatarPath);
        pet.setPetName(petName);
        pet.setPetAge(age);
        pet.setPetSpecies(petSpecies);
        pet.setPetBreed(petBreed);
        pet.setPetWeight(weight);
        pet.setPetHeight(height);
        pet.setPetSpecificParam(specificParameters);

        HttpEntity<Pet> petEntity = new HttpEntity<>(pet);

        CustomRestTemplate.getInstance()
                .customPostForObject("/pet/update", petEntity, Pet.class);
        Notification.show("Pet's information was updated");
        this.close();
        ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new PetPageUI(pet.getObjectId()));
    }
}
