package com.netcracker.ui;

import com.netcracker.asserts.ObjectAssert;

import com.netcracker.ui.bulletinboard.BulletinBoardListContent;
import com.netcracker.ui.bulletinboard.MyBulletinBoardListContent;
import com.netcracker.ui.friendlist.FriendListUI;
import com.netcracker.ui.gallery.AlbumsUI;
import com.netcracker.ui.groups.MyGroupsListUI;
import com.netcracker.ui.messages.MessagesListUI;
import com.netcracker.ui.news.NewsView;
import com.netcracker.ui.pet.AllPetsListUI;
import com.netcracker.ui.pet.MyPetsListUI;
import com.netcracker.ui.profile.ProfileView;
import com.netcracker.ui.request.RequestsUI;
import com.netcracker.ui.secutitybook.SecurityBookUI;
import com.netcracker.ui.users.UsersUI;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.math.BigInteger;

@SpringUI(path = "testpage")
@Theme("valo")
@Title("PetSpace")
public class StubVaadinUI extends UI implements Button.ClickListener {

    private final HorizontalLayout topPanel;

    private final VerticalLayout leftPanel;

    private final VerticalLayout mainLayout;

    private final HorizontalLayout primaryAreaLayout;

    private BigInteger profileId;

    @Autowired
    public StubVaadinUI() {
        topPanel = new StubTopBar(this);
        leftPanel = new StubLeftBar(this);
        mainLayout = new VerticalLayout();
        primaryAreaLayout = new HorizontalLayout();
    }

    @Override
    protected void init(VaadinRequest request) {
        SecurityContext o = (SecurityContext) VaadinSession.getCurrent().getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        String login = o.getAuthentication().getPrincipal().toString();
        profileId = CustomRestTemplate.getInstance().customPostForObject("/user/profileId", login, BigInteger.class);

        primaryAreaLayout.addComponentsAndExpand(leftPanel, new ProfileView(profileId));
        primaryAreaLayout.setExpandRatio(primaryAreaLayout.getComponent(0), 2.0f);
        primaryAreaLayout.setExpandRatio(primaryAreaLayout.getComponent(1), 9.0f);

        mainLayout.addComponentsAndExpand(topPanel, primaryAreaLayout);
        mainLayout.setExpandRatio(mainLayout.getComponent(0), 1.0f);
        mainLayout.setExpandRatio(mainLayout.getComponent(1), 9.0f);
        setContent(mainLayout);
        /*UI.getCurrent().setErrorHandler(new DefaultErrorHandler() {
            @Override
            public void error(com.vaadin.server.ErrorEvent event) {
                ClientExceptionHandler.handle(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR), ErrorMessage.ERROR_500);
            }
        });*/
        //UI.getCurrent().setErrorHandler(new ClientExceptionHandler());
    }
    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        String clickedButtonCaption = clickEvent.getButton().getCaption();
        if (primaryAreaLayout.getComponentCount() > 1) {
            primaryAreaLayout.removeComponent(primaryAreaLayout.getComponent(1));
        }
        switch (clickedButtonCaption) {
            case "News":
                primaryAreaLayout.addComponentsAndExpand(new NewsView(profileId, 1, true));
                break;
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
                primaryAreaLayout.addComponentsAndExpand(new AlbumsUI(profileId));
                break;
            case "My groups":
                primaryAreaLayout.addComponentsAndExpand(new MyGroupsListUI(profileId));
                break;
            case "My friends":
                primaryAreaLayout.addComponentsAndExpand(new FriendListUI(profileId));
                break;
            case "Users":
                primaryAreaLayout.addComponentsAndExpand(new UsersUI(profileId));
                break;
            case "Logout":
                getPage().setLocation("/authorization");
                SecurityContextHolder.clearContext();
                getSession().close();
                break;
            case "Settings":
                primaryAreaLayout.addComponentsAndExpand(new SecurityBookUI(profileId));
                break;
            case "My requests":
                primaryAreaLayout.addComponentsAndExpand(new RequestsUI(profileId));
                break;
            default:
                primaryAreaLayout.addComponentsAndExpand(new VerticalLayout());
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

    public void changePrimaryAreaLayout(Panel panel) {
        ObjectAssert.isNull(panel);
        primaryAreaLayout.removeComponent(primaryAreaLayout.getComponent(1));
        primaryAreaLayout.addComponentsAndExpand(panel);
        primaryAreaLayout.setExpandRatio(primaryAreaLayout.getComponent(0), 2.0f);
        primaryAreaLayout.setExpandRatio(primaryAreaLayout.getComponent(1), 9.0f);
    }
}