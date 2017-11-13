package com.netcracker.testDB;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

import java.util.List;

@SpringBootApplication
@ImportResource("applicationContext.xml")
public class DBTestQuery implements CommandLineRunner {

    @Autowired
    DataSource dataSource;

    @Autowired
    RegionsRepository regionsRepository;

    @Override
    public void run(String... args) throws Exception {

        System.out.println("DATASOURCE = " + dataSource);

        //Get value of certain tomcat datasource setting
        System.out.println("Max active = " + dataSource.getMaxActive());

        System.out.println("Display all regions...");
        List<Region> list = regionsRepository.findAll();
        list.forEach(x -> System.out.println(x));

        System.out.println("Done!");

    }
}
