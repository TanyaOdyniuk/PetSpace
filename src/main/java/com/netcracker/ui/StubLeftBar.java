package com.netcracker.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
class StubLeftBar extends VerticalLayout {

    @Autowired
    StubLeftBar() {
        setSpacing(false);
        setWidth("100%");

        Button button1 = getNewButton("My profile", VaadinIcons.HOME_O, ValoTheme.BUTTON_BORDERLESS_COLORED, Notification.Type.ASSISTIVE_NOTIFICATION, "Profile not implemented yet!");
        Button button2 = getNewButton("My friends", VaadinIcons.USERS, ValoTheme.BUTTON_BORDERLESS_COLORED, Notification.Type.ERROR_MESSAGE, "You have no friends ;(");
        Button button3 = getNewButton("My pets", VaadinIcons.HEART, ValoTheme.BUTTON_BORDERLESS_COLORED, Notification.Type.HUMANIZED_MESSAGE, "No pets bounded!");
        Button button4 = getNewButton("My albums", VaadinIcons.CLIPBOARD_USER, ValoTheme.BUTTON_BORDERLESS_COLORED, Notification.Type.HUMANIZED_MESSAGE, "You have no photos!");
        Button button5 = getNewButton("My adverts", VaadinIcons.USER_CARD, ValoTheme.BUTTON_BORDERLESS_COLORED, Notification.Type.HUMANIZED_MESSAGE, "No adverts from you!");
        Button button6 = getNewButton("Settings", VaadinIcons.TOOLS, ValoTheme.BUTTON_BORDERLESS_COLORED, Notification.Type.HUMANIZED_MESSAGE, "To be done...");
        addComponentsAndExpand(button1, button2, button3, button4, button5, button6);
    }

    private Button getNewButton(String caption, VaadinIcons icon, String style, Notification.Type type, String listenerMessage){
        Button button = new Button();
        button.setWidth("100%");
        button.setCaption(caption);
        button.setIcon(icon);
        button.addStyleName(style);
        button.addClickListener(clickEvent -> {
            Notification notification = new Notification(listenerMessage, type);
            notification.setPosition(Position.MIDDLE_CENTER);
            notification.show(Page.getCurrent());
        });
        return button;
    }
}
