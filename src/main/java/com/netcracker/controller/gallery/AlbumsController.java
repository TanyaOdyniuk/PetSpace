package com.netcracker.controller.gallery;

import com.netcracker.model.album.PhotoAlbum;
import com.netcracker.model.pet.Pet;
import com.netcracker.model.user.Profile;
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

    @GetMapping("/delete/{id}")
    public void deleteAlbum(@PathVariable("id") BigInteger albumId){
        mediaService.deleteAlbum(albumId);
    }

    @GetMapping("/profileId/{id}")
    public Profile getUserProfileIdOfAlbum(@PathVariable("id") BigInteger albumId){
        return mediaService.getUserProfileIdOfAlbum(albumId);
    }

    @GetMapping("/pet/{id}")
    public Pet getPetByAlbum(@PathVariable("id") BigInteger albumId){
        return mediaService.getPetByAlbum(albumId);
    }

    @GetMapping("/contains/{id}")
    public PhotoAlbum getAlbumByPhotoRecord(@PathVariable("id") BigInteger recordId){
        return mediaService.getAlbumByPhotoRecord(recordId);
    }
}
