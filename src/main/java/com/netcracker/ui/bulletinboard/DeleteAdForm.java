package com.netcracker.ui.bulletinboard;

import com.netcracker.ui.AbstractClickListener;
import com.netcracker.ui.MainUI;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;

import java.math.BigInteger;

class DeleteAdForm extends Window {
    private BigInteger profileId;
    private BigInteger adId;
    DeleteAdForm(BigInteger profileId, BigInteger adId) {
        super();
        setModal(true);
        setResizable(false);
        setClosable(false);
        this.profileId = profileId;
        this.adId = adId;
        VerticalLayout subContent = new VerticalLayout();
        setContent(subContent);
        subContent.addComponent(getWarningMessage());
        subContent.addComponent(getButtonLayout());
        center();
    }
    private void wClose(){
        this.close();
    }
    private Label getWarningMessage(){
        return new Label("Are you sure?");
    }
    private Button getOkButton(){
        Button okButton = new Button("Yes", VaadinIcons.CHECK);
        okButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                CustomRestTemplate.getInstance().customGetForObject("/bulletinboard/delete/" + adId, Void.class);
                wClose();
                Notification.show("Advertisement was deleted!");
                MainUI currentUI = (MainUI) UI.getCurrent();
                currentUI.changePrimaryAreaLayout(new MyBulletinBoardListContent(profileId));
            }
        });
        return okButton;
    }
    private Button getCancelButton(){
        Button cancelButton = new Button("No", VaadinIcons.CLOSE);
        cancelButton.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                wClose();
            }
        });
        return cancelButton;
    }
    private HorizontalLayout getButtonLayout(){
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addComponent(getOkButton());
        buttonLayout.addComponent(getCancelButton());
        return buttonLayout;
    }
}
