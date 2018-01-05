package com.netcracker.ui.friendlist;

import com.netcracker.model.user.Profile;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class FriendListUI extends VerticalLayout{

    public FriendListUI(BigInteger profileId){
        super();
        setWidth("100%");
        setHeight("100%");
        List<Profile> friendList = getProfileFriends(profileId);
        for (Profile friend: friendList) {
            HorizontalLayout friendRecord = new HorizontalLayout();
            VerticalLayout friendInfoLayout = new VerticalLayout();

            Image friendAvatar = new Image();
            friendAvatar.setHeight(250, Sizeable.Unit.PIXELS);
            friendAvatar.setWidth(250, Sizeable.Unit.PIXELS);
            friendAvatar.setSource(new ExternalResource(friend.getProfileAvatar()));
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
    }


    private List<Profile> getProfileFriends(BigInteger profileId){
        return Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/friends/" + profileId, Profile[].class));
    }
}
