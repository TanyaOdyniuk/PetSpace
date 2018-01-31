package com.netcracker.ui.users;

import com.netcracker.model.user.Profile;
import com.netcracker.ui.MainUI;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.pet.PetPageUI;
import com.netcracker.ui.profile.ProfileView;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.event.MouseEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class UsersUI extends VerticalLayout {
    private int browserHeight;
    private Panel mainPanel;
    private VerticalLayout mainLayout;
    private BigInteger profileId;

    public UsersUI(BigInteger profileId) {
        this.profileId = profileId;
        browserHeight = UI.getCurrent().getPage().getBrowserWindowHeight();
        mainPanel = new Panel();
        mainPanel.setWidth("100%");
        mainPanel.setHeight(browserHeight * 0.695f, Unit.PIXELS);

        mainLayout = new VerticalLayout();
        mainLayout.addComponents(genSearchContent(), PageElements.getSeparator());
        mainLayout.addComponent(new Label("People with your search request will be here"));

        mainPanel.setContent(mainLayout);
        addComponent(mainPanel);
    }

    private VerticalLayout genSearchContent() {
        VerticalLayout layoutContent = new VerticalLayout();
        TextField searchField = new TextField();
        searchField.setPlaceholder("Enter name or login to search for someone");
        searchField.setWidth("400");
        Button searchButton = new Button("Search", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                String searchRequest = searchField.getValue();
                searchRequest = searchRequest.trim().replaceAll(" +", " ");
                searchForPeople(searchRequest);
            }
        });
        searchButton.setIcon(VaadinIcons.SEARCH_MINUS);
        layoutContent.addComponent(searchField);
        layoutContent.addComponent(searchButton);
        return layoutContent;
    }

    private List<Profile> searchPeopleByFullName(String name, String surname) {
        return Arrays.asList(CustomRestTemplate.getInstance().customGetForObject("/users/search/byFullName/" + name + "/" + surname, Profile[].class));
    }

    private List<Profile> searchPeopleByNameOrSurname(String name) {
        return Arrays.asList(CustomRestTemplate.getInstance().customGetForObject("/users/search/byName/" + name, Profile[].class));
    }

    private List<Profile> searchPeopleByEmail(String email) {
        return Arrays.asList(CustomRestTemplate.getInstance().customGetForObject("/users/search/byEmail/" + email, Profile[].class));
    }

    public static VerticalLayout genPeopleList(List<Profile> people) {
        VerticalLayout layoutContent = new VerticalLayout();
        for (Profile p : people) {
            HorizontalLayout friendRecord = new HorizontalLayout();

            Image friendAvatar = new Image();
            friendAvatar.setHeight(250, Sizeable.Unit.PIXELS);
            friendAvatar.setWidth(250, Sizeable.Unit.PIXELS);
            PageElements.setProfileImageSource(friendAvatar, p.getProfileAvatar());
            friendAvatar.setDescription("Friend's avatar");
            friendAvatar.addClickListener((MouseEvents.ClickListener) clickEvent ->
                    ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(p.getObjectId())));

            Button friendName = new Button(p.getProfileName() + "\r\n" + p.getProfileSurname());
            friendName.setStyleName(ValoTheme.BUTTON_LINK);
            friendName.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(p.getObjectId()));
                }
            });

            //INFO + AVATAR
            friendRecord.addComponents(friendAvatar, friendName);
            friendRecord.setComponentAlignment(friendRecord.getComponent(1), Alignment.MIDDLE_CENTER);

            layoutContent.addComponents(friendRecord);
        }
        return layoutContent;
    }

    private void searchForPeople(String searchRequest) {
        if (searchRequest.trim().length() == 0) {
            Notification.show("Please fill the search field");
        } else {
            searchRequest = searchRequest.trim().replaceAll(" +", " ");
            List<Profile> foundPeople;
            if ((searchRequest.contains("@"))) {
                foundPeople = searchPeopleByEmail(searchRequest);
            } else if (searchRequest.contains(" ")) {
                String[] buf = searchRequest.split(" ");
                String name = buf[0];
                String surname = buf[1];
                foundPeople = searchPeopleByFullName(name, surname);
            } else {
                foundPeople = searchPeopleByNameOrSurname(searchRequest);
            }
            if (foundPeople.isEmpty()) {
                mainLayout.replaceComponent(mainLayout.getComponent(2), new Label("No person with such name or email, please try again."));
            } else {
                mainLayout.replaceComponent(mainLayout.getComponent(2), genPeopleList(foundPeople));
            }
        }
    }
}
