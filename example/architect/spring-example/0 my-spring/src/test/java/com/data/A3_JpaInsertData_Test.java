package com.data;

import com.data.repository.DepartmentRepository;
import com.data.repository.UserRepository;
import com.data.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
//@Transactional
/**
 * 用于向数据库中填充数据，dev阶段使用
 */
public class A3_JpaInsertData_Test {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;


    @Test
    public void prepare() {
        Constant.userList.stream().forEach(x -> userRepository.save(x));

        Constant.departList.stream().forEach(x -> departmentRepository.save(x));
        Constant.joinList.stream().forEach(x -> userRepository.save(x));
    }
}
