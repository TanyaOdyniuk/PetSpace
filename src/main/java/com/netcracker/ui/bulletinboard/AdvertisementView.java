package com.netcracker.ui.bulletinboard;

import com.netcracker.model.advertisement.Advertisement;
import com.vaadin.server.ExternalResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;

@SpringComponent
@UIScope
public class AdvertisementView extends VerticalLayout {

    private final Advertisement ad;

    public AdvertisementView(Advertisement ad){
        this.ad = ad;

        addComponent(new Button(ad.getAdDate().toString()));
        addComponent(new Button(ad.getAdTopic()));
        HorizontalLayout petSignsLayout = new HorizontalLayout();
        if(ad.getAdPetSigns() != null){
            petSignsLayout.addComponent(new Label("Характерные признаки: "));
            for(String petSign : ad.getAdPetSigns()){
                petSignsLayout.addComponent(new Button(petSign));
            }
        }
        addComponent(petSignsLayout);
        addComponent(new Button(String.valueOf(ad.isAdIsVip())));
        addComponent(new Button(ad.getAdBasicInfo()));
        if(ad.getAdLocation() != null){
            addComponent(new Link("Click to see last known location", new ExternalResource(ad.getAdLocation())));
        }


    }
}
