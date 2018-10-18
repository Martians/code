//package com.data.controller;
//
//import com.data.domain.BaseUser;
//import com.data.repository.UserRepository1;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Date;
//import java.util.List;
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////////
///**
// * 在外部定义的两个类，用于让HttpConvert自动转换，Json <-> Class
// * 使用lombok自动生成，对应的成员函数
// */
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//class Period {
//    private Date start;
//    private Date end;
//};
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//class RequestWapper {
//    private Period period;
//    private Long unit;
//    private List<String> products;
//}
/////////////////////////////////////////////////////////////////////////////////////////////////////////
//
///**
// * 相对于BaseController，做了一些简化处理
// *
// * 日志使用方式2：使用lombok自动生成logger，https://projectlombok.org/features/log
// */
//@Slf4j
//@RestController
//@RequestMapping(path="/next")
//public class UserController {
//
//    @Autowired
//    private UserRepository1 userRepository;
//
//    //GetMapping 是 @RequestMapping(method=GET)的缩写
//    @GetMapping(path="/add")
//    public @ResponseBody
//    String addNewUser(@RequestParam String name,
//                      @RequestParam String email)
//    {
//        BaseUser u = new BaseUser(name, 0, email);
//        userRepository.save(u);
//
//        logger.debug("use common slf4j: add new user, name {}, email {}", name, email);
//        return "Saved";
//    }
//
//    @GetMapping(path="/all")
//    public @ResponseBody Iterable<BaseUser> getAllUsers() {
//        // This returns a JSON or XML with the users
//
//        List<BaseUser> list = userRepository.findAll();
//        logger.debug("list all, count: {}", list.size());
//        return list;
//    }
//
//    //////////////////////////////////////////////////////////////////////////////////////////////////////////
//    /**
//     * 这个无法成功
//       RequestWapper list2(@RequestBody Period period,
//                           @RequestBody Integer unit,
//                           @RequestBody List<String> products) {
//     */
//
//    /**
//     * 因为不需要从json -> class, 只是class -> json, response类可以是一个inner class
//     */
//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    class ListResult {
//        private Integer id;
//        private String name;
//    }
//
//    /**
//     * RequestBody不能同时解析多个类，只能是一个类，即使这个类有嵌套
//     * 方法1：使用wrapper class进行解析; Wrapper不能是inner class
//     *
//     curl http://192.168.36.9:8080/next/list -X POST --data '
//         {
//             "period":{
//             "start": "2018-01-01",
//             "end": "2018-01-02"
//         },
//         "unit": 100,
//         "products":[
//             "商品SN",
//             "竞品1SN",
//             "竞品2SN"
//         ]
//         }
//     ' -H "Content-Type:application/json" -v
//     */
//    @RequestMapping(value = "/list", method = RequestMethod.POST)
//    RequestWapper list1(@RequestBody RequestWapper request) {
//        log.debug("parse start: {}, unit: {}, list size: {}",
//                request.getPeriod().getStart(),
//                request.getUnit(),
//                request.getProducts().size());
//        return request;
//    }
//
//    /**
//     curl http://192.168.36.9:8080/next/object -X POST --data '
//         {
//         "period":{
//             "start": "2018-01-01",
//             "end": "2018-01-02"
//         },
//         "unit": 100,
//         "products":[
//             "商品SN",
//             "竞品1SN",
//             "竞品2SN"
//         ]
//         }
//     ' -H "Content-Type:application/json" -v
//     */
//    @RequestMapping(value = "/list2", method = RequestMethod.POST)
//    String list2(@RequestBody String body) {
//        //HashMap<String,String> map = new Gson().fromJson(payload, new TypeToken<HashMap<String, String>>(){}.getType());
//        log.debug("list body: {}", body);
//        return body;
//    }
//    //////////////////////////////////////////////////////////////////////////////////////////////////////////
//}
