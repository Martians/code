package com.data.datasource.m2_static;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.*;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
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
        /**
         * 对应于下边用于生成 LocalContainerEntityManagerFactoryBean 的类的bean
         * */
        entityManagerFactoryRef="entityManagerFactory_DB1",
        /**
         * 对应于下边用于生成 PlatformTransactionManager 的类的bean
         * */
        transactionManagerRef="transactionManager_DB1",
        /**
         * 设置影响的包范围
         *
         * 这个 package 应该设置 Repository的目录，还是 domain 的目录？
         * */
        basePackages= {"com.data.domain.user"})
/**
 * 1. 该类的作用，生成 EntityManager相关
 *      LocalContainerEntityManagerFactoryBean
 *      PlatformTransactionManager （JpaTransactionManager是其子类）
 *      EntityManager (可选)
 *
 * 2. 在一个配置上，全部配置为 Primary
 */
@Slf4j
public class DB1_Config {

    @Autowired
    @Qualifier("db1")
    private DataSource source;

    /**
     * 是否设置此函数，都可以正常运行
     *
     * 1. 设置了：生成的 EntityManager 将会封装在代理中 ？
     * 2. 不设置：
     */
    @Primary
    @Bean(name = "entityManager_DB1")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactory(builder).getObject().createEntityManager();
    }

    /**
     * 这里没有设置Vendor，实际可以设置
     */
    @Primary
    @Bean(name = "entityManagerFactory_DB1")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory (EntityManagerFactoryBuilder builder) {
        return builder
                /**
                 * 设置使用的 DataSource
                 */
                .dataSource(source)
                /**
                 * 从 DataSource 中，反推其从配置文件得到的配置
                 */
                .properties(getVendorProperties(source))
                /**
                 * 设置影响的包范围
                 */
                .packages("com.data.domain.user")
                /**
                 * 设置 PersistenceUnit的名称
                 */
                .persistenceUnit("PersistenceUnit_DB1")
                .build();
    }

    /**
     * 方式一：该函数同上，只是有不同的参数，效果是相同的
     *
     * 这里也可以返回 JpaTransactionManager
     */
    @Primary
    @Bean(name = "transactionManager_DB1")
    public PlatformTransactionManager transactionManager(EntityManagerFactoryBuilder builder) {
        log.debug("transactionManager_DB1 created");
        return new JpaTransactionManager(entityManagerFactory(builder).getObject());
    }

    /**
     * 方式二：该函数同上，只是有不同的参数，效果是相同的
     */
//    @Primary
//    @Bean(name = "transactionManager_DB1")
//    public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory_DB1") EntityManagerFactory entityManagerFactory) {
//        log.debug("transactionManager_DB1 created");
//        return new JpaTransactionManager(entityManagerFactory);
//    }

    /**
     * 方式三
     */
//    @Primary
//    @Bean
//    public PlatformTransactionManager userTransactionManager() {
//
//        JpaTransactionManager transactionManager
//                = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(
//                userEntityManager().getObject());
//        return transactionManager;
//    }

    /**
     * 后置bean，捕获所有异常，转换为spring的异常
     *
     * 是否是必须？？？？
     */
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    @Autowired
    private JpaProperties jpaProperties;

    /**
     * 方式1：从 DataSource 中，反推其从配置文件得到的配置
     */
    private Map<String, String> getVendorProperties(DataSource dataSource) {
        return jpaProperties.getHibernateProperties(dataSource);
    }

    /**
     * 方式2：获取配置的另一种方式
     */
//    private Map<String, Object> hibernateProperties() {
//
//        Resource resource = new ClassPathResource("hibernate.properties");
//        try {
//            Properties properties = PropertiesLoaderUtils.loadProperties(resource);
//            return properties.entrySet().stream()
//                    .collect(Collectors.toMap(
//                            e -> e.getKey().toString(),
//                            e -> e.getValue())
//                    );
//        } catch (IOException e) {
//            return new HashMap<String, Object>();
//        }
//    }

    /**
     * 方式3:：读取配置
     */
//    外部：@PropertySource({ "classpath:persistence-mysql.properties" })
//
//    @Autowired
//    private Environment env;
//
//    final Properties additionalProperties() {
//        final Properties hibernateProperties = new Properties();
//        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
//        hibernateProperties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
//        hibernateProperties.setProperty("hibernate.cache.use_second_level_cache", env.getProperty("hibernate.cache.use_second_level_cache"));
//        hibernateProperties.setProperty("hibernate.cache.use_query_cache", env.getProperty("hibernate.cache.use_query_cache"));
//        // hibernateProperties.setProperty("hibernate.globally_quoted_identifiers", "true");
//        return hibernateProperties;
//    }
}
