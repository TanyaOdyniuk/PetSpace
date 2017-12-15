package com.netcracker.ui.bulletinboard;

import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.category.Category;
import com.netcracker.ui.*;
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
import org.springframework.http.HttpEntity;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;


@SpringComponent
@UIScope
public class MyBulletinBoardListContent extends VerticalLayout {
    //private final VerticalLayout innerLayout;
    private Grid<Advertisement> grid;
    private final Button newAdBtn;
    private final BigInteger profileId;
    private StubPagingBar pagingLayout;

    private final HorizontalLayout mainLayout;
    private VerticalLayout categoryFilterLayout;
    private VerticalLayout gridPagingLayout;
    private CheckBoxGroup<Category> categoryFilter;
    private Category[] selectedCategories;

    public MyBulletinBoardListContent(BigInteger profileId) {
        super();
        setSpacing(false);
        setWidth("100%");
        mainLayout = new HorizontalLayout();
        gridPagingLayout = new VerticalLayout();
        getGrid();
        this.profileId = profileId;
        selectedCategories = new Category[0];
        getPagingLayout();
        newAdBtn = new Button("Add new advertisement", VaadinIcons.PLUS);
        newAdBtn.setHeight(30, Unit.PIXELS);
        gridPagingLayout.addComponent(newAdBtn);
        gridPagingLayout.addComponent(grid);
        gridPagingLayout.addComponent(pagingLayout);
        getCategoryFilterLayout();
        myAdvertisementList(1);

        mainLayout.addComponentsAndExpand(categoryFilterLayout, gridPagingLayout);
        mainLayout.setExpandRatio(mainLayout.getComponent(0), 3.0f);
        mainLayout.setExpandRatio(mainLayout.getComponent(1), 10.0f);
        addComponent(mainLayout);/*
        innerLayout = new VerticalLayout();
        innerLayout.addComponentsAndExpand(newAdBtn, grid);
        innerLayout.setExpandRatio(innerLayout.getComponent(0), 1.0f);
        innerLayout.setExpandRatio(innerLayout.getComponent(1), 15.0f);
        getPagingLayout();
        innerLayout.addComponent(pagingLayout);
        addComponent(innerLayout);*/

        newAdBtn.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                NewAdvertisementForm sub = new NewAdvertisementForm();
                UI.getCurrent().addWindow(sub);
            }
        });
    }

    private void getGrid() {
        grid = new Grid<>();
        grid.setSizeFull();
        Grid.Column topicColumn = grid.addColumn(ad ->
                ad.getAdTopic()).setCaption("Topic").setSortable(false);
        Grid.Column informationColumn = grid.addColumn(ad ->
                ad.getAdBasicInfo()).setCaption("Basic Info").setSortable(false);
        Grid.Column dateColumn = grid.addColumn(ad ->
                ad.getAdDate()).setCaption("Date").setSortable(false);
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
        categoryFilter = new CheckBoxGroup<>("Categories");
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
        selectAll.click();
        deselectAll.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                categoryFilter.deselectAll();
                selectedCategories = new Category[0];
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
                if (categoryFilter.getSelectedItems().size() > 0) {
                    selectedCategories = new Category[categoryFilter.getSelectedItems().size()];
                    categoryFilter.getSelectedItems().toArray(selectedCategories);
                    advertisementListAfterCatFilter(1);
                    getPagingLayout();
                }
            }
        });
    }

    private List<Category> getAllCategories() {
        return Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/category", Category[].class));
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
        if (pagingLayout != null) {
            gridPagingLayout.removeComponent(pagingLayout);
        }
        int pageCount;
        boolean isSelectedCategories = (selectedCategories.length == 0);
        if (isSelectedCategories) {
            pageCount = getPageCount();
        } else {
            pageCount = getPageCountAfterCatFilter();
        }
        if (pageCount > 1) {
            pagingLayout = new StubPagingBar(pageCount);

            ((Button) pagingLayout.getComponent(0)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    Integer page = (Integer) ((Button) pagingLayout.getComponent(0)).getData();
                    if (isSelectedCategories) {
                        myAdvertisementList(page);
                    } else {
                        advertisementListAfterCatFilter(page);
                    }
                    ((TextField) pagingLayout.getComponent(3)).setValue(String.valueOf(1));
                    pagingLayout.currentPageNumber = 1;
                }
            });
            ((Button) pagingLayout.getComponent(6)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    Integer page = (Integer) ((Button) pagingLayout.getComponent(6)).getData();
                    if (isSelectedCategories) {
                        myAdvertisementList(page);
                    } else {
                        advertisementListAfterCatFilter(page);
                    }
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
                    if (isSelectedCategories) {
                        myAdvertisementList(pagingLayout.currentPageNumber);
                    } else {
                        advertisementListAfterCatFilter(pagingLayout.currentPageNumber);
                    }
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
                    if (isSelectedCategories) {
                        myAdvertisementList(pagingLayout.currentPageNumber);
                    } else {
                        advertisementListAfterCatFilter(pagingLayout.currentPageNumber);
                    }
                    ((TextField) pagingLayout.getComponent(3)).setValue(String.valueOf(pagingLayout.currentPageNumber));
                }
            });

            ((TextField) pagingLayout.getComponent(3)).addShortcutListener(new ShortcutListener("Enter", ShortcutAction.KeyCode.ENTER, null) {
                @Override
                public void handleAction(Object o, Object o1) {
                    BinderValidationStatus<PageNumber> status = pagingLayout.pageNumberFieldBinder.validate();
                    if (!status.hasErrors()) {
                        pagingLayout.currentPageNumber = Integer.valueOf(((TextField) pagingLayout.getComponent(3)).getValue());
                        if (isSelectedCategories) {
                            myAdvertisementList(pagingLayout.currentPageNumber);
                        } else {
                            advertisementListAfterCatFilter(pagingLayout.currentPageNumber);
                        }
                    }
                }
            });
            gridPagingLayout.addComponent(pagingLayout);
        }
    }

    //Filters
    private Integer getPageCountAfterCatFilter() {
        HttpEntity<Category[]> createRequest = new HttpEntity<>(selectedCategories);
        return CustomRestTemplate.getInstance().customPostForObject("/category/getPageCountAfterCatFilter/" + profileId, createRequest, Integer.class);
    }

    private void advertisementListAfterCatFilter(int pageNumber) {
        HttpEntity<Category[]> createRequest = new HttpEntity<>(selectedCategories);
        List<Advertisement> ads = Arrays.asList(
                CustomRestTemplate.getInstance().customPostForObject(
                        "/bulletinboard/category/" + pageNumber + "/" + profileId, createRequest, Advertisement[].class));
        grid.setItems(ads);
    }
}