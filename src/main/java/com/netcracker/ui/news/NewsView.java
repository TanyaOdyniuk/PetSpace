package com.netcracker.ui.news;

import com.netcracker.model.record.GroupRecord;
import com.netcracker.model.record.WallRecord;
import com.netcracker.model.user.Profile;
import com.netcracker.service.util.RestResponsePage;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.RecordPanel;
import com.netcracker.ui.PagingBar;
import com.netcracker.ui.util.CustomRestTemplate;
import com.netcracker.ui.util.VaadinValidationBinder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.*;
import com.vaadin.ui.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;
import java.util.List;

public class NewsView extends VerticalLayout {
    private final Profile profile;
    private final BigInteger profileId;
    private PagingBar pagingLayout;
    private Panel wallPanel;
    private RestResponsePage<WallRecord> wallRecordsList;
    private RestResponsePage<GroupRecord> groupRecordsList;
    private int browserHeight;
    private int browserWidth;
    private boolean isFriendsNews;
    private int pageNumber;
    public NewsView(BigInteger profileId, int pageNumb, boolean isFriendNews) {
        super();
        this.profileId = profileId;
        this.isFriendsNews = isFriendNews;
        this.pageNumber = pageNumb;
        profile = CustomRestTemplate.getInstance().
                customGetForObject("/profile/" + this.profileId, Profile.class);

        HorizontalLayout mainLayout = new HorizontalLayout();
        browserHeight = UI.getCurrent().getPage().getBrowserWindowHeight();
        browserWidth = UI.getCurrent().getPage().getBrowserWindowWidth();
        //mainLayout.setSizeFull();
        //mainLayout.setHeight(browserHeight - 250, Unit.PIXELS);
        //mainLayout.setWidth(browserWidth - 450, Unit.PIXELS);
        Panel leftPartPanel = new Panel();
        leftPartPanel.setHeight("100%");
        leftPartPanel.setWidth(250, Sizeable.Unit.PIXELS);
        Panel rightPartPanel = new Panel();
        rightPartPanel.setHeight("100%");

        VerticalLayout leftPartLayout = new VerticalLayout();
        leftPartLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        VerticalLayout rightPartLayout = new VerticalLayout();
        rightPartLayout.setSpacing(true);
        Panel pagingPanel = new Panel();

        //Elements for left part
        Button showNewsOfFriendsButton = new Button("Show news of friends", VaadinIcons.NEWSPAPER);
        showNewsOfFriendsButton.setHeight(50, Unit.PIXELS);
        showNewsOfFriendsButton.setWidth("100%");
        showNewsOfFriendsButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                isFriendsNews = true;
                pageNumber = 1;
                getFriendsRecordsList(pageNumber);
                wallPanel.setContent(getWallRecordsLayout());
                getPagingLayout(pagingPanel, isFriendsNews);
            }
        });

        Button showNewsOfGroupsButton = new Button("Show news of groups", VaadinIcons.NEWSPAPER);
        showNewsOfGroupsButton.setHeight(50, Unit.PIXELS);
        showNewsOfGroupsButton.setWidth("100%");
        showNewsOfGroupsButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                isFriendsNews = false;
                pageNumber = 1;
                getGroupsRecordsList(pageNumber);
                wallPanel.setContent(getGroupRecordsLayout());
                getPagingLayout(pagingPanel, isFriendsNews);
            }
        });
        //Elements for right part
        wallPanel = new Panel();
        wallPanel.setWidth("100%");
        if(isFriendsNews){
            getFriendsRecordsList(pageNumber);
            wallPanel.setContent(getWallRecordsLayout());

        } else {
            getGroupsRecordsList(pageNumber);
            wallPanel.setContent(getGroupRecordsLayout());
        }

        getPagingLayout(pagingPanel, isFriendsNews);

        leftPartLayout.addComponents(showNewsOfFriendsButton, showNewsOfGroupsButton);
        rightPartLayout.addComponents(pagingPanel, wallPanel);
        leftPartPanel.setContent(leftPartLayout);
        rightPartPanel.setContent(rightPartLayout);
        mainLayout.addComponent(leftPartPanel);
        mainLayout.addComponentsAndExpand(rightPartPanel);
        addComponents(mainLayout);
    }

    private void getFriendsRecordsList(int pageNumber) {
        ResponseEntity<RestResponsePage<WallRecord>> pageResponseEntity =
                CustomRestTemplate.getInstance().customExchangeForParametrizedTypes("/news/friends/" + profileId + "/" + pageNumber, HttpMethod.GET,
                        null, new ParameterizedTypeReference<RestResponsePage<WallRecord>>() {
                        });
        wallRecordsList = pageResponseEntity.getBody();
    }

    private void getGroupsRecordsList(int pageNumber) {
        ResponseEntity<RestResponsePage<GroupRecord>> pageResponseEntity =
                CustomRestTemplate.getInstance().customExchangeForParametrizedTypes("/news/groups/" + profileId + "/" + pageNumber, HttpMethod.GET,
                        null, new ParameterizedTypeReference<RestResponsePage<GroupRecord>>() {
                        });
        groupRecordsList = pageResponseEntity.getBody();
    }

    private void getData(int page, boolean isFriendsNews) {
        if (isFriendsNews) {
            getFriendsRecordsList(page);
            wallPanel.setContent(getWallRecordsLayout());
        } else {
            getGroupsRecordsList(page);
            wallPanel.setContent(getGroupRecordsLayout());
        }
        ((TextField) pagingLayout.getComponent(3)).setValue(String.valueOf(page));
    }

    private void getPagingLayout(Panel pagingPanel, boolean isFriendsNews) {
        if (pagingLayout != null) {
            pagingPanel.setContent(null);
        }
        int pageCount;
        if (isFriendsNews) {
            pageCount = (int) wallRecordsList.getTotalElements();
        } else {
            pageCount = (int) groupRecordsList.getTotalElements();
        }
        if (pageCount > 1) {
            pagingLayout = new PagingBar(pageCount, pageNumber);

            ((Button) pagingLayout.getComponent(0)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    Integer page = (Integer) ((Button) pagingLayout.getComponent(0)).getData();
                    getData(page, isFriendsNews);
                    pagingLayout.currentPageNumber = 1;
                    pageNumber = pagingLayout.currentPageNumber;
                }
            });
            ((Button) pagingLayout.getComponent(6)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    Integer page = (Integer) ((Button) pagingLayout.getComponent(6)).getData();
                    getData(page, isFriendsNews);
                    pagingLayout.currentPageNumber = page;
                    pageNumber = pagingLayout.currentPageNumber;
                }
            });
            ((Button) pagingLayout.getComponent(1)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    --pagingLayout.currentPageNumber;
                    if (pagingLayout.currentPageNumber < 1) {
                        pagingLayout.currentPageNumber = pageCount;
                    }
                    pageNumber = pagingLayout.currentPageNumber;
                    getData(pagingLayout.currentPageNumber, isFriendsNews);
                }
            });
            ((Button) pagingLayout.getComponent(5)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    ++pagingLayout.currentPageNumber;
                    if (pagingLayout.currentPageNumber > pageCount) {
                        pagingLayout.currentPageNumber = 1;
                    }
                    pageNumber = pagingLayout.currentPageNumber;
                    getData(pagingLayout.currentPageNumber, isFriendsNews);
                }
            });

            ((TextField) pagingLayout.getComponent(3)).addShortcutListener(new ShortcutListener("Enter", ShortcutAction.KeyCode.ENTER, null) {
                @Override
                public void handleAction(Object o, Object o1) {
                    BinderValidationStatus<VaadinValidationBinder> status = pagingLayout.pageNumberFieldBinder.validate();
                    if (!status.hasErrors()) {
                        String number = ((TextField) pagingLayout.getComponent(3)).getValue();
                        pagingLayout.currentPageNumber = Integer.valueOf(number.trim());
                        pageNumber = pagingLayout.currentPageNumber;
                        getData(pagingLayout.currentPageNumber, isFriendsNews);
                    }
                }
            });
            pagingPanel.setContent(pagingLayout);
        }
    }

    private VerticalLayout getWallRecordsLayout() {
        VerticalLayout wallLayout = new VerticalLayout();
        List<WallRecord> wallRecords = wallRecordsList.getContent();
        int wallRecordsListSize = wallRecords.size();
        HorizontalLayout wallHeaderLayout = createHeader(wallRecordsListSize, true);
        if(wallHeaderLayout != null){
            wallLayout.addComponent(wallHeaderLayout);
        }
        //WallRecords
        for (int i = 0; i < wallRecordsListSize; i++) {
            WallRecord currentWallRecord = wallRecords.get(i);
            Panel singleWallRecordPanel = new RecordPanel(currentWallRecord, profile, true,
                    pageNumber, isFriendsNews);
            wallLayout.addComponent(singleWallRecordPanel);
        }
        return wallLayout;
    }
    private HorizontalLayout createHeader(int size, boolean isWallRecords){
        HorizontalLayout headerLayout = null;
        if(size == 0){
            headerLayout = new HorizontalLayout();
            String labelCaptionStart ="Unfortunately, no news from your ";
            if (isWallRecords) {
                labelCaptionStart += "friends ";
            } else{
                labelCaptionStart += "groups ";
            }
            labelCaptionStart += "were found";
            headerLayout.addComponents(new Label(labelCaptionStart));
        }
        return headerLayout;
    }
    private VerticalLayout getGroupRecordsLayout() {
        VerticalLayout groupLayout = new VerticalLayout();
        List<GroupRecord> groupRecords = groupRecordsList.getContent();
        int groupRecordsListSize = groupRecords.size();
        HorizontalLayout groupHeaderLayout = createHeader(groupRecordsListSize, false);
        if(groupHeaderLayout != null){
            groupLayout.addComponent(groupHeaderLayout);
        }
        //GroupRecords
        for (int i = 0; i < groupRecordsListSize; i++) {
            GroupRecord currentGroupRecord = groupRecords.get(i);
            Panel singleGroupRecordPanel = new RecordPanel(currentGroupRecord, profile, true,
                    pageNumber, isFriendsNews);
            groupLayout.addComponent(singleGroupRecordPanel);
        }
        return groupLayout;
    }
}
