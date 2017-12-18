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
    public Integer getAllAdPageCount() {
        return bulletinBoardService.getAllAdPageCount();
    }

    @GetMapping("/pageCount/{id}")
    public Integer getMyProfileAdPageCount(@PathVariable("id") BigInteger profileId) {
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

    @PostMapping("/categorytopic/{topic}")
    public Integer getPageCount(@PathVariable("topic") String topic, @RequestBody Category[] categories){
        if(topic.equals("empty")){
            topic = "";
        }
        return bulletinBoardService.getAllAdPageCount(topic, categories);
    }

    @PostMapping("/my/categorytopic/{profileId}/{topic}")
    public Integer getMyProfileAdPageCount(@PathVariable("profileId") BigInteger profileId, @PathVariable("topic") String topic, @RequestBody Category[] categories){
        if(topic.equals("empty")){
            topic = "";
        }
        return bulletinBoardService.getMyProfileAdPageCount(profileId, topic, categories);
    }

    @PostMapping("/categorytopic/{topic}/{pageNumber}")
    public List<Advertisement> getProfileAds(@PathVariable("pageNumber") Integer pageNumber, @PathVariable("topic") String topic, @RequestBody Category[] categories){
        if(topic.equals("empty")){
            topic = "";
        }
        return bulletinBoardService.getProfileAds(pageNumber, topic, categories);
    }

    @PostMapping("/my/categorytopic/{profileId}/{topic}/{pageNumber}")
    public List<Advertisement> getProfileAds(@PathVariable("pageNumber") Integer pageNumber, @PathVariable("profileId") BigInteger profileId,
                                             @PathVariable("topic") String topic, @RequestBody Category[] categories){
        if(topic.equals("empty")){
            topic = "";
        }
        return bulletinBoardService.getMyProfileAds(pageNumber, profileId, topic, categories);
    }
    @PostMapping("/add")
    public Advertisement addAd(@RequestBody Advertisement ad){
        return bulletinBoardService.addAd(ad);
    }
}
