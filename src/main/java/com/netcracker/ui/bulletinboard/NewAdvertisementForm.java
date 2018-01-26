package com.netcracker.ui.bulletinboard;

import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.category.Category;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.user.Profile;
import com.netcracker.ui.AbstractClickListener;

import com.netcracker.ui.MainUI;
import com.netcracker.ui.util.CustomRestTemplate;
import com.netcracker.ui.util.VaadinValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import org.springframework.http.HttpEntity;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static com.netcracker.ui.validation.UiValidationConstants.CHECK_FULLNESS;

class NewAdvertisementForm extends Window {
    private Advertisement curAd;
    private BigInteger profileId;
    private HorizontalLayout authorTopicDateLayout;
    private HorizontalLayout categoryPetStatusLayout;
    private HorizontalLayout mainInfoLayout;
    private TextField topicField;
    private DateTimeField dateTimeField;
    private Profile profile;
    private ComboBox<Category> selectedCategory;
    private List<Pet> pets;
    private CheckBoxGroup<String> petCheckBoxGroup;
    private CheckBox selectedStatus;
    private TextArea mainInfo;
    private Binder<VaadinValidationBinder> mainInfoBinder;
    private Binder<VaadinValidationBinder> topicBinder;

    NewAdvertisementForm(BigInteger profileId, Advertisement ad) {
        super();
        setModal(true);
        setResizable(false);
        this.profileId = profileId;
        curAd = ad;
        VerticalLayout subContent = new VerticalLayout();
        setContent(subContent);

        subContent.addComponent(new Label("New advertisement"));
        getAuthorTopicDateLayout();
        subContent.addComponent(authorTopicDateLayout);
        getCategoryPetStatusLayout();
        subContent.addComponent(categoryPetStatusLayout);
        getMainInfoLayout();
        subContent.addComponent(mainInfoLayout);
        Button submit = new Button("Save", VaadinIcons.CHECK_CIRCLE);
        submit.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                if (topicBinder.validate().isOk() && mainInfoBinder.validate().isOk()) {
                    addNewAdvertisement();
                }
            }
        });
        subContent.addComponent(submit);
        center();
    }

    private void addNewAdvertisement() {// карту, характерные признаки
        Advertisement newAd = new Advertisement(topicField.getValue(), "Ad for " + profile.getObjectId());
        newAd.setAdCategory(selectedCategory.getSelectedItem().get());
        newAd.setAdAuthor(profile);
        newAd.setAdBasicInfo(mainInfo.getValue());
        newAd.setAdIsVip(selectedStatus.getValue());
        List<Pet> petSubList = null;
        if(petCheckBoxGroup != null){
            petSubList = getSubList(petCheckBoxGroup.getSelectedItems());
        }
        newAd.setAdPets(petSubList != null ? new HashSet<>(petSubList) : null);
        Timestamp ts = Timestamp.valueOf(dateTimeField.getValue());
        newAd.setAdDate(ts);
        newAd.setAdTopic(topicField.getValue());
        if (curAd != null) {
            newAd.setObjectId(curAd.getObjectId());
        }
        HttpEntity<Advertisement> request = new HttpEntity<>(newAd);
        BigInteger newId = CustomRestTemplate.getInstance().customPostForObject("/bulletinboard/add", request, BigInteger.class);
        if (newId != null) {
            newAd.setObjectId(newId);
            Notification.show("Advertisement was saved!");
        }
        MainUI currentUI = (MainUI) UI.getCurrent();
        currentUI.changePrimaryAreaLayout(new AdvertisementView(newAd));
        this.close();
    }

    private void getAuthorTopicDateLayout() {
        authorTopicDateLayout = new HorizontalLayout();
        topicField = new TextField("Topic");
        topicField.setRequiredIndicatorVisible(true);
        topicField.setValue(curAd != null ? curAd.getAdTopic() : "");
        topicBinder = new Binder<>();
        topicBinder.forField(topicField)
                .asRequired(CHECK_FULLNESS)
                .bind(VaadinValidationBinder::getString, VaadinValidationBinder::setString);
        String dateFormatPattern = "yyyy-MM-dd HH:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
        Date date = new Date();
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        dateTimeField = new DateTimeField("Date");
        dateTimeField.setDateFormat(dateFormatPattern);
        dateTimeField.setValue(curAd != null ? curAd.getAdDate().toLocalDateTime() : localDateTime);
        dateTimeField.setReadOnly(true);
        TextField authorField = new TextField("Author");
        authorField.setIcon(VaadinIcons.USER);
        profile = CustomRestTemplate.getInstance().customGetForObject("/profile/" + profileId, Profile.class);
        String authorInfo = profile.getProfileName() + " " + profile.getProfileSurname();
        authorField.setValue(authorInfo);
        authorField.setReadOnly(true);
        selectedCategory = new ComboBox<>("Category");
        selectedCategory.setEmptySelectionAllowed(false);
        selectedCategory.setIcon(VaadinIcons.FILTER);
        selectedCategory.setRequiredIndicatorVisible(true);
        selectedCategory.setPlaceholder("Type category");
        List<Category> categories = Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/category", Category[].class));
        selectedCategory.setItems(categories);
        selectedCategory.setItemCaptionGenerator(Category::getCategoryName);
        selectedCategory.setSelectedItem(curAd != null ? curAd.getAdCategory() : categories.get(0));
        authorTopicDateLayout.addComponent(topicField);
        authorTopicDateLayout.addComponent(selectedCategory);
        authorTopicDateLayout.addComponent(dateTimeField);
        authorTopicDateLayout.addComponent(authorField);
    }

    private Panel getPetsPanel() {
        pets = Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/pets/" + profileId, Pet[].class));
        Iterator<Pet> profilePets = pets.iterator();
        if (!pets.isEmpty()) {
            Panel petPanel = new Panel("Pets for this advertisement");
            petPanel.setHeight(100, Unit.PIXELS);
            petPanel.setWidth(300, Unit.PIXELS);
            VerticalLayout petLayout = new VerticalLayout();
            petLayout.setMargin(false);
            petCheckBoxGroup = new CheckBoxGroup<>();
            List<String> petNames = new ArrayList<>();
            while (profilePets.hasNext()) {
                Pet curPet = profilePets.next();
                petNames.add(curPet.getPetName());
            }
            petCheckBoxGroup.setItems(petNames);
            petCheckBoxGroup.setData(pets);
            if (curAd != null) {
                for (Pet p : curAd.getAdPets()) {
                    String name = CustomRestTemplate.getInstance().customGetForObject("/pet/" + p.getObjectId(), Pet.class).getPetName();
                    petCheckBoxGroup.select(name);
                }
            }
            petLayout.addComponent(petCheckBoxGroup);
            petPanel.setContent(petLayout);
            return petPanel;
        }
        return null;
    }

    private List<Pet> getSubList(Set<String> selectedPetsName) {
        List<Pet> selectedPets = new ArrayList<>();
        for (String str : selectedPetsName) {
            for (Pet p : pets) {
                if (str.equals(p.getPetName())) {
                    selectedPets.add(p);
                    break;
                }
            }
        }
        return selectedPets;
    }

    private void getCategoryPetStatusLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        categoryPetStatusLayout = new HorizontalLayout();
        selectedStatus = new CheckBox("Make VIP ad?");
        selectedStatus.setValue(true);
        //verticalLayout.addComponent(selectedStatus);
        //categoryPetStatusLayout.addComponent(verticalLayout);
        Panel pets = getPetsPanel();
        if (pets != null) {
            pets.setIcon(VaadinIcons.PIGGY_BANK);
            categoryPetStatusLayout.addComponent(pets);
        }
    }

    private void getMainInfoLayout() {
        mainInfoBinder = new Binder<>();
        mainInfoLayout = new HorizontalLayout();
        mainInfo = new TextArea("Main info");
        mainInfo.setValue(curAd != null ? curAd.getAdBasicInfo() : "");
        mainInfo.setRows(5);
        mainInfo.setRequiredIndicatorVisible(true);
        mainInfo.setWidth("100%");
        mainInfoBinder.forField(mainInfo)
                .asRequired(CHECK_FULLNESS)
                .bind(VaadinValidationBinder::getString, VaadinValidationBinder::setString);
        mainInfoLayout.setWidth("100%");
        mainInfoLayout.addComponent(mainInfo);
    }
}