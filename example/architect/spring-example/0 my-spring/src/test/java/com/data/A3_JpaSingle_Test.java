package com.data;

import com.data.domain.User;
import com.data.repository.UserRepository;
import com.data.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@Slf4j
@RunWith(SpringRunner.class)
/**
 * 不需要专门引用 @SpringBootTest
 *
 * 自动配置嵌入式数据库，搜索标注了@Entity的类
 * 事务性的，执行完成后会回滚
 * 使用TestEntityManager替代标准的JPA EntityManager
 *
 * 1. 取消回滚：@Transactional(propagation = Propagation.NOT_SUPPORTED)
 * 2. 在真实数据库测试：@AutoConfigureTestDatabase(replace=Replace.NONE)
 */
@DataJpaTest
public class A3_JpaSingle_Test {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void prepare() {
        /** 作用相同 */
        entityManager.persist(new User("1010", 100));
        Constant.userList.stream().forEach(x -> userRepository.save(x));
    }

    @Test
    public void get() {
        Optional<User> option = userRepository.findById(1L);
        Assert.assertEquals(true, option.isPresent());
    }

    @Test
    public void addUsers1() {

        List<User> list = userRepository.findByNameOrderByEmailAsc("111");
        log.info("list: size {}", list.size());
        Assert.assertEquals(1, userRepository.findAll().size());

        // assertThat(user.getUsername()).isEqualTo("sboot");
    }

    @Test
    public void addUsers2() {

        List<User> list = userRepository.findByNameOrderByEmailAsc("222");
        log.info("list: size {}", list.size());
        Assert.assertEquals(1, userRepository.findAll().size());
    }
}
