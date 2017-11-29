package com.netcracker.ui.bulletinboard;

import com.netcracker.ui.StubConstants;
import com.netcracker.model.advertisement.Advertisement;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@SpringComponent
@UIScope
public class BulletinBoardListContent extends VerticalLayout {
    private final Grid<ShortAd> grid;

    @Autowired
    public BulletinBoardListContent() {
        setSpacing(false);
        setWidth("100%");
        this.grid = new Grid<>(ShortAd.class);
        grid.setHeight(300, Unit.PIXELS);
        grid.setColumns("topic", "author", "information", "date", "status");
        advertisementList();
        addComponentsAndExpand(grid);
    }

    private void advertisementList() {
        List<Advertisement> ads = Arrays.asList(StubConstants.REST_TEMPLATE.getForObject("http://localhost:8888/bulletinboard", Advertisement[].class));
        List<ShortAd> shortAds = new ArrayList<>();
        for (Advertisement ad : ads) {
            shortAds.add(new ShortAd(
                    ad.getAdTopic(),
                    ad.getAdAuthor().getProfileName() + " " + ad.getAdAuthor().getProfileSurname(),
                    ad.getAdBasicInfo(),
                    ad.getAdDate(),
                    ad.isAdIsVip() ? "VIP" : "Usual"
                    ));
        }
        grid.setItems(shortAds);
    }

}
