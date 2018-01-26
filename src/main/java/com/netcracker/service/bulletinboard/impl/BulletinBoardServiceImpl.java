package com.netcracker.service.bulletinboard.impl;

import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.advertisement.AdvertisementConstant;
import com.netcracker.model.category.Category;
import com.netcracker.model.user.Profile;
import com.netcracker.service.bulletinboard.BulletinBoardService;
import com.netcracker.service.status.StatusService;
import com.netcracker.service.util.BulletinBoardUtilService;
import com.netcracker.service.util.PageCounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

import static com.netcracker.dao.manager.query.Query.IGNORING_DELETED_ELEMENTS;
import static com.netcracker.dao.manager.query.Query.IGNORING_DELETED_ELEMENTS_IN_REF;

@Service
public class BulletinBoardServiceImpl implements BulletinBoardService {
    @Autowired
    private EntityManagerService entityManagerService;
    @Autowired
    private PageCounterService pageCounterService;
    @Autowired
    private BulletinBoardUtilService bulletinBoardUtilService;
    @Autowired
    private StatusService statusService;
    @Value("${advertisement.list.pageCapacity}")
    private String adPageCapacityProp;
    @Value("${advertisement.mylist.pageCapacity}")
    private String myAdPageCapacityProp;

    @Override
    public int getAllAdPageCount() {
        Integer adPageCapacity = new Integer(adPageCapacityProp);
        String select = "SELECT object_id " +
                "FROM Objects " +
                "WHERE object_type_id = " +AdvertisementConstant.AD_TYPE  +
                " intersect SELECT OBJECT_ID" +
                " FROM OBJREFERENCE WHERE ATTRTYPE_ID ="
                + AdvertisementConstant.AD_AUTHOR +
                " and " + IGNORING_DELETED_ELEMENTS_IN_REF;
        return pageCounterService.getPageCount(adPageCapacity, entityManagerService.getBySqlCount(select));
    }
    @Override
    public int getMyProfileAdPageCount(BigInteger profileId) {
        Integer myAdPageCapacity = new Integer(myAdPageCapacityProp);
        String getAdsQuery = "SELECT OBJECT_ID as object_id" +
                " FROM OBJREFERENCE WHERE ATTRTYPE_ID ="
                + AdvertisementConstant.AD_AUTHOR +
                " and REFERENCE = "
                + profileId + " and " + IGNORING_DELETED_ELEMENTS_IN_REF;
        return pageCounterService.getPageCount(myAdPageCapacity, entityManagerService.getBySqlCount(getAdsQuery));
    }
    @Override
    public int getMyProfileAdPageCount(BigInteger profileId, String topic, Category[] categories){
        Integer myAdPageCapacity = new Integer(myAdPageCapacityProp);
        String getAdsQuery = "SELECT OBJECT_ID as object_id" +
                " FROM OBJREFERENCE WHERE ATTRTYPE_ID ="
                + AdvertisementConstant.AD_AUTHOR +
                " and REFERENCE = "
                + profileId + " and " + IGNORING_DELETED_ELEMENTS_IN_REF + " ";
        boolean checkTopic = (topic != null && !topic.isEmpty());
        boolean checkCategory = (categories != null && !(categories.length == 0));
        if (checkCategory || checkTopic) {
            getAdsQuery += " INTERSECT ";
            if (checkTopic) {
                getAdsQuery = "SELECT OBJECT_ID as object_id" +
                        " FROM Attributes WHERE ATTRTYPE_ID ="
                        + AdvertisementConstant.AD_TOPIC +
                        " and LOWER(VALUE) LIKE LOWER('%" + topic + "%')";
            }
            if (checkCategory && checkTopic) {
                getAdsQuery += " INTERSECT ";
            }
            if (checkCategory) {
                getAdsQuery += "SELECT OBJECT_ID as object_id" +
                        " FROM OBJREFERENCE WHERE ATTRTYPE_ID ="
                        + AdvertisementConstant.AD_CATEGORY +
                        " and REFERENCE ";
                getAdsQuery += bulletinBoardUtilService.getFilterCategoryAdditionQuery(categories);
            }
        }
        return pageCounterService.getPageCount(myAdPageCapacity, entityManagerService.getBySqlCount(getAdsQuery));
    }
    @Override
    public int getAllAdPageCount(String topic, Category[] categories){
        int count;
        Integer adPageCapacity = new Integer(adPageCapacityProp);
        String getAdsQuery = "";
        boolean checkTopic = (topic != null && !topic.isEmpty());
        boolean checkCategory = (categories != null && !(categories.length == 0));
        if (checkCategory || checkTopic) {
            if (checkTopic) {
                getAdsQuery = "SELECT OBJECT_ID as object_id" +
                        " FROM Attributes WHERE ATTRTYPE_ID ="
                        + AdvertisementConstant.AD_TOPIC +
                        " and LOWER(VALUE) LIKE LOWER('%" + topic + "%')";
            }
            if (checkCategory && checkTopic) {
                getAdsQuery += " INTERSECT ";
            }
            if (checkCategory) {
                getAdsQuery += "SELECT OBJECT_ID as object_id" +
                        " FROM OBJREFERENCE WHERE ATTRTYPE_ID ="
                        + AdvertisementConstant.AD_CATEGORY +
                        " and REFERENCE ";
                getAdsQuery += bulletinBoardUtilService.getFilterCategoryAdditionQuery(categories) ;
            }
            getAdsQuery += " intersect SELECT OBJECT_ID as object_id" +
            " FROM OBJREFERENCE WHERE ATTRTYPE_ID ="
                    + AdvertisementConstant.AD_AUTHOR +
                    " and " + IGNORING_DELETED_ELEMENTS_IN_REF;
           count = pageCounterService.getPageCount(adPageCapacity, entityManagerService.getBySqlCount(getAdsQuery));
        } else{
            String select = "SELECT object_id " +
                    "FROM Objects " +
                    "WHERE object_type_id = " +AdvertisementConstant.AD_TYPE  +
                    " intersect SELECT OBJECT_ID" +
                    " FROM OBJREFERENCE WHERE ATTRTYPE_ID ="
                    + AdvertisementConstant.AD_AUTHOR +
                    " and " + IGNORING_DELETED_ELEMENTS_IN_REF;
            count = pageCounterService.getPageCount(adPageCapacity, entityManagerService.getBySqlCount(select));
        }
        return count;
    }

