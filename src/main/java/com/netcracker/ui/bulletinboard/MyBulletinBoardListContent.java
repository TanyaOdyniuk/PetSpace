package com.netcracker.ui.bulletinboard;

import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;


@SpringComponent
@UIScope
public class MyBulletinBoardListContent extends VerticalLayout {
    private final VerticalLayout innerLayout;
    private final Grid<Advertisement> grid;
    private final Button newAdBtn;
    private final Integer profileId;
    @Autowired
    public MyBulletinBoardListContent(Integer profileId) {
        super();
        setSpacing(true);
        setWidth("100%");
        this.profileId = profileId;
        newAdBtn = new Button("Add new advertisement", VaadinIcons.PLUS);
        newAdBtn.setHeight(30, Unit.PIXELS);
        this.grid = new Grid<>();
        grid.setHeight(300, Unit.PIXELS);
        grid.setSizeFull();
        Grid.Column topicColumn = grid.addColumn(ad ->
                ad.getAdTopic()).setCaption("Topic");
        Grid.Column informationColumn = grid.addColumn(ad ->
                ad.getAdBasicInfo()).setCaption("Basic Info");
        Grid.Column dateColumn = grid.addColumn(ad ->
                ad.getAdDate()).setCaption("Date");
        Grid.Column statusColumn = grid.addColumn(ad ->
                (ad.isAdIsVip() ? "yes" : "no")).setCaption("VIP");
        Grid.Column categoryColumn = grid.addColumn(ad ->
                (ad.getAdCategory() != null ? ad.getAdCategory().getCategoryName() : "")).setCaption("Category");
        myAdvertisementList();
        innerLayout = new VerticalLayout();
        innerLayout.addComponentsAndExpand(newAdBtn, grid);
        innerLayout.setExpandRatio(innerLayout.getComponent(0), 2.0f);
        innerLayout.setExpandRatio(innerLayout.getComponent(1), 15.0f);
        addComponentsAndExpand(innerLayout);
    }

    private void myAdvertisementList() {
        List<Advertisement> ads = Arrays.asList(
                                    CustomRestTemplate.getInstance()
                                    .customGetForObject("/bulletinboard/"+ profileId,
                                                        Advertisement[].class));
        grid.setItems(ads);
    }
}