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
    private final Grid<Advertisement> grid;

    @Autowired
    public BulletinBoardListContent() {
        super();
        setSpacing(false);
        setWidth("100%");
        this.grid = new Grid<>();
        grid.setHeight(300, Unit.PIXELS);
        grid.setSizeFull();
        Grid.Column topicColumn = grid.addColumn(ad ->
                ad.getAdTopic()).setCaption("Topic");
        Grid.Column authorColumn = grid.addColumn(ad ->
                ad.getAdAuthor().getProfileSurname() + " " +
                ad.getAdAuthor().getProfileName()).setCaption("Author");
        Grid.Column informationColumn = grid.addColumn(ad ->
                ad.getAdBasicInfo()).setCaption("Basic Info");
        Grid.Column dateColumn = grid.addColumn(ad ->
                ad.getAdDate()).setCaption("Date");
        Grid.Column statusColumn = grid.addColumn(ad ->
                (ad.isAdIsVip()? "yes" : "no")).setCaption("VIP");
        Grid.Column categoryColumn = grid.addColumn(ad ->
                (ad.getAdCategory() != null ? ad.getAdCategory().getCategoryName() : "")).setCaption("Category");
        advertisementList();
        addComponentsAndExpand(grid);
    }

    private void advertisementList() {
        List<Advertisement> ads = Arrays.asList(StubConstants.REST_TEMPLATE.getForObject("http://localhost:8888/bulletinboard", Advertisement[].class));
        grid.setItems(ads);
    }

}
