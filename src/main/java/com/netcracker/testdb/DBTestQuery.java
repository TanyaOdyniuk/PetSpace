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
    ObjectRowsRepository objectRowsRepository;

    @Override
    public void run(String... args) throws Exception {

        System.out.println("\nDATASOURCE = " + dataSource);

        //Get value of certain tomcat datasource setting
        System.out.println("Max active = " + dataSource.getMaxActive());

        System.out.println("\nDisplay all objects...");
        List<ObjectRow> list = objectRowsRepository.findAll();
        list.forEach(System.out::println);

        System.out.println("\nDisplay objects hierarchy...");
        List<String> hierarchyList = objectRowsRepository.findAllObjectsHierarchy();
        hierarchyList.forEach(System.out::println);
        System.out.println("\nDone!");

    }
}
