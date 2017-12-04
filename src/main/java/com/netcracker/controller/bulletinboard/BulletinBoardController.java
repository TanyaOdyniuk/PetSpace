package com.netcracker.controller.bulletinboard;

import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.service.bulletinboard.BulletinBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/bulletinboard")
public class BulletinBoardController {
    @Autowired
    BulletinBoardService bulletinBoardService;

    @GetMapping
    public List<Advertisement> getProfileAds() {
        return bulletinBoardService.getProfileAds();
    }

    @GetMapping("/{id}")
    public List<Advertisement> getMyProfileAds(@PathVariable("id") BigInteger profileId) {
        return bulletinBoardService.getMyProfileAds(profileId);
    }
}
