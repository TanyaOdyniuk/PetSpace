package com.netcracker.controller.gallery;

import com.netcracker.model.album.PhotoAlbum;
import com.netcracker.service.media.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
