package com.netcracker.controller.gallery;

import com.netcracker.model.album.PhotoAlbum;
import com.netcracker.model.record.PhotoRecord;
import com.netcracker.service.media.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/albums")
public class AlbumsController {
    @Autowired
    private MediaService mediaService;

    @GetMapping("/{id}")
    public List<PhotoAlbum> getMyAlbums(@PathVariable("id") BigInteger profileId){
        return mediaService.getMyAlbums(profileId);
    }

    @PostMapping("/{id}/add")
    public PhotoAlbum createNewAlbum(@RequestBody PhotoAlbum album, @PathVariable("id") BigInteger petId){
        return mediaService.createAlbum(album, petId);
    }

    @GetMapping("/lastPhotos")
    public List<PhotoRecord> getLastPhotos(){
        return mediaService.getLastPhotos();
    }
}
