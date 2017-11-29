package com.netcracker.dao.testDAO;

import com.netcracker.dao.managerapi.ManagerAPI;
import com.netcracker.dao.testDAO.testEntity.TestCity;
import com.netcracker.dao.testDAO.testEntity.TestConfig;
import com.netcracker.dao.testDAO.testEntity.TestPet;
import com.netcracker.dao.testDAO.testEntity.TestUser;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DAOTEST {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class);
        ManagerAPI managerAPI = context.getBean(ManagerAPI.class);
        TestUser testUser1 = new DAOTEST().testCreateUser(managerAPI);
        BigInteger forUpdate = testUser1.getObjectId();
        new DAOTEST().testUpdateUser(managerAPI, forUpdate);
        TestUser gotTestUser1 = new DAOTEST().testGetUser(managerAPI, forUpdate);
        TestUser testUser2 = new DAOTEST().testCreateWithCities(managerAPI);
        BigInteger getWithCities = testUser2.getObjectId();
        TestUser gotTestUser2 = new DAOTEST().testGetUserWithCities(managerAPI, getWithCities);
        List<TestUser> res = new DAOTEST().getAllTest(managerAPI);
        new DAOTEST().deleteForceTest(managerAPI,new BigInteger("1"), 0);
        new DAOTEST().deleteForceTest(managerAPI,new BigInteger("4"), 0);
        new DAOTEST().deleteForceTest(managerAPI,new BigInteger("1"), 0);
        List<TestUser> res1 = new DAOTEST().getAllTest(managerAPI);
        System.out.printf("");
    }

    public void deleteForceTest(ManagerAPI managerAPI, BigInteger id, Integer type){
        managerAPI.delete(id, type);
    }

    public List<TestUser> getAllTest(ManagerAPI managerAPI){
        return managerAPI.getAll(new BigInteger("1"), TestUser.class);
    }

    public TestUser testCreateUser(ManagerAPI managerAPI) {
        TestUser testUser = new TestUser();
        testUser.setSurname("Pupkin");
        testUser.setUsername("Vasya");
        return managerAPI.create(testUser);
    }

    public void testUpdateUser(ManagerAPI managerAPI, BigInteger forUpdate) {
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

    public TestUser testGetUser(ManagerAPI managerAPI, BigInteger forGet) {
        return managerAPI.getById(forGet, TestUser.class);
    }


    public TestUser testCreateWithCities(ManagerAPI managerAPI) {
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
        return managerAPI.create(testUser);
    }

    public TestUser testGetUserWithCities(ManagerAPI managerAPI, BigInteger forGet) {
        return managerAPI.getById(forGet, TestUser.class);
    }
}