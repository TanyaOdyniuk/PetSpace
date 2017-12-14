package com.netcracker.ui.bulletinboard;

import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.category.Category;
import com.netcracker.model.user.User;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.StubPagingBar;
import com.netcracker.ui.StubVaadinUI;
import com.netcracker.ui.util.CustomRestTemplate;
import com.netcracker.ui.util.PageNumber;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;

import java.util.Arrays;
import java.util.List;


@SpringComponent
@UIScope
public class BulletinBoardListContent extends VerticalLayout {
    private final HorizontalLayout mailLayout;
    private VerticalLayout categoryFilterLayout;
    private VerticalLayout gridPagingLayout;
    private Grid<Advertisement> grid;
    private StubPagingBar pagingLayout;
    private CheckBoxGroup<Category> categoryFilter;
    private Category[] selectedCategories;

    @Autowired
    public BulletinBoardListContent() {
        super();
        mailLayout = new HorizontalLayout();
        gridPagingLayout = new VerticalLayout();
        getGrid();
        getPagingLayout();
        gridPagingLayout.addComponent(grid);
        gridPagingLayout.addComponent(pagingLayout);
        getCategoryFilterLayout();

        advertisementList(1);
        mailLayout.addComponent(categoryFilterLayout);
        mailLayout.addComponent(gridPagingLayout);
        addComponent(mailLayout);
    }

