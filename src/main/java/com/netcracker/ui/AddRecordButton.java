package com.netcracker.ui;

import com.netcracker.model.BaseEntity;
import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.album.PhotoAlbum;
import com.netcracker.model.group.Group;
import com.netcracker.model.record.AbstractRecord;
import com.netcracker.model.record.GroupRecord;
import com.netcracker.model.record.WallRecord;
import com.netcracker.model.user.Profile;
import com.netcracker.ui.gallery.GalleryUI;
import com.netcracker.ui.groups.GroupUI;
import com.netcracker.ui.profile.ProfileView;
import com.netcracker.ui.util.CustomRestTemplate;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.Date;

//Button for creating/updating records, only for wall and group records
public class AddRecordButton extends Button {

    private Window updateWallRecordWindow;
    private Profile currentProfile;
    private BigInteger currentProfileId;
    private final BaseEntity reloadTo;

    public AddRecordButton(BaseEntity recordReceiver) {
        super("Add new record", VaadinIcons.PLUS);
        setCurrentProfile();
        this.reloadTo = recordReceiver;
        this.addClickListener(new AbstractClickListener() {
            @Override
            public void buttonClickListener() {
                editRecord(null, recordReceiver);
                UI.getCurrent().addWindow(updateWallRecordWindow);
            }
        });
    }

    private void editRecord(AbstractRecord currentRecord, BaseEntity recordType) {
        updateWallRecordWindow = new Window();
        updateWallRecordWindow.setWidth("400px");
        updateWallRecordWindow.setHeight("250px");
        updateWallRecordWindow.setModal(true);

        VerticalLayout windowContent = new VerticalLayout();
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        TextArea textArea = new TextArea();
        textArea.setWidth("100%");
        Timestamp date = Timestamp.valueOf(
                new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

        if(currentRecord == null) {
            updateWallRecordWindow.setCaption("New record");
            Button add = new Button("Add");
            add.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    add.setComponentError(null);
                    Class c = recordType.getClass();
                    if (Profile.class.equals(c)) {
                        Profile receiver = (Profile) recordType;
                        WallRecord record = new WallRecord("WallRecord",
                                "From " + currentProfile.getProfileSurname() + " to " + receiver.getProfileSurname());
                        record.setRecordDate(date);
                        record.setRecordText(textArea.getValue());
                        record.setRecordAuthor(currentProfile);
                        record.setWallOwner(receiver);
                        createWallRecord(record);
                    } else if (Group.class.equals(c)){
                        Group receiver = (Group) recordType;
                        GroupRecord record = new GroupRecord("GroupRecord",
                                "From " + currentProfile.getProfileSurname() + " to " + receiver.getGroupName());
                        record.setRecordDate(date);
                        record.setRecordText(textArea.getValue());
                        record.setRecordAuthor(currentProfile);
                        record.setParentGroup(receiver);
                        createGroupRecord(record);
                    }

                    Notification.show("Record added!");
                    updateWallRecordWindow.close();
                    reloadPage(reloadTo);
                }
            });
            buttonsLayout.addComponentsAndExpand(add);
        } else {
            updateWallRecordWindow.setCaption("Edit record");
            textArea.setValue(currentRecord.getRecordText());
            Button edit = new Button("Edit");
            edit.addClickListener(new AbstractClickListener() {
                @Override
                public void buttonClickListener() {
                    edit.setComponentError(null);
                    currentRecord.setRecordText(textArea.getValue());
                    if (currentRecord.getClass().equals(WallRecord.class))
                        updateWallRecord((WallRecord) currentRecord);
                    else if (currentRecord.getClass().equals(GroupRecord.class))
                        updateGroupRecord((GroupRecord) currentRecord);
                    Notification.show("Record edited!");
                    updateWallRecordWindow.close();
                    reloadPage(reloadTo);                }
            });
            buttonsLayout.addComponentsAndExpand(edit);
        }
        Button cancel = new Button("Cancel", click -> updateWallRecordWindow.close());
        buttonsLayout.addComponentsAndExpand(cancel);
        windowContent.addComponents(textArea, buttonsLayout);

        updateWallRecordWindow.setContent(windowContent);
        updateWallRecordWindow.center();
    }

    private void createWallRecord(WallRecord record) {
        HttpEntity<WallRecord> request = new HttpEntity<>(record);
        CustomRestTemplate.getInstance().customPostForObject("/records/wall/add", request, WallRecord.class);
    }

    private void createGroupRecord(GroupRecord record) {
        HttpEntity<GroupRecord> request = new HttpEntity<>(record);
        CustomRestTemplate.getInstance().customPostForObject("/records/group/add", request, GroupRecord.class);
    }

    private void updateWallRecord(WallRecord record) {
        HttpEntity<WallRecord> request = new HttpEntity<>(record);
        CustomRestTemplate.getInstance().customPostForObject("/records/wall/update", request, WallRecord.class);
    }

    private void updateGroupRecord(GroupRecord record) {
        HttpEntity<GroupRecord> request = new HttpEntity<>(record);
        CustomRestTemplate.getInstance().customPostForObject("/records/group/update", request, GroupRecord.class);
    }

    private void reloadPage(BaseEntity reloadTo){
        BigInteger destinationID = reloadTo.getObjectId();
        Class c = reloadTo.getClass();
        if(Profile.class.equals(c)) {
            ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(destinationID));
        } else if(Group.class.equals(c)) {
            ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new GroupUI(destinationID));
        } else if(PhotoAlbum.class.equals(c)) {
            ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new GalleryUI(destinationID));
        } else if(Advertisement.class.equals(c)) {
            ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(destinationID));
        } else if(Profile.class.equals(c)) {
            ((MainUI) UI.getCurrent()).changePrimaryAreaLayout(new ProfileView(destinationID)); //NewsFeed reload
        }
    }

    private void setCurrentProfile() {
        SecurityContext o = (SecurityContext) VaadinSession.getCurrent().getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        String login = o.getAuthentication().getPrincipal().toString();
        BigInteger currentProfileId = CustomRestTemplate.getInstance().customPostForObject("/user/profileId", login, BigInteger.class);
        currentProfile = CustomRestTemplate.getInstance().
                customGetForObject("/profile/" + currentProfileId, Profile.class);
        this.currentProfileId = currentProfile.getObjectId();
    }
}
