package com.netcracker.ui.messages;

import com.netcracker.model.messages.Message;
import com.netcracker.model.user.Profile;
import com.netcracker.service.util.RestResponsePage;
import com.netcracker.ui.*;
import com.netcracker.ui.profile.ProfileView;
import com.netcracker.ui.util.CustomRestTemplate;
import com.netcracker.ui.util.VaadinValidationBinder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.List;

public class MessagesListUI extends VerticalLayout {

    private BigInteger profileId;
    private int browserWidth;
    private int browserHeight;

    private RestResponsePage<Message> messageResponse;

    //LAYOUTS && PAGE ELEMENTS
    private VerticalLayout mainLayout;
    private StubPagingBar pagingLayout;
    private Panel mainPanel;
    private Button sendNewMessage;

    public MessagesListUI(BigInteger profileId) {
        this.profileId = profileId;
        this.browserWidth = UI.getCurrent().getPage().getBrowserWindowWidth();
        this.browserHeight = UI.getCurrent().getPage().getBrowserWindowHeight();

        mainPanel = new Panel();
        mainPanel.setWidth("100%");
        mainPanel.setHeight(browserHeight * 0.695f, Unit.PIXELS);

        mainLayout = new VerticalLayout();

        sendNewMessage = new Button("Create message", VaadinIcons.PLUS);
        sendNewMessage.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                NewMessageWindowUI sub = new NewMessageWindowUI(profileId/*SENDER*/);
                UI.getCurrent().addWindow(sub);
            }
        });

        initMessagePage(1);

        mainPanel.setContent(mainLayout);
        addComponents(sendNewMessage, mainPanel);
        setPagingLayout();
    }

    private List<Message> getAllMessages(BigInteger profileId, int pageNumber) {
        ResponseEntity<RestResponsePage<Message>> pageResponseEntity =
                CustomRestTemplate.getInstance().customExchangeForParametrizedTypes("/message/" + profileId + "/" + pageNumber, HttpMethod.GET,
                        null, new ParameterizedTypeReference<RestResponsePage<Message>>() {
                        });
        messageResponse = pageResponseEntity.getBody();
        return messageResponse.getContent();
    }

    private void initMessagePage(int pageNumber) {
        mainLayout.removeAllComponents();
        List<Message> messagesList = getAllMessages(profileId, pageNumber);
        if (messagesList.isEmpty()) {
            Label noMessages = PageElements.createLabel(5, "You don't have any message yet!");
            mainLayout.addComponent(noMessages);
            mainLayout.setComponentAlignment(noMessages, Alignment.MIDDLE_CENTER);
            mainPanel.setContent(mainLayout);
            addComponents(sendNewMessage, mainPanel);
        } else {
            for (Message message : messagesList) {
                Panel messagePanel = new Panel();
                messagePanel.setWidth("100%");
                HorizontalLayout messageMainLayout = new HorizontalLayout();
                messageMainLayout.setMargin(new MarginInfo(false, true, false, true));
                Profile senderProfile = getProfile(message.getMessageSender().getObjectId());
                Image senderAvatar = new Image("", new ExternalResource(senderProfile.getProfileAvatar() == null ? UIConstants.NO_IMAGE_URL : senderProfile.getProfileAvatar()));
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
                //messageText.setWidth(browserWidth * 0.61f, Unit.PIXELS);
                messageText.setWidth(browserWidth * 0.5f, Unit.PIXELS);

                Label messageData = PageElements.createStandartLabel(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(message.getMessageDate()));
                messageData.setWidth("175px");

                gridLayout.addComponent(messageSender, 0, 0);
                gridLayout.addComponent(messageData, 1, 0);
                gridLayout.setComponentAlignment(messageSender, Alignment.TOP_LEFT);
                gridLayout.setComponentAlignment(messageData, Alignment.TOP_RIGHT);

                messageInfo.addComponents(gridLayout, messageText);

                messageMainLayout.addComponents(senderAvatar, messageInfo);
                messagePanel.setContent(messageMainLayout);
                mainLayout.addComponents(messagePanel);
            }
        }
        if(pagingLayout != null)
            mainLayout.addComponent(pagingLayout);
        else
            setPagingLayout();
    }

    private void setPagingLayout() {
        if (pagingLayout != null) {
            mainLayout.removeComponent(pagingLayout);
        }
        int pageCount = (int) messageResponse.getTotalElements();
        if (pageCount > 1) {
            pagingLayout = new StubPagingBar(pageCount);

            ((Button) pagingLayout.getComponent(0)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    Integer page = (Integer) ((Button) pagingLayout.getComponent(0)).getData();
                    initMessagePage(page);
                    pagingLayout.currentPageNumber = 1;
                    ((TextField) pagingLayout.getComponent(3)).setValue(String.valueOf(page));
                }
            });
            ((Button) pagingLayout.getComponent(6)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    Integer page = (Integer) ((Button) pagingLayout.getComponent(6)).getData();
                    initMessagePage(page);
                    pagingLayout.currentPageNumber = page;
                    ((TextField) pagingLayout.getComponent(3)).setValue(String.valueOf(page));
                }
            });
            ((Button) pagingLayout.getComponent(1)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    if (pagingLayout.currentPageNumber > 1) {
                        --pagingLayout.currentPageNumber;
                        initMessagePage(pagingLayout.currentPageNumber);
                        ((TextField) pagingLayout.getComponent(3)).setValue(String.valueOf(pagingLayout.currentPageNumber));
                    }
                }
            });
            ((Button) pagingLayout.getComponent(5)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    if (pagingLayout.currentPageNumber < pageCount) {
                        ++pagingLayout.currentPageNumber;
                        initMessagePage(pagingLayout.currentPageNumber);
                        ((TextField) pagingLayout.getComponent(3)).setValue(String.valueOf(pagingLayout.currentPageNumber));
                    }
                }
            });

            ((TextField) pagingLayout.getComponent(3)).addShortcutListener(new ShortcutListener("Enter", ShortcutAction.KeyCode.ENTER, null) {
                @Override
                public void handleAction(Object o, Object o1) {
                    BinderValidationStatus<VaadinValidationBinder> status = pagingLayout.pageNumberFieldBinder.validate();
                    if (!status.hasErrors()) {
                        pagingLayout.currentPageNumber = Integer.valueOf(((TextField) pagingLayout.getComponent(3)).getValue());
                        initMessagePage(pagingLayout.currentPageNumber);
                    }
                }
            });
            mainLayout.addComponent(pagingLayout);
        }
    }

    private Profile getProfile(BigInteger profileId) {
        return CustomRestTemplate.getInstance().customGetForObject("/profile/" + profileId.toString(), Profile.class);
    }
}