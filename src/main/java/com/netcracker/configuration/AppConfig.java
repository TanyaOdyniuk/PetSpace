package com.netcracker.configuration;

import com.netcracker.dao.manager.EntityManager;
import com.netcracker.dao.managerservice.EntityManagerService;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@Configuration
public class AppConfig {
    @Bean
    public DataSource getDataSource(){
        DataSource dataSource = new DataSource();
        dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        dataSource.setUrl("jdbc:oracle:thin:@localhost:1521:XE");
        dataSource.setUsername("petspace");
        dataSource.setPassword("p1234");
        dataSource.setInitialSize(30);
        dataSource.setMaxActive(50);
        dataSource.setMaxIdle(30);
        /*
        dataSource.setMinIdle(7);
        dataSource.setMaxWait(50000);
        dataSource.setTestOnBorrow(true);
        dataSource.setValidationQuery("select 1 from dual");
        dataSource.setMaxAge(7000000L);
        dataSource.setRollbackOnReturn(true);
        dataSource.setCommitOnReturn(true);  */
        return dataSource;
    }
    @Bean
    public Logger getLogger(){
        try {
            LogManager.getLogManager().readConfiguration(
                    getClass().getClassLoader().getResourceAsStream("logging.properties"));
        } catch (IOException e) {
            System.err.println("Could not setup logger configuration: " + e.toString());
        }
        return Logger.getLogger("Application getLogger");
    }
    @Bean
    public EntityManager getEntityManager(){
        return new EntityManager(getDataSource());
    }

    @Bean
    public EntityManagerService getEntityManagerService(){
        return new EntityManagerService(getEntityManager());
    }
}