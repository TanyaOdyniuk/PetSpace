package com.netcracker.ui.friendlist;

import com.netcracker.model.user.Profile;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        List<Profile> friendList = getProfileFriends(this.profileId);
        mainLayout.addComponent(genSearchContent(), 0);
        mainLayout.addComponent(genFriendsList(friendList), 1);

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
                List<Profile> foundPeople;
                if ((searchField.getValue().contains("@"))) {
                    foundPeople = searchPeopleByEmail(searchField.getValue());
                } else if (searchField.getValue().contains(" ")) {
                    String[] buf = searchField.getValue().split(" ");
                    String name = buf[0];
                    String surname = buf[1];
                    foundPeople = searchPeopleByFullName(name, surname);
                } else {
                    foundPeople = searchPeopleByNameOrSurname(searchField.getValue());
                }
//                Set<Profile> profiles = new HashSet<>();
//                List<Profile> friendList = getProfileFriends(profileId);
//                for (int i = 0; i < foundPeople.size(); i++) {
//                    for (Profile p : friendList) {
//                        if (foundPeople.get(i).getObjectId().equals(p.getObjectId())) {
//                            profiles.add(p);
//                        }
//                    }
//                }
//                profiles.addAll(foundPeople);
//                foundPeople.removeAll(foundPeople);
//                foundPeople.addAll(profiles);
                mainLayout.replaceComponent(mainLayout.getComponent(1), genFriendsList(foundPeople));
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

    private List<Profile> searchPeopleByFullName(String name, String surname) {
        return Arrays.asList(CustomRestTemplate.getInstance().customGetForObject("/friends/search/people/byFullName/" + name + "/" + surname, Profile[].class));
    }

    private List<Profile> searchPeopleByNameOrSurname(String name) {
        return Arrays.asList(CustomRestTemplate.getInstance().customGetForObject("/friends/search/people/byName/" + name, Profile[].class));
    }

    private List<Profile> searchPeopleByEmail(String email) {
        return Arrays.asList(CustomRestTemplate.getInstance().customGetForObject("/friends/search/people/byEmail/" + email, Profile[].class));
    }

    private VerticalLayout genFriendsList(List<Profile> friends) {
        VerticalLayout layoutContent = new VerticalLayout();
        for (Profile friend : friends) {
            HorizontalLayout friendRecord = new HorizontalLayout();
            VerticalLayout friendInfoLayout = new VerticalLayout();

            Image friendAvatar = new Image();
            friendAvatar.setHeight(250, Sizeable.Unit.PIXELS);
            friendAvatar.setWidth(250, Sizeable.Unit.PIXELS);
            try {
                friendAvatar.setSource(new ExternalResource(friend.getProfileAvatar()));
            } catch (Exception e) {
                friendAvatar.setSource(new ExternalResource("http://s3.amazonaws.com/37assets/svn/765-default-avatar.png"));
            }
            friendAvatar.setDescription("Friend's avatar");

            Link friendName = new Link(friend.getProfileName(), new ExternalResource("https://vaadin.com/"));
            friendName.setDescription("Здесь должна быть ссылка на друга");
            Label friendInfo = new Label(friend.getProfileSurname());

            //FRIEND INFO
            friendInfoLayout.addComponents(friendName, friendInfo);

            //INFO + AVATAR
            friendRecord.addComponents(friendAvatar, friendInfoLayout);

            layoutContent.addComponents(friendRecord);
        }
        return layoutContent;
    }
}
