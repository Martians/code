package com.data.datasource.m3_dynamic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Profile("multi-3")
@Configuration
@Slf4j
public class DataSourceConfig {

    @Autowired
    private Environment env;

    /**
     * 默认使用的数据库的类型
     *      可选的有
     */
    private static final Object DATASOURCE_TYPE_DEFAULT = "org.apache.tomcat.jdbc.pool.DataSource";
    private ConversionService conversionService = new DefaultConversionService();
    private Map<Object, Object> targetDataSources = new HashMap<>();

    /**
     * 实际上有个更快的方法
     *      指定DataSource的名字，注册到 DataSourceRouter 中即可，DataSource还是按照之前的方式进行自动导入
     *
     *      DataSourceRouter通过key查找DataSource，如果发现Key是个String，会直接根据name进行 jdni dataSource lookup
     *      但测试未成功（即使在数据源上设置 DataSource的ndni名字， 并且设置Profile的DataSource自动导入）
     *          见 AbstractRoutingDataSource::resolveSpecifiedDataSource
     *
     *      该方式在使用xml定义DataSource的地方很容易实现
     *          https://www.cnblogs.com/Gyoung/p/7147677.html
     *          http://blog.csdn.net/fangdengfu123/article/details/70139644
     */
    @Bean
    @Primary
    public DataSource get() {
        DataSourceRouter route = new DataSourceRouter();

        try {
            DataSource source = prefixSource("db1");
            route.setDefaultTargetDataSource(source);

            prefixSource("db2");

            route.setTargetDataSources(targetDataSources);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return route;
    }

    private DataSource prefixSource(String prefix) throws Exception {
        DataSource source = createSource(prefix);
        targetDataSources.put(prefix, source);
        return source;
    }

    /**
     * create DataSource for prefix spring.prefix
     *
     * RelaxedPropertyResolver
     */
    private DataSource createSource(String prefix) throws Exception {
        RelaxedPropertyResolver rpr = new RelaxedPropertyResolver(env, "spring." + prefix);

        Map<String, Object> map = rpr.getSubProperties(".");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            log.debug("key: {}, value: {}", entry.getKey(), entry.getValue());
        }
        /**
         * 占位符解析：
         * http://tmmh.iteye.com/blog/1905843
         */
//        String url_key = "spring." + prefix + ".url";
//        map.put("url", env.getProperty(url_key));
//        String cc = (String)map.get("url");


        if (map.get("url") == null
                || map.get("driver-class-name") == null) {
            throw new Exception("DataSource " + prefix + " not config!!!");
        }

/*
        rpr = new RelaxedPropertyResolver(env, "spring." + prefix + ".");
        String username = rpr.getProperty("username");
*/

        try {
            Class<? extends DataSource> dataSourceType = (Class<? extends DataSource>) Class.forName((String) DATASOURCE_TYPE_DEFAULT);

            /**
             * 方式1：所有参数都通过 RelaxedDataBinder 进行设置
             */
            DataSource source = DataSourceBuilder.create()
                    .type(dataSourceType)
                    .build();

            RelaxedDataBinder dataBinder = new RelaxedDataBinder(source);
            dataBinder.setConversionService(conversionService);
            dataBinder.setIgnoreNestedProperties(false);
            dataBinder.setIgnoreInvalidFields(false);
            dataBinder.setIgnoreUnknownFields(true);

            PropertyValues property = new MutablePropertyValues(map);;
            dataBinder.bind(property);
            return source;

            /**
             * 方式2：先设置DataSourceBuilder关注的部分参数，然后设置剩余部分
             */
/*
            String driverClassName = map.get("driver-class-name").toString();
            String url = map.get("url").toString();
            String username = map.get("username").toString();
            String password = map.get("password").toString();

            DataSource source = DataSourceBuilder.create()
                    .driverClassName(driverClassName)
                    .url(url)
                    .username(username)
                    .password(password)
                    .type(dataSourceType)
                    .build();

            RelaxedDataBinder dataBinder = new RelaxedDataBinder(source);
            dataBinder.setConversionService(conversionService);
            dataBinder.setIgnoreNestedProperties(false);
            dataBinder.setIgnoreInvalidFields(false);
            dataBinder.setIgnoreUnknownFields(true);

            // 这里有点问题，执行remove时，跳转到了 Collection的remove函数，显示无此函数，临时注释掉
            //map.remove("driver-class-name");
            //map.remove("url");
            //map.remove("username");
            //map.remove("password");

            PropertyValues property = new MutablePropertyValues(map);
            dataBinder.bind(property);
            return source;
*/

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager b1TM(DataSource source) {
        return new DataSourceTransactionManager(source);
    }

//    @Bean(name="b2")
//    public PlatformTransactionManager b2TM(DataSource source) {
//        return new DataSourceTransactionManager(source);
//    }

}
