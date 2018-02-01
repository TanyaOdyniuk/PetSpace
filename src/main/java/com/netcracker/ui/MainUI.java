package com.netcracker.ui;

import com.netcracker.asserts.ObjectAssert;

import com.netcracker.error.ErrorMessage;
import com.netcracker.error.handler.ClientExceptionHandler;
import com.netcracker.ui.bulletinboard.BulletinBoardListContent;
import com.netcracker.ui.bulletinboard.MyBulletinBoardListContent;
import com.netcracker.ui.friendlist.FriendListUI;
import com.netcracker.ui.gallery.AlbumsUI;
import com.netcracker.ui.groups.AllGroupsListUI;
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
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.client.HttpServerErrorException;

import java.math.BigInteger;


@SpringUI(path = "main")
@Theme("valo")
@Title("PetSpace")
public class MainUI extends UI implements Button.ClickListener {

    private final HorizontalLayout topPanel;

    private final VerticalLayout leftPanel;

    private final VerticalLayout mainLayout;

    private final HorizontalLayout primaryAreaLayout;

    private BigInteger profileId;

    @Autowired
    public MainUI() {
        topPanel = new TopBarUI(this);
        leftPanel = new LeftBarUI(this);
        mainLayout = new VerticalLayout();
        mainLayout.setMargin(false);
        primaryAreaLayout = new HorizontalLayout();
        primaryAreaLayout.setMargin(false);
    }

    @Override
    protected void init(VaadinRequest request) {
        SecurityContext o = (SecurityContext) VaadinSession.getCurrent().getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        String login = o.getAuthentication().getPrincipal().toString();
        profileId = CustomRestTemplate.getInstance().customPostForObject("/user/profileId", login, BigInteger.class);

        ProfileView defaultView = new ProfileView(profileId);
        primaryAreaLayout.addComponents(leftPanel, defaultView);
        setRatioAndDimensions();

        mainLayout.addComponents(topPanel, primaryAreaLayout);
        mainLayout.setExpandRatio(mainLayout.getComponent(0), 1.0f);
        mainLayout.setExpandRatio(mainLayout.getComponent(1), 9.0f);
        setContent(mainLayout);
        /*
        UI.getCurrent().setErrorHandler(new DefaultErrorHandler() {
            @Override
            public void error(com.vaadin.server.ErrorEvent event) {
                ClientExceptionHandler.handle(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR), ErrorMessage.ERROR_500);
            }
        });
        UI.getCurrent().setErrorHandler(new ClientExceptionHandler());*/
    }
    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        String clickedButtonCaption = clickEvent.getButton().getCaption();
        if (primaryAreaLayout.getComponentCount() > 1) {
            primaryAreaLayout.removeComponent(primaryAreaLayout.getComponent(1));
        }
        switch (clickedButtonCaption) {
            case "News":
                primaryAreaLayout.addComponent(new NewsView(profileId, 1, true));
                break;
            case "My profile":
                primaryAreaLayout.addComponent(new ProfileView(profileId));
                break;
            case "My adverts":
                primaryAreaLayout.addComponent(new MyBulletinBoardListContent(profileId));
                break;
            case "Bulletin board":
                primaryAreaLayout.addComponent(new BulletinBoardListContent());
                break;
            case "All groups":
                primaryAreaLayout.addComponent(new AllGroupsListUI());
                break;
            case "My pets":
                primaryAreaLayout.addComponent(new MyPetsListUI(profileId));
                break;
            case "Pets":
                primaryAreaLayout.addComponent(new AllPetsListUI());
                break;
            case "My messages":
                primaryAreaLayout.addComponent(new MessagesListUI(profileId));
                break;
            case "My albums":
                primaryAreaLayout.addComponent(new AlbumsUI(profileId));
                break;
            case "My groups":
                primaryAreaLayout.addComponent(new MyGroupsListUI(profileId));
                break;
            case "My friends":
                primaryAreaLayout.addComponent(new FriendListUI(profileId));
                break;
            case "Users":
                primaryAreaLayout.addComponent(new UsersUI(profileId));
                break;
            case "Logout":
                getPage().setLocation("/authorization");
                SecurityContextHolder.clearContext();
                getSession().close();
                break;
            case "Settings":
                primaryAreaLayout.addComponent(new SecurityBookUI(profileId));
                break;
            case "My requests":
                primaryAreaLayout.addComponent(new RequestsUI(profileId));
                break;
            default:
                primaryAreaLayout.addComponent(new VerticalLayout());
                break;
        }
        setRatioAndDimensions();
    }

    public void changePrimaryAreaLayout(AbstractComponent layoutToSet) {
        ObjectAssert.isNull(layoutToSet);
        primaryAreaLayout.removeComponent(primaryAreaLayout.getComponent(1));
        primaryAreaLayout.addComponents(layoutToSet);
        setRatioAndDimensions();
    }

    private void setRatioAndDimensions() {
        primaryAreaLayout.setExpandRatio(primaryAreaLayout.getComponent(0), 2.0f);
        primaryAreaLayout.setExpandRatio(primaryAreaLayout.getComponent(1), 9.0f);
        AbstractOrderedLayout child = (AbstractOrderedLayout) primaryAreaLayout.getComponent(1);
        child.setHeight(UI.getCurrent().getPage().getBrowserWindowHeight() - topPanel.getHeight() - 55, Unit.PIXELS);
        child.setWidth(UI.getCurrent().getPage().getBrowserWindowWidth() - leftPanel.getWidth() - 15, Unit.PIXELS);
        child.getComponent(0).setSizeFull();
        child.setMargin(false);
    }
}