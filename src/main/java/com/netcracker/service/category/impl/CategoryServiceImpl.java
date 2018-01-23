package com.netcracker.service.category.impl;

import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.model.advertisement.AdvertisementConstant;
import com.netcracker.model.category.Category;
import com.netcracker.model.category.CategoryConstant;
import com.netcracker.service.category.CategoryService;
import com.netcracker.service.util.BulletinBoardUtilService;
import com.netcracker.service.util.PageCounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private EntityManagerService entityManagerService;
    @Autowired
    private PageCounterService pageCounterService;
    @Autowired
    private BulletinBoardUtilService bulletinBoardUtilService;
    @Value("${advertisement.list.pageCapacity}")
    private String adPageCapacityProp;

    @Override
    public List<Category> getCategories() {
        BigInteger attrTypeId = BigInteger.valueOf(CategoryConstant.CAT_TYPE);
        return entityManagerService.getAll(attrTypeId, Category.class, new QueryDescriptor());
    }

    public int getPageCountAfterCatFilter(Category[] categories) {
        Integer adPageCapacity = new Integer(adPageCapacityProp);
        String getAdsQuery = "SELECT OBJECT_ID as object_id" +
                " FROM OBJREFERENCE WHERE ATTRTYPE_ID ="
                + AdvertisementConstant.AD_CATEGORY +
                " and REFERENCE ";
        getAdsQuery += bulletinBoardUtilService.getFilterCategoryAdditionQuery(categories);
        return pageCounterService.getPageCount(adPageCapacity, entityManagerService.getBySqlCount(getAdsQuery));
    }

    @Override
    public int getPageCountAfterCatFilterForProfile(Category[] categories, Integer profileId) {
        Integer adPageCapacity = new Integer(adPageCapacityProp);
        String getAdsQuery = "SELECT o1.OBJECT_ID as object_id " +
                "FROM OBJREFERENCE o1, OBJREFERENCE o2 " +
                "WHERE o1.ATTRTYPE_ID = " + AdvertisementConstant.AD_AUTHOR +
                " and o1.REFERENCE = " + profileId +
                " and o1.object_id = o2.OBJECT_ID" +
                " and o2.ATTRTYPE_ID ="
                + AdvertisementConstant.AD_CATEGORY +
                " and o2.REFERENCE ";
        getAdsQuery += bulletinBoardUtilService.getFilterCategoryAdditionQuery(categories);
        return pageCounterService.getPageCount(adPageCapacity, entityManagerService.getBySqlCount(getAdsQuery));
    }
}
