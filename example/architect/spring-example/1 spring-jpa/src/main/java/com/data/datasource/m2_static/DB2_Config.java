package com.data.datasource.m2_static;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;

@Profile("multi-2")
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef="entityManagerFactory_DB2",
        transactionManagerRef="transactionManager_DB2",
        basePackages= {"com.data.domain.depart"})
@Slf4j
public class DB2_Config {

    @Autowired
    @Qualifier("db2")
    private DataSource source;

    @Primary
    @Bean(name = "entityManager_DB2")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactory(builder).getObject().createEntityManager();
    }


    @Bean(name = "entityManagerFactory_DB2")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory (EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(source)
                .properties(getVendorProperties(source))
                .packages("com.data.domain.depart")
                .persistenceUnit("PersistenceUnit_DB2")
                .build();
    }

    @Bean(name = "transactionManager_DB2")
    public PlatformTransactionManager transactionManager(EntityManagerFactoryBuilder builder) {
        log.debug("transactionManager_DB2 created");
        return new JpaTransactionManager(entityManagerFactory(builder).getObject());
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    @Autowired
    private JpaProperties jpaProperties;
    private Map<String, String> getVendorProperties(DataSource dataSource) {
        return jpaProperties.getHibernateProperties(dataSource);
    }
}
