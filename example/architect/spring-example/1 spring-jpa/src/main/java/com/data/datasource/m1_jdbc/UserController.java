package com.data.datasource.m1_jdbc;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 简介：JDBC方式，不使用JPA
 *
 * 参考例子：
 *      https://www.infoq.com/articles/Multiple-Databases-with-Spring-Boot
 *      https://www.liaoxuefeng.com/article/001484212576147b1f07dc0ab9147a1a97662a0bd270c20000
 *      http://blog.csdn.net/whereismatrix/article/details/54883065
 *
 *
 * 1. 直接使用 JdbcTemplate 进行数据库操作
 * 2. 配置不同的 DataSource，再生成不同的 JdbcTemplate
 *
 *
 * http://127.0.0.1:8080/multi-1/1
 * http://127.0.0.1:8080/multi-1/2
 */

@Profile("multi-1")
@Slf4j
@RestController
@RequestMapping(path="/multi-1")
public class UserController {

    @Autowired
    @Qualifier("db1-jdbc")
    private JdbcTemplate jdbc1;

    @Autowired
    @Qualifier("db2-jdbc")
    private JdbcTemplate jdbc2;

    @GetMapping(path="/1")
    public List get1() {
        log.debug("m1_jdbc");
        String sql = "select * from user_table";
        return jdbc1.queryForList(sql);
    }

    @GetMapping(path="/2")
    public List get2() {
        log.debug("m1_jdbc");
        String sql = "select * from user_table";
        return jdbc2.queryForList(sql);
    }

}
