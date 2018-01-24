package com.netcracker.ui.messages;

import com.netcracker.model.messages.Message;
import com.netcracker.model.user.Profile;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.UIConstants;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

public class NewMessageWindowUI extends Window {

    private Profile sender;
    private Profile receiver;

    public NewMessageWindowUI(BigInteger senderId, BigInteger receiverId) {
        super();
        initWindow(senderId, receiverId);
    }

    public NewMessageWindowUI(BigInteger senderId){
        super();
        initWindow(senderId, null);
    }

    private void initWindow(BigInteger senderId, BigInteger receiverId){
        this.sender = getProfile(senderId);
        setCaption("Write message");
        setWidth("400px");

        VerticalLayout mainLayout = new VerticalLayout();
        GridLayout infoLayout = new GridLayout(2, 1);
        //infoLayout.setSpacing(true);
        infoLayout.setWidth("100%");
        infoLayout.setMargin(new MarginInfo(false, false, false, false));

        Image imageAvatar;

        if(receiverId != null){
            this.receiver = getProfile(receiverId);
            imageAvatar = new Image();
            PageElements.setImageSource(imageAvatar, receiver.getProfileAvatar());

            Label nameLabel = PageElements.createStandartLabel(receiver.getProfileName() + " " + receiver.getProfileSurname());

            infoLayout.addComponent(nameLabel, 1,0);
            infoLayout.setComponentAlignment(nameLabel, Alignment.MIDDLE_LEFT);
        }
        else{
            List<Profile> friendsList = getFriends(senderId);
            if(friendsList.size() == 0){
                Notification.show("You don't have any\nfriend to send message", Notification.Type.WARNING_MESSAGE);
                return;
            }
            this.receiver = friendsList.get(0);
            imageAvatar = new Image();
            PageElements.setImageSource(imageAvatar, receiver.getProfileAvatar());
            ComboBox<Profile> receiverSelect = new ComboBox<>("", friendsList);
            receiverSelect.setItemCaptionGenerator(Profile::getProfileFullName);
            receiverSelect.setEmptySelectionAllowed(false);
            receiverSelect.setTextInputAllowed(false);
            receiverSelect.setValue(friendsList.get(0));
            infoLayout.addComponent(receiverSelect, 1,0);
            infoLayout.setComponentAlignment(receiverSelect, Alignment.MIDDLE_RIGHT);
            receiverSelect.setWidth("175px");

            receiverSelect.addSelectionListener(event -> {
                    String avatarURL = receiverSelect.getValue().getProfileAvatar();
                    ((Image)infoLayout.getComponent(0,0)).setSource(new ExternalResource(avatarURL == null ? UIConstants.NO_IMAGE_URL : avatarURL));
                    this.receiver = receiverSelect.getValue();
            });
        }

        imageAvatar.setHeight("150px");
        imageAvatar.setWidth("150px");
        infoLayout.addComponent(imageAvatar, 0,0);

        TextArea messageArea = new TextArea("Enter your message");
        messageArea.setWidth("100%");

        Button sendMessageButton = new Button("Send");
        sendMessageButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                if(!("".equals(messageArea.getValue())) || messageArea.getValue() == null)
                    sendMessage(new Message(messageArea.getValue(), new Timestamp(System.currentTimeMillis()), receiver, sender));
                else
                    Notification.show("You cannot send empty message!", Notification.Type.WARNING_MESSAGE);
            }
        });

        mainLayout.addComponents(infoLayout, messageArea, sendMessageButton);
        mainLayout.setComponentAlignment(sendMessageButton, Alignment.MIDDLE_CENTER);
        setContent(mainLayout);
        center();
        setResizable(false);
    }

    private Profile getProfile(BigInteger profileId){
        return CustomRestTemplate.getInstance().customGetForObject("/profile/" + profileId.toString(), Profile.class);
    }

    private void sendMessage(Message message){
        CustomRestTemplate.getInstance().customPostForObject("/message/send", message, Message.class);
        close();
        Notification.show("Сообщение отправлено!");
    }

    private List<Profile> getFriends(BigInteger profileId){
        return Arrays.asList(CustomRestTemplate.getInstance().customGetForObject("/friends/" + profileId, Profile[].class));
    }
}
