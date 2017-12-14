package com.netcracker.ui.pet;

import com.netcracker.error.asserts.ObjectAssert;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.pet.PetSpecies;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringComponent
@UIScope
public class PetFormUI extends VerticalLayout {

    Pet createdPet;

    public PetFormUI() {
        createdPet = new Pet();

        Panel mainPanel = new Panel();
        mainPanel.setWidth("100%");
        mainPanel.setHeight(750, Unit.PIXELS);
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

        /*ComboBox<PetSpecies> speciesTypeSelect = new ComboBox("Вид", getSpecies());
        speciesTypeSelect.setItemCaptionGenerator(PetSpecies::getSpeciesName);*/

        //TODO CHANGE CODE BELOW WITH COMMENTED HIGHER CODE
        List<String> speciesList = new ArrayList<>();
        speciesList.add("Собака");
        speciesList.add("Кошка");
        speciesList.add("Хомяк");
        ComboBox<String> speciesTypeSelect = new ComboBox("Вид", speciesList);
        //TODO TILL THIS PLACE

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
                avatarSelect.setComponentError(null);
                //TODO Add pet
            }
        });

        mainLayout.addComponents(avatarLayout, petNameField, speciesTypeSelect, breedTypeField,
                ageField, weightField, heightField, specParamField, addPet);

        mainPanel.setContent(mainLayout);

        addComponentsAndExpand(mainPanel);
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

    private void createPet(String avatar, String petName, Integer petAge, PetSpecies petSpecies, String petBreed,
                           Double petWeight, Double petHeight, String specificParameters) {

        Pet createdPet = new Pet(avatar, petName, petAge, petSpecies, petBreed,
                petWeight, petHeight, specificParameters);

        /*BigInteger maxId = */

        HttpEntity<Pet> requestUpdate = new HttpEntity<>(createdPet);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        CustomRestTemplate.getInstance()
                .customExchange("/pets/" + createdPet.getObjectId(), HttpMethod.PUT, requestUpdate, Void.class);


        //CustomRestTemplate.getInstance().customExchange();
        /*
        HttpEntity<StubUser> requestUpdate = new HttpEntity<>(stubUser);
                HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                CustomRestTemplate.getInstance()
                        .customExchange("/restcontroller/"+ stubUser.getId(), HttpMethod.PUT, requestUpdate, Void.class);
        */
    }
}
