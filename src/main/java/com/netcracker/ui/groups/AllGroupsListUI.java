package com.netcracker.ui.groups;

import com.netcracker.model.group.Group;
import com.netcracker.service.util.RestResponsePage;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.MainUI;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.PagingBar;
import com.netcracker.ui.util.CustomRestTemplate;
import com.netcracker.ui.util.VaadinValidationBinder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AllGroupsListUI extends VerticalLayout {
    private VerticalLayout mainLayout;
    private VerticalLayout allGroupsLayout;
    private PagingBar pagingLayout;
    private TextField findGroupField;
    private Label amountOfMyGroups;
    private RestResponsePage<Group> allGroups;

    public AllGroupsListUI(){
        super();
        Panel mainPanel = new Panel();
        mainLayout = new VerticalLayout();
        List<Group> allGroupsList = Arrays.asList(CustomRestTemplate.getInstance()
                .customGetForObject("/groupList/all", Group[].class));
        amountOfMyGroups = new Label("All found groups " + allGroupsList.size());
        findGroupField = PageElements.createTextField(null, "Enter the group name...", false);
        findGroupField.setWidth("100%");
        VerticalLayout headerLayout = new VerticalLayout();
        HorizontalLayout findGroupLayout = new HorizontalLayout();
        findGroupLayout.setWidth("100%");
        findGroupLayout.setHeight("45px");
        Button findGroupButton = getButtonForFindGroup(allGroupsList);
        findGroupLayout.addComponents(findGroupField, findGroupButton);
        headerLayout.addComponents(amountOfMyGroups, findGroupLayout);

        allGroupsLayout = new VerticalLayout();
        mainLayout.addComponents(headerLayout, PageElements.getSeparator());
        mainPanel.setContent(mainLayout);
        initShowGroupsWithPaging(1);
        setPagingLayout();
        addComponent(mainPanel);
    }

    private List<Group> getAllGroupsPaging(int pageNumber) {
        ResponseEntity<RestResponsePage<Group>> pageResponseEntity =
                CustomRestTemplate.getInstance().customExchangeForParametrizedTypes("/groupList/all/" + pageNumber, HttpMethod.GET,
                        null, new ParameterizedTypeReference<RestResponsePage<Group>>() {
                        });
        allGroups = pageResponseEntity.getBody();
        return allGroups.getContent();
    }

    private void setPagingLayout() {
        if (pagingLayout != null) {
            mainLayout.removeComponent(pagingLayout);
        }
        int pageCount = (int) allGroups.getTotalElements();

        if (pageCount > 1) {
            pagingLayout = new PagingBar(pageCount, 1);

            ((Button) pagingLayout.getComponent(0)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    Integer page = (Integer) ((Button) pagingLayout.getComponent(0)).getData();
                    initShowGroupsWithPaging(page);
                    pagingLayout.currentPageNumber = 1;
                    ((TextField) pagingLayout.getComponent(3)).setValue(String.valueOf(page));
                }
            });
            ((Button) pagingLayout.getComponent(6)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    Integer page = (Integer) ((Button) pagingLayout.getComponent(6)).getData();
                    initShowGroupsWithPaging(page);
                    pagingLayout.currentPageNumber = page;
                    ((TextField) pagingLayout.getComponent(3)).setValue(String.valueOf(page));
                }
            });
            ((Button) pagingLayout.getComponent(1)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    if (pagingLayout.currentPageNumber > 1) {
                        --pagingLayout.currentPageNumber;
                        initShowGroupsWithPaging(pagingLayout.currentPageNumber);
                        ((TextField) pagingLayout.getComponent(3)).setValue(String.valueOf(pagingLayout.currentPageNumber));
                    }
                }
            });
            int finalPageCount = pageCount;
            ((Button) pagingLayout.getComponent(5)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    if (pagingLayout.currentPageNumber < finalPageCount) {
                        ++pagingLayout.currentPageNumber;
                        initShowGroupsWithPaging(pagingLayout.currentPageNumber);
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
                        initShowGroupsWithPaging(pagingLayout.currentPageNumber);
                    }
                }
            });
            mainLayout.addComponent(pagingLayout);
            mainLayout.setComponentAlignment(pagingLayout, Alignment.BOTTOM_CENTER);
        }
    }

    private void initShowGroupsWithPaging(int page) {
        mainLayout.removeComponent(allGroupsLayout);
        List<Group> groupsList = getAllGroupsPaging(page);

        HorizontalLayout everyGroupLayout;
        VerticalLayout infoGroupLayout;
        allGroupsLayout = new VerticalLayout();
        for (int i = 0; i < groupsList.size(); i++) {
            everyGroupLayout = new HorizontalLayout();
            Image groupAvatar = new Image();
            PageElements.setDefaultImageSource(groupAvatar, groupsList.get(i).getGroupAvatar());
            groupAvatar.setWidth("120px");
            groupAvatar.setHeight("120px");
            groupAvatar.setDescription("Group avatar");
            int finalI = i;
            List<Group> finalGroupsList = groupsList;
            groupAvatar.addClickListener((MouseEvents.ClickListener) clickEvent -> ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new GroupUI(finalGroupsList.get(finalI).getObjectId())));
            infoGroupLayout = new VerticalLayout();
            infoGroupLayout.addComponents(new Label(groupsList.get(i).getGroupName(), ContentMode.PREFORMATTED));
            everyGroupLayout.addComponents(groupAvatar, infoGroupLayout);
            allGroupsLayout.addComponent(everyGroupLayout);
            if (finalI != (groupsList.size() - 1))
                allGroupsLayout.addComponent(PageElements.getSeparator());
        }
        mainLayout.addComponents(allGroupsLayout);
        if (pagingLayout != null) {
            mainLayout.addComponent(pagingLayout);
            mainLayout.setComponentAlignment(pagingLayout, Alignment.BOTTOM_CENTER);
        } else
            setPagingLayout();
    }

    private Button getButtonForFindGroup(List<Group> groupList) {
        Button findGroupButton = new Button("Find");
        findGroupButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                List<Group> foundGroups = new ArrayList<>();
                for (Group group : groupList)
                    if (findGroupField.getValue().equals(group.getGroupName()))
                        foundGroups.add(group);
                if (foundGroups.size() != 0) {
                    if(pagingLayout != null)
                        mainLayout.removeComponent(pagingLayout);
                    mainLayout.removeComponent(allGroupsLayout);
                    showGroups(foundGroups);
                    amountOfMyGroups.setValue("All selected groups " + foundGroups.size());
                } else if (findGroupField.getValue().isEmpty())
                    Notification.show("You should enter group name!");
                else
                    Notification.show("No groups with this name!");
            }
        });
        return findGroupButton;
    }

    private void showGroups(List<Group> groupsList) {
        HorizontalLayout everyGroupLayout;
        VerticalLayout infoGroupLayout;
        allGroupsLayout = new VerticalLayout();
        for (int i = 0; i < groupsList.size(); i++) {
            everyGroupLayout = new HorizontalLayout();
            Image groupAvatar = new Image();
            PageElements.setDefaultImageSource(groupAvatar, groupsList.get(i).getGroupAvatar());
            groupAvatar.setWidth("120px");
            groupAvatar.setHeight("120px");
            groupAvatar.setDescription("Group avatar");
            int finalI = i;
            groupAvatar.addClickListener((MouseEvents.ClickListener) clickEvent -> ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new GroupUI(groupsList.get(finalI).getObjectId())));
            infoGroupLayout = new VerticalLayout();
            infoGroupLayout.addComponents(new Label(groupsList.get(i).getGroupName(), ContentMode.PREFORMATTED));
            everyGroupLayout.addComponents(groupAvatar, infoGroupLayout);
            allGroupsLayout.addComponent(everyGroupLayout);
            if (finalI != (groupsList.size() - 1))
                allGroupsLayout.addComponent(PageElements.getSeparator());
        }
        mainLayout.addComponents(allGroupsLayout);
    }
}
