package com.netcracker.ui.profile;

import com.netcracker.model.user.Profile;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

@SpringComponent
@UIScope
public class ProfileView extends VerticalLayout {
    private final Grid<Profile> grid;
    private final VerticalLayout innerLayout;
    private final Button newAdBtn;
    private final BigInteger profileId;
    @Autowired
    public ProfileView(BigInteger profileId) {
        super();
        setSpacing(true);
        setWidth("100%");
        this.profileId = profileId;
        newAdBtn = new Button("Test", VaadinIcons.VIMEO);
        newAdBtn.setHeight(30, Unit.PIXELS);
        this.grid = new Grid<>();
        grid.setHeight(300, Unit.PIXELS);
        grid.setSizeFull();
        grid.addColumn(Profile::getProfileAvatar).setCaption("Avatar");
        grid.addColumn(Profile::getProfileName).setCaption("Name");
        grid.addColumn(Profile::getProfileSurname).setCaption("Surname");
        grid.addColumn(Profile::getProfileAge).setCaption("Age");
        grid.addColumn(Profile::getProfileCurrencyBalance).setCaption("Currency");
        grid.addColumn(Profile::getProfileStatus).setCaption("State");
/*        Grid.Column categoryColumn = grid.addColumn(ad ->
                (ad.getAdCategory() != null ? ad.getAdCategory().getCategoryName() : "")).setCaption("Category");*/
        profileList();
        innerLayout = new VerticalLayout();
        innerLayout.addComponentsAndExpand(newAdBtn, grid);
        innerLayout.setExpandRatio(innerLayout.getComponent(0), 2.0f);
        innerLayout.setExpandRatio(innerLayout.getComponent(1), 15.0f);
        addComponentsAndExpand(innerLayout);
    }

    private void profileList() {
        List<Profile> profiles = Arrays.asList(
                CustomRestTemplate.getInstance().customGetForObject(
                        "/profile/"+ profileId, Profile[].class));
        grid.setItems(profiles);
    }
}
