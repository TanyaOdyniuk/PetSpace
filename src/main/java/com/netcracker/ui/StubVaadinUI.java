package com.netcracker.ui;

import com.netcracker.asserts.ObjectAssert;
import com.netcracker.model.StubUser;

import com.netcracker.service.user.UserService;
import com.netcracker.ui.bulletinboard.BulletinBoardListContent;
import com.netcracker.ui.bulletinboard.MyBulletinBoardListContent;
import com.netcracker.ui.friendlist.FriendListUI;
import com.netcracker.ui.gallery.AlbumsUI;
import com.netcracker.ui.groups.MyGroupsListUI;
import com.netcracker.ui.messages.MessagesListUI;
import com.netcracker.ui.pet.AllPetsListUI;
import com.netcracker.ui.pet.MyPetsListUI;
import com.netcracker.ui.profile.ProfileView;
import com.netcracker.ui.secutitybook.SecurityBookUI;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;


@SpringUI(path = "testpage")
@Theme("valo")
@Title("PetSpace")
public class StubVaadinUI extends UI implements Button.ClickListener {

    private final HorizontalLayout topPanel;

    private final VerticalLayout leftPanel;

    private StubUserEditor stubUserEditor;

    private Grid<StubUser> grid;

    private Button addNewBtn;

    private final VerticalLayout mainLayout;

    private final HorizontalLayout primaryAreaLayout;

    private final VerticalLayout addUsersLayout;


    @Autowired
    UserService userService;

    @Autowired
    public StubVaadinUI(StubUserEditor stubUserEditor) {
        this.stubUserEditor = stubUserEditor;
        grid = new Grid<>(StubUser.class);
        addNewBtn = new Button("New user", VaadinIcons.PLUS);
        topPanel = new StubTopBar(this);
        leftPanel = new StubLeftBar(this);
        mainLayout = new VerticalLayout();
        primaryAreaLayout = new HorizontalLayout();
        addUsersLayout = new VerticalLayout();
    }

    @Override
    protected void init(VaadinRequest request) {
        addUsersLayout.addComponentsAndExpand(addNewBtn, grid, stubUserEditor);
        addUsersLayout.setExpandRatio(addUsersLayout.getComponent(0), 1.0f);
        addUsersLayout.setExpandRatio(addUsersLayout.getComponent(1), 15.0f);
        addUsersLayout.setExpandRatio(addUsersLayout.getComponent(2), 15.0f);

        primaryAreaLayout.addComponentsAndExpand(leftPanel, addUsersLayout);
        primaryAreaLayout.setExpandRatio(primaryAreaLayout.getComponent(0), 2.0f);
        primaryAreaLayout.setExpandRatio(primaryAreaLayout.getComponent(1), 9.0f);

        mainLayout.addComponentsAndExpand(topPanel, primaryAreaLayout);
        mainLayout.setExpandRatio(mainLayout.getComponent(0), 1.0f);
        mainLayout.setExpandRatio(mainLayout.getComponent(1), 9.0f);
        setContent(mainLayout);

        addNewBtn.setHeight(30, Unit.PIXELS);
        grid.setHeight(300, Unit.PIXELS);
        grid.setColumns("id", "firstName", "lastName");
        stubUserEditor.setHeight(250, Unit.PIXELS);
        addNewBtn.addClickListener(clickEvent -> stubUserEditor.editUser(new StubUser(++StubUser.objectCount, "", "")));

        grid.asSingleSelect().addValueChangeListener(e -> stubUserEditor.editUser(e.getValue()));

        stubUserEditor.setChangeHandler(() -> {
            stubUserEditor.setVisible(false);
            listCustomers();
        });

        listCustomers();

        /*UI.getCurrent().setErrorHandler(new DefaultErrorHandler() {
            @Override
            public void error(com.vaadin.server.ErrorEvent event) {
                ClientExceptionHandler.handle(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR), ErrorMessage.ERROR_500);
            }
        });*/
        //UI.getCurrent().setErrorHandler(new ClientExceptionHandler());
    }

    private void listCustomers() {
        List<StubUser> users = Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/restcontroller", StubUser[].class));
        grid.setItems(users);
    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
       // BigInteger profileId = userService.getCurrentUser("levil133@gmail.com").getProfile().getObjectId();
        BigInteger profileId = BigInteger.valueOf(25);
        String clickedButtonCaption = clickEvent.getButton().getCaption();
        if (primaryAreaLayout.getComponentCount() > 1) {
            primaryAreaLayout.removeComponent(primaryAreaLayout.getComponent(1));
        }
        switch (clickedButtonCaption) {
            case "My profile":
                primaryAreaLayout.addComponentsAndExpand(new ProfileView(profileId));
                break;
            case "My adverts":
                primaryAreaLayout.addComponentsAndExpand(new MyBulletinBoardListContent(profileId));
                break;
            case "Bulletin board":
                primaryAreaLayout.addComponentsAndExpand(new BulletinBoardListContent());
                break;
            case "My pets":
                primaryAreaLayout.addComponentsAndExpand(new MyPetsListUI(profileId));
                break;
            case "Pets":
                primaryAreaLayout.addComponentsAndExpand(new AllPetsListUI());
                break;
            case "My messages":
                primaryAreaLayout.addComponentsAndExpand(new MessagesListUI(profileId));
                break;
            case "My albums":
                primaryAreaLayout.addComponentsAndExpand(new AlbumsUI(BigInteger.valueOf(50)));
                break;
            case "My groups":
                primaryAreaLayout.addComponentsAndExpand(new MyGroupsListUI(profileId));
                break;
            case "My friends":
                primaryAreaLayout.addComponentsAndExpand(new FriendListUI(profileId));
                break;
            case "Logout":
                getPage().setLocation("/authorization");
                SecurityContextHolder.clearContext();
                getSession().close();
                break;
            case "Settings":
                primaryAreaLayout.addComponentsAndExpand(new SecurityBookUI(profileId));
                break;
            default:
                primaryAreaLayout.addComponentsAndExpand(addUsersLayout);
                break;
        }
        primaryAreaLayout.setExpandRatio(primaryAreaLayout.getComponent(0), 2.0f);
        primaryAreaLayout.setExpandRatio(primaryAreaLayout.getComponent(1), 9.0f);
    }

    public void changePrimaryAreaLayout(AbstractOrderedLayout layoutToSet) {
        ObjectAssert.isNull(layoutToSet);
        primaryAreaLayout.removeComponent(primaryAreaLayout.getComponent(1));
        primaryAreaLayout.addComponentsAndExpand(layoutToSet);
        primaryAreaLayout.setExpandRatio(primaryAreaLayout.getComponent(0), 2.0f);
        primaryAreaLayout.setExpandRatio(primaryAreaLayout.getComponent(1), 9.0f);
    }

    public VerticalLayout getLeftPanel() {
        return leftPanel;
    }
}