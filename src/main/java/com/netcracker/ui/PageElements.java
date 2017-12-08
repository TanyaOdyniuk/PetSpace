package com.netcracker.ui;

import com.netcracker.ui.profile.ProfileView;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

public class PageElements {

    public static Label createGrayLabel(String text) {
        return createLabel(3, "gray", text);
    }

    public static Label createStandartLabel(String text) {
        return createLabel(3, "black", text);
    }

    public static Label createLabel(Integer fontSize, String text) {
        return createLabel(fontSize, "black", text);
    }

    public static Label createLabel(Integer fontSize, String textColor, String text) {
        return new Label("<font size = \"" + fontSize + "\" color=\"" + textColor + "\"> " + text, ContentMode.HTML);
    }

    public static Panel getSeparator() {
        return new Panel();
    }

    public static Button createClickedLabel(String text) {
        Button clickedLabel = new Button(text);
        clickedLabel.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        return clickedLabel;
    }
    public static Button createBlueClickedLabel(String text) {
        Button clickedLabel = new Button(text);
        clickedLabel.addStyleName(ValoTheme.BUTTON_LINK);
        //clickedLabel.setDisableOnClick(true);
        return clickedLabel;
    }

    public static String htmlTabulation = "&nbsp&nbsp&nbsp&nbsp";
}
