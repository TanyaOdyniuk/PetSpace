package com.netcracker.service.bulletinboard;

import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.category.Category;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public interface BulletinBoardService {
    int getAllAdPageCount(String topic, Category[] categories);
    int getAllAdPageCount();
    int getMyProfileAdPageCount(BigInteger profileId);
    int getMyProfileAdPageCount(BigInteger profileId, String topic, Category[] categories);

    List<Advertisement> getProfileAds(Integer pageNumber);
    List<Advertisement> getProfileAds(Integer pageNumber, String topic, Category[] categories);

    List<Advertisement> getMyProfileAds(BigInteger profileId, Integer pageNumber);
    List<Advertisement> getMyProfileAds(Integer pageNumber, BigInteger profileId, String topic, Category[] categories);

    BigInteger addAd(Advertisement ad);

    void deleteAd(BigInteger adId);

}
