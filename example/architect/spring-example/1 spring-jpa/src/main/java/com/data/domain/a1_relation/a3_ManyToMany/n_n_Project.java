package com.data.domain.a3_ManyToMany;

import com.data.domain.a2_OneToOne.l_1_ParkingSpace;
import java.util.Collection;

import javax.persistence.*;

/**
 * ManyToMany
 *
 * 两边都设置@ManyToMany, 反方设置mappedBy
 *
 * 1. 使用默认配置, 生成的中间表名称为 n_n_user_projects
 *
 * 2. 设置中间表
 *
 *      @JoinTable(name = "User_Project",
 *          joinColumns = @JoinColumn(name = "user_ref"),
 *          inverseJoinColumns = @JoinColumn(name="project_ref"))
 *
 *      JoinTable必须设置在 关系拥有者的一方
 *      joinColumns是所有者的联接列，inverseJoinColumns是反方的联接列
 */
@Entity
public class n_n_Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "projects")
    private Collection<n_n_User> users;
}
