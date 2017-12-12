package com.netcracker.dao.testdao;

import com.netcracker.configuration.AppConfig;
import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.model.advertisement.Advertisement;
import com.netcracker.model.user.Profile;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AdvertisementTest {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);//TestConfig.class);
        EntityManagerService entityManagerService = context.getBean(EntityManagerService.class);
        new AdvertisementTest().addAd(entityManagerService);
    }
    void addAd(EntityManagerService entityManagerService){
        Advertisement advertisement = new Advertisement();
        advertisement.setAdTopic("Second Topic");
        Profile profile = new Profile();
        profile.setProfileName("Petr");
        profile.setProfileSurname("Petrov");
        entityManagerService.create(profile);
        advertisement.setAdAuthor(profile);
        advertisement.setAdBasicInfo("InfoInfoInfo");
        String date = "2000-12-13";
        java.sql.Date javaSqlDate = java.sql.Date.valueOf(date);
        advertisement.setAdDate(javaSqlDate);
        advertisement.setAdIsVip(true);
        entityManagerService.create(advertisement);
    }
}
