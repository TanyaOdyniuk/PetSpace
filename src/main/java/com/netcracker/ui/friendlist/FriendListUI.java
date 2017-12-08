package com.netcracker.ui.friendlist;

import com.netcracker.model.user.Profile;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Sizeable;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

@SpringComponent
@UIScope
public class FriendListUI extends Panel{
    private BigInteger profileId;

    @Autowired
    public FriendListUI(BigInteger profileId){
        super();
        setWidth("100%");
        setHeight("100%");
        VerticalLayout friendRecordsLayout = new VerticalLayout();
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

            friendRecordsLayout.addComponents(friendRecord);
        }
        setContent(friendRecordsLayout);
    }


    private List<Profile> getProfileFriends(BigInteger profileId){
        List<Profile> friendList = Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/friends/" + profileId, Profile[].class));
        return friendList;
    }
}
