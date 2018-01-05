package com.netcracker.ui.secutitybook;


import com.netcracker.model.securitybook.SecurityBook;
import com.netcracker.model.securitybook.SecurityType;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringComponent
@UIScope
public class SecurityBookUI extends VerticalLayout {

    private BigInteger profileId;
    private List<SecurityType> bookList;
    private int browserWidth;

    public SecurityBookUI(BigInteger profileId) {
        this.profileId = profileId;
        this.bookList = getSecurityBook();
        this.browserWidth = UI.getCurrent().getPage().getBrowserWindowWidth()/3;

        setSizeFull();
        setSpacing(true);

        VerticalLayout mainLayout = new VerticalLayout();

        Panel mainPanel = new Panel();

        Label textLabel = PageElements.createLabel(5, "Настройки приватности");

        ComboBox<SecurityType> friendsBook = new ComboBox<>("Кто может видеть моих друзей", bookList);
        friendsBook.setItemCaptionGenerator(SecurityType::getSecurityType);
        friendsBook.setEmptySelectionAllowed(false);
        friendsBook.setTextInputAllowed(false);
        friendsBook.setWidth(250, Unit.PIXELS);
        friendsBook.setValue(bookList.get(0));

        ComboBox<SecurityType> albumsBook = new ComboBox<>("Кто может видеть мои альбомы", bookList);
        albumsBook.setItemCaptionGenerator(SecurityType::getSecurityType);
        albumsBook.setEmptySelectionAllowed(false);
        albumsBook.setTextInputAllowed(false);
        albumsBook.setWidth(250, Unit.PIXELS);
        albumsBook.setValue(bookList.get(0));

        ComboBox<SecurityType> petsBook = new ComboBox<>("Кто может видеть моих питомцев", bookList);
        petsBook.setItemCaptionGenerator(SecurityType::getSecurityType);
        petsBook.setEmptySelectionAllowed(false);
        petsBook.setTextInputAllowed(false);
        petsBook.setWidth(250, Unit.PIXELS);
        petsBook.setValue(bookList.get(0));

        ComboBox<SecurityType> recordsBook = new ComboBox<>("Кто может видеть мои записи", bookList);
        recordsBook.setItemCaptionGenerator(SecurityType::getSecurityType);
        recordsBook.setEmptySelectionAllowed(false);
        recordsBook.setTextInputAllowed(false);
        recordsBook.setWidth(250, Unit.PIXELS);
        recordsBook.setValue(bookList.get(0));

        ComboBox<SecurityType> infoBook = new ComboBox<>("Кто может видеть информацию обо мне", bookList);
        infoBook.setItemCaptionGenerator(SecurityType::getSecurityType);
        infoBook.setEmptySelectionAllowed(false);
        infoBook.setTextInputAllowed(false);
        infoBook.setWidth(250, Unit.PIXELS);
        infoBook.setValue(bookList.get(0));

        Button confirmButton = new Button("Сохранить настройки");
        confirmButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                saveChanges();
            }
        });

        mainLayout.addComponents(textLabel, friendsBook, albumsBook, petsBook, recordsBook, infoBook, confirmButton);
        mainLayout.setComponentAlignment(textLabel, Alignment.MIDDLE_CENTER);
        mainLayout.setComponentAlignment(confirmButton, Alignment.MIDDLE_CENTER);
        mainPanel.setContent(mainLayout);
        addComponent(mainPanel);
    }



    private List<SecurityType> getSecurityBook(){
        List<SecurityType> securityTypeList = Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/securitybook/types", SecurityType[].class));
        return securityTypeList;
    }

    private void saveChanges(){

    }
}
