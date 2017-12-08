package com.netcracker.ui.bulletinboard;

import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.StubVaadinUI;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;


@SpringComponent
@UIScope
public class BulletinBoardListContent extends VerticalLayout {
    private final Grid<Advertisement> grid;
    private final HorizontalLayout pagingLayout;
    private final Panel panel;
    @Autowired
    public BulletinBoardListContent() {
        super();
        setSpacing(false);
        setWidth("100%");
        panel = new Panel();
        pagingLayout = new HorizontalLayout();
        grid = new Grid<>();
        grid.setSizeFull();
        addComponent(grid);
        addComponent(panel);
        getPagingLayout();

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
                (ad.isAdIsVip() ? "yes" : "no")).setCaption("VIP");
        Grid.Column categoryColumn = grid.addColumn(ad ->
                (ad.getAdCategory() != null ? ad.getAdCategory().getCategoryName() : "")).setCaption("Category");
        grid.addItemClickListener(event ->((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new AdvertisementView(event.getItem())));
        advertisementList(1);
    }

    private void advertisementList(int pageNumber) {
        List<Advertisement> ads = Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/bulletinboard/"+pageNumber, Advertisement[].class));
        grid.setItems(ads);
    }

    private int getPageCount() {
        return CustomRestTemplate.getInstance().customGetForObject(
                "/bulletinboard/pageCount", Integer.class);
    }

    private void getPagingLayout() {
        pagingLayout.removeAllComponents();
        int pageCount = getPageCount();
        if (pageCount > 1) {
            pagingLayout.setSpacing(false);
            panel.setWidth("100%");
            for (int i = 1; i < pageCount; i++) {
                Button pageRef = PageElements.createBlueClickedLabel(String.valueOf(i));
                pageRef.setData(i);
                pageRef.addClickListener(new AbstractClickListener() {
                    @Override
                    public void buttonClickListener() {
                        advertisementList((Integer) pageRef.getData());
                        getPagingLayout();
                    }
                });
                pagingLayout.addComponent(pageRef);
            }
            panel.setContent(pagingLayout);
        }
    }
}
