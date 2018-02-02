package com.netcracker;

import com.netcracker.configuration.AppConfig;
import com.netcracker.model.StubUser;
import com.netcracker.security.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableTransactionManagement
public class PetSpaceApplication {
    public static void main(String[] args) {
        SpringApplication.run(new Object[]{PetSpaceApplication.class, AppConfig.class, SecurityConfig.class}, args);
    }
}