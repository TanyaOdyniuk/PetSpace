package com.netcracker.ui.bulletinboard;

import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.StubPagingBar;
import com.netcracker.ui.StubVaadinUI;
import com.netcracker.ui.util.CustomRestTemplate;
import com.netcracker.ui.util.PageNumber;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
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
    private StubPagingBar pagingLayout;
    @Autowired
    public BulletinBoardListContent() {
        super();
        setSpacing(false);
        setWidth("100%");
        grid = new Grid<>();
        grid.setSizeFull();
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
        addComponent(grid);
        addComponent(pagingLayout);
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
        int pageCount = getPageCount();
        if (pageCount > 1) {
            pagingLayout = new StubPagingBar(pageCount);

            ((Button)pagingLayout.getComponent(0)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    advertisementList((Integer) ((Button)pagingLayout.getComponent(0)).getData());
                    ((TextField)pagingLayout.getComponent(3)).setValue(String.valueOf(1));
                    pagingLayout.currentPageNumber = 1;
                }
            });
            ((Button)pagingLayout.getComponent(6)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    advertisementList((Integer) ((Button)pagingLayout.getComponent(6)).getData());
                    pagingLayout.currentPageNumber = pageCount;
                    ((TextField)pagingLayout.getComponent(3)).setValue(String.valueOf(pageCount));
                }
            });
            ((Button)pagingLayout.getComponent(1)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    --pagingLayout.currentPageNumber;
                    if (pagingLayout.currentPageNumber < 1) {
                        pagingLayout.currentPageNumber = pageCount;
                    }
                    advertisementList(pagingLayout.currentPageNumber);
                    ((TextField)pagingLayout.getComponent(3)).setValue(String.valueOf(pagingLayout.currentPageNumber));
                }
            });
            ((Button)pagingLayout.getComponent(5)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    ++pagingLayout.currentPageNumber;
                    if (pagingLayout.currentPageNumber > pageCount) {
                        pagingLayout.currentPageNumber = 1;
                    }
                    advertisementList(pagingLayout.currentPageNumber);
                    ((TextField)pagingLayout.getComponent(3)).setValue(String.valueOf(pagingLayout.currentPageNumber));
                }
            });

            ((TextField)pagingLayout.getComponent(3)).addShortcutListener(new ShortcutListener("Enter", ShortcutAction.KeyCode.ENTER, null) {
                @Override
                public void handleAction(Object o, Object o1) {
                    BinderValidationStatus<PageNumber> status = pagingLayout.pageNumberFieldBinder.validate();
                    if (!status.hasErrors()) {
                        pagingLayout.currentPageNumber = Integer.valueOf(((TextField) pagingLayout.getComponent(3)).getValue());
                        advertisementList(pagingLayout.currentPageNumber);
                    }
                }
            });
        }
    }
}
