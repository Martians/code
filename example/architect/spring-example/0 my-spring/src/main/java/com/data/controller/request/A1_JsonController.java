package com.data.controller.request;

import com.data.domain.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import com.data.util.DateTime;

import javax.swing.text.html.parser.Entity;
import java.util.*;

/*
    api：https://docs.spring.io/spring/docs/5.0.3.RELEASE/javadoc-api/
        search：org.springframework.web.bind.annotation

    1. 使用REST工具进行测试，可以看得更清楚（chrome的rest插件）
    2. curl -v -H "Content-Type:application/json;charset=UTF-8" -X POST --data '{"name":"good122223","id":123}' http://192.168.36.9:8080/


    curl http://192.168.36.9:8080/json/map_object -X POST --data '{"xx":"x--","yy": {"name":"good122223","id":123}}' -H "Content-Type:application/json" -v
1
json串内层参数需要符合json格式规范，最后的ip是目的主机的地址和端口号
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
class DatePeriod {
    public DatePeriod(String start, String end) {
        this.start = DateTime.parse(start);
        this.end = DateTime.parse(end);
    }
    private Date start;
    private Date end;
};
@Data
@AllArgsConstructor
@NoArgsConstructor
class RequestWrapper {
    private Long unit;
    private DatePeriod period;
    private List<String> products;
}

/**
 * 将RequestBody中的请求，直接转换为Bean类、类数组、Map
 *
 * 请求类型：contentType: application/json
 * 将通过HttpMessageConverter解析
 *
 * 函数名是什么无关紧要
 * */
@Slf4j
@RestController
@RequestMapping(path="/json")
public class A1_JsonController {

    /**
     * @RequestBody 处理：Content-Type: application/json, application/xml
     * 通过HttpMessageConverters，把HTTP request body，转换为 object type
     */

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 解析为单个object
     *
     * 可以只填充部分参数，甚至不带任何参数
         curl http://192.168.36.9:8080/json/type -X POST -H "Content-Type:application/json" -v --data '{"name":"good122223","id":123}'
         curl http://192.168.36.9:8080/json/type -X POST -H "Content-Type:application/json" -v --data '{"name":"good122223"}'
         curl http://192.168.36.9:8080/json/type -X POST -H "Content-Type:application/json" -v --data '{}'
     */
    @RequestMapping(value = "/type")
    User get4(@RequestBody User user) { /** 直接将Json转换为 type class */
        return user;
    }

    /**
     * 多个object
     *
         curl http://192.168.36.9:8080/json/type_list -X POST -H "Content-Type:application/json" -v \
            --data '[{"name":"good122223","id":123}, {"name":"2222","id":123}]'
     */
    @RequestMapping(value = "/type_list")
    List<User> get5(@RequestBody List<User> list) { /** 直接将Json转换为 class 数组 */
        return list;
    }

    /**
     * 保存在string中，进行解析
     *
         curl http://192.168.36.9:8080/json/body -X POST -H "Content-Type:application/json" -v \
            --data '{"body":"good122223","idaa":123}'
     */
    @RequestMapping(value = "/body")
    String get5(@RequestBody String body) {
        return body;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 保存在string中，将json转换为 object
     *
         curl http://192.168.36.9:8080/json/body_json -X POST -H "Content-Type:application/json" -v \
            --data '{"name":"222"}'
     */
    @RequestMapping(value = "/body_json")
    User get6(@RequestBody String body) {
        User user = new Gson().fromJson(body, User.class);
        return user;
    }

    /**
     * 保存在string中，进行为object list
     *
     curl http://192.168.36.9:8080/json/body_json_list -X POST -H "Content-Type:application/json" -v \
         --data '[{"name":"111"}, {"name":"222"}]'
     */
    @RequestMapping(value = "/body_json_list")
    List<User> get7(@RequestBody String body) {
        List<User> list = new Gson().fromJson(body, new TypeToken<ArrayList<User>>() {}.getType());
        return list;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 为了快速解析request，将请求放在一个中间类中，由spring自动解析
     *
     * 注意：这里的wrapper不能是inner class
     *
     curl http://192.168.36.9:8080/json/body_json_class -X POST --data '
     {
         "period":{
             "start": "2018-01-01",
             "end": "2018-01-02"
         },
         "unit": 100,
         "products":[
             "商品SN",
             "竞品1SN",
             "竞品2SN"
         ]
     }
     ' -H "Content-Type:application/json" -v
     */
    @RequestMapping(value = "/body_json_class")
    RequestWrapper get8(@RequestBody RequestWrapper request) {
        return request;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 保存为 string map
     *
     curl http://192.168.36.9:8080/json/map_string -X POST -H "Content-Type:application/json" -v --data '{"map1":"111","map2":123}'
     */
    @RequestMapping(value = "/map_string")
    String get5(@RequestBody Map<String, String> map) {
        String json = new Gson().toJson(map);
        return "== " + map.size() + ", " + json;
    }

    /**
     * 先转换为object
     *
     curl http://192.168.36.9:8080/json/map_object -X POST --data '
     {
         "period":{
             "start": "2018-01-01",
             "end": "2018-01-02"
         },
         "unit": 100,
         "products":[
             "商品SN",
             "竞品1SN",
             "竞品2SN"
         ]
     }
     ' -H "Content-Type:application/json" -v
     */
    @RequestMapping(value = "/map_object")
    Map<String, Object> get6(@RequestBody Map<String, Object> params) {

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            // if (entry.getValue() instanceof Integer)
            Object object = entry.getValue();
            log.debug("{} : [{}] - {}", entry.getKey(),
                    object.getClass().getName(), object);
        }
        return params;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 错误做法：数据只能保存到一个body中去
     */
    @RequestMapping(value = "/multi")
    String get7(@RequestBody String xx,
                @RequestBody String yy)   /** 直接将json的内容解析到字符串 */
    {
        return xx + "-----------" + yy;
    }

    //////////////////////////////////////////////////////////////////////////////////
    // Todo: @SessionAttributes @ModelAttribute
}
