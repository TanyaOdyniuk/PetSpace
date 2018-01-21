package com.netcracker.ui.util.upload;

import com.netcracker.ui.pet.PetEditFormUI;

import java.io.File;

/**
 * Necessary for correct {@link ImageReceiver} behavior.
 */
public interface UploadableComponent{

    /**
     * Should contain logic for updating the UI's Image source.
     * See example at {@link PetEditFormUI} 161-166 str.
     *
     * @param imageFile File, which contains uploaded image.
     */
    void updateImage(File imageFile);
}
