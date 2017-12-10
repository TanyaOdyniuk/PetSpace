package com.netcracker.ui;

import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
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

    public static TextField createTextField(String caption, String placeHolder){
        return createTextField(caption, placeHolder, false);
    }

    public static TextField createTextField(String caption, String placeHolder, Boolean isRequired){
        TextField textField = new TextField(caption);
        textField.setPlaceholder(placeHolder);
        textField.setRequiredIndicatorVisible(isRequired);
        return textField;
    }

    public static Image getNoImage(){
        return new Image("", new ExternalResource("https://assets2.bus.com/assets/camaleon_cms/image-not-found-4a963b95bf081c3ea02923dceaeb3f8085e1a654fc54840aac61a57a60903fef.png"));
    }

    public static String htmlTabulation = "&nbsp&nbsp&nbsp&nbsp";
}
