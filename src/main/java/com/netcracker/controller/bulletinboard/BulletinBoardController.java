package com.netcracker.controller.bulletinboard;

import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.category.Category;
import com.netcracker.service.bulletinboard.BulletinBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/category/{pageNumber}")
    public List<Advertisement> getAllAdAfterCatFilter(@PathVariable("pageNumber") Integer pageNumber, @RequestBody Category[] categories){
        return bulletinBoardService.getAllAdAfterCatFilter(pageNumber, categories);
    }

    @PostMapping("/category/{pageNumber}/{id}")
    public List<Advertisement> getAllAdAfterCatFilterFromProfile(@PathVariable("pageNumber") Integer pageNumber, @PathVariable("id") Integer profileId, @RequestBody Category[] categories){
        return bulletinBoardService.getAllAdAfterCatFilterFromProfile(pageNumber, profileId, categories);
    }

    @PostMapping("/add")
    public Advertisement addAd(@RequestBody Advertisement ad){
        return bulletinBoardService.addAd(ad);
    }
}
