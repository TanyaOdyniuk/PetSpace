package com.netcracker.ui.request;

import com.netcracker.model.request.FriendRequest;
import com.netcracker.model.user.Profile;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.MainUI;
import com.netcracker.ui.profile.ProfileView;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class RequestsUI extends VerticalLayout {

    public RequestsUI(BigInteger profileId) {
        super();

        int browserHeight = UI.getCurrent().getPage().getBrowserWindowHeight();

        Panel mainPanel = new Panel();
        mainPanel.setWidth("100%");
        mainPanel.setHeight(browserHeight - 250, Unit.PIXELS);

        VerticalLayout requestsLayout = new VerticalLayout();

        List<FriendRequest> requestsList = getProfileRequests(profileId);
        if(requestsList.isEmpty()){
            Label noPetsLabel = PageElements.createLabel(5, "You don't have any requests!");
            requestsLayout.addComponent(noPetsLabel);
            requestsLayout.setComponentAlignment(noPetsLabel, Alignment.MIDDLE_CENTER);
        }
        else {
            for (FriendRequest request : requestsList) {
                Profile requesterProfile = getProfile(request.getReqFrom().getObjectId());

                Panel requestPanel = new Panel();
                HorizontalLayout panelLayout = new HorizontalLayout();
                VerticalLayout infoLayout = new VerticalLayout();
                HorizontalLayout buttonsLayout = new HorizontalLayout();

                requestPanel.setWidth("100%");
                requestPanel.setHeight("190px");

                panelLayout.setSpacing(true);
                panelLayout.setMargin(new MarginInfo(true));

                infoLayout.setWidth("100%");
                buttonsLayout.setWidth("100%");

                Image profileAvatar = new Image();
                PageElements.setProfileImageSource(profileAvatar, requesterProfile.getProfileAvatar());
                profileAvatar.setWidth("150px");
                profileAvatar.setHeight("150px");

                Button requesterName = PageElements.createClickedLabel(requesterProfile.getProfileFullName());
                requesterName.addClickListener(new AbstractClickListener() {
                    @Override
                    public void buttonClickListener() {
                        ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(requesterProfile.getObjectId()));
                    }
                });

                Button confirmButton = new Button("Confirm", VaadinIcons.CHECK);
                Button declineButton = new Button("Decline", VaadinIcons.CLOSE_BIG);

                confirmButton.setWidth("250px");
                declineButton.setWidth("250px");

                confirmButton.addClickListener(new AbstractClickListener() {
                    @Override
                    public void buttonClickListener() {
                        confirmFriendship(request);
                        Notification.show("Request was confirmed!");
                        ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new RequestsUI(profileId));
                    }
                });

                declineButton.addClickListener(new AbstractClickListener() {
                    @Override
                    public void buttonClickListener() {
                        declineFriendship(request);
                        Notification.show("Request was declined!");
                        ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new RequestsUI(profileId));
                    }
                });

                buttonsLayout.addComponents(confirmButton, declineButton);
                infoLayout.addComponents(requesterName, buttonsLayout);
                panelLayout.addComponents(profileAvatar, infoLayout);

                panelLayout.setComponentAlignment(profileAvatar, Alignment.MIDDLE_CENTER);
                panelLayout.setComponentAlignment(infoLayout, Alignment.MIDDLE_CENTER);

                requestPanel.setContent(panelLayout);
                requestsLayout.addComponent(requestPanel);
            }
        }
        mainPanel.setContent(requestsLayout);
        addComponent(mainPanel);
    }

    private List<FriendRequest> getProfileRequests(BigInteger profileId){
        return Arrays.asList(CustomRestTemplate.getInstance().customGetForObject("/request/" + profileId, FriendRequest[].class));
    }

    private Profile getProfile(BigInteger profileId){
        return CustomRestTemplate.getInstance().customGetForObject("/profile/" + profileId, Profile.class);
    }

    private void confirmFriendship(FriendRequest request){
        CustomRestTemplate.getInstance().customPostForObject("/request/confirm", request, FriendRequest.class);
    }

    private void declineFriendship(FriendRequest request){
        CustomRestTemplate.getInstance().customPostForObject("/request/decline", request, FriendRequest.class);
    }
}
