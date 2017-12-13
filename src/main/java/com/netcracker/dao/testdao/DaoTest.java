package com.netcracker.dao.testdao;

import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.dao.testdao.testentity.TestCity;
import com.netcracker.dao.testdao.testentity.TestConfig;
import com.netcracker.dao.testdao.testentity.TestPet;
import com.netcracker.dao.testdao.testentity.TestUser;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DaoTest {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class);
        EntityManagerService entityManagerService = context.getBean(EntityManagerService.class);
        TestUser testUser1 = new DaoTest().testCreateUser(entityManagerService);
        BigInteger forUpdate = testUser1.getObjectId();
        new DaoTest().testUpdateUser(entityManagerService, forUpdate);
        TestUser gotTestUser1 = new DaoTest().testGetUser(entityManagerService, forUpdate);
        TestUser testUser2 = new DaoTest().testCreateWithCities(entityManagerService);
        BigInteger getWithCities = testUser2.getObjectId();
        TestUser gotTestUser2 = new DaoTest().testGetUserWithCities(entityManagerService, getWithCities);
        List<TestUser> res = new DaoTest().getAllTest(entityManagerService);
        new DaoTest().deleteForceTest(entityManagerService,new BigInteger("1"), 0);
        new DaoTest().deleteForceTest(entityManagerService,new BigInteger("4"), 0);
        new DaoTest().deleteForceTest(entityManagerService,new BigInteger("1"), 0);
        List<TestUser> res1 = new DaoTest().getAllTest(entityManagerService);
        System.out.printf("");
    }

    public void deleteForceTest(EntityManagerService entityManagerService, BigInteger id, Integer type){
        entityManagerService.delete(id, type);
    }

    public List<TestUser> getAllTest(EntityManagerService entityManagerService){
        return entityManagerService.getAll(new BigInteger("1"), TestUser.class, new QueryDescriptor());
    }

    public TestUser testCreateUser(EntityManagerService entityManagerService) {
        TestUser testUser = new TestUser();
        testUser.setSurname("Pupkin");
        testUser.setUsername("Vasya");
        return entityManagerService.create(testUser);
    }

    public void testUpdateUser(EntityManagerService entityManagerService, BigInteger forUpdate) {
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
        pet.setObjectId(entityManagerService.create(pet).getObjectId());
        myPets.add(pet);
        favouriteBeards.add("Horse");
        favouriteBeards.add("Cat");
        testUser.setFavouritePets(favouriteBeards);
        testUser.setMyPets(myPets);
        entityManagerService.update(testUser);
    }

    public TestUser testGetUser(EntityManagerService entityManagerService, BigInteger forGet) {
        return entityManagerService.getById(forGet, TestUser.class);
    }


    public TestUser testCreateWithCities(EntityManagerService entityManagerService) {
        TestUser testUser = new TestUser();
        testUser.setSurname("Ivanov");
        testUser.setUsername("Ivan");
        Set<TestCity> myCities = new HashSet<>();
        TestCity city1 = new TestCity();
        city1.setCityName("Poltava");
        city1.setObjectId(entityManagerService.create(city1).getObjectId());
        myCities.add(city1);
        TestCity city2 = new TestCity();
        city2.setCityName("Kharkov");
        city2.setObjectId(entityManagerService.create(city2).getObjectId());
        myCities.add(city2);
        testUser.setMycities(myCities);
        return entityManagerService.create(testUser);
    }

    public TestUser testGetUserWithCities(EntityManagerService entityManagerService, BigInteger forGet) {
        return entityManagerService.getById(forGet, TestUser.class);
    }
}