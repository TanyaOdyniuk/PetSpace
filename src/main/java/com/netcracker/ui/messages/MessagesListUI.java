package com.netcracker.ui.messages;

import com.netcracker.model.messages.Message;
import com.netcracker.model.user.Profile;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.StubPagingBar;
import com.netcracker.ui.StubVaadinUI;
import com.netcracker.ui.profile.ProfileView;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class MessagesListUI extends VerticalLayout {

    private BigInteger profileId;
    private int browserWidth;
    private StubPagingBar pagingLayout;

    public MessagesListUI(BigInteger profileId) {
        this.profileId = profileId;
        this.browserWidth = UI.getCurrent().getPage().getBrowserWindowWidth();
        pagingLayout = new StubPagingBar(10);

        Panel mainPanel = new Panel();
        mainPanel.setWidth("100%");

        VerticalLayout mainLayout = new VerticalLayout();

        Button sendNewMessage = new Button("Создать сообщение", VaadinIcons.PLUS);
        sendNewMessage.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                NewMessageWindowUI sub = new NewMessageWindowUI(profileId/*SENDER*//*, BigInteger.valueOf(27)*//*RECEIVER*/);
                UI.getCurrent().addWindow(sub);
            }
        });

        List<Message> messages = getAllMessages(profileId);

        if(messages.size() == 0){
            Label noMessages = PageElements.createLabel(5, "У вас ещё нет сообщений!");
            mainLayout.addComponent(noMessages);
            mainLayout.setComponentAlignment(noMessages, Alignment.MIDDLE_CENTER);
            mainPanel.setContent(mainLayout);
            addComponents(sendNewMessage, mainPanel);
            return;
        }
        for (Message message: messages) {
            Panel messagePanel = new Panel();
            HorizontalLayout messageMainLayout = new HorizontalLayout();
            messageMainLayout.setMargin(new MarginInfo(false, true, false, true));
            Profile senderProfile = getProfile(message.getMessageSender().getObjectId());
            Image senderAvatar = new Image("", new ExternalResource(senderProfile.getProfileAvatar() == null ? PageElements.noImageURL : senderProfile.getProfileAvatar()));
            senderAvatar.setHeight("100px");
            senderAvatar.setWidth("100px");

            VerticalLayout messageInfo = new VerticalLayout();
            GridLayout gridLayout = new GridLayout(2, 1);
            messageInfo.setWidth("100%");
            gridLayout.setWidth("100%");
            gridLayout.setSpacing(true);

            Button messageSender = PageElements.createClickedLabel(senderProfile.getProfileName() + " " + senderProfile.getProfileSurname());
            messageSender.setHeight("25px");
            messageSender.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(senderProfile.getObjectId()));
                }
            });

            Label messageText = PageElements.createStandartLabel(message.getMessageText());
            messageText.setWidth(browserWidth * 0.61f, Unit.PIXELS);


            Label messageData = PageElements.createStandartLabel(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(message.getMessageDate()));
            messageData.setWidth("200px");

            gridLayout.addComponent(messageSender, 0, 0);
            gridLayout.addComponent(messageData, 1, 0);
            gridLayout.setComponentAlignment(messageSender, Alignment.TOP_LEFT);
            gridLayout.setComponentAlignment(messageData, Alignment.TOP_RIGHT);

            messageInfo.addComponents(gridLayout, messageText);

            messageMainLayout.addComponents(senderAvatar, messageInfo);
            messagePanel.setContent(messageMainLayout);
            mainLayout.addComponents(messagePanel, pagingLayout);
        }
        mainPanel.setContent(mainLayout);
        addComponents(sendNewMessage, mainPanel);
    }

    private List<Message> getAllMessages(BigInteger profileId){
        List<Message> messagesList = Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/message/" + profileId, Message[].class));
        return messagesList;
    }

    private Profile getProfile(BigInteger profileId){
        return CustomRestTemplate.getInstance().customGetForObject("/profile/" + profileId.toString(), Profile.class);
    }
}
