package com.netcracker.ui;

import com.netcracker.asserts.ObjectAssert;
import com.netcracker.error.ErrorMessage;
import com.netcracker.error.handler.ClientExceptionHandler;
import com.netcracker.model.StubUser;
import com.netcracker.service.user.impl.UserService;
import com.netcracker.ui.bulletinboard.BulletinBoardListContent;
import com.netcracker.ui.bulletinboard.MyBulletinBoardListContent;
import com.netcracker.ui.friendlist.FriendListUI;
import com.netcracker.ui.gallery.GalleryUI;
import com.netcracker.ui.pet.AllPetsListUI;
import com.netcracker.ui.pet.MyPetsListUI;
import com.netcracker.ui.profile.ProfileView;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;


@SpringUI(path = "testpage")
@Theme("valo")
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
        userService.getCurrentUser();
        VaadinSession.getCurrent().getSession().setMaxInactiveInterval(999999999);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Notification.show(auth.getName());
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
        String clickedButtonCaption = clickEvent.getButton().getCaption();
        primaryAreaLayout.removeComponent(primaryAreaLayout.getComponent(1));
        switch (clickedButtonCaption) {
            case "My profile":
                primaryAreaLayout.addComponentsAndExpand(new ProfileView(BigInteger.valueOf(1)));
                break;
            case "My adverts":
                primaryAreaLayout.addComponentsAndExpand(new MyBulletinBoardListContent(BigInteger.valueOf(1)));
                break;
            case "Bulletin board":
                primaryAreaLayout.addComponentsAndExpand(new BulletinBoardListContent());
                break;
            case "My pets":
                //BigInteger profileId = new UserService().getCurrentUser().getProfile().getObjectId();
                primaryAreaLayout.addComponentsAndExpand(new MyPetsListUI(BigInteger.valueOf(1)));
                break;
            case "Pets":
                primaryAreaLayout.addComponentsAndExpand(new AllPetsListUI());
                break;
            case "My albums":
                primaryAreaLayout.addComponentsAndExpand(new GalleryUI(BigInteger.valueOf(26)));
                break;
            case "My friends":
                primaryAreaLayout.addComponentsAndExpand(new FriendListUI(BigInteger.valueOf(1)));
                break;
            case "Logout":
                getPage().setLocation("/authorization");
                SecurityContextHolder.getContext().setAuthentication(null);
                getSession().close();
                break;
            default:
                primaryAreaLayout.addComponentsAndExpand(addUsersLayout);
                break;
        }
        primaryAreaLayout.setExpandRatio(primaryAreaLayout.getComponent(0), 2.0f);
        primaryAreaLayout.setExpandRatio(primaryAreaLayout.getComponent(1), 9.0f);
    }

    public void changePrimaryAreaLayout(AbstractOrderedLayout layoutToSet){
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