    private void getGrid() {
        grid = new Grid<>();
        grid.setSizeFull();
        Grid.Column topicColumn = grid.addColumn(ad ->
                ad.getAdTopic()).setCaption("Topic").setSortable(false);
        Grid.Column authorColumn = grid.addColumn(ad ->
                ad.getAdAuthor().getProfileSurname() + " " +
                        ad.getAdAuthor().getProfileName()).setCaption("Author").setSortable(false);
        Grid.Column informationColumn = grid.addColumn(ad ->
                ad.getAdBasicInfo()).setCaption("Basic Info").setSortable(false);
        Grid.Column dateColumn = grid.addColumn(ad ->
                ad.getAdDate()).setCaption("Date");
        Grid.Column statusColumn = grid.addColumn(ad ->
                (ad.isAdIsVip() ? "yes" : "no")).setCaption("VIP").setSortable(false);
        Grid.Column categoryColumn = grid.addColumn(ad ->
                (ad.getAdCategory() != null ? ad.getAdCategory().getCategoryName() : "")).setCaption("Category").setSortable(false);
        grid.addItemClickListener(event -> ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new AdvertisementView(event.getItem())));
    }

    private void getCategoryFilterLayout() {
        categoryFilterLayout = new VerticalLayout();
        HorizontalLayout selDeselButtonsLayout = new HorizontalLayout();
        Button filter = new Button("Filter");
        Button selectAll = new Button("Select all");
        Button deselectAll = new Button("Deselect all");
        categoryFilter = new CheckBoxGroup<>("My Selection");
        List<Category> categories = getAllCategories();
        categoryFilter.setItems(categories);
        categoryFilter.setItemCaptionGenerator(Category::getCategoryName);
        selectAll.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                for (Category c : categories) {
                    categoryFilter.select(c);
                }
            }
        });
        deselectAll.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                categoryFilter.deselectAll();
            }
        });
        selDeselButtonsLayout.addComponent(selectAll);
        selDeselButtonsLayout.addComponent(deselectAll);
        categoryFilterLayout.addComponent(selDeselButtonsLayout);
        categoryFilterLayout.addComponent(categoryFilter);
        categoryFilterLayout.addComponent(filter);
        filter.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                if(categoryFilter.getSelectedItems().size() > 0){
                    selectedCategories = new Category[categoryFilter.getSelectedItems().size()];
                    categoryFilter.getSelectedItems().toArray(selectedCategories);
                    advertisementListAfterCatFilter(1);
                    getPagingLayoutAfterCatFilter();
                }
            }
        });
    }

    private List<Category> getAllCategories() {
        return Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/category", Category[].class));
    }

    private void advertisementList(int pageNumber) {
        List<Advertisement> ads = Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/bulletinboard/" + pageNumber, Advertisement[].class));
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

            ((Button) pagingLayout.getComponent(0)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    advertisementList((Integer) ((Button) pagingLayout.getComponent(0)).getData());
                    ((TextField) pagingLayout.getComponent(3)).setValue(String.valueOf(1));
                    pagingLayout.currentPageNumber = 1;
                }
            });
            ((Button) pagingLayout.getComponent(6)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    advertisementList((Integer) ((Button) pagingLayout.getComponent(6)).getData());
                    pagingLayout.currentPageNumber = pageCount;
                    ((TextField) pagingLayout.getComponent(3)).setValue(String.valueOf(pageCount));
                }
            });
            ((Button) pagingLayout.getComponent(1)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    --pagingLayout.currentPageNumber;
                    if (pagingLayout.currentPageNumber < 1) {
                        pagingLayout.currentPageNumber = pageCount;
                    }
                    advertisementList(pagingLayout.currentPageNumber);
                    ((TextField) pagingLayout.getComponent(3)).setValue(String.valueOf(pagingLayout.currentPageNumber));
                }
            });
            ((Button) pagingLayout.getComponent(5)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    ++pagingLayout.currentPageNumber;
                    if (pagingLayout.currentPageNumber > pageCount) {
                        pagingLayout.currentPageNumber = 1;
                    }
                    advertisementList(pagingLayout.currentPageNumber);
                    ((TextField) pagingLayout.getComponent(3)).setValue(String.valueOf(pagingLayout.currentPageNumber));
                }
            });

            ((TextField) pagingLayout.getComponent(3)).addShortcutListener(new ShortcutListener("Enter", ShortcutAction.KeyCode.ENTER, null) {
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

    //Filters
    private Integer getPageCountAfterCatFilter() {
        HttpEntity<Category[]> createRequest = new HttpEntity<>(selectedCategories);
        return CustomRestTemplate.getInstance().customPostForObject("/category/getPageCountAfterCatFilter", createRequest, Integer.class);
    }

    private void advertisementListAfterCatFilter(int pageNumber) {
        HttpEntity<Category[]> createRequest = new HttpEntity<>(selectedCategories);
        List<Advertisement> ads = Arrays.asList(
                CustomRestTemplate.getInstance().customPostForObject(
                        "/bulletinboard/category/" + pageNumber, createRequest, Advertisement[].class));
        grid.setItems(ads);
    }

    private void getPagingLayoutAfterCatFilter() {
        gridPagingLayout.removeComponent(pagingLayout);
        int pageCount = getPageCountAfterCatFilter();
        if (pageCount > 1) {
            pagingLayout = new StubPagingBar(pageCount);

            ((Button) pagingLayout.getComponent(0)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    advertisementListAfterCatFilter((Integer) ((Button) pagingLayout.getComponent(0)).getData());
                    ((TextField) pagingLayout.getComponent(3)).setValue(String.valueOf(1));
                    pagingLayout.currentPageNumber = 1;
                }
            });
            ((Button) pagingLayout.getComponent(6)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    advertisementListAfterCatFilter((Integer) ((Button) pagingLayout.getComponent(6)).getData());
                    pagingLayout.currentPageNumber = pageCount;
                    ((TextField) pagingLayout.getComponent(3)).setValue(String.valueOf(pageCount));
                }
            });
            ((Button) pagingLayout.getComponent(1)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    --pagingLayout.currentPageNumber;
                    if (pagingLayout.currentPageNumber < 1) {
                        pagingLayout.currentPageNumber = pageCount;
                    }
                    advertisementListAfterCatFilter(pagingLayout.currentPageNumber);
                    ((TextField) pagingLayout.getComponent(3)).setValue(String.valueOf(pagingLayout.currentPageNumber));
                }
            });
            ((Button) pagingLayout.getComponent(5)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    ++pagingLayout.currentPageNumber;
                    if (pagingLayout.currentPageNumber > pageCount) {
                        pagingLayout.currentPageNumber = 1;
                    }
                    advertisementListAfterCatFilter(pagingLayout.currentPageNumber);
                    ((TextField) pagingLayout.getComponent(3)).setValue(String.valueOf(pagingLayout.currentPageNumber));
                }
            });

            ((TextField) pagingLayout.getComponent(3)).addShortcutListener(new ShortcutListener("Enter", ShortcutAction.KeyCode.ENTER, null) {
                @Override
                public void handleAction(Object o, Object o1) {
                    BinderValidationStatus<PageNumber> status = pagingLayout.pageNumberFieldBinder.validate();
                    if (!status.hasErrors()) {
                        pagingLayout.currentPageNumber = Integer.valueOf(((TextField) pagingLayout.getComponent(3)).getValue());
                        advertisementListAfterCatFilter(pagingLayout.currentPageNumber);
                    }
                }
            });
            gridPagingLayout.addComponent(pagingLayout);
        }
    }
}
