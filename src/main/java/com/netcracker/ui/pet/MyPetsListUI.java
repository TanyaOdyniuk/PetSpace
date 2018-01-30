package com.netcracker.ui.pet;

import com.netcracker.model.pet.Pet;
import com.netcracker.service.util.RestResponsePage;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.PagingBar;
import com.netcracker.ui.MainUI;
import com.netcracker.ui.util.CustomRestTemplate;
import com.netcracker.ui.util.VaadinValidationBinder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;
import java.util.List;

public class MyPetsListUI extends VerticalLayout {

    private BigInteger profileId;
    private int browserHeight;

    private RestResponsePage<Pet> petsResponse;

    private VerticalLayout petRecordsLayout;
    private PagingBar pagingLayout;
    private Panel mainPanel;

    public MyPetsListUI(BigInteger profileId) {
        super();
        this.profileId = profileId;
        this.browserHeight = UI.getCurrent().getPage().getBrowserWindowHeight();

        this.addStyleName("v-scrollable");
        this.setHeightUndefined();

        mainPanel = new Panel();
        mainPanel.setWidth("100%");
        mainPanel.setHeight(browserHeight - 300, Unit.PIXELS);
        petRecordsLayout = new VerticalLayout();
        Button addNewPet = new Button("Add new pet", VaadinIcons.PLUS);
        addNewPet.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                PetEditFormUI sub = new PetEditFormUI(new Pet());
                UI.getCurrent().addWindow(sub);
            }
        });

        initMyPetsPage(1);

        mainPanel.setContent(petRecordsLayout);
        addComponents(addNewPet, mainPanel);
    }

    private void initMyPetsPage(int pageNumber) {
        petRecordsLayout.removeAllComponents();
        List<Pet> petList = getProfilePets(profileId, pageNumber);
        if (petList.isEmpty()) {
            Label noPetsLabel = PageElements.createLabel(5, "You don't have any pet yet!");
            petRecordsLayout.addComponent(noPetsLabel);
            petRecordsLayout.setComponentAlignment(noPetsLabel, Alignment.MIDDLE_CENTER);
            return;
        }
        for (Pet pet : petList) {
            HorizontalLayout petRecord = new HorizontalLayout();
            VerticalLayout petInfoLayout = new VerticalLayout();
            Image petAvatar = new Image();
            petAvatar.setHeight(250, Unit.PIXELS);
            petAvatar.setWidth(250, Unit.PIXELS);
            String petAvatarSource = pet.getPetAvatar();
            PageElements.setPetImageSource(petAvatar, petAvatarSource);
            petAvatar.setDescription("Pet avatar");
            petAvatar.addClickListener((MouseEvents.ClickListener) clickEvent -> ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new PetPageUI(pet.getObjectId())));

            Label petNameSign = PageElements.createGrayLabel("Pet's name");
            Button petName = PageElements.createClickedLabel(pet.getPetName());
            petName.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new PetPageUI(pet.getObjectId()));
                }
            });
            Label petInfo = PageElements.createCheckedValueLabel(pet.getPetSpecificParam());
            petInfo.setCaption("Information about pet");

            //PET INFO
            petInfoLayout.addComponents(petNameSign, petName, petInfo);

            //INFO + AVATAR
            petRecord.addComponents(petAvatar, petInfoLayout);

            petRecordsLayout.addComponents(petRecord, PageElements.getSeparator());
        }
        if (pagingLayout != null)
            petRecordsLayout.addComponent(pagingLayout);
        else
            setPagingLayout();
    }

    private List<Pet> getProfilePets(BigInteger profileId, int pageNumber) {
        ResponseEntity<RestResponsePage<Pet>> pageResponseEntity =
                CustomRestTemplate.getInstance().customExchangeForParametrizedTypes("/pets/" + profileId + "/" + pageNumber, HttpMethod.GET,
                        null, new ParameterizedTypeReference<RestResponsePage<Pet>>() {
                        });
        petsResponse = pageResponseEntity.getBody();
        return petsResponse.getContent();
    }

    private void setPagingLayout() {
        if (pagingLayout != null) {
            petRecordsLayout.removeComponent(pagingLayout);
        }
        int pageCount = (int) petsResponse.getTotalElements();
        if (pageCount > 1) {
            pagingLayout = new PagingBar(pageCount, 1);
            pagingLayout.checkButtonsState();
            pagingLayout.getFirstPageButton().addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    Integer page = (Integer) pagingLayout.getFirstPageButton().getData();
                    initMyPetsPage(page);
                    pagingLayout.currentPageNumber = 1;
                    pagingLayout.getPageNumberField().setValue(String.valueOf(page));
                    pagingLayout.checkButtonsState();
                }
            });
            pagingLayout.getLastPageButton().addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    Integer page = (Integer) pagingLayout.getLastPageButton().getData();
                    initMyPetsPage(page);
                    pagingLayout.currentPageNumber = page;
                    pagingLayout.getPageNumberField().setValue(String.valueOf(page));
                    pagingLayout.checkButtonsState();
                }
            });
            pagingLayout.getPrevPageButton().addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    if (pagingLayout.currentPageNumber > 1) {
                        --pagingLayout.currentPageNumber;
                        initMyPetsPage(pagingLayout.currentPageNumber);
                        pagingLayout.getPageNumberField().setValue(String.valueOf(pagingLayout.currentPageNumber));
                        pagingLayout.checkButtonsState();
                    }
                }
            });
            pagingLayout.getNextPageButton().addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    if (pagingLayout.currentPageNumber < pageCount) {
                        ++pagingLayout.currentPageNumber;
                        initMyPetsPage(pagingLayout.currentPageNumber);
                        pagingLayout.getPageNumberField().setValue(String.valueOf(pagingLayout.currentPageNumber));
                        pagingLayout.checkButtonsState();
                    }
                }
            });

            pagingLayout.getPageNumberField().addShortcutListener(new ShortcutListener("Enter", ShortcutAction.KeyCode.ENTER, null) {
                @Override
                public void handleAction(Object o, Object o1) {
                    BinderValidationStatus<VaadinValidationBinder> status = pagingLayout.pageNumberFieldBinder.validate();
                    if (!status.hasErrors()) {
                        pagingLayout.currentPageNumber = Integer.valueOf(pagingLayout.getPageNumberField().getValue());
                        pagingLayout.checkButtonsState();
                        initMyPetsPage(pagingLayout.currentPageNumber);
                    }
                }
            });
            petRecordsLayout.addComponent(pagingLayout);
        }
    }
}