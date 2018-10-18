package com.data;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


/**
 * import 这几个之后，就可以使用testpage2了，书写方法简化很多
 * 相比于上边，增加了static
 */
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.Assert.*;


/**
 * 使用 Spring Mock MVC方法，在近似真实的模拟Servlet容器里测试控制器，而不用实际启动应用服务器
 *
 * 例子：
 *      https://www.cnblogs.com/xiaohui123-com/p/6547026.html
 *      https://www.cnblogs.com/0201zcr/p/5756642.html
 *
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
/**
 * 在这里，此语句可有可无？去掉了似乎不影响
 *
 * 开启Web测试，声明这里创建的应用程序上下文类型
 * 使用spring应用程序上下文来构建Mock MVC，上下文中包含已经配置好的多个控制器
 *
 * spring in action, 69
 */
@WebAppConfiguration
@AutoConfigureMockMvc  /** 定义了这个，就不需要再专门生成 mockMvc 了*/

/**
 * 可以指定一个不同的配置文件？
 */
//@ContextConfiguration(locations={"classpath:spring/applicationContext-*xml"})
public class A2_MockMVCTest {

    @Autowired
    private MockMvc mock;

    ///////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 方式1：不定义 @AutoConfigureMockMvc
     * 需要执行下列指令，否则无法成功
     */
    /*
    @Autowired
    private WebApplicationContext webContext;

    @Before
    public void setupMockMvc() {
        assert(false);
        mockMvc = MockMvcBuilders
            .webAppContextSetup(webContext)
            .build();
    }
    */
    /////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 完整写法，MockMvcRequestBuilders、MockMvcResultMatchers
     */
    @Test
    public void testpage1() throws Exception {
        mock.perform(MockMvcRequestBuilders.get("/home"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * #############################################################
     * 主要参考函数
     *
     *
     * 简化写法：静态导入，import static web.servlet.request.MockMvcRequestBuilders.*等之后
     */
    @Test
    public void testpage2() throws Exception {
        String result = mock.perform(get("/home"))
                /**
                 * 打印请求详细类型
                 */
                .andDo(print())

                .andExpect(status().isOk())
                /**
                 * 返回 return 的 context部分内容
                 */
                .andReturn().getResponse().getContentAsString();
        log.debug("return: {}", result);
    }

    /*
    @Test
    public void homePage() throws Exception {
        mock.perform(get("/readingList"))
                .andExpect(status().isOk())
                .andExpect(view().name("readingList"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", is(empty())));
    }
    */
    /////////////////////////////////////////////////////////////////////////////////////////
    /**
     * get请求
     *
     * 构建请求头部
     */
    @Test
    public void testpage3() throws Exception {
        mock.perform(get("/simple/path_param/type/test_name")
                        /** header、param、cookie */
                        .accept(MediaType.TEXT_PLAIN)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("simple-path-param [test_name]"));
    }

    /**
     * post请求
     */
    @Test
    public void testpage4() throws Exception {
        mock.perform(post("/simple/path_param/type/test_name")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());
    }

    @Test
    public void testpage5() throws Exception {
        mock.perform(post("/simple/path_param/type/test_name")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "name1")
                        .param("id", "222"))
                .andExpect(status().isOk());
    }
    /////////////////////////////////////////////////////////////////////////////////////////
}
