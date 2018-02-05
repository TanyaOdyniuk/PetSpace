package com.netcracker.ui.friendlist;

import com.netcracker.model.user.Profile;
import com.netcracker.service.util.RestResponsePage;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.MainUI;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.PagingBar;
import com.netcracker.ui.profile.ProfileView;
import com.netcracker.ui.users.UsersUI;
import com.netcracker.ui.util.CustomRestTemplate;
import com.netcracker.ui.util.VaadinValidationBinder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class FriendListUI extends VerticalLayout {

    private int browserHeight;
    private Panel mainPanel;
    private VerticalLayout mainLayout;
    private BigInteger profileId;
    private PagingBar pagingLayout;
    private RestResponsePage<Profile> allFriendsResponse;

    public FriendListUI(BigInteger profileId) {
        this.profileId = profileId;
        browserHeight = UI.getCurrent().getPage().getBrowserWindowHeight();
        mainPanel = new Panel();
        mainPanel.setWidth("100%");
        mainPanel.setHeight(browserHeight * 0.695f, Unit.PIXELS);

        mainLayout = new VerticalLayout();

        initFriendsPage(1);

        mainPanel.setContent(mainLayout);
        addComponent(mainPanel);
        //setAllUsersPagingLayout();
    }

    private void initFriendsPage(int pageNumber) {
        mainLayout.removeAllComponents();
        List<Profile> userList = getProfileFriends(profileId, pageNumber);
        mainLayout.addComponents(genSearchContent(), PageElements.getSeparator());
        if (pagingLayout != null) {
            mainLayout.addComponent(pagingLayout);
        } else
            setAllUsersPagingLayout();

        if (userList.isEmpty()) {
            mainLayout.addComponent(new Label("No friends were found"));
        }
        mainLayout.addComponents(genFriendsList(getProfileFriends(profileId, pageNumber)), PageElements.getSeparator());
    }

    private HorizontalLayout genSearchContent() {
        HorizontalLayout layoutContent = new HorizontalLayout();
        TextField searchField = new TextField();
        searchField.setPlaceholder("Enter name or login to search for someone in your friend list");
        searchField.setWidth("600");
        Button searchButton = new Button("Search", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                String searchRequest = searchField.getValue();
                searchForFriends(searchRequest);
            }
        });
        searchButton.setIcon(VaadinIcons.SEARCH);
        layoutContent.addComponent(searchField);
        layoutContent.addComponent(searchButton);
        return layoutContent;
    }

    private void searchForFriends(String searchRequest) {
        if (pagingLayout != null) {
            mainLayout.removeComponent(pagingLayout);
        }
        if (searchRequest.trim().length() == 0) {
            Notification.show("Please fill the search field");
        } else {
            searchRequest = searchRequest.trim().replaceAll(" +", " ");
            List<Profile> foundPeople;
            if ((searchRequest.contains("@"))) {
                foundPeople = searchForFriendsByEmail(searchRequest, profileId);
            } else if (searchRequest.contains(" ")) {
                String[] buf = searchRequest.split(" ");
                String name = buf[0];
                String surname = buf[1];
                foundPeople = searchForFriendsByFullName(name, surname, profileId);
            } else {
                foundPeople = searchForFriendsByNameOrSurname(searchRequest, profileId);
            }
            if (foundPeople.isEmpty()) {
                mainLayout.replaceComponent(mainLayout.getComponent(2), new Label("No person with such name or email in your friend list, please try again."));
            } else {
                mainLayout.replaceComponent(mainLayout.getComponent(2), genFriendsList(foundPeople));
            }
        }
    }

    private List<Profile> getProfileFriends(BigInteger profileId, int pageNumber) {
        ResponseEntity<RestResponsePage<Profile>> pageResponseEntity =
                CustomRestTemplate.getInstance().customExchangeForParametrizedTypes("/friends/" + profileId + "/all/" + pageNumber, HttpMethod.GET,
                        null, new ParameterizedTypeReference<RestResponsePage<Profile>>() {
                        });
        allFriendsResponse = pageResponseEntity.getBody();
        return allFriendsResponse.getContent();

//        return Arrays.asList(
//                CustomRestTemplate.getInstance().customGetForObject(
//                        "/friends/" + profileId, Profile[].class));
    }

    private List<Profile> searchForFriendsByFullName(String name, String surname, BigInteger profileId) {
        return Arrays.asList(CustomRestTemplate.getInstance().customGetForObject("/friends/search/byFullName/" + profileId + "?name=" + name + "&surname=" + surname, Profile[].class));
    }

    private List<Profile> searchForFriendsByNameOrSurname(String name, BigInteger profileId) {
        return Arrays.asList(CustomRestTemplate.getInstance().customGetForObject("/friends/search/byName/" + profileId + "?name=" + name, Profile[].class));
    }

    private List<Profile> searchForFriendsByEmail(String email, BigInteger profileId) {
        return Arrays.asList(CustomRestTemplate.getInstance().customGetForObject("/friends/search/byEmail/" + profileId + "?email=" + email, Profile[].class));
    }

    private VerticalLayout genFriendsList(List<Profile> friends) {
        VerticalLayout layoutContent = new VerticalLayout();
        for (Profile p : friends) {
            HorizontalLayout friendRecord = new HorizontalLayout();

            Image friendAvatar = new Image();
            friendAvatar.setHeight(250, Sizeable.Unit.PIXELS);
            friendAvatar.setWidth(250, Sizeable.Unit.PIXELS);
            PageElements.setProfileImageSource(friendAvatar, p.getProfileAvatar());
            friendAvatar.setDescription("Friend's avatar");
            friendAvatar.addClickListener((MouseEvents.ClickListener) clickEvent ->
                    ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(p.getObjectId())));

            Button friendName = new Button(p.getProfileName() + "\r\n" + p.getProfileSurname());
            friendName.setStyleName(ValoTheme.BUTTON_LINK);
            friendName.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(p.getObjectId()));
                }
            });

            //INFO + AVATAR
            friendRecord.addComponents(friendAvatar, friendName);
            friendRecord.setComponentAlignment(friendRecord.getComponent(1), Alignment.MIDDLE_CENTER);

            layoutContent.addComponents(friendRecord, PageElements.getSeparator());
        }
        return layoutContent;
        //return UsersUI.genPeopleList(friends);
    }

    private void setAllUsersPagingLayout() {
        if (pagingLayout != null) {
            mainLayout.removeComponent(pagingLayout);
        }
        int pageCount = (int) allFriendsResponse.getTotalElements();
        if (pageCount > 1) {
            pagingLayout = new PagingBar(pageCount, 1);
            pagingLayout.checkButtonsState();
            pagingLayout.getFirstPageButton().addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    Integer page = (Integer) pagingLayout.getFirstPageButton().getData();
                    initFriendsPage(page);
                    pagingLayout.currentPageNumber = 1;
                    pagingLayout.getPageNumberField().setValue(String.valueOf(page));
                    pagingLayout.checkButtonsState();
                }
            });
            pagingLayout.getLastPageButton().addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    Integer page = (Integer) pagingLayout.getLastPageButton().getData();
                    initFriendsPage(page);
                    pagingLayout.currentPageNumber = page;
                    pagingLayout.getPageNumberField().setValue(String.valueOf(page));
                    pagingLayout.checkButtonsState();
                }
            });
            pagingLayout.getPrevPageButton().addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    if (pagingLayout.currentPageNumber > 1) {
                        --pagingLayout.currentPageNumber;
                        initFriendsPage(pagingLayout.currentPageNumber);
                        pagingLayout.getPageNumberField().setValue(String.valueOf(pagingLayout.currentPageNumber));
                        pagingLayout.checkButtonsState();
                    }
                }
            });
            pagingLayout.getNextPageButton().addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    if (pagingLayout.currentPageNumber < pageCount) {
                        ++pagingLayout.currentPageNumber;
                        initFriendsPage(pagingLayout.currentPageNumber);
                        pagingLayout.getPageNumberField().setValue(String.valueOf(pagingLayout.currentPageNumber));
                        pagingLayout.checkButtonsState();
                    }
                }
            });
            pagingLayout.getPageNumberField().addShortcutListener(new ShortcutListener("Enter", ShortcutAction.KeyCode.ENTER, null) {
                @Override
                public void handleAction(Object o, Object o1) {
                    BinderValidationStatus<VaadinValidationBinder> status = pagingLayout.pageNumberFieldBinder.validate();
                    if (!status.hasErrors()) {
                        pagingLayout.currentPageNumber = Integer.valueOf(pagingLayout.getPageNumberField().getValue());
                        pagingLayout.checkButtonsState();
                        initFriendsPage(pagingLayout.currentPageNumber);
                    }
                }
            });
            mainLayout.addComponent(pagingLayout);
        }
    }
}
