package com.netcracker.controller.gallery;

import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.model.record.PhotoRecord;
import com.netcracker.service.media.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/gallery")
public class GalleryController {
    @Autowired
    MediaService mediaService;
//    @GetMapping("/{id}")
//    public PhotoAlbum getAlbum(@PathVariable("id") BigInteger albumId){
//        return mediaService.getAlbum(albumId);
//    }

    @GetMapping("/{id}")
    public List<PhotoRecord> getImagesGalary(@PathVariable("id") BigInteger albumId){
        QueryDescriptor queryDescriptor = new QueryDescriptor();
        return mediaService.getImagesGallery(albumId);
    }
}
