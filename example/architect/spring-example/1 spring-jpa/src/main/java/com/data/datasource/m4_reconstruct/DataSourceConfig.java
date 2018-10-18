package com.data.datasource.m4_reconstruct;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Working with Multiple Databases in Spring
 *
 * https://www.infoq.com/articles/Multiple-Databases-with-Spring-Boot
 */
@Profile("multi-4")
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef="EntityManagerFactory_Reconstruct",
        transactionManagerRef="TransactionManager_Reconstruct",
        basePackages= {"com.data.domain.user"})
@Slf4j
/**
 * 因为这里关闭了 数据库自动配置，所以这里的设置有很大不同
 *
 * 此时 要自动创建 EntityManagerFactoryBuilder，检查是否有配置遗漏的地方
 */
public class DataSourceConfig {

    @Autowired(required = false)
    private PersistenceUnitManager persistenceUnitManager;

    /**
     * 相当于进行一些公共配置
     *
     * 这里应该设置为 spring.db1 ？
     */
    @Bean
    @Primary
    @ConfigurationProperties("spring.jpa")
    public JpaProperties customerJpaProperties() {
        return new JpaProperties();
    }

    @Bean
    @Qualifier("db1")
    @ConfigurationProperties(prefix = "spring.db1")
    public DataSource db1DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Qualifier("db2")
    @ConfigurationProperties(prefix = "spring.db2")
    public DataSource db2DataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 为了使 routeDataSource 中能够添加 db1DataSource、db2DataSource，需要修改配置
     *
     * 1. 关闭自动配置机制，EntityManager都有用户自行配置
            @SpringBootApplication(exclude = {
                DataSourceAutoConfiguration.class,
                DataSourceTransactionManagerAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class })

          否则报错：
                The dependencies of some of the beans in the application context form a cycle:

     * 2. 通过Autowired，由容器类配置
            @Autowired
            @Qualifier("db1")
            private DataSource source;

          此方式仍然报错：
            Requested bean is currently in creation: Is there an unresolvable circular reference?
     */
    @Bean
    @Primary
    public DataSource routeDataSource() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        DataSource source = db1DataSource();
        targetDataSources.put("db1", source);
        targetDataSources.put("db2", db2DataSource());

        DataSourceRouter clientRoutingDatasource = new DataSourceRouter();
        clientRoutingDatasource.setTargetDataSources(targetDataSources);
        clientRoutingDatasource.setDefaultTargetDataSource(source);
        return clientRoutingDatasource;
    }

    @Bean(name = "EntityManagerFactory_Reconstruct")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory (
            final JpaProperties customerJpaProperties) {

        EntityManagerFactoryBuilder builder =
                createEntityManagerFactoryBuilder(customerJpaProperties);

        return builder.dataSource(routeDataSource())
                .packages("com.data.domain.user")
                .persistenceUnit("customerEntityManager")
                .build();
    }

    @Bean(name = "TransactionManager_Reconstruct")
    @Primary
    public JpaTransactionManager transactionManager (
            @Qualifier("EntityManagerFactory_Reconstruct") final EntityManagerFactory factory) {
        return new JpaTransactionManager(factory);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 因为关闭了自动配置，在这里生成 EntityManagerFactoryBuilder
     */
    private EntityManagerFactoryBuilder createEntityManagerFactoryBuilder(
            JpaProperties customerJpaProperties) {
        JpaVendorAdapter jpaVendorAdapter =
                createJpaVendorAdapter(customerJpaProperties);
        return new EntityManagerFactoryBuilder(jpaVendorAdapter,
                customerJpaProperties.getProperties(), this.persistenceUnitManager);
    }

    private JpaVendorAdapter createJpaVendorAdapter(
            JpaProperties jpaProperties) {
        AbstractJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(jpaProperties.isShowSql());
        adapter.setDatabase(jpaProperties.getDatabase());
        adapter.setDatabasePlatform(jpaProperties.getDatabasePlatform());
        adapter.setGenerateDdl(jpaProperties.isGenerateDdl());
        return adapter;
    }
}
