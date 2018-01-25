package com.netcracker.ui.pet;

import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.PageElements;
import com.netcracker.ui.MainUI;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;

import java.math.BigInteger;

public class PetDeleteConfirmationWindow extends Window {

    private Boolean isDeleted;

    public PetDeleteConfirmationWindow(BigInteger petId, BigInteger ownerId) {
        super();
        this.isDeleted = false;
        setCaption("Confirm the action");
        setDraggable(false);
        setResizable(false);
        setModal(true);
        center();

        setWidth("300px");
        setHeight("150px");

        VerticalLayout mainLayout = new VerticalLayout();
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        mainLayout.setWidth("100%");
        buttonsLayout.setWidth("100%");

        Button yesButton = new Button("YES", VaadinIcons.CHECK);
        Button noButton = new Button("NO", VaadinIcons.CLOSE_BIG);

        yesButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                CustomRestTemplate.getInstance().customPostForObject("/pet/delete", petId, BigInteger.class);
                Notification.show("Your pet was successfully deleted!", Notification.Type.TRAY_NOTIFICATION);
                isDeleted = true;
                close();
            }
        });

        noButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                close();
            }
        });

        Label confirmText = PageElements.createStandartLabel("Are you sure to delete pet's page?");

        buttonsLayout.addComponents(yesButton, noButton);

        buttonsLayout.setComponentAlignment(yesButton, Alignment.MIDDLE_LEFT);
        buttonsLayout.setComponentAlignment(noButton, Alignment.MIDDLE_RIGHT);
        yesButton.setWidth("125px");
        noButton.setWidth("125px");

        mainLayout.addComponents(confirmText, buttonsLayout);
        mainLayout.setComponentAlignment(confirmText, Alignment.MIDDLE_CENTER);
        setContent(mainLayout);

        addCloseListener((CloseListener) e -> {
            if(isDeleted)
                ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new MyPetsListUI(ownerId));
        });
    }
}