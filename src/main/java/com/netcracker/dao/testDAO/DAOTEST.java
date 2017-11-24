package com.netcracker.dao.testDAO;

import com.netcracker.dao.managerapi.ManagerAPI;
import com.netcracker.dao.testDAO.testEntity.TestCity;
import com.netcracker.dao.testDAO.testEntity.TestConfig;
import com.netcracker.dao.testDAO.testEntity.TestPet;
import com.netcracker.dao.testDAO.testEntity.TestUser;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DAOTEST {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class);
        ManagerAPI managerAPI = context.getBean(ManagerAPI.class);
        Integer forUpdate = new DAOTEST().testCreateUser(managerAPI);
        new DAOTEST().testUpdateUser(managerAPI, forUpdate);
        new DAOTEST().testGetUser(managerAPI, forUpdate);
        Integer getWithCities = new DAOTEST().testCreateWithCities(managerAPI);
        new DAOTEST().testGetUserWithCities(managerAPI, getWithCities);
    }

    public Integer testCreateUser(ManagerAPI managerAPI) {
        TestUser testUser = new TestUser();
        testUser.setSurname("Pupkin");
        testUser.setUsername("Vasya");
        return managerAPI.create(testUser).getObjectId();
    }

    public void testUpdateUser(ManagerAPI managerAPI, Integer forUpdate) {
        TestUser testUser = new TestUser();
        testUser.setObjectId(forUpdate);
        testUser.setSurname("Ololoev");
        testUser.setUsername("Alex");
        List<String> favouriteBeards = new ArrayList<>();
        List<TestPet> myPets = new ArrayList<>();
        TestPet pet = new TestPet();
        pet.setAge(5);
        String date = "2000-11-01";
        java.sql.Date javaSqlDate = java.sql.Date.valueOf(date);
        pet.setBirth(javaSqlDate);
        pet.setGood(true);
        pet.setObjectId(managerAPI.create(pet).getObjectId());
        myPets.add(pet);
        favouriteBeards.add("Horse");
        favouriteBeards.add("Cat");
        testUser.setFavouritePets(favouriteBeards);
        testUser.setMyPets(myPets);
        managerAPI.update(testUser);
    }

    public TestUser testGetUser(ManagerAPI managerAPI, Integer forGet) {
        TestUser testUser = managerAPI.getById(forGet, TestUser.class);
        return testUser;
    }


    public Integer testCreateWithCities(ManagerAPI managerAPI) {
        TestUser testUser = new TestUser();
        testUser.setSurname("Ivanov");
        testUser.setUsername("Ivan");
        Set<TestCity> myCities = new HashSet<>();
        TestCity city1 = new TestCity();
        city1.setCityName("Poltava");
        city1.setObjectId(managerAPI.create(city1).getObjectId());
        myCities.add(city1);
        TestCity city2 = new TestCity();
        city2.setCityName("Kharkov");
        city2.setObjectId(managerAPI.create(city2).getObjectId());
        myCities.add(city2);
        testUser.setMycities(myCities);
        return managerAPI.create(testUser).getObjectId();
    }

    public TestUser testGetUserWithCities(ManagerAPI managerAPI, Integer forGet) {
        TestUser testUser = managerAPI.getById(forGet, TestUser.class);
        return testUser;
    }
}