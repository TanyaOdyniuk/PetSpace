package com.netcracker.ui.pet;

import com.netcracker.asserts.PetDataAssert;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.pet.PetSpecies;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.StubVaadinUI;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.*;
import org.springframework.http.HttpEntity;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class PetEditFormUI extends Window {

    Pet pet;

    public PetEditFormUI(Pet pet) {
        super();
        this.pet = pet;
        setCaption("Информация о питомце");
        VerticalLayout mainLayout = new VerticalLayout();

        GridLayout avatarLayout = new GridLayout(2,1);
        VerticalLayout avatarContext = new VerticalLayout();

        Image avatar;
        if(pet.getPetAvatar() == null)
            avatar = PageElements.getNoImage();
        else
            avatar = new Image("", new ExternalResource(pet.getPetAvatar()));
        avatar.setHeight("200px");
        avatar.setWidth("200px");
        TextField avatarField = PageElements.createTextField("Аватар", "Ссылка на аватар", true);
        avatarField.setWidth("100%");
        avatarField.setValue(pet.getPetAvatar() == null ? "" : pet.getPetAvatar());
        Button avatarSelect = new Button("Загрузить");
        avatarSelect.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                avatarSelect.setComponentError(null);
                updateAvatar(avatarField.getValue(), avatar);
            }
        });

        avatarContext.addComponents(avatarField, avatarSelect);

        avatarLayout.addComponent(avatar, 0, 0);
        avatarLayout.addComponent(avatarContext, 1, 0);

        mainLayout.addComponent(avatarLayout);

        GridLayout infoLayout = new GridLayout(2,3);
        infoLayout.setWidth("100%");
        infoLayout.setSpacing(true);
        //FIELDS
        TextField petNameField = PageElements.createTextField("Кличка питомца", "Кличка питомца", true);
        petNameField.setWidth("100%");
        petNameField.setValue(pet.getPetName() == null ? "" : pet.getPetName());

        List<PetSpecies> speciesList = getSpecies();
        ComboBox<PetSpecies> speciesTypeSelect = new ComboBox("Вид", speciesList);
        speciesTypeSelect.setItemCaptionGenerator(PetSpecies::getSpeciesName);

        speciesTypeSelect.setEmptySelectionAllowed(false);
        speciesTypeSelect.setRequiredIndicatorVisible(true);
        speciesTypeSelect.setWidth("100%");
        speciesTypeSelect.setTextInputAllowed(false);
        speciesTypeSelect.setValue(pet.getPetSpecies() == null ? speciesList.get(0) : pet.getPetSpecies());

        TextField breedTypeField = PageElements.createTextField("Порода питомца", "Порода питомца");
        breedTypeField.setWidth("100%");
        breedTypeField.setValue(pet.getPetBreed() == null ? "" : pet.getPetBreed());

        TextField ageField = PageElements.createTextField("Возраст", "Возраст (полных лет)");
        ageField.setWidth("100%");
        ageField.setValue(pet.getPetAge() == null ? "" : pet.getPetAge().toString() );

        TextField weightField = PageElements.createTextField("Вес", "Вес (кг)");
        weightField.setWidth("100%");
        weightField.setValue(pet.getPetWeight() == null ? "" : pet.getPetWeight().toString());

        TextField heightField = PageElements.createTextField("Рост", "Рост (м)");
        heightField.setWidth("100%");
        heightField.setValue(pet.getPetHeight() == null ? "" : pet.getPetHeight().toString());

        infoLayout.addComponent(petNameField, 0,0);
        infoLayout.addComponent(speciesTypeSelect, 1,0);
        infoLayout.addComponent(breedTypeField, 0,1);
        infoLayout.addComponent(ageField, 1,1);
        infoLayout.addComponent(weightField, 0,2);
        infoLayout.addComponent(heightField, 1,2);

        mainLayout.addComponent(infoLayout);

        TextArea specParamField = new TextArea("Особые данные");
        specParamField.setWidth("100%");
        specParamField.setValue(pet.getPetSpecificParam() == null ? "" : pet.getPetSpecificParam());

        Button addPet;
        if(pet.getObjectId() == null) {
            addPet = new Button("Добавить питомца");
            addPet.setWidth("100%");
            addPet.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    addPet.setComponentError(null);
                    createPet(avatarField.getValue(), petNameField.getValue(), ageField.getValue(), speciesTypeSelect.getValue(), breedTypeField.getValue(),
                            weightField.getValue(), heightField.getValue(), specParamField.getValue());
                }
            });
        } else {
            addPet = new Button("Обновить данные");
            addPet.setWidth("100%");
            addPet.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    addPet.setComponentError(null);
                    updatePet(avatarField.getValue(), petNameField.getValue(), ageField.getValue(), speciesTypeSelect.getValue(), breedTypeField.getValue(),
                            weightField.getValue(), heightField.getValue(), specParamField.getValue());
                }
            });
        }

        mainLayout.addComponents(specParamField, addPet);

        setContent(mainLayout);
        center();
    }

    private void updateAvatar(String imageURL, Image imageToUpdate) {
        PetDataAssert.assertAvatarURL(imageURL);
        pet.setPetAvatar(imageURL);
        imageToUpdate.setSource(new ExternalResource(imageURL));
    }

    private List<PetSpecies> getSpecies() {
        List<PetSpecies> speciesList = Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/species", PetSpecies[].class));
        return speciesList;
    }

    private void createPet(String avatar, String petName, String petAge, PetSpecies petSpecies, String petBreed,
                           String petWeight, String petHeight, String specificParameters) {
        PetDataAssert.assertAvatarURL(avatar);
        PetDataAssert.assertName(petName);

        Integer age = PetDataAssert.assertAge(petAge);
        Double weight = PetDataAssert.assertWeight(petWeight);
        Double height = PetDataAssert.assertHeight(petHeight);

        /*User currentUser = new UserService().getCurrentUser();
        Profile profile = new Profile();
        profile.setObjectId(BigInteger.valueOf(1));*/

        Pet createdPet = new Pet(avatar, petName, age, petSpecies, petBreed,
                weight, height, specificParameters/*, profile*/);

        HttpEntity<Pet> petEntity = new HttpEntity<>(createdPet);

        CustomRestTemplate.getInstance()
                .customPostForObject("/pet/add", petEntity, Pet.class);
        Notification.show("Питомец успешно добавлен!");
        this.close();
        ((StubVaadinUI)UI.getCurrent()).changePrimaryAreaLayout(new MyPetsListUI(BigInteger.valueOf(1)));
    }

    private void updatePet(String avatar, String petName, String petAge, PetSpecies petSpecies, String petBreed,
                           String petWeight, String petHeight, String specificParameters){
        PetDataAssert.assertAvatarURL(avatar);
        PetDataAssert.assertName(petName);

        Integer age = PetDataAssert.assertAge(petAge);
        Double weight = PetDataAssert.assertWeight(petWeight);
        Double height = PetDataAssert.assertHeight(petHeight);

        pet.setPetAvatar(avatar);
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
        Notification.show("Данные питомца обновлены");
        this.close();
        ((StubVaadinUI)UI.getCurrent()).changePrimaryAreaLayout(new PetPageUI(pet.getObjectId()));
    }
}
