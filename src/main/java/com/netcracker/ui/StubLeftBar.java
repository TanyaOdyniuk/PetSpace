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
public class StubLeftBar extends VerticalLayout {

    @Autowired
    public StubLeftBar(Button.ClickListener clickListener) {
        super();
        setSpacing(false);
        setWidth("100%");
        Panel panel = new Panel();
        panel.setSizeUndefined();
        Button profile = getNewButton("My profile", VaadinIcons.HOME_O, ValoTheme.BUTTON_BORDERLESS_COLORED, clickListener);
        Button friends = getNewButton("My friends", VaadinIcons.USERS, ValoTheme.BUTTON_BORDERLESS_COLORED, clickListener);
        Button pets = getNewButton("My pets", VaadinIcons.HEART, ValoTheme.BUTTON_BORDERLESS_COLORED, clickListener);
        Button albums = getNewButton("My albums", VaadinIcons.CLIPBOARD_USER, ValoTheme.BUTTON_BORDERLESS_COLORED, clickListener);
        Button adverts = getNewButton("My adverts", VaadinIcons.USER_CARD, ValoTheme.BUTTON_BORDERLESS_COLORED, clickListener);
        Button groups = getNewButton("My groups", VaadinIcons.GROUP, ValoTheme.BUTTON_BORDERLESS_COLORED, clickListener);
        Button settings = getNewButton("Settings", VaadinIcons.TOOLS, ValoTheme.BUTTON_BORDERLESS_COLORED, clickListener);
        Button messages = getNewButton("My messages", VaadinIcons.ENVELOPES_O, ValoTheme.BUTTON_BORDERLESS_COLORED, clickListener);
        VerticalLayout layout = new VerticalLayout();
        layout.addComponentsAndExpand(profile, friends, pets, messages, albums, adverts, groups, settings);
        panel.setContent(layout);
        panel.setWidth("100%");
        addComponentsAndExpand(panel);
    }

    private Button getNewButton(String caption, VaadinIcons icon, String style, Button.ClickListener listener) {
        Button button = new Button();
        button.setWidth("100%");
        button.setCaption(caption);
        button.setIcon(icon);
        button.addStyleName(style);
        button.addClickListener(listener);
        return button;
    }
}
