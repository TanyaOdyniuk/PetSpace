package com.netcracker.ui.bulletinboard;

import com.netcracker.model.advertisement.Advertisement;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;

import java.text.SimpleDateFormat;

@SpringComponent
@UIScope
public class AdvertisementView extends VerticalLayout {

    private final Advertisement adv;

    public AdvertisementView(Advertisement ad) {
        this.adv = ad;

        Panel mainPanel = new Panel();
        VerticalLayout mainLayout = new VerticalLayout();

        HorizontalLayout headerLayout = new HorizontalLayout();
        Label themeLabel = new Label("Theme: " + adv.getAdTopic());
        Label dateLabel = new Label("Date: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(adv.getAdDate()));
        headerLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        if (adv.isAdIsVip()) {
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
        if (adv.getAdPetSigns() != null) {
            for (String petSign : adv.getAdPetSigns()) {
                petSignsLayout.addComponent(new Button(petSign));
            }
        }

        TextField mainInfo = new TextField();
        mainInfo.setValue(adv.getAdBasicInfo());
        mainInfo.setHeight(200, Unit.PIXELS);
        mainInfo.setWidth("100%");

        HorizontalLayout mapLayout = new HorizontalLayout();
        Label mapLabel = new Label("Location info: ");
        if (adv.getAdLocation() != null) {
            Link mapLink = new Link("Click to see last known location", new ExternalResource(adv.getAdLocation()));
            mapLayout.addComponents(mapLabel, mapLink);
        }

        mainLayout.addComponents(headerLayout, petSignsLayout, mainInfo, mapLayout);
        mainPanel.setContent(mainLayout);
        addComponent(mainPanel);
    }
}
