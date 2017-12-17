package com.netcracker.ui.pet;

import com.netcracker.error.asserts.ObjectAssert;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.pet.PetSpecies;
import com.netcracker.model.user.Profile;
import com.netcracker.model.user.User;
import com.netcracker.service.user.impl.UserService;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.server.ExternalResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringComponent
@UIScope
public class PetFormUI extends VerticalLayout {

    Pet createdPet;

    public PetFormUI() {
        createdPet = new Pet();
        this.addStyleName("v-scrollable");
        this.setHeight("100%");
        Panel mainPanel = new Panel();
        VerticalLayout mainLayout = new VerticalLayout();

        //AVATAR
        GridLayout avatarLayout = new GridLayout(2, 1);
        avatarLayout.setWidth("100%");
        avatarLayout.setColumnExpandRatio(1, 2);

        VerticalLayout avatarContext = new VerticalLayout();

        Image avatar = PageElements.getNoImage();
        avatar.setWidth("225px");
        avatar.setHeight("150px");

        TextField avatarField = PageElements.createTextField("Аватар", "Ссылка на аватар");
        avatarField.setWidth("100%");
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

        //FIELDS
        TextField petNameField = PageElements.createTextField("Кличка питомца", "Кличка питомца", true);
        petNameField.setWidth("100%");

        List<PetSpecies> speciesList = getSpecies();
        ComboBox<PetSpecies> speciesTypeSelect = new ComboBox("Вид", speciesList);
        speciesTypeSelect.setItemCaptionGenerator(PetSpecies::getSpeciesName);

        speciesTypeSelect.setEmptySelectionAllowed(false);
        speciesTypeSelect.setRequiredIndicatorVisible(true);
        speciesTypeSelect.setWidth("100%");
        speciesTypeSelect.setTextInputAllowed(false);
        speciesTypeSelect.setValue(speciesList.get(0));

        TextField breedTypeField = PageElements.createTextField("Порода питомца", "Порода питомца", true);
        breedTypeField.setWidth("100%");

        TextField ageField = PageElements.createTextField("Возраст", "Возраст (полных лет)");
        ageField.setWidth("100%");

        TextField weightField = PageElements.createTextField("Вес", "Вес (кг)");
        weightField.setWidth("100%");

        TextField heightField = PageElements.createTextField("Рост", "Рост (м)");
        heightField.setWidth("100%");

        TextArea specParamField = new TextArea("Особые данные");
        specParamField.setWidth("100%");

        Button addPet = new Button("Добавить питомца");
        addPet.setWidth("100%");
        addPet.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                addPet.setComponentError(null);
                createPet(avatarField.getValue(), petNameField.getValue(), ageField.getValue(), speciesTypeSelect.getValue(), breedTypeField.getValue(),
                        weightField.getValue(), heightField.getValue(), specParamField.getValue());
                Notification.show("Питомец успешно добавлен!");
            }
        });

        mainLayout.addComponents(avatarLayout, petNameField, speciesTypeSelect, breedTypeField,
                ageField, weightField, heightField, specParamField, addPet);

        mainPanel.setContent(mainLayout);

        addComponents(mainPanel);
    }

    private void updateAvatar(String imageURL, Image imageToUpdate) {
        ObjectAssert.isNullOrEmpty(imageURL, "Вы должны ввести ссылку на картинку!");
        createdPet.setPetAvatar(imageURL);
        imageToUpdate.setSource(new ExternalResource(imageURL));
    }

    private List<PetSpecies> getSpecies(){
        List<PetSpecies> speciesList = Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/species" , PetSpecies[].class));
        return speciesList;
    }

    private void createPet(String avatar, String petName, String petAge, PetSpecies petSpecies, String petBreed,
                           String petWeight, String petHeight, String specificParameters) {

        ObjectAssert.isNullOrEmpty(petName);
        ObjectAssert.isNull(petSpecies);

        //User currentUser = new UserService().getCurrentUser();
        /*Profile profile = new Profile();
        profile.setObjectId(BigInteger.valueOf(1));*/

        Integer age = Integer.parseInt(petAge);
        Double weight = Double.parseDouble(petWeight);
        Double height = Double.parseDouble(petHeight);
        Pet createdPet = new Pet(avatar, petName, age, petSpecies, petBreed,
                weight, height, specificParameters/*, profile*/);

        HttpEntity<Pet> pet = new HttpEntity<>(createdPet);

        CustomRestTemplate.getInstance()
                .customPostForObject("/pet/add", pet, Pet.class);
    }
}
