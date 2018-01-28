package com.netcracker.ui.groups;

import com.netcracker.asserts.ObjectAssert;
import com.netcracker.asserts.PetDataAssert;
import com.netcracker.model.group.Group;
import com.netcracker.service.util.RestResponsePage;
import com.netcracker.ui.*;
import com.netcracker.ui.util.CustomRestTemplate;
import com.netcracker.ui.util.VaadinValidationBinder;
import com.netcracker.ui.util.upload.ImageUpload;
import com.netcracker.ui.util.upload.UploadableComponent;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyGroupsListUI extends Panel implements UploadableComponent {
    private VerticalLayout mainLayout;
    private Window newGroupWindow;
    private BigInteger profileId;
    private VerticalLayout allGroupsLayout;
    private Label amountOfMyGroups;
    private TextField findGroupField;
    private Button findGroupButton;
    private RestResponsePage<Group> allGroups;
    private RestResponsePage<Group> administerGroups;
    private RestResponsePage<Group> myGroups;
    private PagingBar pagingLayout;
    private Image avatar;
    private Boolean isFileResource;
    private String avatarPath;


    public MyGroupsListUI(BigInteger profileId) {
        this.profileId = profileId;
        addStyleName("v-scrollable");
        setHeight("100%");
        mainLayout = new VerticalLayout();

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidth("100%");
        headerLayout.setHeight("45px");
        getNewGroupWindow();
        int buttonWidth = 156;
        Button headerButton = new Button("Create group", VaadinIcons.PLUS);
        headerButton.setWidth(buttonWidth, Unit.PIXELS);
        headerButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                UI.getCurrent().addWindow(newGroupWindow);
            }
        });
        List<Group> groupsList = Arrays.asList(CustomRestTemplate.getInstance().
                customGetForObject("/groupList/" + profileId, Group[].class));
        amountOfMyGroups = new Label("My groups " + groupsList.size());
        headerLayout.addComponents(amountOfMyGroups, headerButton);
        headerLayout.setComponentAlignment(amountOfMyGroups, Alignment.MIDDLE_LEFT);
        headerLayout.setComponentAlignment(headerButton, Alignment.MIDDLE_RIGHT);

        HorizontalLayout findGroupLayout = new HorizontalLayout();
        findGroupLayout.setWidth("100%");
        findGroupLayout.setHeight("45px");
        findGroupField = PageElements.createTextField(null, "Enter the group name...", false);
        findGroupField.setWidth("100%");
        findGroupButton = getButtonForFindGroup(groupsList);

        Button selectAllGroups = new Button("Select all groups");
        Button myCreatedGroups = new Button("Created groups");
        selectAllGroups.setWidth(buttonWidth, Unit.PIXELS);
        myCreatedGroups.setWidth(buttonWidth, Unit.PIXELS);
        myCreatedGroups.setWidth("156px");
        selectAllGroups.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                List<Group> allGr = Arrays.asList(CustomRestTemplate.getInstance()
                        .customGetForObject("/groupList/all", Group[].class));
                initShowGroupsWithPaging(1, true, false);
                amountOfMyGroups.setValue("All found groups " + allGr.size());
                setPagingLayout(true, false);
                Button findAllGroupsButton = getButtonForFindGroup(allGr);
                findGroupLayout.replaceComponent(findGroupButton, findAllGroupsButton);
                myCreatedGroups.setEnabled(false);
                selectAllGroups.setEnabled(false);
            }
        });
        myCreatedGroups.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                List<Group> allCreatedGr = Arrays.asList(CustomRestTemplate.getInstance()
                        .customGetForObject("/groups/isAdmin/" + profileId, Group[].class));
                initShowGroupsWithPaging(1, false, false);
                amountOfMyGroups.setValue("You administer " + allCreatedGr.size() + " groups");
                setPagingLayout(false, false);
                Button findAllGroupsButton = getButtonForFindGroup(allCreatedGr);
                findGroupLayout.replaceComponent(findGroupButton, findAllGroupsButton);
                myCreatedGroups.setEnabled(false);
                selectAllGroups.setEnabled(false);
            }
        });
        findGroupLayout.addComponents(findGroupField, findGroupButton, selectAllGroups);
        findGroupLayout.setComponentAlignment(selectAllGroups, Alignment.MIDDLE_RIGHT);

        mainLayout.addComponents(headerLayout, findGroupLayout, myCreatedGroups);
        mainLayout.setComponentAlignment(myCreatedGroups, Alignment.MIDDLE_RIGHT);
        mainLayout.setComponentAlignment(findGroupLayout, Alignment.TOP_CENTER);

        allGroupsLayout = new VerticalLayout();
        setContent(mainLayout);
        initShowGroupsWithPaging(1, false, true);
        setPagingLayout(false, true);
    }

    private List<Group> getMyGroupsList(int pageNumber) {
        ResponseEntity<RestResponsePage<Group>> pageResponseEntity =
                CustomRestTemplate.getInstance().customExchangeForParametrizedTypes("/groupList/" + profileId + "/" + pageNumber, HttpMethod.GET,
                        null, new ParameterizedTypeReference<RestResponsePage<Group>>() {
                        });
        myGroups = pageResponseEntity.getBody();
        return myGroups.getContent();
    }

    private List<Group> getProfileGroupsPaging(int pageNumber) {
        ResponseEntity<RestResponsePage<Group>> pageResponseEntity =
                CustomRestTemplate.getInstance().customExchangeForParametrizedTypes("/groups/isAdmin/" + profileId + "/" + pageNumber, HttpMethod.GET,
                        null, new ParameterizedTypeReference<RestResponsePage<Group>>() {
                        });
        administerGroups = pageResponseEntity.getBody();
        return administerGroups.getContent();
    }

    private List<Group> getAllGroupsPaging(int pageNumber) {
        ResponseEntity<RestResponsePage<Group>> pageResponseEntity =
                CustomRestTemplate.getInstance().customExchangeForParametrizedTypes("/groupList/all/" + pageNumber, HttpMethod.GET,
                        null, new ParameterizedTypeReference<RestResponsePage<Group>>() {
                        });
        allGroups = pageResponseEntity.getBody();
        return allGroups.getContent();
    }

    private void initShowGroupsWithPaging(int page, boolean m, boolean mm) {
        mainLayout.removeComponent(allGroupsLayout);
        List<Group> groupsList = null;
        if (m && !mm)
            groupsList = getAllGroupsPaging(page);
        if (!m && !mm)
            groupsList = getProfileGroupsPaging(page);
        if (mm)
            groupsList = getMyGroupsList(page);

        HorizontalLayout everyGroupLayout;
        VerticalLayout infoGroupLayout;
        allGroupsLayout = new VerticalLayout();
        for (int i = 0; i < groupsList.size(); i++) {
            everyGroupLayout = new HorizontalLayout();
            Image groupAvatar = new Image();
            PageElements.setImageSource(groupAvatar, groupsList.get(i).getGroupAvatar());
            groupAvatar.setWidth("120px");
            groupAvatar.setHeight("120px");
            int finalI = i;
            List<Group> finalGroupsList = groupsList;
            groupAvatar.addClickListener((MouseEvents.ClickListener) clickEvent -> ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new GroupUI(finalGroupsList.get(finalI).getObjectId())));
            infoGroupLayout = new VerticalLayout();
            infoGroupLayout.addComponents(new Label(groupsList.get(i).getGroupName(), ContentMode.PREFORMATTED),
                    new Label("72630 subscribers"));
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
            setPagingLayout(m, mm);
    }

    private void setPagingLayout(boolean p, boolean pp) {
        if (pagingLayout != null) {
            mainLayout.removeComponent(pagingLayout);
        }
        int pageCount = 0;
        if (p && !pp)
            pageCount = (int) allGroups.getTotalElements();
        if (!p && !pp)
            pageCount = (int) administerGroups.getTotalElements();
        if (pp)
            pageCount = (int) myGroups.getTotalElements();

        if (pageCount > 1) {
            pagingLayout = new PagingBar(pageCount, 1);

            ((Button) pagingLayout.getComponent(0)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    Integer page = (Integer) ((Button) pagingLayout.getComponent(0)).getData();
                    initShowGroupsWithPaging(page, p, pp);
                    pagingLayout.currentPageNumber = 1;
                    ((TextField) pagingLayout.getComponent(3)).setValue(String.valueOf(page));
                }
            });
            ((Button) pagingLayout.getComponent(6)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    Integer page = (Integer) ((Button) pagingLayout.getComponent(6)).getData();
                    initShowGroupsWithPaging(page, p, pp);
                    pagingLayout.currentPageNumber = page;
                    ((TextField) pagingLayout.getComponent(3)).setValue(String.valueOf(page));
                }
            });
            ((Button) pagingLayout.getComponent(1)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    if (pagingLayout.currentPageNumber > 1) {
                        --pagingLayout.currentPageNumber;
                        initShowGroupsWithPaging(pagingLayout.currentPageNumber, p, pp);
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
                        initShowGroupsWithPaging(pagingLayout.currentPageNumber, p, pp);
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
                        initShowGroupsWithPaging(pagingLayout.currentPageNumber, p, pp);
                    }
                }
            });
            mainLayout.addComponent(pagingLayout);
            mainLayout.setComponentAlignment(pagingLayout, Alignment.BOTTOM_CENTER);
        }
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
            PageElements.setImageSource(groupAvatar, groupsList.get(i).getGroupAvatar());
