package com.netcracker.controller.gallery;

import com.netcracker.model.album.PhotoAlbum;
import com.netcracker.service.media.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/albums")
public class AlbumsController {
    @Autowired
    MediaService mediaService;

    @GetMapping("/{id}")
    public List<PhotoAlbum> getAlbum(@PathVariable("id") BigInteger petId){
        return mediaService.getMyAlbums(petId,false,null, null);
    }

    @PostMapping("/{id}/add")
    public PhotoAlbum createNewPet(@RequestBody PhotoAlbum album, @PathVariable("id") BigInteger petId){
        return mediaService.createAlbum(album, petId);
    }
}
