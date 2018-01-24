package com.netcracker.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
class StubTopBar extends HorizontalLayout {

    @Autowired
    StubTopBar(Button.ClickListener clickListener) {
        setSpacing(false);
        setWidth("100%");
        Button button1 = getNewButton("News", VaadinIcons.HOME, ValoTheme.BUTTON_DANGER, clickListener);
        Button button2 = getNewButton("Top", VaadinIcons.TROPHY, ValoTheme.BUTTON_FRIENDLY, clickListener);
        Button button3 = getNewButton("Users", VaadinIcons.USER, ValoTheme.BUTTON_FRIENDLY, clickListener);
        Button button4 = getNewButton("Pets", VaadinIcons.MEDAL, ValoTheme.BUTTON_FRIENDLY, clickListener);
        Button button5 = getNewButton("Bulletin board", VaadinIcons.CALENDAR_USER, ValoTheme.BUTTON_FRIENDLY, clickListener);
        Button button6 = getNewButton("Logout", VaadinIcons.EXIT, ValoTheme.BUTTON_DANGER, clickListener);
        addComponentsAndExpand(button1, button2, button3, button4, button5, button6);
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