    private List<Advertisement> getCategoryAndOwner(List<Advertisement> advertisements) {
        for (Advertisement ad : advertisements) {
            Category category = ad.getAdCategory();
            if (category != null) {
                ad.setAdCategory(entityManagerService.getById(category.getObjectId(), Category.class));
            }
            Profile author = ad.getAdAuthor();
            if (author != null) {
                ad.setAdAuthor(entityManagerService.getById(author.getObjectId(), Profile.class));
            }
        }
        return advertisements;
    }

    @Override
    public List<Advertisement> getProfileAds(Integer pageNumber) {
        Integer adPageCapacity = new Integer(adPageCapacityProp);
        QueryDescriptor queryDescriptor = new QueryDescriptor();
        queryDescriptor.addPagingDescriptor(pageNumber, adPageCapacity);
        queryDescriptor.addSortingDesc(6, "DESC", true);
        String select = "SELECT object_id " +
                "FROM Objects " +
                "WHERE object_type_id = " +AdvertisementConstant.AD_TYPE  +
                " intersect SELECT OBJECT_ID as object_id" +
                " FROM OBJREFERENCE WHERE ATTRTYPE_ID ="
                + AdvertisementConstant.AD_AUTHOR +
                " and " + IGNORING_DELETED_ELEMENTS_IN_REF;
        List<Advertisement> advertisements = entityManagerService.getObjectsBySQL(select, Advertisement.class, queryDescriptor);
        return getCategoryAndOwner(advertisements);
    }

