package com.netcracker.ui.users;

import com.netcracker.model.user.Profile;
import com.netcracker.service.util.RestResponsePage;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.MainUI;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.PagingBar;
import com.netcracker.ui.profile.ProfileView;
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
import java.util.*;

public class UsersUI extends VerticalLayout {
    private int browserHeight;
    private Panel mainPanel;
    private VerticalLayout mainLayout;
    private BigInteger profileId;
    private PagingBar pagingLayout;
    private RestResponsePage<Profile> allUsersResponse;

    public UsersUI(BigInteger profileId) {
        super();
        this.profileId = profileId;
        browserHeight = UI.getCurrent().getPage().getBrowserWindowHeight();
        mainPanel = new Panel();
        mainPanel.setWidth("100%");
        mainPanel.setHeight(browserHeight * 0.695f, Unit.PIXELS);

        mainLayout = new VerticalLayout();

        initUsersPage(1);

        mainPanel.setContent(mainLayout);
        addComponent(mainPanel);
        //setAllUsersPagingLayout();
    }

    private void initUsersPage(int pageNumber) {
        mainLayout.removeAllComponents();
        List<Profile> userList = getAllUsers(pageNumber);
        mainLayout.addComponents(genSearchContent(), PageElements.getSeparator());
        if (userList.isEmpty()) {
            mainLayout.addComponent(new Label("No people were found"));
        }
        if (pagingLayout != null) {
            mainLayout.addComponent(pagingLayout);
        } else
            setAllUsersPagingLayout();
        mainLayout.addComponents(genPeopleList(getAllUsers(pageNumber)), PageElements.getSeparator());


    }

    private HorizontalLayout genSearchContent() {
        HorizontalLayout layoutContent = new HorizontalLayout();
        TextField searchField = new TextField();
        searchField.setPlaceholder("Enter name or login to search for someone");
        searchField.setWidth("400");
        Button searchButton = new Button("Search", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                String searchRequest = searchField.getValue();
                searchRequest = searchRequest.trim().replaceAll(" +", " ");
                searchForPeople(searchRequest);
            }
        });
        searchButton.setIcon(VaadinIcons.SEARCH_MINUS);
        layoutContent.addComponent(searchField);
        layoutContent.addComponent(searchButton);
        return layoutContent;
    }

    private List<Profile> getAllUsers(int pageNumber) {
        ResponseEntity<RestResponsePage<Profile>> pageResponseEntity =
                CustomRestTemplate.getInstance().customExchangeForParametrizedTypes("/users/all/" + pageNumber, HttpMethod.GET,
                        null, new ParameterizedTypeReference<RestResponsePage<Profile>>() {
                        });
        allUsersResponse = pageResponseEntity.getBody();
        return allUsersResponse.getContent();
    }

    private List<Profile> searchPeopleByFullName(String name, String surname) {
        return Arrays.asList(CustomRestTemplate.getInstance().customGetForObject("/users/search/byFullName" + "?name=" + name + "&surname=" + surname, Profile[].class));
    }

    private List<Profile> searchPeopleByNameOrSurname(String name) {
        return Arrays.asList(CustomRestTemplate.getInstance().customGetForObject("/users/search/byName" + "?name=" + name, Profile[].class));
    }

    private List<Profile> searchPeopleByEmail(String email) {
        return Arrays.asList(CustomRestTemplate.getInstance().customGetForObject("/users/search/byEmail" + "?email=" + email, Profile[].class));
    }

    private static VerticalLayout genPeopleList(List<Profile> people) {
        VerticalLayout layoutContent = new VerticalLayout();
        for (Profile p : people) {
            HorizontalLayout userRecord = new HorizontalLayout();

            Image userAvatar = new Image();
            userAvatar.setHeight(250, Sizeable.Unit.PIXELS);
            userAvatar.setWidth(250, Sizeable.Unit.PIXELS);
            PageElements.setProfileImageSource(userAvatar, p.getProfileAvatar());
            userAvatar.setDescription("User`s avatar");
            userAvatar.addClickListener((MouseEvents.ClickListener) clickEvent ->
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
            userRecord.addComponents(userAvatar, friendName);
            userRecord.setComponentAlignment(userRecord.getComponent(1), Alignment.MIDDLE_CENTER);

            layoutContent.addComponents(userRecord, PageElements.getSeparator());
        }
        return layoutContent;
    }

    private void searchForPeople(String searchRequest) {
        if (pagingLayout != null) {
            mainLayout.removeComponent(pagingLayout);
        }
        if (searchRequest.trim().length() == 0) {
            Notification.show("Please fill the search field");
        } else {
            List<Profile> foundPeople;
            if ((searchRequest.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"))) {
                searchRequest = searchRequest.trim().replaceAll(" +", " ");
                foundPeople = searchPeopleByEmail(searchRequest);
            } else if (searchRequest.contains(" ")) {
                searchRequest = validateSearchRequest(searchRequest);
                String[] buf = searchRequest.split(" ");
                if (buf.length >= 2) {
                    String name = buf[0];
                    String surname = buf[1];
                    foundPeople = searchPeopleByFullName(name, surname);
                } else {
                    foundPeople = new ArrayList<>();
                }

            } else {
                searchRequest = validateSearchRequest(searchRequest);
                foundPeople = searchPeopleByNameOrSurname(searchRequest);
            }
            if (foundPeople.isEmpty()) {
                mainLayout.replaceComponent(mainLayout.getComponent(2), new Label("No person with such name or email, please try again."));
            } else {
                mainLayout.replaceComponent(mainLayout.getComponent(2), genPeopleList(foundPeople));
            }
        }
    }

    private String validateSearchRequest(String searchRequest) {
        searchRequest = searchRequest.replaceAll("[^a-zA-Z ]+", "");
        searchRequest = searchRequest.trim().replaceAll(" +", " ");
        return searchRequest;
    }

    private void setAllUsersPagingLayout() {
        if (pagingLayout != null) {
            mainLayout.removeComponent(pagingLayout);
        }
        int pageCount = (int) allUsersResponse.getTotalElements();
        if (pageCount > 1) {
            pagingLayout = new PagingBar(pageCount, 1);
            pagingLayout.checkButtonsState();
            pagingLayout.getFirstPageButton().addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    Integer page = (Integer) pagingLayout.getFirstPageButton().getData();
                    initUsersPage(page);
                    pagingLayout.currentPageNumber = 1;
                    pagingLayout.getPageNumberField().setValue(String.valueOf(page));
                    pagingLayout.checkButtonsState();
                }
            });
            pagingLayout.getLastPageButton().addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    Integer page = (Integer) pagingLayout.getLastPageButton().getData();
                    initUsersPage(page);
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
                        initUsersPage(pagingLayout.currentPageNumber);
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
                        initUsersPage(pagingLayout.currentPageNumber);
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
                        initUsersPage(pagingLayout.currentPageNumber);
                    }
                }
            });
            mainLayout.addComponent(pagingLayout);
        }
    }
}
