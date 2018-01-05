package com.netcracker.ui.bulletinboard;

import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.category.Category;
import com.netcracker.service.util.RestResponsePage;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.StubPagingBar;
import com.netcracker.ui.StubVaadinUI;
import com.netcracker.ui.util.CustomRestTemplate;
import com.netcracker.ui.util.VaadinValidationBinder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;
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
    private RestResponsePage<Advertisement> ads;
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
        gridPagingLayout.setWidth("100%");
        gridPagingLayout.addComponent(grid);
        advertisementList(1);
        getPagingLayout();
        if (pagingLayout != null) {
            gridPagingLayout.addComponent(pagingLayout);
        } else {
            grid.setWidth("100%");
            gridPagingLayout.setWidth("100%");
            gridPagingLayout.setHeight("100%");
        }
        getCategoryFilterLayout();

        mainLayout.addComponentsAndExpand(categoryFilterLayout, gridPagingLayout);
        mainLayout.setExpandRatio(mainLayout.getComponent(0), 4.0f);
        mainLayout.setExpandRatio(mainLayout.getComponent(1), 13.0f);
        addComponent(mainLayout);
    }

    private void getGrid() {
        grid = new Grid<>();
        grid.setWidth("100%");
        Grid.Column topicColumn = grid.addColumn(ad ->
                ad.getAdTopic()).setCaption("Topic").setWidth(150).setSortable(false);
        Grid.Column authorColumn = grid.addColumn(ad ->
                ad.getAdAuthor().getProfileSurname() + " " +
                        ad.getAdAuthor().getProfileName()).setCaption("Author").setWidth(120).setSortable(false);
        Grid.Column informationColumn = grid.addColumn(ad ->
                ad.getAdBasicInfo()).setCaption("Basic Info").setWidth(200).setSortable(false);
        Grid.Column dateColumn = grid.addColumn(ad ->
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ad.getAdDate())).setWidth(130).setCaption("Date");
        Grid.Column statusColumn = grid.addColumn(ad ->
                (ad.isAdIsVip() ? "yes" : "no")).setCaption("VIP").setWidth(65).setSortable(false);
        Grid.Column categoryColumn = grid.addColumn(ad ->
                (ad.getAdCategory() != null ? ad.getAdCategory().getCategoryName() : "")).setCaption("Category").setWidth(130).setSortable(false);
        grid.addItemClickListener(event -> ((StubVaadinUI) UI.getCurrent()).changePrimaryAreaLayout(new AdvertisementView(event.getItem())));
    }

    private void getCategoryFilterLayout() {
        Panel filterPanel = new Panel();
        filterPanel.setHeight("100px");
        categoryFilterLayout = new VerticalLayout();
        HorizontalLayout filterButtonLayout = new HorizontalLayout();
        HorizontalLayout selDeselButtonsLayout = new HorizontalLayout();
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
        categoryFilterLayout.addComponent(PageElements.getSeparator());
        topicField.setCaption("Topic filter");
        topicField.setWidth("100%");
        topicField.setPlaceholder("Type topic here");
        Button filterButton = new Button("Filter", VaadinIcons.SEARCH);
        filterButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                if (!topicField.isEmpty()) {
                    topic = topicField.getValue();
                    if (categoryFilter.getSelectedItems().size() > 0) {
                        selectedCategories = new Category[categoryFilter.getSelectedItems().size()];
                        categoryFilter.getSelectedItems().toArray(selectedCategories);
                    }
                    advertisementList(1, topic, selectedCategories);
                    getPagingLayout();
                } else {
                    topic = "";
                    if (categoryFilter.getSelectedItems().size() > 0) {
                        selectedCategories = new Category[categoryFilter.getSelectedItems().size()];
                        categoryFilter.getSelectedItems().toArray(selectedCategories);
                    }
                    advertisementList(1, topic, selectedCategories);
                    getPagingLayout();
                }
            }
        });

        categoryFilterLayout.addComponent(topicField);
        categoryFilterLayout.addComponent(PageElements.getSeparator());
        filterButtonLayout.addComponent(filterButton);
        Button dropFiltersButton = new Button("Drop filters", VaadinIcons.TRASH);
        dropFiltersButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                deselectAll.click();
                topicField.clear();
                topic = "";
                advertisementList(1);
                getPagingLayout();
            }
        });
        filterButtonLayout.addComponent(dropFiltersButton);
        categoryFilterLayout.addComponent(filterButtonLayout);
    }

    private List<Category> getAllCategories() {
        return Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/category", Category[].class));
    }

    private void advertisementList(int pageNumber) {
        ResponseEntity<RestResponsePage<Advertisement>> pageResponseEntity =
                CustomRestTemplate.getInstance().customExchangeForParametrizedTypes("/bulletinboard/"+ pageNumber, HttpMethod.GET,
                        null, new ParameterizedTypeReference<RestResponsePage<Advertisement>>(){});
        ads = pageResponseEntity.getBody();
        List<Advertisement> advertisements = ads.getContent();
        if (advertisements.isEmpty()) {
            Notification.show("No ads were found");
            gridPagingLayout.removeComponent(grid);
        } else{
            if(gridPagingLayout.getComponentCount() == 0){
                gridPagingLayout.addComponent(grid);
            }
            grid.setItems(advertisements);
            int height = advertisements.size() * 100;
            grid.setHeight("" + height + "px");
        }
    }

    private void advertisementList(int pageNumber, String topic, Category[] categories) {
        if (topic.isEmpty()) {
            topic = "empty";
        }
        HttpEntity<Category[]> createRequest = new HttpEntity<>(categories);
        ResponseEntity<RestResponsePage<Advertisement>> pageResponseEntity =
                CustomRestTemplate.getInstance().customExchangeForParametrizedTypes("/bulletinboard/categorytopic/" + topic + '/' + pageNumber, HttpMethod.POST,
                createRequest, new ParameterizedTypeReference<RestResponsePage<Advertisement>>(){});
        ads = pageResponseEntity.getBody();
        List<Advertisement> advertisements = ads.getContent();
        if (advertisements.isEmpty()) {
            Notification.show("No ads with the specified filters were found");
            gridPagingLayout.removeComponent(grid);
        } else{
            if(gridPagingLayout.getComponentCount() == 0){
                gridPagingLayout.addComponent(grid);
            }
            grid.setItems(advertisements);
            int height = advertisements.size() * 100;
            grid.setHeight("" + height + "px");
        }
    }

    private void getData(boolean isNotSelectedCategories, boolean isTopicFilter, int page) {
        if (isNotSelectedCategories && !isTopicFilter) {
            advertisementList(page);
        } else {
            advertisementList(page, topic, selectedCategories);
        }
        ((TextField) pagingLayout.getComponent(3)).setValue(String.valueOf(page));
    }

    private void getPagingLayout() {
        if (pagingLayout != null) {
            gridPagingLayout.removeComponent(pagingLayout);
        }
        int pageCount = (int) ads.getTotalElements();
        boolean isNotSelectedCategories = (selectedCategories.length == 0);
        boolean isTopicFilter = !topic.isEmpty();
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
                    BinderValidationStatus<VaadinValidationBinder> status = pagingLayout.pageNumberFieldBinder.validate();
                    if (!status.hasErrors()) {
                        pagingLayout.currentPageNumber = Integer.valueOf(((TextField) pagingLayout.getComponent(3)).getValue());
                        getData(isNotSelectedCategories, isTopicFilter, pagingLayout.currentPageNumber);
                    }
                }
            });
            gridPagingLayout.addComponent(pagingLayout);
        }
    }
}
