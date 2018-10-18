package com.data;

import com.data.domain.User;
import com.data.repository.DepartmentRepository;
import com.data.repository.UserRepository;
import com.data.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
    api：https://docs.spring.io/spring/docs/5.0.3.RELEASE/javadoc-api/
        search：org.springframework.web.bind.annotation
 **/

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
/** 这个似乎不起作用？ */
//@TestPropertySource(locations = {"classpath:application-test.yml"})
/** 执行完成后，会执行回滚操作 */
//@Transactional
/**
 * 这里只激活test条件下的配置
 */
@ActiveProfiles("test")
public class A3_JpaIntergration_Test {

    /**
     * ？？？？？？？？？？？？
     */
//    @Autowired(required=false)
//    private DataSource dataSource;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @BeforeClass
    public static void prepareClass() {
    }

    /**
     * 此处如果使用before，每个用例执行时都会调用，导致重复写入
     *
     * 方法1：使用@Test，不符合单元测试标准，导致测试依赖于执行顺序
     * 方法2：每个函数单独执行后，在after中执行清理操作
     * 方法3：每个测试函数，加上@Transactional
     */
    @Before
    public void before() {
        Constant.userList.stream().forEach(x -> userRepository.save(x));

        Constant.departList.stream().forEach(x -> departmentRepository.save(x));
        Constant.joinList.stream().forEach(x -> userRepository.save(x));
    }

    @After
    public void after() {
        userRepository.deleteAll();
        log.info("\n\n");
    }

    @Test
    /**
     * Transactional 使用该注解之后，就不需要执行after()进行清理了，相当于自动执行了清理
     */
    @Transactional
    public void queryByID() {
        Optional<User> option = userRepository.findById(1L);
        Assert.assertEquals(true, option.isPresent());
    }

    @Test
    @Transactional
    public void queryByName() {
        List<User> list = userRepository.findByNameOrderByEmailAsc("999");
        log.info("list: size {}", list.size());
        list.stream().forEach(System.out::println);
        Assert.assertEquals(2, list.size());
    }

    @Test
    @Transactional
    public void queryDistinct() {
        ArrayList<User> list = userRepository.findDistinctUserByName("999");
        log.info("list: size {}", list.size());
        Assert.assertEquals(2, list.size());
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * pagable
     */
    @Test
    public void queryPagable() {
        //Pageable pageable = new PageRequest(0,3);
        Pageable pageable = new PageRequest(0,3, Sort.Direction.DESC,"id");
        Page<User> page = userRepository.findAll(pageable);

        log.info("total page: {}, count: {}",
                page.getTotalPages(),
                page.getTotalElements());

        while (page.hasContent()) {
            List<User> list = page.getContent();
            list.stream().forEach(System.out::println);

            log.info("prevFirst: {},  offset: {}, next: {}",
                    pageable.previousOrFirst().getOffset(),
                    pageable.getOffset(),
                    pageable.next().getOffset());
            /**
             * 通过page，也可以获得下一个, pageable = page.nextPageable()
             */
            pageable = pageable.next();
            page = userRepository.findAll(pageable);
        }
    }

    @Test
    public void queryOriginSQLPagable() {
        Pageable pageable = new PageRequest(0,3);
        //Pageable pageable = new PageRequest(0,3, Sort.Direction.DESC,"id");
        Page<User> page = userRepository.findAllUser(pageable);

        log.info("total page: {}, count: {}",
                page.getTotalPages(),
                page.getTotalElements());

        while (page.hasContent()) {
            List<User> list = page.getContent();
            list.stream().forEach(System.out::println);

            log.info("prevFirst: {},  offset: {}, next: {}",
                    pageable.previousOrFirst().getOffset(),
                    pageable.getOffset(),
                    pageable.next().getOffset());
            /**
             * 通过page，也可以获得下一个, pageable = page.nextPageable()
             */
            pageable = pageable.next();
            page = userRepository.findAllUser(pageable);
        }
    }

    @Test
    public void joinUserDepart() {
        List<String> list = userRepository.findUserJoinDepartment();
        list.stream().forEach(System.out::println);
    }
}
