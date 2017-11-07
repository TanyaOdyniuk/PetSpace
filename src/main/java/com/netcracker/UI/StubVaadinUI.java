package com.netcracker.UI;

import com.netcracker.model.StubUser;
import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Odyniuk on 06/11/2017.
 */

@SpringUI
@Theme("valo")
public class StubVaadinUI extends UI {

    private final StubUserEditor stubUserEditor;

    private final Grid<StubUser> grid;

    private final Button addNewBtn;

    @Autowired
    public StubVaadinUI(StubUserEditor stubUserEditor) {
        this.stubUserEditor= stubUserEditor;
        this.grid = new Grid<>(StubUser.class);
        this.addNewBtn = new Button("New user", VaadinIcons.PLUS);
    }

    @Override
    protected void init(VaadinRequest request) {
        HorizontalLayout actions = new HorizontalLayout(addNewBtn);
        VerticalLayout mainLayout = new VerticalLayout(actions, grid, stubUserEditor);
        setContent(mainLayout);

        grid.setHeight(300, Unit.PIXELS);
        grid.setColumns("id", "firstName", "lastName");
        addNewBtn.addClickListener(clickEvent -> stubUserEditor.editUser(new StubUser(++StubUser.objectCount, "", "")));

        grid.asSingleSelect().addValueChangeListener(e -> stubUserEditor.editUser(e.getValue()));

        stubUserEditor.setChangeHandler(() -> {
            stubUserEditor.setVisible(false);
            listCustomers();
        });

        listCustomers();
    }

    private void listCustomers() {
        List<StubUser> users = Arrays.asList(StubConstants.REST_TEMPLATE.getForObject(StubConstants.RESOURCE_URL, StubUser[].class));
        grid.setItems(users);
    }

}