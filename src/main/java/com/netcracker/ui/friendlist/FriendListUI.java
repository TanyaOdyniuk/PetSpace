package com.netcracker.ui.friendlist;

import com.netcracker.model.user.Profile;
import com.netcracker.ui.users.UsersUI;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class FriendListUI extends VerticalLayout {

    private int browserHeight;
    private Panel mainPanel;
    private VerticalLayout mainLayout;
    private BigInteger profileId;

    public FriendListUI(BigInteger profileId) {
        this.profileId = profileId;
        browserHeight = UI.getCurrent().getPage().getBrowserWindowHeight();
        mainPanel = new Panel();
        mainPanel.setWidth("100%");
        mainPanel.setHeight(browserHeight * 0.695f, Unit.PIXELS);

        mainLayout = new VerticalLayout();
        List<Profile> friendList = getProfileFriends(profileId);
        mainLayout.addComponent(genSearchContent(), 0);
        mainLayout.addComponent(genFriendsList(friendList), 1);

        mainPanel.setContent(mainLayout);
        addComponent(mainPanel);
    }

    private VerticalLayout genSearchContent() {
        VerticalLayout layoutContent = new VerticalLayout();
        TextField searchField = new TextField();
        searchField.setIcon(VaadinIcons.SEARCH);
        searchField.setPlaceholder("Enter name or login to search for someone in your friend list");
        searchField.setWidth("600");
        Button searchButton = new Button("Search", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                String searchRequest = searchField.getValue();
                if (searchRequest.trim().length() == 0) {
                    Notification.show("Please fill the search field");
                } else {
                    searchRequest = searchRequest.trim().replaceAll(" +", " ");
                    List<Profile> foundPeople;
                    if ((searchRequest.contains("@"))) {
                        foundPeople = searchForFriendsByEmail(searchRequest, profileId);
                    } else if (searchRequest.contains(" ")) {
                        String[] buf = searchRequest.split(" ");
                        String name = buf[0];
                        String surname = buf[1];
                        foundPeople = searchForFriendsByFullName(name, surname, profileId);
                    } else {
                        foundPeople = searchForFriendsByNameOrSurname(searchRequest, profileId);
                    }
                    if (foundPeople.isEmpty()) {
                        mainLayout.replaceComponent(mainLayout.getComponent(1), new Label("No person with such name or email in your friend list, please try again."));
                    } else {
                        mainLayout.replaceComponent(mainLayout.getComponent(1), genFriendsList(foundPeople));
                    }
                }
            }
        });
        layoutContent.addComponent(searchField);
        layoutContent.addComponent(searchButton);
        return layoutContent;
    }


    private List<Profile> getProfileFriends(BigInteger profileId) {
        return Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/friends/" + profileId, Profile[].class));
    }

    private List<Profile> searchForFriendsByFullName(String name, String surname, BigInteger profileId) {
        return Arrays.asList(CustomRestTemplate.getInstance().customGetForObject("/friends/search/byFullName/" + name + "/" + surname + "/" + profileId, Profile[].class));
    }

    private List<Profile> searchForFriendsByNameOrSurname(String name, BigInteger profileId) {
        return Arrays.asList(CustomRestTemplate.getInstance().customGetForObject("/friends/search/byName/" + name + "/" + profileId, Profile[].class));
    }

    private List<Profile> searchForFriendsByEmail(String email, BigInteger profileId) {
        return Arrays.asList(CustomRestTemplate.getInstance().customGetForObject("/friends/search/byEmail/" + email + "/" + profileId, Profile[].class));
    }

    private VerticalLayout genFriendsList(List<Profile> friends) {
        return UsersUI.genPeopleList(friends);
    }
}
