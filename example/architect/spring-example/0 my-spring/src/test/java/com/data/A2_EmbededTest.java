package com.data;

import com.data.domain.BaseUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.naming.Name;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

/**
 * 使用 Spring Mock MVC方法，在近似真实的模拟Servlet容器里测试控制器，而不用实际启动应用服务器
 *
 * 例子：
 *      https://segmentfault.com/a/1190000008952438
 */

@Slf4j
@RunWith(SpringRunner.class)
/**
 * 已经替代了 WebIntegrationTest
 * 表明希望Spring Boot为测试创建应用程序上下文，并会启动一个嵌入式的Servlet容器
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class A2_EmbededTest {

    /** 获取随机分配的端口号 */
    @Value("${local.server.port}")
    public int port;

    ///////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 使用RestTemplate
     *
     * 无法使用@Autowired，必须在使用时重新生成一个
     */
    @Test(expected=HttpClientErrorException.class)
    public void pageNotFound() {
        try {
            RestTemplate rest = new RestTemplate();
            rest.getForObject(
                    "http://localhost:{port}/bogusPage", String.class, port);
            fail("Should result in HTTP 404");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
            throw e;
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 使用TestRestTemplate，是RestTemplate的别名
     * 由@SpringBootTest自动创建出来
     *
     * api：https://docs.spring.io/spring-boot/docs/current/api/
     *      search：org.springframework.boot.test.web.client
     */
    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * 简单获取，不带参数
     */
    @Test
    public void exampleTest1() {
        String body = restTemplate.getForObject("/home", String.class);
        assertThat(body).isEqualTo("home");
    }

    /**
     * 使用参数
     */
    @Test
    public void exampleTest2() {
        String name = "abcd";
        Integer time = 11111;

        /** 从 A0_SimpleController 获取过来的实现 */
        String result = "simple-param-simple, name: " + name + " time: " + time;

        Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("name", name);
        uriVariables.put("time", time);


        /** url不能只是 /simple/param/simple */
        String url = "/simple/param/simple/?name={name}&time={time}";

        String body = restTemplate.getForObject(url, String.class, uriVariables);
        assertThat(body).isEqualTo(result);

        String body2 = restTemplate.getForObject(url, String.class, name, time);
        assertThat(body).isEqualTo(result);
    }

    /**
     * 获得Header
     */
    @Test
    public void exampleTest3() throws Exception {
        HttpHeaders headers = restTemplate.getForEntity("/home", String.class).getHeaders();
        //assertThat(headers.getLocation().toString(), containsString("myotherhost"));
    }

    /*
    @Bean
    public TestRestTemplate testRestTemplate(
            ObjectProvider<RestTemplateBuilder> builderProvider,
            Environment environment) {
        RestTemplateBuilder builder = builderProvider.getIfAvailable();
        TestRestTemplate template = builder == null ? new TestRestTemplate()
                : new TestRestTemplate(builder.build());
        template.setUriTemplateHandler(new LocalHostUriTemplateHandler(environment));
        return template;
    }
    */
}
