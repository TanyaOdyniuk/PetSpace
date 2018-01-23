package com.netcracker.ui.bulletinboard;

import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.pet.Pet;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.StubVaadinUI;
import com.netcracker.ui.pet.PetPageUI;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import javax.rmi.CORBA.Util;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

class AdvertisementView extends VerticalLayout {
    private final Advertisement adv;
    AdvertisementView(Advertisement ad) {
        this.adv = ad;

        Panel mainPanel = new Panel();
        VerticalLayout mainLayout = new VerticalLayout();

        HorizontalLayout headerLayout = new HorizontalLayout();
        Label themeLabel = new Label("Theme: " + adv.getAdTopic());
        Label dateLabel = new Label("Date: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(adv.getAdDate()));
        headerLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        if (adv.isAdIsVip()) {
            Panel vipPanel = new Panel();
            vipPanel.setHeight(40, Unit.PIXELS);
            vipPanel.setWidth(100, Unit.PIXELS);
            Button vipButton = new Button("VIP");
            vipButton.setIcon(VaadinIcons.MONEY);
            vipButton.setSizeFull();
            vipPanel.setContent(vipButton);
            headerLayout.addComponents(vipPanel);
        }
        headerLayout.addComponentsAndExpand(themeLabel, dateLabel);

        HorizontalLayout adPetLayout = new HorizontalLayout();
        if(adv.getAdPets() != null){

            Iterator<Pet> adPets = adv.getAdPets().iterator();
            if (!adv.getAdPets().isEmpty()){
                Panel petPanel = new Panel("Pets in this advertisement");
                petPanel.setHeight(100, Unit.PIXELS);
                petPanel.setWidth(300, Unit.PIXELS);
                VerticalLayout petLayout = new VerticalLayout();
                petLayout.setMargin(false);
                while (adPets.hasNext()){
                    Pet tempPet = CustomRestTemplate.getInstance().customGetForObject("/pet/" + adPets.next().getObjectId(), Pet.class);
                    Button petButton = PageElements.createBlueClickedLabel(tempPet.getPetName(), null);
                    petButton.addClickListener(new AbstractClickListener() {
                        @Override
                        public void buttonClickListener() {
                            ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new PetPageUI(tempPet.getObjectId()));
                        }
                    });
                    petLayout.addComponent(petButton);
                }
                petPanel.setContent(petLayout);
                adPetLayout.addComponents(petPanel);
            }
        }
        TextField mainInfo = new TextField();
        mainInfo.setReadOnly(true);
        mainInfo.setValue(adv.getAdBasicInfo());
        mainInfo.setHeight(200, Unit.PIXELS);
        mainInfo.setWidth("100%");

        HorizontalLayout mapLayout = new HorizontalLayout();
        Label mapLabel = new Label("Location info: ");
        if (adv.getAdLocation() != null) {
            Link mapLink = new Link("Click to see last known location", new ExternalResource(adv.getAdLocation()));
            mapLayout.addComponents(mapLabel, mapLink);
        }

        mainLayout.addComponents(headerLayout, adPetLayout, mainInfo, mapLayout);
        mainPanel.setContent(mainLayout);
        SecurityContext o = (SecurityContext) VaadinSession.getCurrent().getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        String login = o.getAuthentication().getPrincipal().toString();
        BigInteger profileId  = CustomRestTemplate.getInstance().customPostForObject("/user/profileId", login, BigInteger.class);

        if(profileId.equals(adv.getAdAuthor().getObjectId())){
            HorizontalLayout authorButtonLayuout = new HorizontalLayout();
            authorButtonLayuout.addComponent(getDeleteAdvButton());
            authorButtonLayuout.addComponent(getEditAdvButton());
            addComponent(authorButtonLayuout);
        }
        addComponent(mainPanel);
    }
    private Button getDeleteAdvButton(){
        Button tempButton = new Button("Delete advertisement", VaadinIcons.TRASH);
        tempButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                DeleteAdForm sub = new DeleteAdForm(adv.getAdAuthor().getObjectId(), adv.getObjectId());
                UI.getCurrent().addWindow(sub);
            }
        });
        return tempButton;
    }

    private Button getEditAdvButton(){
        Button tempButton = new Button("Edit advertisement", VaadinIcons.EDIT);
        tempButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                NewAdvertisementForm sub = new NewAdvertisementForm(adv.getAdAuthor().getObjectId(), adv);
                UI.getCurrent().addWindow(sub);
            }
        });
        return tempButton;
    }
}
