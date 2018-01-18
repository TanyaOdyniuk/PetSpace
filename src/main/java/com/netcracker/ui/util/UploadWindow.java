package com.netcracker.ui.util;

import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamVariable;
import com.vaadin.ui.*;
import com.vaadin.ui.dnd.FileDropTarget;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;

public class UploadWindow extends Window {

    Html5File file;

    public UploadWindow() {
        super();

        setWidth("300px");
        setHeight("500px");

        final Label infoLabel = new Label("Drop image here");
        infoLabel.setWidth(240.0f, Unit.PIXELS);

        final VerticalLayout dropPane = new VerticalLayout(infoLabel);
        dropPane.setComponentAlignment(infoLabel, Alignment.MIDDLE_CENTER);
        dropPane.addStyleName("drop-area");
        //dropPane.setSizeUndefined();
        dropPane.setWidth("100%");
        dropPane.setHeight("500px");

        ProgressBar progress = new ProgressBar();
        progress.setIndeterminate(true);
        progress.setVisible(false);
        dropPane.addComponent(progress);

        new FileDropTarget<>(dropPane, fileDropEvent -> {
            final int fileSizeLimit = 2 * 1024 * 1024; // 2MB

            fileDropEvent.getFiles().forEach(html5File -> {
                file = html5File;
                final String fileName = html5File.getFileName();

                if (html5File.getFileSize() > fileSizeLimit) {
                    Notification.show(
                            "File rejected. Max 2MB files are accepted by Sampler",
                            Notification.Type.WARNING_MESSAGE);
                } else {
                    final ByteArrayOutputStream bas = new ByteArrayOutputStream();
                    final StreamVariable streamVariable = new StreamVariable() {

                        @Override
                        public OutputStream getOutputStream() {
                            return bas;
                        }

                        @Override
                        public boolean listenProgress() {
                            return false;
                        }

                        @Override
                        public void onProgress(
                                final StreamingProgressEvent event) {
                        }

                        @Override
                        public void streamingStarted(
                                final StreamingStartEvent event) {
                        }

                        @Override
                        public void streamingFinished(
                                final StreamingEndEvent event) {
                            progress.setVisible(false);
                            showFile(fileName, bas);
                        }

                        @Override
                        public void streamingFailed(
                                final StreamingErrorEvent event) {
                            progress.setVisible(false);
                        }

                        @Override
                        public boolean isInterrupted() {
                            return false;
                        }
                    };
                    html5File.setStreamVariable(streamVariable);
                    progress.setVisible(true);
                }
            });
        });
        setContent(dropPane);
    }

    private void showFile(final String name, final ByteArrayOutputStream bas) {
        // resource for serving the file contents
        final StreamResource.StreamSource streamSource = () -> {
            if (bas != null) {
                final byte[] byteArray = bas.toByteArray();
                return new ByteArrayInputStream(byteArray);
            }
            return null;
        };
        final StreamResource resource = new StreamResource(streamSource, name);

        // show the file contents - images only for now
        final Embedded embedded = new Embedded(name, resource);
        showComponent(embedded, name);
        String sRootPath = new File("").getAbsolutePath();

    }

    private void showComponent(final Component c, final String name) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setSizeUndefined();
        layout.setMargin(true);
        final Window w = new Window(name, layout);
        w.addStyleName("dropdisplaywindow");
        w.setSizeUndefined();
        w.setResizable(false);
        c.setSizeUndefined();
        layout.addComponent(c);
        UI.getCurrent().addWindow(w);
    }
}
