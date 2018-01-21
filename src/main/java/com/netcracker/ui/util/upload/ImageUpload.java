package com.netcracker.ui.util.upload;

import com.netcracker.ui.util.UploadableComponent;
import com.vaadin.ui.Upload;

import java.math.BigInteger;

public class ImageUpload extends Upload {

    public ImageUpload(String path, BigInteger objectId, UploadableComponent calledComponent) {
        super();

        ImageReceiver receiver = new ImageReceiver(path, calledComponent, objectId, this);
        setReceiver(receiver);
        addSucceededListener(receiver);
        addFailedListener(receiver);
        addStartedListener(receiver);
        addProgressListener(receiver);
    }
}
