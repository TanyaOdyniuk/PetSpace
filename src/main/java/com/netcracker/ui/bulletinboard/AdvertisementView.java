package com.netcracker.ui.bulletinboard;

import com.netcracker.model.advertisement.Advertisement;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@UIScope
public class AdvertisementView extends VerticalLayout {

    private final Advertisement ad;

    public AdvertisementView(Advertisement ad) {
        this.ad = ad;

        Panel mainPanel = new Panel();
        VerticalLayout mainLayout = new VerticalLayout();

        HorizontalLayout headerLayout = new HorizontalLayout();
        Label themeLabel = new Label("Theme: " + ad.getAdTopic());
        Label dateLabel = new Label("Date: " + ad.getAdDate());
        headerLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        if (ad.isAdIsVip()) {
            Panel vipPanel = new Panel();
            vipPanel.setHeight(40, Unit.PIXELS);
            vipPanel.setWidth(100, Unit.PIXELS);
            Button vipButton = new Button("VIP");
            vipButton.setIcon(VaadinIcons.MONEY);
            vipButton.setSizeFull();
            vipPanel.setContent(vipButton);
            headerLayout.addComponents(vipPanel);
        }
        headerLayout.addComponentsAndExpand(themeLabel, dateLabel);

        HorizontalLayout petSignsLayout = new HorizontalLayout();
        petSignsLayout.addComponent(new Label("Характерные признаки: "));
        for (String petSign : ad.getAdPetSigns()) {
            petSignsLayout.addComponent(new Button(petSign));
        }

        TextField mainInfo = new TextField();
        mainInfo.setValue(ad.getAdBasicInfo());
        mainInfo.setHeight(200, Unit.PIXELS);
        mainInfo.setWidth("100%");

        HorizontalLayout mapLayout = new HorizontalLayout();
        Label mapLabel = new Label("Location info: ");
        Link mapLink = new Link("Click to see last known location", new ExternalResource(ad.getAdLocation()));
        mapLayout.addComponents(mapLabel, mapLink);

        mainLayout.addComponents(headerLayout, petSignsLayout, mainInfo, mapLayout);
        mainPanel.setContent(mainLayout);
        addComponent(mainPanel);
    }
}
