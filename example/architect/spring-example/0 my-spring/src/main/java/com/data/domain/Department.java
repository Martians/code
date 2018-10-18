package com.data.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="department")
public class Department {

    /**
     * only for test
     */
    public Department(String name) {
        this.name = name;
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    @Id
    private String name;

//    @GeneratedValue(generator = "uuid")
//    @GenericGenerator(name="uuid",strategy = "uuid2")
//    private String uuid;

    /**
     * OneToMany: 定义一对多的单向关联
     *
     * cascade: 设置级联关系：删除 n_1_Department 时，会删除所有相关的 n_n_User
     * fetch: 延迟加载
     */
    @OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    /**
     * https://www.jianshu.com/p/0a2163273b3e
     * 1. 临时表关联：不使用 JoinColumn
     *      会在 n_1_Department、User之间生成一个临时表 'department_users'
     *
     * 2. 外键关联：使用 JoinColumn，定义在 1:n 的 1 的一方中，n 的一方不用定义
     *      使用之后，会在User表中（注意：不是修改Department的内容），定义User的外键
     *      这里定义的是User表的外键的列名，将对应于 department 的 Key
     */
    @JoinColumn(name = "department")
    /**
     * 通过一个集合，定义与 User的关系
     *
     * 这个属性不会作为一个字段写入到数据库中
     *
     * 该集合是否还有其他用途？
     *
     * 生成一个ArrayList可以方便后续使用
     */
    //private List<n_n_User> users;
    private List<User> users1 = new ArrayList<User>();
}
