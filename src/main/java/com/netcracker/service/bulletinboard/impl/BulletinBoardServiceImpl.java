package com.netcracker.service.bulletinboard.impl;

import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.advertisement.AdvertisementConstant;
import com.netcracker.model.category.Category;
import com.netcracker.model.user.Profile;
import com.netcracker.service.bulletinboard.BulletinBoardService;
import com.netcracker.service.util.PageCounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class BulletinBoardServiceImpl implements BulletinBoardService {
    @Autowired
    EntityManagerService entityManagerService;
    @Autowired
    PageCounterService pageCounterService;
    @Value("${advertisement.list.pageCapasity}")
    String adPageCapacityProp;
    @Value("${advertisement.mylist.pageCapasity}")
    String myAdPageCapacityProp;

    public int getAllAdPageCount() {
        Integer adPageCapacity = new Integer(adPageCapacityProp);
        return pageCounterService.getPageCount(adPageCapacity, entityManagerService.getAllCount(BigInteger.valueOf(AdvertisementConstant.AD_TYPE)));
    }

    public int getMyProfileAdPageCount(BigInteger profileId) {
        Integer myAdPageCapacity = new Integer(myAdPageCapacityProp);
        String getAdsQuery = "SELECT OBJECT_ID as object_id" +
                " FROM OBJREFERENCE WHERE ATTRTYPE_ID ="
                + AdvertisementConstant.AD_AUTHOR +
                " and REFERENCE = "
                + profileId +
                "UNION select REFERENCE as object_id FROM " +
                "OBJREFERENCE WHERE ATTRTYPE_ID =" +
                +AdvertisementConstant.AD_AUTHOR +
                " and OBJECT_ID = " + profileId;
        return pageCounterService.getPageCount(myAdPageCapacity, entityManagerService.getBySqlCount(getAdsQuery));

    }

    @Override
    public List<Advertisement> getProfileAds(Integer pageNumber) {
        Integer adPageCapacity = new Integer(adPageCapacityProp);
        QueryDescriptor queryDescriptor = new QueryDescriptor();
        queryDescriptor.addPagingDescriptor(pageNumber, adPageCapacity);
        BigInteger attrTypeId = BigInteger.valueOf(AdvertisementConstant.AD_TYPE);
        List<Advertisement> advertisements = entityManagerService.getAll(attrTypeId, Advertisement.class, queryDescriptor);
        for (Advertisement ad : advertisements) {
            Profile author = ad.getAdAuthor();
            Category category = ad.getAdCategory();
            if (author != null) {
                ad.setAdAuthor(entityManagerService.getById(author.getObjectId(), Profile.class));
            }
            if (category != null) {
                ad.setAdCategory(entityManagerService.getById(category.getObjectId(), Category.class));
            }
        }
        return advertisements;
    }

    @Override
    public List<Advertisement> getMyProfileAds(BigInteger profileId, Integer pageNumber) {
        String getAdsQuery = "SELECT OBJECT_ID as object_id" +
                " FROM OBJREFERENCE WHERE ATTRTYPE_ID ="
                + AdvertisementConstant.AD_AUTHOR +
                " and REFERENCE = "
                + profileId +
                "UNION select REFERENCE as object_id FROM " +
                "OBJREFERENCE WHERE ATTRTYPE_ID =" +
                +AdvertisementConstant.AD_AUTHOR +
                " and OBJECT_ID = " + profileId;
        Integer myAdPageCapacity = new Integer(myAdPageCapacityProp);
        QueryDescriptor queryDescriptor = new QueryDescriptor();
        queryDescriptor.addPagingDescriptor(pageNumber, myAdPageCapacity);
        List<Advertisement> advertisements = entityManagerService.getObjectsBySQL(getAdsQuery, Advertisement.class, queryDescriptor);
        for (Advertisement ad : advertisements) {
            Category category = ad.getAdCategory();
            if (category != null) {
                ad.setAdCategory(entityManagerService.getById(category.getObjectId(), Category.class));
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
