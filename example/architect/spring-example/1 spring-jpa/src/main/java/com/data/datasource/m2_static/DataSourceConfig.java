package com.data.datasource.multi_3_static;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Slf4j
@Profile("multi-2")
@Configuration
public class DataSourceConfig {
    @Primary
    @Bean(name = "db1")
    @Qualifier("db1")
    @ConfigurationProperties(prefix = "spring.db1")
    public DataSource db1DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "db2")
    @Qualifier("db2")
    @ConfigurationProperties(prefix = "spring.db2")
    public DataSource db2DataSource() {
        return DataSourceBuilder.create().build();
    }

}
