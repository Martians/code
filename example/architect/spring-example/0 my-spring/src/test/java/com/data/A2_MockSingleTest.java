package com.data;

import com.data.controller.request.A0_SimpleController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.beans.factory.annotation.*;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
/**
 * 通常只用来测试一个controller
 *
 * 连带配置了@AutoConfigureMockMvc、@AutoConfigureWebMvc等
 *
 * 自动配置，限制只关注如下的bean：
 *      @Controller, @ControllerAdvice, @JsonComponent, Filter, WebMvcConfigurer and HandlerMethodArgumentResolver
 *
 */
@WebMvcTest(A0_SimpleController.class)
/**
 * 其他说明：
 * 如果要测试的是BaseController，因为WebMvcTest不会自动扫描所有的bean，
 * 而BaseController会引用其他的类，需要手动导入
 *      @Import(Settings.class)
 */
public class A2_MockSingleTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void testExample() throws Exception {
        mvc.perform(get("/simple/1")
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("simple-111"));
    }
}