    @Override
    public List<Advertisement> getMyProfileAds(BigInteger profileId, Integer pageNumber) {
        String getAdsQuery = "SELECT OBJECT_ID as object_id" +
                " FROM OBJREFERENCE WHERE ATTRTYPE_ID ="
                + AdvertisementConstant.AD_AUTHOR +
                " and REFERENCE = "
                + profileId + " and " + IGNORING_DELETED_ELEMENTS_IN_REF;
        Integer myAdPageCapacity = new Integer(myAdPageCapacityProp);
        QueryDescriptor queryDescriptor = new QueryDescriptor();
        queryDescriptor.addPagingDescriptor(pageNumber, myAdPageCapacity);
        queryDescriptor.addSortingDesc(6, "DESC", true);
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
    public BigInteger addAd(Advertisement ad) {
        ad.setAdStatus(statusService.getActiveStatus());
        BigInteger id = ad.getObjectId();
        if(id != null){
            entityManagerService.dropRef(AdvertisementConstant.AD_CATEGORY, id, 0);
            entityManagerService.dropRef(AdvertisementConstant.AD_PETS, id, 1);
        }
        return entityManagerService.update(ad);
    }

    @Override
    public void deleteAd(BigInteger adId) {
        entityManagerService.delete(adId, -1);
    }

    @Override
    public List<Advertisement> getProfileAds(Integer pageNumber, String topic, Category[] categories) {
        Integer adPageCapacity = new Integer(adPageCapacityProp);
        List<Advertisement> advertisements;
        String getAdsQuery = "";
        QueryDescriptor queryDescriptor = new QueryDescriptor();
        queryDescriptor.addPagingDescriptor(pageNumber, adPageCapacity);
        queryDescriptor.addSortingDesc(6, "DESC", true);
        boolean checkTopic = (topic != null && !topic.isEmpty());
        boolean checkCategory = (categories != null && !(categories.length == 0));
        if (checkCategory || checkTopic) {
            if (checkTopic) {
                getAdsQuery = "SELECT OBJECT_ID as object_id" +
                        " FROM Attributes WHERE ATTRTYPE_ID ="
                        + AdvertisementConstant.AD_TOPIC +
                        " and LOWER(VALUE) LIKE LOWER('%" + topic + "%')";
            }
            if (checkCategory && checkTopic) {
                getAdsQuery += " INTERSECT ";
            }
            if (checkCategory) {
                getAdsQuery += "SELECT OBJECT_ID as object_id" +
                        " FROM OBJREFERENCE WHERE ATTRTYPE_ID ="
                        + AdvertisementConstant.AD_CATEGORY +
                        " and REFERENCE ";
                getAdsQuery += bulletinBoardUtilService.getFilterCategoryAdditionQuery(categories);
            }
            getAdsQuery += " intersect SELECT OBJECT_ID as object_id" +
                    " FROM OBJREFERENCE WHERE ATTRTYPE_ID ="
                    + AdvertisementConstant.AD_AUTHOR +
                    " and " + IGNORING_DELETED_ELEMENTS_IN_REF;
            advertisements = entityManagerService.getObjectsBySQL(getAdsQuery, Advertisement.class, queryDescriptor);
        } else {
            String select = "SELECT object_id " +
                    "FROM Objects " +
                    "WHERE object_type_id = " +AdvertisementConstant.AD_TYPE  +
                    " intersect SELECT OBJECT_ID as object_id" +
                    " FROM OBJREFERENCE WHERE ATTRTYPE_ID ="
                    + AdvertisementConstant.AD_AUTHOR +
                    " and " + IGNORING_DELETED_ELEMENTS_IN_REF;
            advertisements = entityManagerService.getObjectsBySQL(select, Advertisement.class, queryDescriptor);
        }
        return getCategoryAndOwner(advertisements);
    }
    @Override
    public List<Advertisement> getMyProfileAds(Integer pageNumber, BigInteger profileId, String topic, Category[] categories){
        String getAdsQuery = "SELECT OBJECT_ID as object_id" +
                " FROM OBJREFERENCE WHERE ATTRTYPE_ID ="
                + AdvertisementConstant.AD_AUTHOR +
                " and REFERENCE = "
                + profileId + " and " + IGNORING_DELETED_ELEMENTS_IN_REF;
        Integer myAdPageCapacity = new Integer(myAdPageCapacityProp);
        List<Advertisement> advertisements;
        QueryDescriptor queryDescriptor = new QueryDescriptor();
        queryDescriptor.addPagingDescriptor(pageNumber, myAdPageCapacity);
        queryDescriptor.addSortingDesc(6, "DESC", true);
        boolean checkTopic = (topic != null && !topic.isEmpty());
        boolean checkCategory = (categories != null && !(categories.length == 0));
        if (checkCategory || checkTopic) {
            getAdsQuery += " INTERSECT ";
            if (checkTopic) {
                getAdsQuery += "SELECT OBJECT_ID as object_id" +
                        " FROM Attributes WHERE ATTRTYPE_ID ="
                        + AdvertisementConstant.AD_TOPIC +
                        " and LOWER(VALUE) LIKE LOWER('%" + topic + "%')";
            }
            if (checkCategory && checkTopic) {
                getAdsQuery += " INTERSECT ";
            }
            if (checkCategory) {
                getAdsQuery += "SELECT OBJECT_ID as object_id" +
                        " FROM OBJREFERENCE WHERE ATTRTYPE_ID ="
                        + AdvertisementConstant.AD_CATEGORY +
                        " and REFERENCE ";
                getAdsQuery += bulletinBoardUtilService.getFilterCategoryAdditionQuery(categories);
            }
        }
        advertisements = entityManagerService.getObjectsBySQL(getAdsQuery, Advertisement.class, queryDescriptor);
        for (Advertisement ad : advertisements) {
            Category category = ad.getAdCategory();
            if (category != null) {
                ad.setAdCategory(entityManagerService.getById(category.getObjectId(), Category.class));
            }
        }
        return advertisements;
    }
}
