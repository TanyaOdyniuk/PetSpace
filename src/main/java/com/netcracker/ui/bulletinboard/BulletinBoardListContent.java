package com.netcracker.ui.bulletinboard;

import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.category.Category;
import com.netcracker.service.util.RestResponsePage;
import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.PagingBar;
import com.netcracker.ui.MainUI;
import com.netcracker.ui.util.CustomRestTemplate;
import com.netcracker.ui.util.VaadinValidationBinder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Sizeable;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
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
    private Grid<Advertisement> grid;
    private PagingBar pagingLayout;
    private final HorizontalLayout mainLayout;
    private VerticalLayout categoryFilterLayout;
    private VerticalLayout rightPartLayout;
    private CheckBoxGroup<Category> categoryFilter;
    private Category[] selectedCategories;
    private String topic = "";
    private TextField topicField;
    private RestResponsePage<Advertisement> ads;
    private int browserHeight;
    private int browserWidth;
    private Panel gridPanel;
    private Panel pagingPanel;
    public BulletinBoardListContent() {
        super();
        topicField = new TextField();
        selectedCategories = new Category[0];
        gridPanel = new Panel();
        mainLayout = new HorizontalLayout();

        browserHeight = UI.getCurrent().getPage().getBrowserWindowHeight();
        browserWidth = UI.getCurrent().getPage().getBrowserWindowWidth();
        mainLayout.setHeight(browserHeight - 250, Sizeable.Unit.PIXELS);
        mainLayout.setWidth(browserWidth - 450, Sizeable.Unit.PIXELS);
        Panel leftPartPanel = new Panel();
        leftPartPanel.setHeight("100%");
        leftPartPanel.setWidth(300, Sizeable.Unit.PIXELS);
        Panel rightPartPanel = new Panel();
        rightPartPanel.setHeight("100%");

        VerticalLayout leftPartLayout = new VerticalLayout();
        leftPartLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        rightPartLayout = new VerticalLayout();
        rightPartLayout.setSpacing(true);
        pagingPanel = new Panel();

        //Elements for left part
        //Filter
        getCategoryFilterLayout();
        Panel categoryFilterPanel = new Panel();
        categoryFilterPanel.setContent(categoryFilterLayout);

        getGrid();
        advertisementList(1);
        pagingPanel.setWidth("100%");
        getPagingLayout(pagingPanel);

        leftPartLayout.addComponents(PageElements.getSeparator(), categoryFilterPanel, PageElements.getSeparator());

        rightPartLayout.addComponents(pagingPanel, gridPanel);
        leftPartPanel.setContent(leftPartLayout);
        rightPartPanel.setContent(rightPartLayout);
        mainLayout.addComponent(leftPartPanel);
        mainLayout.addComponentsAndExpand(rightPartPanel);
        addComponents(mainLayout);
    }
    private List<Category> getAllCategories() {
        return Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/category", Category[].class));
    }

    private void getCategoryFilterLayout() {
        Panel subFilterPanel = new Panel();
        Panel categoryPanel = new Panel();
        VerticalLayout subLayout = new VerticalLayout();
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
        deselectAll.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                categoryFilter.deselectAll();
                selectedCategories = new Category[0];
            }
        });

        selDeselButtonsLayout.addComponents(selectAll, deselectAll);
        categoryPanel.setContent(categoryFilter);
        categoryPanel.setHeight("100px");
        subLayout.addComponents(selDeselButtonsLayout, categoryPanel);
        subFilterPanel.setContent(subLayout);
        Panel topicPanel = new Panel();
        topicField.setPlaceholder("Type topic here");
        topicField.setSizeFull();
        topicPanel.setContent(topicField);
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
                    getPagingLayout(pagingPanel);
                } else {
                    topic = "";
                    if (categoryFilter.getSelectedItems().size() > 0) {
                        selectedCategories = new Category[categoryFilter.getSelectedItems().size()];
                        categoryFilter.getSelectedItems().toArray(selectedCategories);
                    }
                    advertisementList(1, topic, selectedCategories);
                    getPagingLayout(pagingPanel);
                }
            }
        });

        Button dropFiltersButton = new Button("Drop filters", VaadinIcons.TRASH);
        dropFiltersButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                deselectAll.click();
                topicField.clear();
                topic = "";
                advertisementList(1);
                getPagingLayout(pagingPanel);
            }
        });
        filterButtonLayout.addComponents(filterButton, dropFiltersButton);
        categoryFilterLayout.addComponents(subFilterPanel, topicPanel, filterButtonLayout);
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
        /*Grid.Column statusColumn = grid.addColumn(ad ->
                (ad.isAdIsVip() ? "yes" : "no")).setCaption("VIP").setWidth(65).setSortable(false);*/
        Grid.Column categoryColumn = grid.addColumn(ad ->
                (ad.getAdCategory() != null ? ad.getAdCategory().getCategoryName() : "")).setCaption("Category").setWidth(130).setSortable(false);
        grid.addItemClickListener(event -> ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new AdvertisementView(event.getItem())));
    }


    private void advertisementList(int pageNumber) {
        ResponseEntity<RestResponsePage<Advertisement>> pageResponseEntity =
                CustomRestTemplate.getInstance().customExchangeForParametrizedTypes("/bulletinboard/"+ pageNumber, HttpMethod.GET,
                        null, new ParameterizedTypeReference<RestResponsePage<Advertisement>>(){});
        ads = pageResponseEntity.getBody();
        List<Advertisement> advertisements = ads.getContent();
        if (advertisements.isEmpty()) {
            gridPanel.setContent(new Label("Unfortunately, no ads were found"));
        } else {
            grid.setHeightByRows(advertisements.size());
            grid.setItems(advertisements);
            gridPanel.setContent(grid);
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
            gridPanel.setContent(new Label("Unfortunately, no ads with these filters were found"));
        } else {
            grid.setHeightByRows(advertisements.size());
            grid.setItems(advertisements);
            gridPanel.setContent(grid);
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

    private void getPagingLayout(Panel pagingPanel) {
        if (pagingLayout != null) {
            rightPartLayout.removeComponent(pagingPanel);
        }
        int pageCount = (int) ads.getTotalElements();
        boolean isNotSelectedCategories = (selectedCategories.length == 0);
        boolean isTopicFilter = !topic.isEmpty();
        if (pageCount > 1) {
            pagingLayout = new PagingBar(pageCount, 1);

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
            pagingPanel.setContent(pagingLayout);
            rightPartLayout.addComponent(pagingPanel, 0);
        }
    }
}
