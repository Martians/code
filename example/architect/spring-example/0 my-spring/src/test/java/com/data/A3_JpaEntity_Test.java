package com.data;

import com.data.domain.Department;
import com.data.domain.User;
import com.data.repository.DepartmentRepository;
import com.data.repository.UserRepository;
import com.data.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
//@Transactional
/**
 * 该测试重点关注 Entity 本身
 */
public class A3_JpaEntity_Test {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;


    @Test
    public void addDepartment_cascade_addUser() {

        Department department = new Department("web_group",
                Arrays.asList(new User("aaa", 10)));
        departmentRepository.save(department);

        /**
         * 级联添加，department里边保存的 n_n_User，会被自动保存到User表中，并在外键部分赋值为 web_group
         */
        User user = userRepository.findByNameOrEmail("aaa", "").get(0);
        Assert.assertEquals("web_group", user.getDepartment());
    }
}
