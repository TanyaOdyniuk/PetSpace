package com.netcracker.dao.testdao;

import com.netcracker.dao.managerapi.ManagerAPI;
import com.netcracker.dao.testdao.testentity.TestConfig;
import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.user.Profile;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AdvertisementTest {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class);
        ManagerAPI managerAPI = context.getBean(ManagerAPI.class);
        new AdvertisementTest().addAd(managerAPI);
    }
    void addAd(ManagerAPI managerAPI){
        Advertisement advertisement = new Advertisement();
        advertisement.setAdTopic("First Topic");
        Profile profile = new Profile();
        profile.setProfileName("Ivan");
        profile.setProfileSurname("Ivanov");
        managerAPI.create(profile);
        advertisement.setAdAuthor(profile);
        advertisement.setAdBasicInfo("Main INFO");
        String date = "2000-11-01";
        java.sql.Date javaSqlDate = java.sql.Date.valueOf(date);
        advertisement.setAdDate(javaSqlDate);
        advertisement.setAdIsVip(true);
        managerAPI.create(advertisement);
    }
}
