package com.netcracker.ui.friendlist;

import com.netcracker.model.user.Profile;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class FriendListUI extends VerticalLayout {

    public FriendListUI(BigInteger profileId) {
        super();
        setWidth("100%");
        setHeight("100%");
        List<Profile> friendList = getProfileFriends(profileId);
        for (Profile friend : friendList) {
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

            addComponents(friendRecord);

        }
        addComponent(genSearchContent());
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
                String[] buf = searchField.getValue().split(" ");
                String name = buf[0];
                String surname = buf[1];

                List<Profile> foundFriends = searchFriendsByName(name, surname);
                if (foundFriends.isEmpty()) {
                    foundFriends = searchFriendsByEmail(searchField.getValue());
                }
                for (Profile p : foundFriends) {
                    System.out.println(p.getProfileName());
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

    private List<Profile> searchFriendsByName(String name, String surname) {
        return Arrays.asList(CustomRestTemplate.getInstance().customGetForObject("/friends/search/" + name + "/" + surname, Profile[].class));
    }

    private List<Profile> searchFriendsByEmail(String email) {
        return Arrays.asList(CustomRestTemplate.getInstance().customGetForObject("/friends/search/" + email, Profile[].class));
    }
}
