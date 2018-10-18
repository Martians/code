package com.data.controller.request;

import com.data.util.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 最基础的处理，展示一些可用的功能
 *
 * 外置配置
 *
 */

/*
准备工作：
    1. 在mysql中创建数据库
    2. 配置spring.jpa.hibernate.ddl-auto: create，或者直接在mysql中创建表格

执行命令：
    http://192.168.36.9:8080/base/value

    curl '192.168.36.9:8080/base/add?name=First&email=someemail@someemailprovider.com'
    curl '192.168.36.9:8080/base/all'
 */

@RestController
@RequestMapping(path="/base")
@ConfigurationProperties(prefix="start_prefix")
public class BaseController {

    /** 日志使用方式1：这里手动定义logger，使用的是 org.slf4j.Logger */
    private final Logger logger = LoggerFactory.getLogger(this.getClass()); // BaseController.class

    ////////////////////////////////////////////////////////////////////////////////////
    /**
     * 使用config
     */

    /** 在application.properties、命令行中，可以重新定义其值 */
    @Value("${name}")
    private String name;

    /** 对应于start_prefix生效 */
    private String address;
    public void setAddress(String address) {    /** 必须有setter，否则无法设置成功 */
        this.address = address;
    }

    @Autowired
    private Settings settings;

    @RequestMapping("/value")
    String value() {
        return "value-" + name + ", start.address-" + address + ", settings.address-" + settings.getAddress();
    }
    ////////////////////////////////////////////////////////////////////////////////////



    //Page<l_1_User> users = repository.findAll(new PageRequest(1, 20));
}
