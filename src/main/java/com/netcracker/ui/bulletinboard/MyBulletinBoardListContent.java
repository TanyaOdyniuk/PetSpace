package com.netcracker.ui.bulletinboard;

import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;


@SpringComponent
@UIScope
public class MyBulletinBoardListContent extends VerticalLayout {
    private final VerticalLayout innerLayout;
    private final Grid<Advertisement> grid;
    private final Button newAdBtn;
    private final BigInteger profileId;
    private final HorizontalLayout pagingLayout;
    private final Panel pagingPanel;
    @Autowired
    public MyBulletinBoardListContent(BigInteger profileId) {
        super();
        setSpacing(false);
        setWidth("100%");
        pagingPanel = new Panel();
        pagingLayout = new HorizontalLayout();
        grid = new Grid<>();
        grid.setSizeFull();
        this.profileId = profileId;
        newAdBtn = new Button("Add new advertisement", VaadinIcons.PLUS);
        newAdBtn.setHeight(30, Unit.PIXELS);
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
        myAdvertisementList(1);
        innerLayout = new VerticalLayout();
        innerLayout.addComponentsAndExpand(newAdBtn, grid);
        innerLayout.setExpandRatio(innerLayout.getComponent(0), 2.0f);
        innerLayout.setExpandRatio(innerLayout.getComponent(1), 15.0f);
        addComponent(innerLayout);
        addComponent(pagingPanel);
        getPagingLayout();
    }

    private void myAdvertisementList(int pageNumber) {
        List<Advertisement> ads = Arrays.asList(
                CustomRestTemplate.getInstance()
                        .customGetForObject("/bulletinboard/myAds/" + profileId + "/" + pageNumber,
                                Advertisement[].class));
        grid.setItems(ads);
    }

    private int getPageCount() {
        return CustomRestTemplate.getInstance().customGetForObject(
                "/bulletinboard/pageCount/" + profileId, Integer.class);
    }

    private void getPagingLayout() {
        pagingLayout.removeAllComponents();
        int pageCount = getPageCount();
        if (pageCount > 1) {
            pagingLayout.setSpacing(false);
            pagingPanel.setWidth("100%");
            for (int i = 1; i < pageCount; i++) {
                Button pageRef = PageElements.createBlueClickedLabel(String.valueOf(i));
                pageRef.setData(i);
                pageRef.addClickListener(new AbstractClickListener() {
                    @Override
                    public void buttonClickListener() {
                        myAdvertisementList((Integer) pageRef.getData());
                        getPagingLayout();
                    }
                });
                pagingLayout.addComponent(pageRef);
            }
            pagingPanel.setContent(pagingLayout);
        }
    }
}