//            groupAvatar.setSource(new ExternalResource(groupsList.get(i).getGroupAvatar()));
            groupAvatar.setWidth("120px");
            groupAvatar.setHeight("120px");
            int finalI = i;
            groupAvatar.addClickListener((MouseEvents.ClickListener) clickEvent -> ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new GroupUI(groupsList.get(finalI).getObjectId())));
            infoGroupLayout = new VerticalLayout();
            infoGroupLayout.addComponents(new Label(groupsList.get(i).getGroupName(), ContentMode.PREFORMATTED),
                    new Label("72630 subscribers"));
            everyGroupLayout.addComponents(groupAvatar, infoGroupLayout);
            allGroupsLayout.addComponent(everyGroupLayout);
            if (finalI != (groupsList.size() - 1))
                allGroupsLayout.addComponent(PageElements.getSeparator());
        }
        mainLayout.addComponents(allGroupsLayout);
    }

    private void getNewGroupWindow() {
        newGroupWindow = new Window();
        newGroupWindow.setModal(true);
        newGroupWindow.setWidth("450px");
        newGroupWindow.setHeight("495px");
        newGroupWindow.setCaption("Creating a new group:");
        VerticalLayout windowContent = new VerticalLayout();
        HorizontalLayout addAlbumButtonsLayout = new HorizontalLayout();

        GridLayout avatarLayout = new GridLayout(2, 1);
        VerticalLayout avatarContext = new VerticalLayout();
        TextField avatarField = PageElements.createTextField("Avatar", "Avatar's URL");
        avatar = PageElements.getNoImage();
        avatar.setHeight("200px");
        avatar.setWidth("200px");

        Button avatarSelect = new Button("Set URL");
        avatarSelect.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                avatarSelect.setComponentError(null);
                updateImage(avatarField.getValue(), avatar);
            }
        });
        Upload uploadAvatar = new ImageUpload(UIConstants.PATH_TO_PHOTOS,
                profileId == null ? getCurrentUserProfileId() : profileId, this);
        avatarSelect.setWidth("100%");
        uploadAvatar.setWidth("100%");

        avatarContext.addComponents(avatarField, avatarSelect, uploadAvatar);
        avatarContext.setComponentAlignment(avatarSelect, Alignment.MIDDLE_CENTER);
        avatarContext.setComponentAlignment(uploadAvatar, Alignment.MIDDLE_CENTER);

        avatarLayout.addComponent(avatar, 0, 0);
        avatarLayout.addComponent(avatarContext, 1, 0);

        TextField groupName = PageElements.createTextField("Enter group name:", "group name", true);
        groupName.setWidth("100%");
        TextField description = PageElements.createTextField("Enter group description:", "group description", false);
        description.setWidth("100%");

        Button addGroupButton = new Button("Create group");
        addGroupButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                addGroupButton.setComponentError(null);
                createGroup(groupName.getValue(), avatarPath, description.getValue(), profileId);
                newGroupWindow.close();
                Notification.show("You have just created a new group!");
            }
        });
        Button cancelAddingNewAlbum = new Button("Cancel", click -> newGroupWindow.close());
        addAlbumButtonsLayout.addComponentsAndExpand(addGroupButton, cancelAddingNewAlbum);
        windowContent.addComponents(avatarLayout, groupName, description, addAlbumButtonsLayout);

        newGroupWindow.setContent(windowContent);
        newGroupWindow.center();
    }

    private void createGroup(String groupName, String groupAvatar, String description, BigInteger profileId) {
        ObjectAssert.isNullOrEmpty(groupAvatar);
        ObjectAssert.isNullOrEmpty(groupName);
        if (!isFileResource)
            groupAvatar = PetDataAssert.assertAvatarURL(groupAvatar);
        Group createdGroup = new Group(groupName, description, groupAvatar);
        HttpEntity<Group> group = new HttpEntity<>(createdGroup);
        CustomRestTemplate.getInstance().customPostForObject("/groupList/" + profileId + "/add", group, Group.class);
        ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new MyGroupsListUI(profileId));
    }

    private void updateImage(String imageURL, Image imageToUpdate) {
        imageURL = PetDataAssert.assertAvatarURL(imageURL);
        imageToUpdate.setSource(new ExternalResource(imageURL));
        isFileResource = false;
        avatarPath = imageURL;
    }

    private BigInteger getCurrentUserProfileId() {
        SecurityContext o = (SecurityContext) VaadinSession.getCurrent().getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        String login = o.getAuthentication().getPrincipal().toString();
        return CustomRestTemplate.getInstance().customPostForObject("/user/profileId", login, BigInteger.class);
    }

    @Override
    public void updateImage(File imageFile) {
        avatar.setSource(new FileResource(imageFile));
        isFileResource = true;
        avatarPath = imageFile.getPath();
    }
}
