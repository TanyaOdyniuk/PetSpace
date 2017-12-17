package com.netcracker.ui.bulletinboard;

import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.category.Category;
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
    private final HorizontalLayout mainLayout;
    private VerticalLayout categoryFilterLayout;
    private VerticalLayout gridPagingLayout;
    private Grid<Advertisement> grid;
    private StubPagingBar pagingLayout;
    private CheckBoxGroup<Category> categoryFilter;
    private Category[] selectedCategories;
    private String topic = "";
    private TextField topicField;

    @Autowired
    public BulletinBoardListContent() {
        super();
        setWidth("100%");
        setHeight("100%");
        topicField = new TextField();
        mainLayout = new HorizontalLayout();
        gridPagingLayout = new VerticalLayout();
        getGrid();
        selectedCategories = new Category[0];
        getPagingLayout();
        gridPagingLayout.addComponent(grid);
        if (pagingLayout != null) {
            gridPagingLayout.addComponent(pagingLayout);
        } else{
            grid.setWidth("100%");
            grid.setHeight("100%");
            gridPagingLayout.setWidth("100%");
            gridPagingLayout.setHeight("100%");
        }
        getCategoryFilterLayout();

        advertisementList(1);
        mainLayout.addComponentsAndExpand(categoryFilterLayout, gridPagingLayout);
        mainLayout.setExpandRatio(mainLayout.getComponent(0), 3.0f);
        mainLayout.setExpandRatio(mainLayout.getComponent(1), 10.0f);
        addComponent(mainLayout);
    }

    private void getGrid() {
        grid = new Grid<>();
        grid.setHeight("50%");
        grid.setWidth("100%");
        grid.setStyleName("v-scrollable");
        Grid.Column topicColumn = grid.addColumn(ad ->
                ad.getAdTopic()).setCaption("Topic").setWidth(150).setSortable(false);
        Grid.Column authorColumn = grid.addColumn(ad ->
                ad.getAdAuthor().getProfileSurname() + " " +
                        ad.getAdAuthor().getProfileName()).setCaption("Author").setSortable(false);
        Grid.Column informationColumn = grid.addColumn(ad ->
                ad.getAdBasicInfo()).setCaption("Basic Info").setWidth(200).setSortable(false);
        Grid.Column dateColumn = grid.addColumn(ad ->
                ad.getAdDate()).setCaption("Date");
        Grid.Column statusColumn = grid.addColumn(ad ->
                (ad.isAdIsVip() ? "yes" : "no")).setCaption("VIP").setSortable(false);
        Grid.Column categoryColumn = grid.addColumn(ad ->
                (ad.getAdCategory() != null ? ad.getAdCategory().getCategoryName() : "")).setCaption("Category").setSortable(false);
        grid.addItemClickListener(event -> ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new AdvertisementView(event.getItem())));
    }

    private void getCategoryFilterLayout() {
        Panel filterPanel = new Panel();
        filterPanel.setSizeUndefined();
        categoryFilterLayout = new VerticalLayout();
        HorizontalLayout topicSearch = new HorizontalLayout();
        topicSearch.setCaption("Search by topic");
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
        filterPanel.setContent(categoryFilter);
        categoryFilterLayout.addComponent(filterPanel);
        categoryFilterLayout.addComponent(filter);
        categoryFilterLayout.addComponent(PageElements.getSeparator());
        topicField.setPlaceholder("Type topic here");
        Button searchTopic = new Button("", VaadinIcons.SEARCH);
        searchTopic.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                if (!topicField.isEmpty()) {
                    topic = topicField.getValue();
                    advertisementListTopicSearch(1);
                    deselectAll.click();
                    selectedCategories = new Category[0];
                    getPagingLayout();
                } else {
                    topic = "";
                }
            }
        });
        topicSearch.addComponent(topicField);
        topicSearch.addComponent(searchTopic);
        categoryFilterLayout.addComponent(topicSearch);
        filter.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                if (categoryFilter.getSelectedItems().size() > 0) {
                    selectedCategories = new Category[categoryFilter.getSelectedItems().size()];
                    categoryFilter.getSelectedItems().toArray(selectedCategories);
                    advertisementListAfterCatFilter(1);
                    topicField.setValue("");
                    topic = "";
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

    private void getData(boolean isNotSelectedCategories, boolean isTopicFilter, int page) {
        if (isNotSelectedCategories && !isTopicFilter) {
            advertisementList(page);
        } else {
            if (isTopicFilter) {
                advertisementListTopicSearch(page);
            } else {
                advertisementListAfterCatFilter(page);
            }
        }
        ((TextField) pagingLayout.getComponent(3)).setValue(String.valueOf(page));
    }

    private void getPagingLayout() {
        if (pagingLayout != null) {
            gridPagingLayout.removeComponent(pagingLayout);
        }
        int pageCount;
        boolean isNotSelectedCategories = (selectedCategories.length == 0);
        boolean isTopicFilter = !topic.isEmpty();
        if (isNotSelectedCategories && !isTopicFilter) {
            pageCount = getPageCount();
        } else {
            if (isTopicFilter) {
                pageCount = getPageCountTopicSearch(topic);
            } else {
                pageCount = getPageCountAfterCatFilter();
            }
        }
        if (pageCount > 1) {
            pagingLayout = new StubPagingBar(pageCount);

            ((Button) pagingLayout.getComponent(0)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    Integer page = (Integer) ((Button) pagingLayout.getComponent(0)).getData();
                    getData(isNotSelectedCategories, isTopicFilter, page);
                    pagingLayout.currentPageNumber = 1;
                }
            });
            ((Button) pagingLayout.getComponent(6)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    Integer page = (Integer) ((Button) pagingLayout.getComponent(6)).getData();
                    getData(isNotSelectedCategories, isTopicFilter, page);
                    pagingLayout.currentPageNumber = page;
                }
            });
            ((Button) pagingLayout.getComponent(1)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    --pagingLayout.currentPageNumber;
                    if (pagingLayout.currentPageNumber < 1) {
                        pagingLayout.currentPageNumber = pageCount;
                    }
                    getData(isNotSelectedCategories, isTopicFilter, pagingLayout.currentPageNumber);
                }
            });
            ((Button) pagingLayout.getComponent(5)).addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    ++pagingLayout.currentPageNumber;
                    if (pagingLayout.currentPageNumber > pageCount) {
                        pagingLayout.currentPageNumber = 1;
                    }
                    getData(isNotSelectedCategories, isTopicFilter, pagingLayout.currentPageNumber);
                }
            });

            ((TextField) pagingLayout.getComponent(3)).addShortcutListener(new ShortcutListener("Enter", ShortcutAction.KeyCode.ENTER, null) {
                @Override
                public void handleAction(Object o, Object o1) {
                    BinderValidationStatus<PageNumber> status = pagingLayout.pageNumberFieldBinder.validate();
                    if (!status.hasErrors()) {
                        pagingLayout.currentPageNumber = Integer.valueOf(((TextField) pagingLayout.getComponent(3)).getValue());
                        getData(isNotSelectedCategories, isTopicFilter, pagingLayout.currentPageNumber);
                    }
                }
            });
            gridPagingLayout.addComponent(pagingLayout);
        }
    }

    //Search
    private int getPageCountTopicSearch(String topic) {
        return CustomRestTemplate.getInstance().customGetForObject("/bulletinboard/pageCount/topicSearch/" + topic, Integer.class);
    }

    private void advertisementListTopicSearch(int pageNumber) {
        List<Advertisement> ads = Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/bulletinboard/topicSearch/" + pageNumber + "/" + topic, Advertisement[].class));
        grid.setItems(ads);
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
}
