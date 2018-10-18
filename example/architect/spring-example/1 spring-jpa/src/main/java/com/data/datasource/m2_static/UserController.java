package com.data.datasource.m2_static;

import com.data.domain.depart.Department;
import com.data.domain.depart.DepartmentRepository;
import com.data.domain.user.User;
import com.data.domain.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 简介：Static方式，将Entity 和 DataSource 绑定
 *
 * 参考例子：
 *      http://blog.csdn.net/ABAP_Brave/article/details/52980885
 *      http://roufid.com/spring-boot-multiple-databases-configuration/
 *          code：https://github.com/RadouaneRoufid/spring-boot/blob/master/spring-boot-multi-database-tutorial/src/main/java/com/roufid/tutorial/configuration/MysqlConfiguration.java
 *
 *      http://www.baeldung.com/spring-data-jpa-multiple-databases
 *          code：https://github.com/eugenp/tutorials/blob/master/persistence-modules/spring-jpa/src/main/java/org/baeldung/config/PersistenceJPAConfig.java
 *              以及test部分代码
 *
 * 1. 通过替换JPA底层的EntitiManager，仍然使用 JPA 的机制
 * 2. 每个package绑定一个数据源，为数据源单独进行配置，只影响其对应的 Entity -> Table
 *
 * Note：
 * 1. 要求Repository也要放在这个package之下，绑定数据源？否则 UserController 找不到对应的bean，？？
 *
 * http://127.0.0.1:8080/multi-2/1
 * http://127.0.0.1:8080/multi-2/2
 */

@Profile("multi-2")
@Slf4j
@RestController
@RequestMapping(path="/multi-2")
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private DepartmentRepository departRepo;

    @GetMapping(path="/1")
    public User get1() {
        log.debug("m2_static");

        User user = new User(1L, "11", 11, "sss");
        userRepo.save(user);
        return userRepo.getOne(1L);
    }

    @GetMapping(path="/2")
    public Department get2() {
        log.debug("m2_static");

        Department depart = new Department(1L, "11111");
        departRepo.save(depart);

        return departRepo.findById(1L);
    }

}
