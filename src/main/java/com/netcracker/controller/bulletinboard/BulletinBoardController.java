package com.netcracker.controller.bulletinboard;

import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.category.Category;
import com.netcracker.service.bulletinboard.BulletinBoardService;
import com.netcracker.service.util.RestResponsePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/bulletinboard")
public class BulletinBoardController {
    @Autowired
    private BulletinBoardService bulletinBoardService;
    @Value("${advertisement.list.pageCapacity}")
    private String adPageCapacityProp;

    @GetMapping("/{pageNumber}")
    public RestResponsePage<Advertisement> getProfileAds(@PathVariable("pageNumber") Integer pageNumber) {
        Integer count = bulletinBoardService.getAllAdPageCount();
        List<Advertisement> advertisements =  bulletinBoardService.getProfileAds(pageNumber);
        return new RestResponsePage<>(advertisements, null, count);
    }

    @GetMapping("/myAds/{id}/{pageNumber}")
    public RestResponsePage<Advertisement>  getMyProfileAds(@PathVariable("id") BigInteger profileId, @PathVariable("pageNumber") Integer pageNumber) {
        Integer count = bulletinBoardService.getMyProfileAdPageCount(profileId);
        List<Advertisement> advertisements = bulletinBoardService.getMyProfileAds(profileId, pageNumber);
        return new RestResponsePage<>(advertisements, null, count);
    }

    @PostMapping("/categorytopic/{pageNumber}")
    public RestResponsePage<Advertisement> getProfileAds(@PathVariable("pageNumber") Integer pageNumber, @RequestParam("topic") String topic, @RequestBody Category[] categories){
        Integer count = bulletinBoardService.getAllAdPageCount(topic, categories);
        List<Advertisement> advertisements = bulletinBoardService.getProfileAds(pageNumber, topic, categories);
        return new RestResponsePage<>(advertisements, null, count);
    }

    @PostMapping("/my/categorytopic/{profileId}/{pageNumber}")
    public RestResponsePage<Advertisement>getProfileAds(@PathVariable("pageNumber") Integer pageNumber, @PathVariable("profileId") BigInteger profileId,
                                             @RequestParam("topic") String topic, @RequestBody Category[] categories){
        Integer count = bulletinBoardService.getMyProfileAdPageCount(profileId, topic, categories);
        List<Advertisement> advertisements =  bulletinBoardService.getMyProfileAds(pageNumber, profileId, topic, categories);
        return new RestResponsePage<>(advertisements, null, count);
    }
    @PostMapping("/add")
    public BigInteger addAd(@RequestBody Advertisement ad){
        return bulletinBoardService.addAd(ad);
    }

    @GetMapping("/delete/{adId}")
    public void deleteAd(@PathVariable("adId") BigInteger adId){
        bulletinBoardService.deleteAd(adId);
    }
}
