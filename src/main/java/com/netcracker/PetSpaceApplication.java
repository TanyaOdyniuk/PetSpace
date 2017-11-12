package com.netcracker;

import com.netcracker.model.StubUser;
import com.netcracker.testDB.Region;
import com.netcracker.testDB.RegionsRepository;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@ImportResource("applicationContext.xml")
public class PetSpaceApplication {

    @Bean(name = "users")
    public List<StubUser> getUsers() {
        List<StubUser> users = new ArrayList<>();
        users.add(new StubUser(StubUser.objectCount++, "Vasya", "Pupkin"));
        users.add(new StubUser(StubUser.objectCount++, "Ivan", "Ivanov"));
        users.add(new StubUser(StubUser.objectCount, "Petr", "Petrov"));
        return users;
    }


    public static void main(String[] args) {
        SpringApplication.run(PetSpaceApplication.class, args);
    }


}
