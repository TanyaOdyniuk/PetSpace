package com.netcracker.ui.util.upload;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Upload;

public class ImageUpload extends Upload {

    public ImageUpload(String path, AbstractComponent calledComponent) {
        super();

        ImageReceiver receiver = new ImageReceiver(path, calledComponent, this);
        setReceiver(receiver);
        addSucceededListener(receiver);
        addFailedListener(receiver);
        addStartedListener(receiver);
        addProgressListener(receiver);
    }
}
