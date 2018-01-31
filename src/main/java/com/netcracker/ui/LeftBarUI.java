package com.netcracker.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class LeftBarUI extends VerticalLayout {

    @Autowired
    public LeftBarUI(Button.ClickListener clickListener) {
        setSpacing(false);
        setMargin(false);
        setWidth(UIConstants.LEFT_BAR_WIDTH, Unit.PIXELS);
        setHeight("100%");
        Panel panel = new Panel();
        Button profile = getNewButton("My profile", VaadinIcons.HOME_O, ValoTheme.BUTTON_BORDERLESS_COLORED, clickListener);
        Button friends = getNewButton("My friends", VaadinIcons.USERS, ValoTheme.BUTTON_BORDERLESS_COLORED, clickListener);
        Button requests = getNewButton("My requests", VaadinIcons.BELL_O, ValoTheme.BUTTON_BORDERLESS_COLORED, clickListener);
        Button pets = getNewButton("My pets", VaadinIcons.HEART, ValoTheme.BUTTON_BORDERLESS_COLORED, clickListener);
        Button albums = getNewButton("My albums", VaadinIcons.CLIPBOARD_USER, ValoTheme.BUTTON_BORDERLESS_COLORED, clickListener);
        Button adverts = getNewButton("My adverts", VaadinIcons.USER_CARD, ValoTheme.BUTTON_BORDERLESS_COLORED, clickListener);
        Button groups = getNewButton("My groups", VaadinIcons.GROUP, ValoTheme.BUTTON_BORDERLESS_COLORED, clickListener);
        //Button settings = getNewButton("Settings", VaadinIcons.TOOLS, ValoTheme.BUTTON_BORDERLESS_COLORED, clickListener);
        Button messages = getNewButton("My messages", VaadinIcons.ENVELOPES_O, ValoTheme.BUTTON_BORDERLESS_COLORED, clickListener);
        VerticalLayout layout = new VerticalLayout();
        layout.addComponentsAndExpand(profile, friends, requests, pets, messages, albums, adverts, groups/*, settings*/);
        panel.setContent(layout);
        panel.setWidth(UIConstants.LEFT_BAR_WIDTH, Unit.PIXELS);
        panel.setHeight("100%");
        addComponents(panel);
    }

    private Button getNewButton(String caption, VaadinIcons icon, String style, Button.ClickListener listener) {
        Button button = new Button();
        button.setWidth("100%");
        button.setHeight("50px");
        button.setCaption(caption);
        button.setIcon(icon);
        button.addStyleName(style);
        button.addClickListener(listener);
        return button;
    }
}
