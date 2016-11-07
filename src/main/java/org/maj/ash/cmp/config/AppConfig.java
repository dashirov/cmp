package org.maj.ash.cmp.config;


import com.googlecode.objectify.ObjectifyFilter;
import org.maj.ash.cmp.dao.AccountServiceDao;
import org.maj.ash.cmp.dao.AccountServiceHSQLDao;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;

/**
 * @author shamik.majumdar
 */

@Configuration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
@EnableConfigurationProperties
@EnableScheduling
@ConfigurationProperties
@ComponentScan(basePackages = {"org.maj.ash"})
public class AppConfig {

    @Bean
    @Profile("hsql")
    public DataSource createDataSource(){
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("h2-schema.sql")
                .build();
    }

    @Bean
    @Profile("hsql")
    public AccountServiceDao accountServiceHSQLDao(){
        AccountServiceHSQLDao dao = new AccountServiceHSQLDao();
        dao.setDatasource(createDataSource());
        return dao;
    }

    @Bean
    @Profile("gae")
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new ObjectifyFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }
}