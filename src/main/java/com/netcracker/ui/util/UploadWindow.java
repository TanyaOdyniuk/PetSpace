package com.netcracker.ui.util;

import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.netcracker.ui.pet.PetEditFormUI;

import java.io.*;

public class UploadWindow extends Window {

    private String path;
    private Window calledWindow;

    public UploadWindow(String path, Window calledWindow) {
        this.path = path;
        this.calledWindow = calledWindow;
        setCaption("Image upload");
        VerticalLayout mainLayout = new VerticalLayout();

        ImageReceiver receiver = new ImageReceiver();

        Upload upload = new Upload("Upload it here", receiver);
        upload.setButtonCaption("Start Upload");
        upload.addSucceededListener(receiver);

        long UPLOAD_LIMIT = 1024 * 1024; //1MB
        upload.addStartedListener((Upload.StartedListener) event -> {
            if (event.getContentLength() > UPLOAD_LIMIT) {
                Notification.show("Too big file",
                        Notification.Type.ERROR_MESSAGE);
                upload.interruptUpload();
            }
        });

        upload.addProgressListener((Upload.ProgressListener) (readBytes, contentLength) -> {
            if (readBytes > UPLOAD_LIMIT) {
                Notification.show("Too big file",
                        Notification.Type.ERROR_MESSAGE);
                upload.interruptUpload();
            }
        });

        mainLayout.addComponent(upload);

        File uploads = new File(path);
        if (!uploads.exists() && !uploads.mkdir())
            Notification.show("Could not create upload directory", Notification.Type.ERROR_MESSAGE);

        setContent(mainLayout);
    }

    class ImageReceiver implements Upload.Receiver, Upload.SucceededListener {

        private File file;

        public OutputStream receiveUpload(String filename, String mimeType) {
            FileOutputStream fos;
            try {
                file = new File(path + filename);
                fos = new FileOutputStream(file);
            } catch (final java.io.FileNotFoundException e) {
                new Notification("Could not open file<br/>",
                        e.getMessage(),
                        Notification.Type.ERROR_MESSAGE)
                        .show(Page.getCurrent());
                return null;
            }
            return fos;
        }

        public void uploadSucceeded(Upload.SucceededEvent event) {
            Notification.show("Upload completed!", Notification.Type.ASSISTIVE_NOTIFICATION);
            ((PetEditFormUI) calledWindow).updateAvatar(file);
            close();
        }
    }
}
