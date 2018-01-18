package com.netcracker;

import com.netcracker.configuration.AppConfig;
import com.netcracker.model.StubUser;
import com.netcracker.security.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
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
        SpringApplication.run(new Object[]{PetSpaceApplication.class, AppConfig.class, SecurityConfig.class}, args);
    }
}