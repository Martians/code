package com.data.source.multi_1_jdbc;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Working with Multiple Databases in Spring
 *
 * https://www.infoq.com/articles/Multiple-Databases-with-Spring-Boot
 */
@Profile("multi-1")
@Configuration
public class DataSourceConfig {
    @Bean(name = "db1")
    /**
     * 自定义bean的限定符，这样其他地方引用时，除了使用bean id，也可以直接使用 限定符 来引用bean
     */
    @Qualifier("db1")
    @ConfigurationProperties(prefix = "spring.db1")
    /**
     * 配置为默认数据库
     */
    @Primary
    public DataSource db1DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "db2")
    @Qualifier("db2")
    @ConfigurationProperties(prefix = "spring.db2")
    public DataSource db2DataSource() {
        return DataSourceBuilder.create().build();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Primary
    @Bean(name = "db1-jdbc")
    /**
     * Qualifier表明这里引用的
     */
    public JdbcTemplate db1jdbcTemplate(@Qualifier("db1") DataSource dsMySQL) {
        return new JdbcTemplate(dsMySQL);
    }

    @Bean(name = "db2-jdbc")
    public JdbcTemplate db2jdbcTemplate(@Qualifier("db2") DataSource dsMySQL) {
        return new JdbcTemplate(dsMySQL);
    }
}
