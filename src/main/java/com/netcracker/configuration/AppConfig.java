package com.netcracker.configuration;

import com.netcracker.dao.manager.EntityManager;
import com.netcracker.dao.managerservice.EntityManagerService;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public DataSource getDataSource(){
        DataSource dataSource = new DataSource();
        dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        dataSource.setUrl("jdbc:oracle:thin:@sql.edu-netcracker.com:1251:XE");
        dataSource.setUsername("ODESSA_19");
        dataSource.setPassword("ODESSA_19");
        //dataSource.setUsername("ODESSA_18");
        //dataSource.setPassword("testpass");
        dataSource.setInitialSize(3);
        dataSource.setMaxActive(14);
        dataSource.setMaxIdle(12);
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
    public EntityManager getEntityManager(){
        return new EntityManager(getDataSource());
    }

    @Bean
    public EntityManagerService getManagerAPI(){
        return new EntityManagerService(getEntityManager());
    }
}
