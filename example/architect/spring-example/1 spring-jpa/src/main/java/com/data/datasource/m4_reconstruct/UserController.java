package com.data.datasource.m4_reconstruct;

import com.data.domain.user.User;
import com.data.domain.user.UserRepository;
import com.data.util.StringFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
 * 参考例子：
 * 1. http://kimrudolph.de/blog/spring-datasource-routing
 *      代码：https://github.com/krudolph/spring-datasource-routing
 *
 * 2. https://github.com/jknecht/multitenant-spring-jpa，这里没有用 spring boot，只是用到 spring
 *      该例子用static的方式，绑定了 Locations和 tenants 两个数据源
 *      然后 Locations注册了动态 DataSource，根据在 tenants表中查到的数据库名称，重新建立多个 DataSource，然后切换上去
 *      最终还是用到了jpa的框架
 *
 * 3. http://kimrudolph.de/blog/spring-datasource-routing
 *      该方式比较好，但是未测试成功
 *
 * 代码修改：
 * 1. SpringBootApplication中，关闭数据库自动配置
 * 2. GeneratedValue 中不能使用AUTO，要使用 INDENTIFY
 *      否则，第一次生成数据库的时候，会报错： Table 'db2.hibernate_sequence' doesn't exist
 *      MySQL不支持这种模式，因为这里关闭了数据库自动配置，所以 没有做好配置
 *      这里可以修改一个默认配置，做 spring 所做的事情，或者根据情况自行调整吧
 *
 * Note：
 * 1. 表必须是已经存在
 *      只有初始链接时，选定的 默认数据库链接时，会自动创建 Table，后续切换时已经错过了这个时机
 *
 * http://127.0.0.1:8080/multi-4/1
 * http://127.0.0.1:8080/multi-4/2
 * http://127.0.0.1:8080/multi-4/switch
 */
@Profile("multi-4")
@Slf4j
@RestController
@RequestMapping(path="/multi-4")
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping(path="/1")
    public User get1() {
        log.debug("m4_reconstruct");
        DataSourceContextHolder.set("db1");

        User user = new User(1L, "11", 11, "sss");
        userRepo.save(user);
        return userRepo.findById(1L);
    }

    @GetMapping(path="/2")
    public User get2() {
        log.debug("m4_reconstruct");
        DataSourceContextHolder.set("db2");

        User user = new User(2L, "22", 22, "sss");
        userRepo.save(user);
        return userRepo.findById(2L);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 切换数据源，可以访问成功
     */
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
}
