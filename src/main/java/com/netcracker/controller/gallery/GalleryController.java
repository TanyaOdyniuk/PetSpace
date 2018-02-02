package com.netcracker.controller.gallery;

import com.netcracker.model.record.PhotoRecord;
import com.netcracker.model.user.Profile;
import com.netcracker.service.media.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/gallery")
public class GalleryController {
    @Autowired
    private MediaService mediaService;

    @GetMapping("/{id}")
    public List<PhotoRecord> getImagesGallery(@PathVariable("id") BigInteger albumId){
        return mediaService.getImagesGallery(albumId);
    }

    @GetMapping("/profileId/{id}")
    public Profile getUserProfileIdOfAlbumByPhotoRecord(@PathVariable("id") BigInteger recordId){
        return mediaService.getUserProfileIdOfAlbumByPhotoRecord(recordId);
    }

    @PostMapping("/{id}/add")
    public PhotoRecord createPhotoRecord(@RequestBody PhotoRecord photoRecord, @PathVariable("id") BigInteger albumId){
        return mediaService.createPhotoRecord(photoRecord, albumId);
    }
}
