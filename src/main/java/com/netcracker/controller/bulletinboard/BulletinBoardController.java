package com.netcracker.controller.bulletinboard;

import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.service.bulletinboard.BulletinBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @GetMapping("/pageCount")
    public Integer getBulletinBoardPageCount() {
        return bulletinBoardService.getAllAdPageCount();
    }

    @GetMapping("/pageCount/{id}")
    public Integer getBulletinBoardPageCount(@PathVariable("id") BigInteger profileId) {
        return bulletinBoardService.getMyProfileAdPageCount(profileId);
    }

    @GetMapping("/{pageNumber}")
    public List<Advertisement> getProfileAds(@PathVariable("pageNumber") Integer pageNumber) {
        return bulletinBoardService.getProfileAds(pageNumber);
    }

    @GetMapping("/myAds/{id}/{pageNumber}")
    public List<Advertisement> getMyProfileAds(@PathVariable("id") BigInteger profileId, @PathVariable("pageNumber") Integer pageNumber) {
        return bulletinBoardService.getMyProfileAds(profileId, pageNumber);
    }
}
