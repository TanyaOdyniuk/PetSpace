package com.netcracker.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
class TopBarUI extends HorizontalLayout {

    @Autowired
    TopBarUI(Button.ClickListener clickListener) {
        setSpacing(false);
        setMargin(false);
        setWidth("100%");
        Button newsButton = getNewButton("News", VaadinIcons.HOME, ValoTheme.BUTTON_FRIENDLY, clickListener);
        Button allUsersButton = getNewButton("Users", VaadinIcons.USER, ValoTheme.BUTTON_FRIENDLY, clickListener);
        Button allPetsButton = getNewButton("Pets", VaadinIcons.MEDAL, ValoTheme.BUTTON_FRIENDLY, clickListener);
        Button allBulletinsButton = getNewButton("Bulletin board", VaadinIcons.CALENDAR_USER, ValoTheme.BUTTON_FRIENDLY, clickListener);
        Button allGroupsButton = getNewButton("All groups", VaadinIcons.HAND, ValoTheme.BUTTON_FRIENDLY, clickListener);
        Button logoutButton = getNewButton("Logout", VaadinIcons.EXIT, ValoTheme.BUTTON_DANGER, clickListener);
        addComponentsAndExpand(newsButton, allUsersButton, allPetsButton, allBulletinsButton, allGroupsButton, logoutButton);
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
