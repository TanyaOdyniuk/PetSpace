package com.netcracker.ui.bulletinboard;

import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.comment.AdvertisementComment;
import com.netcracker.model.pet.Pet;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.CommentsPanel;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.MainUI;
import com.netcracker.ui.pet.PetPageUI;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Iterator;

public class AdvertisementView extends VerticalLayout {
    private BigInteger profileId;
    private final Advertisement adv;
    private int browserHeight;
    private int browserWidth;
    public AdvertisementView(Advertisement ad) {
        this.adv = ad;
        profileId = getProfileId();
        HorizontalLayout mainLayout = new HorizontalLayout();
        browserHeight = UI.getCurrent().getPage().getBrowserWindowHeight();
        browserWidth = UI.getCurrent().getPage().getBrowserWindowWidth();
        mainLayout.setHeight(browserHeight - 250, Unit.PIXELS);
        mainLayout.setWidth(browserWidth - 450, Unit.PIXELS);

        Panel leftPartPanel = new Panel();
        leftPartPanel.setHeight("100%");
        leftPartPanel.setWidth(250, Unit.PIXELS);

        Panel rightPartPanel = new Panel();
        rightPartPanel.setHeight("100%");

        VerticalLayout leftPartLayout = getAuthorButtonsLayout();

        VerticalLayout rightPartLayout = new VerticalLayout();
        rightPartLayout.setSpacing(true);
        rightPartLayout.addComponent(getTopicCategoryLayout());
        rightPartLayout.addComponent(getDateAuthorPetsLayout());
        rightPartLayout.addComponent(getMainInfoLayout());
        rightPartLayout.addComponent(getCommentPanel(rightPartLayout));
        if(leftPartLayout != null){
            leftPartPanel.setContent(leftPartLayout);
            mainLayout.addComponent(leftPartPanel);
        }
        rightPartPanel.setContent(rightPartLayout);
        mainLayout.addComponentsAndExpand(rightPartPanel);

        addComponent(mainLayout);
    }
    private BigInteger getProfileId(){
        SecurityContext o = (SecurityContext) VaadinSession.getCurrent().getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        String login = o.getAuthentication().getPrincipal().toString();
        return CustomRestTemplate.getInstance().customPostForObject("/user/profileId", login, BigInteger.class);
    }
    private HorizontalLayout getTopicCategoryLayout(){
        HorizontalLayout topicCategoryLayout = new HorizontalLayout();
        Label topicLabel = new Label("Topic: " + adv.getAdTopic());
        Label categoryLabel = new Label("Category: " + adv.getAdCategory().getCategoryName());
        topicCategoryLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        topicCategoryLayout.addComponentsAndExpand(topicLabel, categoryLabel);
        return topicCategoryLayout;
    }
    private HorizontalLayout getDateAuthorPetsLayout(){
        HorizontalLayout dateAuthorPetLayout = new HorizontalLayout();
        Label dateLabel = new Label("Date: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(adv.getAdDate()));
        String authorStr;
        if(adv.getAdAuthor().getObjectId().equals(profileId)){
            authorStr = "you";
        } else {
            authorStr = adv.getAdAuthor().getProfileName() + " " + adv.getAdAuthor().getProfileSurname();
        }
        Label authorLabel = new Label("Author: " + authorStr);
        dateAuthorPetLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        Panel petPanel = getAdPetPanel();
        if(petPanel != null){
            dateAuthorPetLayout.addComponentsAndExpand(petPanel);
        }
        dateAuthorPetLayout.addComponentsAndExpand(dateLabel, authorLabel);
        return dateAuthorPetLayout;
    }
    private Panel getAdPetPanel(){
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
                            ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new PetPageUI(tempPet.getObjectId()));
                        }
                    });
                    petLayout.addComponent(petButton);
                }
                petPanel.setContent(petLayout);
                return  petPanel;
            }
        }
        return null;
    }
    private HorizontalLayout getMainInfoLayout(){
        HorizontalLayout mainInfoLayout = new HorizontalLayout();
        mainInfoLayout.setSizeFull();
        mainInfoLayout.setCaption("Main information");
        TextField mainInfo = new TextField();
        mainInfo.setReadOnly(true);
        mainInfo.setValue(adv.getAdBasicInfo());
        mainInfo.setSizeFull();
        mainInfo.setHeight(200, Unit.PIXELS);
        mainInfoLayout.addComponent(mainInfo);
        return mainInfoLayout;
    }

    private Button getDeleteAdvButton(){
        Button tempButton = new Button("Delete advertisement", VaadinIcons.TRASH);
        tempButton.setSizeFull();
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
        tempButton.setSizeFull();
        tempButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                NewAdvertisementForm sub = new NewAdvertisementForm(adv.getAdAuthor().getObjectId(), adv);
                UI.getCurrent().addWindow(sub);
            }
        });
        return tempButton;
    }
    private VerticalLayout getAuthorButtonsLayout(){
        if(profileId.equals(adv.getAdAuthor().getObjectId())){
            VerticalLayout authorButtonLayout = new VerticalLayout();
            authorButtonLayout.addComponent(getDeleteAdvButton());
            authorButtonLayout.addComponent(getEditAdvButton());
            return authorButtonLayout;
        }
        return null;
    }
    private Panel getCommentPanel(VerticalLayout rightPartLayout){
        return new CommentsPanel<>(
                adv, AdvertisementComment.class, rightPartLayout, adv);
    }
}
