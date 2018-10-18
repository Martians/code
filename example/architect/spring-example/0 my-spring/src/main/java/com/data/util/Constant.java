package com.data.util;

import com.data.domain.Department;
import com.data.domain.User;

import java.util.Arrays;
import java.util.List;

public class Constant {
    public static List<User> userList = Arrays.asList(
            new User("000", 10),
            new User("111", 11),
            new User("222", 12),
            new User("333", 13),
            new User("444", 14),
            new User("555", 15),
            new User("666", 16),
            new User("777", 17),
            new User("888", 18),
            new User("999", 19),
            new User("999", 20, null, "e99@126.com"));

    public static List<User> joinList = Arrays.asList(
            new User("aaa", 20, "dp-1"),
            new User("bbb", 20, "dp-2"));
    // 如：new n_n_User("bbb", 20, "dp-3"), 无法加入，违反外键约定

    public static List<Department> departList = Arrays.asList(
            new Department("dp-1"),
            new Department("dp-2"));
}
