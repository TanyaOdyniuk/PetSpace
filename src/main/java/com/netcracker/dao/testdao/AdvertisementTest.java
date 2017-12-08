package com.netcracker.dao.testdao;

import com.netcracker.configuration.AppConfig;
import com.netcracker.dao.managerapi.ManagerAPI;
import com.netcracker.dao.testdao.testentity.TestConfig;
import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.user.Profile;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AdvertisementTest {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);//TestConfig.class);
        ManagerAPI managerAPI = context.getBean(ManagerAPI.class);
        new AdvertisementTest().addAd(managerAPI);
    }
    void addAd(ManagerAPI managerAPI){
        Advertisement advertisement = new Advertisement();
        advertisement.setAdTopic("Second Topic");
        Profile profile = new Profile();
        profile.setProfileName("Petr");
        profile.setProfileSurname("Petrov");
        managerAPI.create(profile);
        advertisement.setAdAuthor(profile);
        advertisement.setAdBasicInfo("InfoInfoInfo");
        String date = "2000-12-13";
        java.sql.Date javaSqlDate = java.sql.Date.valueOf(date);
        advertisement.setAdDate(javaSqlDate);
        advertisement.setAdIsVip(true);
        managerAPI.create(advertisement);
    }
}
