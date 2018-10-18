package com.data.datasource.m3_dynamic;

import com.data.domain.user.User;
import com.data.domain.user.UserRepository;
import com.data.util.StringFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
/**
 * 简介：利用 AbstractRoutingDataSource；并最大程度的利用了 Spring的自动化处理，没有禁用任何 SpringBootApplication
 *      在容器中只生成 DynamicDataSource，通过这个“代理”，切换到当前选择的实际 DataSource 上
 *
 * 参考例子：
 * 1. http://412887952-qq-com.iteye.com/blog/2303075
 *      从配置中创建数据源的方法 buildDataSource ；创建时机（继承EnvironmentAware即可）
 *      添加注解的方法 registerBeanDefinitions；创建时机：ImportBeanDefinitionRegistrar；最终实现AOP
 *
 * 2. https://spring.io/blog/2007/01/23/dynamic-datasource-routing
 *
 * Todo：
 * 1. 切面的使用，通过注解切换数据源
 *      http://412887952-qq-com.iteye.com/blog/2303075
 *
 * Note:
 * 1. 此方法没有很好的解决 一个函数(一个事务中)多数据源切换问题
 *      http://blog.csdn.net/ErixHao/article/details/52133153
 *      http://blog.csdn.net/zuoshuaiax/article/details/72306994?locationNum=14&fps=1
 *      https://www.cnblogs.com/chen-msg/p/7485701.html
 *
 *
 * http://127.0.0.1:8080/multi-3/1
 * http://127.0.0.1:8080/multi-3/2
 * http://127.0.0.1:8080/multi-3/switch
 * http://127.0.0.1:8080/multi-3/1_2
 * http://127.0.0.1:8080/multi-3/2_2
 * http://127.0.0.1:8080/multi-3/3
 */
@Profile("multi-3")
@Slf4j
@RestController
@RequestMapping(path="/multi-3")
public class UserController {

    @Autowired
    private UserRepository userRepo;

    /**
     * 三种方法切换 DataSource
     * 1. @TargetSource("db1")
     *      将 TargetSource 直接设置到 UserRepository 上是无效的，似乎因为 UserRepository 指示一个接口描述？？？？
     *
     * 2. DataSourceContextHolder.set("db1");
     *    使用此种方法，记得要将数据库设置回去
     *
     * 3. DataSourceAspect 中指定范围，指定 package 或者类、函数
     */

    /**
     * 使用 TargetSource
     */
    @TargetSource("db1")
    @GetMapping(path="/1")
    public List<User> get1() {
        log.debug("m3_dynamic");

        User user = new User(1L, "11", 11, "sss");
        userRepo.save(user);
        return userRepo.findAll();
    }

    @TargetSource("db2")
    @GetMapping(path="/2")
    public List<User> get2() {
        log.debug("m3_dynamic");
        //DataSourceContextHolder.set("db2");

        User user = new User(2L, "22", 22, "sss");
        userRepo.save(user);
        return userRepo.findAll();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 使用 package aspect
     */
    @GetMapping(path="/2_package")
    public List<User> get2_package() {
        log.debug("m3_dynamic");

        return userRepo.findAll();
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 在一个函数中切换事务，无法生效！！！！
     *
     * 做过的尝试：
     * 1. 使用 Transactional，NOT_SUPPORTED、REUQIRED_NEW 等
     *      Propagation.REQUIRES_NEW: 没有任何反应
     *      NOT_SUPPORTED：有尝试进行 determineCurrentLookupKey 的动作，但是数据源并未切换
     * 2. 将获取的过程封装在函数中，然后修改函数的 Transactional
     */
    @Transactional(propagation= Propagation.NOT_SUPPORTED)
    @GetMapping(path="/switch")
    public String get_switch() {
        List<User> list, list1, list2;
        String s1, s2;

        DataSourceContextHolder.set("db1");
        list = userRepo.findAll();
        log.debug("origin test: size: {}, {}", list.size(), StringFormat.list(list));


        DataSourceContextHolder.set("db2");
        list2 = userRepo.findAll();
        s2 = StringFormat.list(list2);
        log.debug("switch db2: size: {}, {}", list.size(), s2);

        DataSourceContextHolder.set("db1");
        list1 = userRepo.findAll();
        s1 = StringFormat.list(list1);
        log.debug("switch db1: size: {}, {}", list.size(), s1);
        return s1 + "\r\n" + s2;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 使用 DataSourceContextHolder
     */
    @GetMapping(path="/1_2")
    public List<User> get1_2() {
        DataSourceContextHolder.set("db1");

        log.debug("m3_dynamic");
        return userRepo.findAll();
    }

    @GetMapping(path="/2_2")
    public List<User> get2_2() {
        DataSourceContextHolder.set("db2");

        log.debug("m3_dynamic");
        return userRepo.findAll();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @TargetSource("db3")
    @GetMapping(path="/3")
    public List<User> get3() {
        log.debug("m3_dynamic: source not exist");
        //DataSourceContextHolder.set("db2");

        User user = new User(3L, "33", 33, "sss");
        userRepo.save(user);
        return userRepo.findAll();
    }
}
