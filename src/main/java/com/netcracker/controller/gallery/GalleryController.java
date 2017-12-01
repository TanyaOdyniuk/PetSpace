package com.netcracker.controller.gallery;

import com.netcracker.model.record.PhotoRecord;
import com.netcracker.service.media.impl.MediaServiceImpl;
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
    MediaServiceImpl mediaService;


    @GetMapping("/{id}")
    public List<PhotoRecord> getImagesGalary(@PathVariable("id") BigInteger albumId){
        return mediaService.getImagesGalary(albumId);
    }
}
