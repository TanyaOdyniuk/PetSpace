package com.netcracker.ui;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;

public class PageElements {

    public static Label createGrayLabel(String text){
        return createLabel(3, "gray", text);
    }

    public static Label createStandartLabel(String text){
        return createLabel(3, "black", text);
    }

    public static Label createLabel(Integer fontSize, String textColor, String text){
        return new Label("<font size = \"" + fontSize + "\" color=\"" + textColor + "\"> " + text, ContentMode.HTML);
    }

    public static String htmlTabulation = "&nbsp&nbsp&nbsp&nbsp";
}
