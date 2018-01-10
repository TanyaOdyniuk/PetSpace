package com.netcracker.ui.messages;

import com.netcracker.model.messages.Message;
import com.netcracker.model.user.Profile;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;

import java.math.BigInteger;
import java.sql.Timestamp;

public class NewMessageWindowUI extends Window {

    private Profile sender;
    private Profile receiver;


    public NewMessageWindowUI(BigInteger senderId, BigInteger receiverId) {
        super();
        this.sender = getProfile(senderId);
        this.receiver = getProfile(receiverId);
        setCaption("Написать сообщение");
        setWidth("400px");

        VerticalLayout mainLayout = new VerticalLayout();
        //HorizontalLayout infoLayout = new HorizontalLayout();
        GridLayout infoLayout = new GridLayout(2, 1);
        //infoLayout.setSpacing(true);
        infoLayout.setWidth("100%");
        infoLayout.setMargin(new MarginInfo(false, false, false, false));

        String avatarUrl = receiver.getProfileAvatar();
        Image imageAvatar = new Image("", new ExternalResource(avatarUrl));
        //Image imageAvatar = PageElements.getNoImage();
        imageAvatar.setHeight("150px");
        imageAvatar.setWidth("150px");

        Label nameLabel = PageElements.createStandartLabel(receiver.getProfileName() + " " + receiver.getProfileSurname());
        //Label nameLabel = PageElements.createStandartLabel("Иван Иванов");

        TextArea messageArea = new TextArea("Введите Ваше сообщение");
        messageArea.setWidth("100%");

        Button sendMessageButton = new Button("Отправить");
        sendMessageButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                if(!("".equals(messageArea.getValue())) || messageArea.getValue() == null)
                    sendMessage(new Message(messageArea.getValue(), new Timestamp(System.currentTimeMillis()), receiver, sender));
                else
                    Notification.show("Вы не можете отправить пустое сообщение!", Notification.Type.WARNING_MESSAGE);
            }
        });

        //infoLayout.addComponents(imageAvatar, nameLabel);
        infoLayout.addComponent(imageAvatar, 0,0);
        infoLayout.addComponent(nameLabel, 1,0);
        infoLayout.setComponentAlignment(nameLabel, Alignment.MIDDLE_LEFT);
        mainLayout.addComponents(infoLayout, messageArea, sendMessageButton);
        mainLayout.setComponentAlignment(sendMessageButton, Alignment.MIDDLE_CENTER);
        setContent(mainLayout);
        center();
    }

    private Profile getProfile(BigInteger profileId){
        return CustomRestTemplate.getInstance().customGetForObject("/profile/" + profileId.toString(), Profile.class);
    }

    private void sendMessage(Message message){
        CustomRestTemplate.getInstance().customPostForObject("/message/send", message, Message.class);
        close();
        Notification.show("Сообщение отправлено!");
    }
}
