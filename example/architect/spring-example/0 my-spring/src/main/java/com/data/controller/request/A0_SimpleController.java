package com.data.controller.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/*
    本例测试简单文本格式的请求

    api：https://docs.spring.io/spring/docs/5.0.3.RELEASE/javadoc-api/
        search：org.springframework.web.bind.annotation

    /////////////////////////////////////////////////////////////////////////////////////
    curl '192.168.36.9:8080/demo/add?name=First&email=someemail@someemailprovider.com'
    curl '192.168.36.9:8080/demo/all'

    /////////////////////////////////////////////////////////////////////////////////////
    http://192.168.36.9:8080/simple/1
    http://192.168.36.9:8080/simple/2
    http://192.168.36.9:8080/simple/3
    http://192.168.36.9:8080/simple/4
    http://192.168.36.9:8080/simple/5

    http://192.168.36.9:8080/simple/path_param/num/6
    http://192.168.36.9:8080/simple/path_param/type/string_type
    http://192.168.36.9:8080/simple/path_param/simple/6

    http://192.168.36.9:8080/simple/param/?id=1&name=long
    http://192.168.36.9:8080/simple/param/?id=2&name=long&email=long@126.com
    http://192.168.36.9:8080/simple/param/simple/?name=long&time=9000000
    http://192.168.36.9:8080/simple/param/default/?name=long&time=9000000

    curl http://192.168.36.9:8080/simple/head -H "Content-Type:application/json" -v
 */

/**
 * 演示：对请求参数的解析，Url部分的、head部分的
 *
 * 请求类型：Content-Type: text/plain;charset=UTF-8
 */
@Slf4j
@RestController
/** 这里可以也绑定一个URL的样式进行样式映射
 *  @RequestMapping(path="/simple/{path}")  路径：http://192.168.36.9:8080/simple/path_aa/1访问，赋予path样式内容为path_aa
 * */
@RequestMapping(path="/simple")
public class A0_SimpleController {

    ////////////////////////////////////////////////////////////////////////////////////
    @RequestMapping(value = "/1")
    public @ResponseBody    /** 使用RestController后，现在已经不需要这个注解了!!! */
    String simple1() {
        return "simple-111";
    }

    @RequestMapping(path = "/2")
    /** 这里使用path、value、不写前缀是一样的 */
    String simple2() {
        return "simple-222";
    }

    @RequestMapping(path = {"/3", "/4"})
    /** 同时定义两个端点 */
    String simple4() {
        return "simple-444";
    }
    ////////////////////////////////////////////////////////////////////////////////////

    /**
     * @GetMapping, @PostMapping, @PutMapping, @DeleteMapping, or @PatchMapping
     */
    @GetMapping(path = "/5")
    /** 相当于 @RequestMapping(method = RequestMethod.GET) */
    String simple5() {
        return "simple-555";
    }


    ////////////////////////////////////////////////////////////////////////////////////

    /**
     * 从request URL路径部分获取参数，REST风格的，似乎将被弃用？相当于定义了样式（类似于占位符）
     **************************/
    @Deprecated
    @RequestMapping("/path_param/num/{id}")
    /** 获取url中的数据，将URL中的字段，绑定到“id”这个标识上 */
    String param1(@PathVariable("id") int id) {
        return "simple-path-param [" + id + "]";
    }

    @RequestMapping("/path_param/type/{type-name}")
    /** 绑定指定了名字，可以指定与参数不同的名字，type-name - type */
    String param2(@PathVariable("type-name") String type) { /** 绑定为字符串类型 */
        return "simple-path-param [" + type + "]";
    }

    /** Todo: 同时绑定多个变量 */
    //@GetMapping(value = "/login/{name}&{pwd}")

    /** 简化版本，不在PathVariable中指定名字，默认使用与参数字面值，相同的名字作为样式名 */
    @RequestMapping("/path_param/simple/{id}")
    String param3(@PathVariable int id) {
        return "simple-path-param-left [" + id + "]";
    }
    ////////////////////////////////////////////////////////////////////////////////////

    /**
     * 从request URL param 部分提取参数, 处理简单类型的绑定
     * 处理：application/x-www-form-urlencoded类型的内容
     *************************/
    @RequestMapping("/param")
    String param3(@RequestParam("name") String name,  /** 指定name，可以指定为一个不同的名字 */
                  @RequestParam("id") Long id,
                  /** 指定是否必需；指定不存在时的默认值 */
                  @RequestParam(name = "email", required = false, defaultValue = "aa@email.com") String email) {
        return "simple-param, id: " + id + " name:" + name + ", email: " + email;
    }

    @RequestMapping("/param/simple/")
    String param4(@RequestParam String name,    /** 省略name，默认使用参数的字符名字 */
                  @RequestParam Long time) {
        return "simple-param-simple, name: " + name + " time: " + time;
    }

    @RequestMapping("/param/default/")
    /** 简化：不使用RequestParam，这个是默认配置的 */
    String param5(String name, Long time) {
        return "simple-param-simple, name: " + name + " time: " + time;
    }

    /** Todo: 将多个参数使用map方式保存下来 */
    //public boolean test(@RequestParam Map<String, String> fieldValueList) {

    ////////////////////////////////////////////////////////////////////////////////////

    /** 从request header 部分提取参数 *************************/
    @RequestMapping("/head")
    public String param6(@RequestHeader("content-type") String context) {
        return context;
    }

    //Todo：@CookieValue
}