package com.data;


import com.data.domain.BaseUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Doc: https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html
 * api: https://docs.spring.io/spring-boot/docs/current/api/
 */

@Slf4j
/**
 * 指定JUnit的Runner，以便支持-集成测试
 * SpringRunner 是SpringJUnit4ClassRunner的别名
 * */
@RunWith(SpringRunner.class)
/**
 * 可以不指定参数，spring会搜索主配置，找到@SpringBootApplication or @SpringBootConfiguration后加入
 *
 * @deprecated 之前是使用@ContextConfiguration(classes=…​)，默认配置SpringBootContextLoader
 * SpringBootTest 会加载程序上下文，还会开启日志、加载外部属性（配置文件等），处理方式与生产环境更接近
 *
 * SpringApplicationConfiguration，已经被替换掉
 * Todo: 使用也行? @SpringApplicationConfiguration(classes = SpringBootSampleApplication.class)
 */
@SpringBootTest(classes = MyworkApplication.class)  /** 传入application，主要 @configure的类？*/
public class A1_BaseTest {

    ///////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 使用 mockit
     *
     * 会替代后台依赖的bean
     * 这里的例子只是显式引用
     */
    @MockBean
    private BaseUser user;
    @Test
    public void exampleTest2() {
        /**
         * import static org.mockito.BDDMockito.*;
         */
        given(user.getAge()).willReturn(100);
        /**
         * import static org.assertj.core.api.Assertions.*
         **/
        assertThat(user.getAge()).isEqualTo(100);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 环境变量测试
     * https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html#boot-features-environment-test-utilities
     */
    @Autowired
    private Environment env;

    @Autowired
    private ConfigurableEnvironment env2;

    @Test
    public void loadProperties() {
        /** 测试环境中进行配置 */
        EnvironmentTestUtils.addEnvironment(env2, "org=Spring", "name=Boot");

        /** 测试中配置的，可以立即读取检查 */
        assertEquals("Spring", env2.getProperty("org"));
        /** 在ConfigurableEnvironment中配置的，在Environment中也可以读取 */
        assertEquals("Spring", env.getProperty("org"));

        /** 从配置文件中读取的 */
        assertEquals("abcde", env.getProperty("settings.address"));
    }

    /**
     * 访问多行字符串
     *
     * 其他方案：http://www.itstrike.cn/Question/c3a4a3fb-eab9-4728-8d79-aec2df33ada9.html
     */
    @Test
    public void loadStringLines() throws Exception {
        log.debug("load string: {}", env.getProperty("test.product_sale"));
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     *  捕捉：System.out and System.err
     */
    @Rule
    public OutputCapture capture = new OutputCapture();

    @Test
    public void testName() throws Exception {
        System.out.println("Hello World!");
        assertThat(capture.toString(), containsString("World"));
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
