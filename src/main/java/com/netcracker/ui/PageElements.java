package com.netcracker.ui;

import com.netcracker.asserts.CommonDataAssert;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
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

    public static Label createCheckedValueLabel(Object value, String additionString) {
        if (value != null) {
            String text = value.toString();
            if (additionString != null)
                return createStandartLabel(PageElements.htmlTabulation + text + " " + additionString);
            else
                return createStandartLabel(PageElements.htmlTabulation + text);
        } else
            return createStandartLabel(PageElements.htmlTabulation + "No information");
    }

    public static Label createCheckedValueLabel(Object value) {
        return createCheckedValueLabel(value, null);
    }

    public static Panel getSeparator() {
        return new Panel();
    }

    public static Button createClickedLabel(String text) {
        Button clickedLabel = new Button(text);
        clickedLabel.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        return clickedLabel;
    }

    public static Button createBlueClickedLabel(String text, Resource icon) {
        Button clickedLabel;
        if (icon != null) {
            clickedLabel = new Button(text, icon);
        } else {
            clickedLabel = new Button(text);
        }

        clickedLabel.addStyleName(ValoTheme.BUTTON_LINK);
        return clickedLabel;
    }

    public static TextField createTextField(String caption, String placeHolder) {
        return createTextField(caption, placeHolder, false);
    }

    public static TextField createTextField(String caption, String placeHolder, Boolean isRequired) {
        TextField textField = new TextField(caption);
        textField.setPlaceholder(placeHolder);
        textField.setRequiredIndicatorVisible(isRequired);
        return textField;
    }

    public static Image getNoImage() {
        return new Image("", new ExternalResource(UIConstants.NO_IMAGE_URL));
    }

    public static void setProfileImageSource(Image image, String source){
        setImageSource(image, source, "profile");
    }

    public static void setPetImageSource(Image image, String source){
        setImageSource(image, source, "pet");
    }

    public static void setDefaultImageSource(Image image, String source){
        setImageSource(image, source, "default");
    }

    private static void setImageSource(Image image, String source, String imageType){
        if (source != null) {
            if (CommonDataAssert.isCommonURL(source))
                image.setSource(new ExternalResource(source));
            else
                image.setSource(new FileResource(new File(source)));
        } else {
            switch(imageType){
                case "profile":
                    image.setSource(new ExternalResource(UIConstants.PROFILE_NO_IMAGE_URL));
                    break;
                case "pet":
                    image.setSource(new ExternalResource(UIConstants.PET_NO_IMAGE_URL));
                    break;
                default:
                    image.setSource(new ExternalResource(UIConstants.NO_IMAGE_URL));
                    break;
            }
        }
    }

    public static String htmlTabulation = "&nbsp&nbsp&nbsp&nbsp";
}
