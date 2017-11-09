package com.netcracker.UI;

import com.netcracker.model.StubUser;
import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.Arrays;


@SpringComponent
@UIScope
public class StubUserEditor extends VerticalLayout {
    private StubUser stubUser;

    private TextField lastName = new TextField("Last name");
    private TextField firstName = new TextField("First name");

    private Button save = new Button("Save", VaadinIcons.SAFE);
    private Button delete = new Button("Delete", VaadinIcons.TRASH);
    private CssLayout actions = new CssLayout(save, delete);

    private Binder<StubUser> binder = new Binder<>(StubUser.class);

    @Autowired
    public StubUserEditor() {
        addComponents(firstName, lastName, actions);
        binder.bindInstanceFields(this);
        setSpacing(true);
        actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        save.addClickListener(e ->
        {
            stubUser.setFirstName(firstName.getValue());
            stubUser.setLastName(lastName.getValue());
            HttpEntity<StubUser> requestUpdate = new HttpEntity<>(stubUser);
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            StubConstants.REST_TEMPLATE
                    .exchange(StubConstants.RESOURCE_URL + '/' + stubUser.getId(), HttpMethod.PUT, requestUpdate, Void.class);
        });

        delete.addClickListener(e -> StubConstants.REST_TEMPLATE
                .delete(StubConstants.RESOURCE_URL + '/' + stubUser.getId()));
        setVisible(false);
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editUser(StubUser u) {
        if (u == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = u.getId() < StubUser.objectCount;
        if (persisted) {
            stubUser = StubConstants.REST_TEMPLATE
                    .getForObject(StubConstants.RESOURCE_URL + '/' + u.getId(), StubUser.class);
        } else {
            stubUser = u;
        }
        binder.setBean(stubUser);

        setVisible(true);

        save.focus();

        lastName.selectAll();
        firstName.selectAll();

    }

    public void setChangeHandler(ChangeHandler h) {
        save.addClickListener(e -> h.onChange());
        delete.addClickListener(e -> h.onChange());
    }
}
