package com.netcracker.ui.pet;

import com.netcracker.asserts.PetDataAssert;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.user.Profile;
import com.netcracker.service.util.RestResponsePage;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.StubPagingBar;
import com.netcracker.ui.StubVaadinUI;
import com.netcracker.ui.profile.ProfileView;
import com.netcracker.ui.util.CustomRestTemplate;
import com.netcracker.ui.util.VaadinValidationBinder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.ui.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.List;

public class AllPetsListUI extends VerticalLayout {

    private int browserHeight;

    private RestResponsePage<Pet> petsResponse;

    private StubPagingBar pagingLayout;
    private VerticalLayout petRecordsLayout;

    public AllPetsListUI() {
        super();
        this.browserHeight = UI.getCurrent().getPage().getBrowserWindowHeight();

        Panel mainPanel = new Panel();
        mainPanel.setWidth("100%");
        mainPanel.setHeight(browserHeight - 250, Unit.PIXELS);
        petRecordsLayout = new VerticalLayout();

        initPetsPage(1);

        mainPanel.setContent(petRecordsLayout);
        addComponent(mainPanel);
        setPagingLayout();
    }

    private void initPetsPage(int pageNumber){
        petRecordsLayout.removeAllComponents();
        List<Pet> petList = getAllPets(pageNumber);
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
        if(pagingLayout != null)
            petRecordsLayout.addComponent(pagingLayout);
        else
            setPagingLayout();
    }

    private List<Pet> getAllPets(int pageNumber) {
        ResponseEntity<RestResponsePage<Pet>> pageResponseEntity =
                CustomRestTemplate.getInstance().customExchangeForParametrizedTypes("/pets/" + pageNumber, HttpMethod.GET,
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
            pagingLayout = new StubPagingBar(pageCount);
            pagingLayout.setBorderButtonsState(true);
            pagingLayout.getFirstPageButton().addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    pagingLayout.setBorderButtonsState(true);
                    Integer page = (Integer) pagingLayout.getFirstPageButton().getData();
                    initPetsPage(page);
                    pagingLayout.currentPageNumber = 1;
                    pagingLayout.getPageNumberField().setValue(String.valueOf(page));
                }
            });
            pagingLayout.getLastPageButton().addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    pagingLayout.setBorderButtonsState(false);
                    Integer page = (Integer) pagingLayout.getLastPageButton().getData();
                    initPetsPage(page);
                    pagingLayout.currentPageNumber = page;
                    pagingLayout.getPageNumberField().setValue(String.valueOf(page));
                }
            });
            pagingLayout.getPrevPageButton().addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    if (pagingLayout.currentPageNumber > 1) {
                        --pagingLayout.currentPageNumber;
                        initPetsPage(pagingLayout.currentPageNumber);
                        pagingLayout.getPageNumberField().setValue(String.valueOf(pagingLayout.currentPageNumber));
                        if (pagingLayout.currentPageNumber == 1)
                            pagingLayout.setBorderButtonsState(true);
                        else
                            pagingLayout.setAllButtonsStateEnabled();
                    }
                }
            });
            pagingLayout.getNextPageButton().addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    if (pagingLayout.currentPageNumber < pageCount) {
                        ++pagingLayout.currentPageNumber;
                        initPetsPage(pagingLayout.currentPageNumber);
                        pagingLayout.getPageNumberField().setValue(String.valueOf(pagingLayout.currentPageNumber));
                        if (pagingLayout.currentPageNumber == pageCount)
                            pagingLayout.setBorderButtonsState(false);
                        else
                            pagingLayout.setAllButtonsStateEnabled();
                    }
                }
            });

            pagingLayout.getPageNumberField().addShortcutListener(new ShortcutListener("Enter", ShortcutAction.KeyCode.ENTER, null) {
                @Override
                public void handleAction(Object o, Object o1) {
                    BinderValidationStatus<VaadinValidationBinder> status = pagingLayout.pageNumberFieldBinder.validate();
                    if (!status.hasErrors()) {
                        pagingLayout.currentPageNumber = Integer.valueOf(pagingLayout.getPageNumberField().getValue());
                        initPetsPage(pagingLayout.currentPageNumber);
                    }
                }
            });
            petRecordsLayout.addComponent(pagingLayout);
        }
    }
}
