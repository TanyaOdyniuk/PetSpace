package com.netcracker.ui.profile;

import com.netcracker.asserts.ProfileDataAssert;
import com.netcracker.model.user.Profile;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.MainUI;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.UIConstants;
import com.netcracker.ui.util.CustomRestTemplate;
import com.netcracker.ui.util.upload.ImageUpload;
import com.netcracker.ui.util.upload.UploadableComponent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.io.File;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class ProfileEditUI extends Window implements UploadableComponent {
    private Profile currentProfile;
    private Image avatar;
    private String avatarPath;
    private Boolean isFileResource;
    private TextField avatarField;
    private TextField nameField;
    private TextField surnameField;
    private TextField ageField;
    private TextField hobbiesField;
    private TextField breedsField;

    ProfileEditUI(Profile profile) {
        super();
        if (!profile.getObjectId().equals(getCurrentProfile().getObjectId())) {
            this.close();
            Notification.show("Not your profile!");
        }
        this.currentProfile = profile;
        this.isFileResource = false;
        setCaption("Profile information");
        setModal(true);
        setResizable(false);
        VerticalLayout mainLayout = new VerticalLayout();

        GridLayout avatarLayout = new GridLayout(2, 1);
        VerticalLayout avatarContext = new VerticalLayout();
        String profileAvatarSource = currentProfile.getProfileAvatar();
        avatar = new Image();
        avatarField = PageElements.createTextField("Avatar", "Avatar's URL");
        avatarField.setValue(currentProfile.getProfileAvatar());
        PageElements.setProfileImageSource(avatar, profileAvatarSource);
        avatar.setHeight("200px");
        avatar.setWidth("200px");

        avatarField.setWidth("100%");

        Button avatarSelect = new Button("Set URL");
        avatarSelect.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                avatarSelect.setComponentError(null);
                updateImage(avatarField.getValue(), avatar);
            }
        });

        Upload uploadAvatar = new ImageUpload(UIConstants.PATH_TO_AVATAR_PROFILE,
                currentProfile.getObjectId() == null ? currentProfile.getObjectId() : currentProfile.getObjectId(), this);

        avatarSelect.setWidth("100%");
        uploadAvatar.setWidth("100%");

        avatarContext.addComponents(avatarField, avatarSelect, uploadAvatar);
        avatarContext.setComponentAlignment(avatarSelect, Alignment.MIDDLE_CENTER);
        avatarContext.setComponentAlignment(uploadAvatar, Alignment.MIDDLE_CENTER);

        avatarLayout.addComponent(avatar, 0, 0);
        avatarLayout.addComponent(avatarContext, 1, 0);

        mainLayout.addComponent(avatarLayout);

        VerticalLayout infoLayout = new VerticalLayout();
        infoLayout.setWidth("100%");
        infoLayout.setSpacing(true);

        //Fields
        nameField = PageElements.createTextField(currentProfile.getProfileName(), "Your desired name", true);
        nameField.setWidth("100%");
        nameField.setValue(currentProfile.getProfileName());

        surnameField = PageElements.createTextField(currentProfile.getProfileSurname(), "Your desired surname", true);
        surnameField.setWidth("100%");
        surnameField.setValue(currentProfile.getProfileSurname());

        ageField = PageElements.createTextField("Age", "Age (full years)", false);
        ageField.setWidth("100%");
        ageField.setValue(currentProfile.getProfileAge() == null ? "" : currentProfile.getProfileAge().toString());

        List<String> profileHobbies = currentProfile.getProfileHobbies();
        hobbiesField = PageElements.createTextField("Hobbies", "Hobbie #1, hobbie #2, ...", false);
        hobbiesField.setWidth("100%");
        hobbiesField.setValue(getParsedString(profileHobbies));

        List<String> profileFavBreeds = currentProfile.getProfileFavouriteBreeds();
        breedsField = PageElements.createTextField("Favourite breeds", "Breed #1, breed #2, ...", false);
        breedsField.setWidth("100%");
        breedsField.setValue(getParsedString(profileFavBreeds));

        Button updateProfileButton = getUpdateProfileButton();

        infoLayout.addComponents(nameField, surnameField, ageField, hobbiesField, breedsField);
        mainLayout.addComponents(infoLayout, updateProfileButton);
        setContent(mainLayout);
        center();
    }

    private String getParsedString(List<String> paramsList) {
        StringBuilder sb = new StringBuilder();
        if (paramsList != null && paramsList.size() > 0) {
            for (String s : paramsList) {
                if (s != null)
                    sb.append(s).append(", ");
            }
        } else {
            sb.append("  ");
        }
        return sb.substring(0, sb.length() - 2);
    }

    private Button getUpdateProfileButton() {
        final Button result = new Button("Update information");
        result.setWidth("100%");
        result.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                result.setComponentError(null);
                if (!isFileResource)
                    currentProfile.setProfileAvatar(ProfileDataAssert.assertAvatarURL(avatarField.getValue()));
                else
                    currentProfile.setProfileAvatar(avatarField.getValue());
                ProfileDataAssert.assertName(nameField.getValue());
                currentProfile.setProfileName(nameField.getValue());
                ProfileDataAssert.assertName(surnameField.getValue());
                currentProfile.setProfileSurname(surnameField.getValue());
                currentProfile.setProfileAge(ProfileDataAssert.assertAge(ageField.getValue()));
                String hobbiesString = hobbiesField.getValue().trim();
                if (!hobbiesString.equals("")) {
                    String[] hobbiesArray = hobbiesString.split(" *, *");
                    List<String> hobbies = Arrays.asList(hobbiesArray);
                    currentProfile.setProfileHobbies(hobbies);
                } else {
                    currentProfile.setProfileHobbies(null);
                }
                String breedsString = breedsField.getValue().trim();
                if (!breedsString.equals("")) {
                    String[] favBreedsArray = breedsString.split(" *, *");
                    List<String> favBreeds = Arrays.asList(favBreedsArray);
                    currentProfile.setProfileFavouriteBreeds(favBreeds);
                } else {
                    currentProfile.setProfileFavouriteBreeds(null);
                }
                updateProfile(currentProfile);
            }
        });
        return result;
    }

    @Override
    public void updateImage(File imageFile) {
        avatar.setSource(new FileResource(imageFile));
        this.isFileResource = true;
        this.avatarPath = imageFile.getPath();
        avatarField.setValue(avatarPath);
    }

    private void updateImage(String imageURL, Image imageToUpdate) {
        imageURL = ProfileDataAssert.assertAvatarURL(imageURL);
        currentProfile.setProfileAvatar(imageURL);
        imageToUpdate.setSource(new ExternalResource(imageURL));
        this.isFileResource = false;
        this.avatarPath = imageURL;
    }

    private void updateProfile(Profile currentProfile) {
        HttpEntity<Profile> profileEntity = new HttpEntity<>(currentProfile);
        CustomRestTemplate.getInstance()
                .customPostForObject("/profile/update", profileEntity, Profile.class);
        Notification.show("Profile information was updated!");
        this.close();
        ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(currentProfile.getObjectId()));
    }

    private Profile getCurrentProfile() {
        SecurityContext o = (SecurityContext) VaadinSession.getCurrent().getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        String login = o.getAuthentication().getPrincipal().toString();
        BigInteger currentProfileId = CustomRestTemplate.getInstance().customPostForObject("/user/profileId", login, BigInteger.class);
        return CustomRestTemplate.getInstance().
                customGetForObject("/profile/" + currentProfileId, Profile.class);
    }

}