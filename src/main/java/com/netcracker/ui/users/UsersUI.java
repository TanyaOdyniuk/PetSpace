package com.netcracker.ui.users;

import com.netcracker.model.user.Profile;
import com.netcracker.ui.MainUI;
import com.netcracker.ui.profile.ProfileView;
import com.netcracker.ui.util.CustomRestTemplate;
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
        mainLayout.addComponent(genSearchContent(), 0);
        mainLayout.addComponent(new Label("People with your search request will be here"), 1);

        mainPanel.setContent(mainLayout);
        addComponent(mainPanel);
    }

    private VerticalLayout genSearchContent() {
        VerticalLayout layoutContent = new VerticalLayout();
        TextField searchField = new TextField();
        searchField.setIcon(VaadinIcons.SEARCH);
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
            VerticalLayout friendInfoLayout = new VerticalLayout();

            Image friendAvatar = new Image();
            friendAvatar.setHeight(250, Sizeable.Unit.PIXELS);
            friendAvatar.setWidth(250, Sizeable.Unit.PIXELS);
            try {
                friendAvatar.setSource(new ExternalResource(p.getProfileAvatar()));
            } catch (Exception e) {
                friendAvatar.setSource(new ExternalResource("http://s3.amazonaws.com/37assets/svn/765-default-avatar.png"));
            }
            friendAvatar.setDescription("Friend's avatar");

            Button friendName = new Button(p.getProfileName());
            friendName.setStyleName(ValoTheme.BUTTON_LINK);
            friendName.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(p.getObjectId()));
                }
            });
            Label friendInfo = new Label(p.getProfileSurname());

            //FRIEND INFO
            friendInfoLayout.addComponents(friendName, friendInfo);

            //INFO + AVATAR
            friendRecord.addComponents(friendAvatar, friendInfoLayout);

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
                mainLayout.replaceComponent(mainLayout.getComponent(1), new Label("No person with such name or email, please try again."));
            } else {
                mainLayout.replaceComponent(mainLayout.getComponent(1), genPeopleList(foundPeople));
            }
        }
    }
}
