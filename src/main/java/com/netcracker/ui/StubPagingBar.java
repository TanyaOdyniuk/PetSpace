package com.netcracker.ui;

import com.netcracker.ui.util.VaadinValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;

public class StubPagingBar extends HorizontalLayout {
    private final Label pageLabel;
    private final TextField pageNumberField;
    private final Label pageMaxLabel;
    private final Button firstPageButton;
    private final Button lastPageButton;
    private final Button nextPageButton;
    private final Button prevPageButton;
    private final int minPageSize;
    private final int maxPageSize;
    public int currentPageNumber;
    public Binder<VaadinValidationBinder> pageNumberFieldBinder;

    public StubPagingBar(int maxPageSize, int curPageNumb) {
        minPageSize = 1;
        currentPageNumber = curPageNumb;
        this.maxPageSize = maxPageSize;
        firstPageButton = PageElements.createBlueClickedLabel("", VaadinIcons.FAST_BACKWARD);
        firstPageButton.setData(minPageSize);
        lastPageButton = PageElements.createBlueClickedLabel("", VaadinIcons.FAST_FORWARD);
        lastPageButton.setData(this.maxPageSize);
        nextPageButton = PageElements.createBlueClickedLabel("", VaadinIcons.FORWARD);
        prevPageButton = PageElements.createBlueClickedLabel("", VaadinIcons.BACKWARDS);
        pageLabel = new Label("Page: ");
        pageLabel.setHeight(String.valueOf(firstPageButton.getHeight()));
        pageNumberField = new TextField();
        pageNumberField.setValue(String.valueOf(currentPageNumber));
        pageNumberField.setHeight(70.0F, Unit.PERCENTAGE);
        getBinder();
        pageMaxLabel = new Label("/ " + this.maxPageSize);
        addComponent(firstPageButton, 0);
        addComponent(prevPageButton, 1);
        addComponent(pageLabel, 2);
        addComponent(pageNumberField, 3);
        addComponent(pageMaxLabel, 4);
        addComponent(nextPageButton, 5);
        addComponent(lastPageButton, 6);
    }

    public void setBorderButtonsState(boolean isFirstPage){
        firstPageButton.setEnabled(!isFirstPage);
        prevPageButton.setEnabled(!isFirstPage);
        nextPageButton.setEnabled(isFirstPage);
        lastPageButton.setEnabled(isFirstPage);
    }

    public void setAllButtonsStateEnabled(){
        firstPageButton.setEnabled(true);
        prevPageButton.setEnabled(true);
        nextPageButton.setEnabled(true);
        lastPageButton.setEnabled(true);
    }

    public Button getFirstPageButton() {
        return firstPageButton;
    }

    public Button getLastPageButton() {
        return lastPageButton;
    }

    public Button getNextPageButton() {
        return nextPageButton;
    }

    public Button getPrevPageButton() {
        return prevPageButton;
    }

    public TextField getPageNumberField() {
        return pageNumberField;
    }

    private void getBinder(){
        pageNumberFieldBinder = new Binder<>();
        pageNumberFieldBinder.forField(pageNumberField)
                .withConverter(new StringToIntegerConverter("Must be integer value"))
                .withValidator(new IntegerRangeValidator("Not valid page number", minPageSize, maxPageSize))
                .bind(VaadinValidationBinder::getPageNumber, VaadinValidationBinder::setPageNumber);
    }
}
