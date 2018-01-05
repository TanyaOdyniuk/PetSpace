package com.netcracker.ui.groups;

import com.netcracker.model.group.Group;
import com.netcracker.model.group.GroupType;
import com.netcracker.model.user.Profile;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupUI extends VerticalLayout {
    private HorizontalLayout headerLayout = new HorizontalLayout();
    private HorizontalLayout mainLayout = new HorizontalLayout();

    public GroupUI(BigInteger groupId) {
        addStyleName("v-scrollable");
        setHeight("100%");
        Group curGroup = getGroup(groupId);
        List<GroupType> curGroupTypes = getGroupType(groupId);
        GroupType curGroupType = curGroupTypes.get(0);

        //HEADER
        Panel infoGroupPanel = new Panel();
        infoGroupPanel.setWidth("710px");
        infoGroupPanel.setHeight("240px");

        VerticalLayout infoLayout = new VerticalLayout();
        Panel privateGroupPanel = new Panel();
        privateGroupPanel.setWidth("685px");
        privateGroupPanel.setHeight("30px");
        privateGroupPanel.setContent(new Label(curGroupType.getGroupType() + " group"));

        HorizontalLayout descrLayuot = new HorizontalLayout();
        Label descriptionWordLabel = new Label("Description:  ");
        Label descriptionLabel = new Label(curGroup.getGroupDescription());
        descriptionLabel.setWidth("570");
        descrLayuot.addComponents(descriptionWordLabel, descriptionLabel);

        infoLayout.addComponents(privateGroupPanel, new Label(curGroup.getGroupName(), ContentMode.PREFORMATTED), descrLayuot);
        infoGroupPanel.setContent(infoLayout);
        Image avatar = new Image();
        avatar.setWidth("240px");
        avatar.setHeight("240px");
        avatar.setSource(new ExternalResource(curGroup.getGroupAvatar()));
        headerLayout.addComponents(infoGroupPanel, avatar);
        //-----------------HEADER

        //MAIN
        Panel mainLeftPanel = new Panel();
        mainLeftPanel.setWidth("710px");
        mainLeftPanel.setHeightUndefined();
        Panel groupRecord = new Panel();
        groupRecord.setWidth("685px");
        groupRecord.setHeight("300px");
        Panel groupRecord1 = new Panel();
        groupRecord1.setWidth("685px");
        groupRecord1.setHeight("300px");
        Panel groupRecord2 = new Panel();
        groupRecord2.setWidth("685px");
        groupRecord2.setHeight("300px");

        VerticalLayout groupRecordsLayout = new VerticalLayout();
        HorizontalLayout wallListAddRecordayout = new HorizontalLayout();
        wallListAddRecordayout.setWidth("685px");
        Button addRecordButton = new Button("Add record", VaadinIcons.PLUS);
        wallListAddRecordayout.addComponents(new Label("Wall 396 records"), addRecordButton);
        wallListAddRecordayout.setComponentAlignment(addRecordButton, Alignment.TOP_RIGHT);
        groupRecordsLayout.addComponents(wallListAddRecordayout, groupRecord, groupRecord1, groupRecord2);
        mainLeftPanel.setContent(groupRecordsLayout);


        //SUBSCRIBED FRIENDS
        List<Profile> friends = new ArrayList<>(
                Arrays.asList(new Profile(),new Profile(),new Profile(),new Profile(),new Profile()));
        for (int i = 0; i < 5; i++)
            friends.get(i).setProfileAvatar("https://goo.gl/h9Dg5U");
        Panel mainRightPanel = new Panel();
        mainRightPanel.setWidth("240px");
        Panel friendSubscribersPanel = new Panel();
        friendSubscribersPanel.setWidth("215px");
        friendSubscribersPanel.setHeight("110px");
        GridLayout friendsGrid = getPhotosGrid(friends);
        VerticalLayout friendsLayout = new VerticalLayout();
        friendsLayout.addComponents(new Label("Your friends already here"), friendsGrid);
        friendSubscribersPanel.setContent(friendsLayout);
        //-----------------SUBSCRIBED FRIENDS

        //SUBSCRIBERS
        List<Profile> subscribers = new ArrayList<>(
                Arrays.asList(new Profile(),new Profile(),new Profile(),new Profile(),new Profile()));
        for (int i = 0; i < 5; i++)
            subscribers.get(i).setProfileAvatar("http://wallpaper.getwall.ru/7/tumbs/49342.jpg");
        Panel subscribersPanel = new Panel();
        subscribersPanel.setWidth("215px");
        subscribersPanel.setHeight("200px");
        GridLayout subscribersGrid = getPhotosGrid(subscribers);
        VerticalLayout subscribersLayout = new VerticalLayout();
        subscribersLayout.addComponents(new Label("Subscribers"), subscribersGrid);
        subscribersPanel.setContent(subscribersLayout);
        //-----------------SUBSCRIBERS

        //ADMIN
        Profile admin = new Profile();
            admin.setProfileAvatar("https://goo.gl/KBss3v");
        Panel adminPanel = new Panel();
        adminPanel.setWidth("215px");
        adminPanel.setHeight("110px");
        Image adminImage = new Image();
        adminImage.setSource(new ExternalResource(admin.getProfileAvatar()));
        adminImage.setWidth("52px");
        adminImage.setHeight("45px");
        VerticalLayout adminLayout = new VerticalLayout();
        adminLayout.addComponents(new Label("Admin"), adminImage);
        adminPanel.setContent(adminLayout);
        //-----------------ADMIN

        VerticalLayout mainRightLayout = new VerticalLayout();
        mainRightLayout.addComponents(friendSubscribersPanel, subscribersPanel, adminPanel);
        mainRightPanel.setContent(mainRightLayout);
        mainLayout.addComponents(mainLeftPanel, mainRightPanel);

        addComponents(headerLayout, mainLayout);
    }

    private  GridLayout getPhotosGrid(List<Profile> list){
        GridLayout photosGrid = new GridLayout(3, (list.size() / 3) + 1);
        photosGrid.setSpacing(true);
        for (int i = 0; i < list.size(); i++) {
            Panel singlePhotoPanel = new Panel();
            Image singlePhotoImage = new Image();
            singlePhotoImage.setSource(new ExternalResource(list.get(i).getProfileAvatar()));
            singlePhotoImage.setWidth("52px");
            singlePhotoImage.setHeight("45px");
            singlePhotoPanel.setContent(singlePhotoImage);
            photosGrid.addComponent(singlePhotoPanel);
        }
        return photosGrid;
    }

    private Group getGroup(BigInteger groupId) {
        return CustomRestTemplate.getInstance().customGetForObject("/groups/" + groupId, Group.class);
    }

    private List<GroupType> getGroupType(BigInteger groupId) {
        return Arrays.asList(CustomRestTemplate.getInstance().
                customGetForObject("/groups/" + groupId + "/type", GroupType[].class));
    }
}
