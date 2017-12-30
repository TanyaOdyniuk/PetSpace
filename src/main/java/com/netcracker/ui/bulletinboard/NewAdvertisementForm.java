package com.netcracker.ui.bulletinboard;

import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.category.Category;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.user.Profile;
import com.netcracker.ui.AbstractClickListener;

import com.netcracker.ui.StubVaadinUI;
import com.netcracker.ui.util.CustomRestTemplate;
import com.netcracker.ui.util.VaadinValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import org.springframework.http.HttpEntity;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.netcracker.ui.validation.UiValidationConstants.CHECK_FULLNESS;

public class NewAdvertisementForm extends Window {
    private HorizontalLayout authorTopicDateLayout;
    private HorizontalLayout categoryPetStatusLayout;
    private HorizontalLayout mainInfoLayout;
    private TextField topicField;
    private DateTimeField dateTimeField;
    private Profile profile;
    private ComboBox<Category> selectedCategory;
    private TwinColSelect<Pet> selectedPets;
    private CheckBox selectedStatus;
    private TextArea mainInfo;
    private Binder<VaadinValidationBinder> mainInfoBinder;
    private Binder<VaadinValidationBinder> topicBinder;
    public NewAdvertisementForm() {
        super();
        Window subWindow = new Window("Sub-window");
        VerticalLayout subContent = new VerticalLayout();
        setContent(subContent);

        subContent.addComponent(new Label("New advertisement"));
        getAuthorTopicDateLayout();
        subContent.addComponent(authorTopicDateLayout);
        getCategoryPetStatusLayout();
        subContent.addComponent(categoryPetStatusLayout);
        getMainInfoLayout();
        subContent.addComponent(mainInfoLayout);
        Button submit = new Button("Add advertisement", VaadinIcons.CHECK_CIRCLE);
        submit.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                if(topicBinder.validate().isOk() && mainInfoBinder.validate().isOk()){
                    addNewAdvertisement();
                }
            }
        });
        subContent.addComponent(submit);
        center();
    }
    private void addNewAdvertisement(){//установить статус, карту, характерные признаки
        Advertisement newAd = new Advertisement(topicField.getValue(), "Ad for " + profile.getObjectId());
        newAd.setAdCategory(selectedCategory.getSelectedItem().get());
        newAd.setAdAuthor(profile);
        newAd.setAdBasicInfo(mainInfo.getValue());
        newAd.setAdIsVip(selectedStatus.getValue());
        newAd.setAdPets(selectedPets.getSelectedItems());
        Timestamp ts = Timestamp.valueOf(dateTimeField.getValue());
        newAd.setAdDate(ts);
        newAd.setAdTopic(topicField.getValue());
        HttpEntity<Advertisement> request = new HttpEntity<>(newAd);
        Advertisement ad = CustomRestTemplate.getInstance().customPostForObject("/bulletinboard/add", request, Advertisement.class);
        if(ad != null){
            Notification.show("Advertisement was added!");
        }
        this.close();
        StubVaadinUI currentUI = (StubVaadinUI) UI.getCurrent();
        currentUI.changePrimaryAreaLayout(new AdvertisementView(newAd));
    }
    private void getAuthorTopicDateLayout() {
        authorTopicDateLayout = new HorizontalLayout();
        topicField = new TextField("Topic");
        topicField.setRequiredIndicatorVisible(true);
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
        dateTimeField.setValue(localDateTime);
        dateTimeField.setReadOnly(true);
        TextField authorField = new TextField("Author");
        authorField.setIcon(VaadinIcons.USER);
        profile = CustomRestTemplate.getInstance().customGetForObject("/profile/" + 8, Profile.class);
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
        selectedCategory.setSelectedItem(categories.get(0));
        authorTopicDateLayout.addComponent(topicField);
        authorTopicDateLayout.addComponent(selectedCategory);
        authorTopicDateLayout.addComponent(dateTimeField);
        authorTopicDateLayout.addComponent(authorField);
    }

    private void getCategoryPetStatusLayout() {
        selectedPets = new TwinColSelect<>("Pets");
        selectedPets.setIcon(VaadinIcons.PIGGY_BANK);
        List<Pet> pets = Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/pets/" + 23, Pet[].class));
        selectedPets.setItems(pets);
        selectedPets.setItemCaptionGenerator(Pet::getPetName);
        selectedPets.setRows(3);
        VerticalLayout verticalLayout = new VerticalLayout();
        categoryPetStatusLayout = new HorizontalLayout();
        selectedStatus = new CheckBox("Make VIP ad?");
        selectedStatus.setValue(true);
        verticalLayout.addComponent(selectedStatus);
        categoryPetStatusLayout.addComponent(verticalLayout);
        categoryPetStatusLayout.addComponent(selectedPets);
    }
    private void getMainInfoLayout(){
        mainInfoBinder = new Binder<>();
        mainInfoLayout = new HorizontalLayout();
        mainInfo = new TextArea("Main info");
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