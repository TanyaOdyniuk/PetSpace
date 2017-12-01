package com.netcracker.service.bulletinboard.impl;

import com.netcracker.dao.managerapi.ManagerAPI;
import com.netcracker.model.Status;
import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.advertisement.AdvertisementConstant;
import com.netcracker.model.category.Category;
import com.netcracker.model.user.Profile;
import com.netcracker.service.bulletinboard.BulletinBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
@Service
public class BulletinBoardServiceImpl implements BulletinBoardService {
    @Autowired
    ManagerAPI managerAPI;

    @Override
    public List<Advertisement> getProfileAds() {
        BigInteger attrTypeId = BigInteger.valueOf(AdvertisementConstant.AD_TYPE);
        List<Advertisement> advertisements = managerAPI.getAll(attrTypeId, Advertisement.class);
        for (Advertisement ad : advertisements) {
            Profile author = ad.getAdAuthor();
            Category category = ad.getAdCategory();
            if(author != null){
                ad.setAdAuthor(managerAPI.getById(author.getObjectId(), Profile.class));
            }
            if(category != null){
                ad.setAdCategory(managerAPI.getById(category.getObjectId(), Category.class));
            }
        }
        return advertisements;
    }

    @Override
    public List<Advertisement> getMyProfileAds(BigInteger profileId) {
        String getAdsQuery = "SELECT OBJECT_ID as object_id" +
                " FROM OBJREFERENCE WHERE ATTRTYPE_ID ="
                + AdvertisementConstant.AD_AUTHOR +
                " and REFERENCE = "
                + profileId +
                "UNION select REFERENCE as object_id FROM " +
                "OBJREFERENCE WHERE ATTRTYPE_ID =" +
                + AdvertisementConstant.AD_AUTHOR +
                " and OBJECT_ID = " + profileId;
        List<Advertisement> advertisements = managerAPI.getObjectsBySQL(getAdsQuery, Advertisement.class);
        for (Advertisement ad : advertisements) {
            Category category = ad.getAdCategory();
            if(category != null){
                ad.setAdCategory(managerAPI.getById(category.getObjectId(), Category.class));
            }
        }
        return advertisements;
    }

    @Override
    public List<Advertisement> sortAds(List<Advertisement> listAds) {
        return null;
    }

    @Override
    public void newAd(Advertisement ad) {
    }

    @Override
    public List<String> getAd(Advertisement ad) {
        return null;
    }

    @Override
    public void updateAd(Advertisement ad) {

    }

    @Override
    public void deleteAd(Advertisement ad) {

    }

    @Override
    public void chooseCategoryForAd(Advertisement listAds, Category categoryAd) {

    }

    @Override
    public void filterAdsByCategory(List<Advertisement> listAds, List<Category> categoryAds) {

    }
}
