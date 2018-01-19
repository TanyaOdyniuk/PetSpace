package com.netcracker.ui.util.upload;

import com.netcracker.ui.pet.PetEditFormUI;
import com.vaadin.server.Page;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ImageReceiver implements Upload.Receiver, Upload.SucceededListener, Upload.FailedListener, Upload.StartedListener, Upload.ProgressListener {

    private File file;
    private String path;
    private AbstractComponent calledComponent;
    private Upload upload;
    private long UPLOAD_LIMIT;
    private List<String> allowedMimeTypes;

    public ImageReceiver(String path, AbstractComponent calledComponent, Upload upload) {
        this.path = path;
        this.calledComponent = calledComponent;
        this.upload = upload;
        this.UPLOAD_LIMIT = 1024 * 1024 /*1MB*/;
        this.allowedMimeTypes = generateAllowedTypes();
    }

    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {
        FileOutputStream fos;
        try {
            file = new File(path + filename);
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            new Notification("Could not open file!",
                    e.getMessage(),
                    Notification.Type.ERROR_MESSAGE)
                    .show(Page.getCurrent());
            return null;
        }
        return fos;
    }

    @Override
    public void uploadSucceeded(Upload.SucceededEvent event) {
        Notification.show("Upload completed!", Notification.Type.ASSISTIVE_NOTIFICATION);
        ((PetEditFormUI) calledComponent).updateAvatar(file);
    }

    @Override
    public void uploadFailed(Upload.FailedEvent event) {
        try{
            file.delete();
        } catch (Exception e) {
            // Silent exception. If we can't delete the file, it's not big problem. May the file did not even exist.
        }
    }

    @Override
    public void updateProgress(long readBytes, long contentLength) {
        if (readBytes > UPLOAD_LIMIT) {
            Notification.show("Upload error!", "Too big file. Please, choose file with size < 1MB.",
                    Notification.Type.ERROR_MESSAGE);
            upload.interruptUpload();
        }
    }

    @Override
    public void uploadStarted(Upload.StartedEvent event) {
        boolean allowed = false;
        for (String mimeType : allowedMimeTypes) {
            if (mimeType.equalsIgnoreCase(event.getMIMEType())) {
                allowed = true;
            }
        }
        if (!allowed) {
            Notification.show("Upload error!", "Format doesn't supported! Please, choose\n file with .jpg, .png or .bmp extension.", Notification.Type.ERROR_MESSAGE);
            upload.interruptUpload();
            return;
        }
        if (event.getContentLength() > UPLOAD_LIMIT) {
            Notification.show("Upload error!", "Too big file. Please, choose file with size < 1MB.",
                    Notification.Type.ERROR_MESSAGE);
            upload.interruptUpload();
        }
    }

    private List<String> generateAllowedTypes(){
        List<String> allowedMimeTypes = new ArrayList<>();
        allowedMimeTypes.add("image/jpeg");
        allowedMimeTypes.add("image/png");
        allowedMimeTypes.add("image/bmp");
        return allowedMimeTypes;
    }
}
