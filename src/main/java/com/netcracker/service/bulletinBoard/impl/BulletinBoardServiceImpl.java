package com.netcracker.service.bulletinBoard.impl;

import com.netcracker.dao.managerapi.ManagerAPI;
import com.netcracker.model.Status;
import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.advertisement.AdvertisementConstant;
import com.netcracker.model.category.Category;
import com.netcracker.model.user.Profile;
import com.netcracker.service.bulletinBoard.BulletinBoardService;
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
            ad.setAdAuthor(managerAPI.getById(ad.getAdAuthor().getObjectId(), Profile.class));
            ad.setAdStatus(managerAPI.getById(ad.getAdStatus().getObjectId(), Status.class));
        }
        return advertisements;
    }

    @Override
    public List<Advertisement> getMyProfileAds(BigInteger profileId) {
        String getAdsQuery = "SELECT OBJECT_ID FROM OBJREFERENCE WHERE ATTRTYPE_ID ="
                + AdvertisementConstant.AD_AUTHOR + " and REFERENCE = "
                + profileId;
        List<Advertisement> advertisements = managerAPI.getObjectsBySQL(getAdsQuery, Advertisement.class);
        for (Advertisement ad : advertisements) {
            ad.setAdStatus(managerAPI.getById(ad.getAdStatus().getObjectId(), Status.class));
        }
        return advertisements;
    }

    @Override
    public List<Advertisement> sortAd(List<Advertisement> listAds) {
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
    public void chooseCategory(Advertisement listAds, Category categoryAd) {

    }

    @Override
    public void filterByCategory(List<Advertisement> listAds, List<Category> categoryAds) {

    }
}
