package com.kravchenko.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by john on 4/16/17.
 */
@Configuration
@EnableTransactionManagement
public class HibernateConfig {

    @Autowired
    private Environment env;

//    @Autowired
//    private DataSource dataSource;

//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
//        driverManagerDataSource.setDriverClassName(env.getProperty("db.driver"));
//        driverManagerDataSource.setUrl(env.getProperty("db.url"));
//        driverManagerDataSource.setUsername(env.getProperty("db.username"));
//        driverManagerDataSource.setPassword(env.getProperty("db.password"));
//        return driverManagerDataSource;
//    }


}
