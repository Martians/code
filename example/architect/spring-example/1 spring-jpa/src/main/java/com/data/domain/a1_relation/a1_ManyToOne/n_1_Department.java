package com.data.domain.a1_ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
/**
      CREATE TABLE `n_1_department` (
        `name` VARCHAR(255) NOT NULL COLLATE 'utf8_unicode_ci',
        PRIMARY KEY (`name`)
    )

    CREATE TABLE `n_1_user` (
        `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
        `department` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8_unicode_ci',
        `name` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8_unicode_ci',
        PRIMARY KEY (`id`),
        INDEX `FKir1ukk17alu8deraryp9opfhb` (`department`),
        CONSTRAINT `FKir1ukk17alu8deraryp9opfhb` FOREIGN KEY (`department`) REFERENCES `n_1_department` (`name`)
    )
 */

/**
 * 说明：
 *      单向关系、双向关系，指的是实体中，是否包含指向另一个实体的引用特性（field、properties）
 *
 *
 *
 * 一、ManyToOne：仅仅考虑单向 N：1 关系（N的一方）
 * 1. Department:
 *      Nothing
 *
 * 2. Department:
 *      @ManyToOne
 *      private n_1_Department department_ref;
 *      能取得与上述方式相同的效果，该方式最简单
 *      可以使用 JoinColumn，改变外键列的列名
 *

 * 二、OneToMany：仅仅考虑单向 1：N 关系（1的一方）
 * 1. Department:
 *      1）OneToMany
 *         JoinColumn: 指定User表中的外键
 *
 *         此方案案例说是 ”准双向“ 关系，因为在物理表达上，user有指向 Department的外键
 *         但是User的实体中，实际是没有这个field的，因此User实体并没有保存到Department的关联，这个关系可以认为是单向的
 *
 *      2) @OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
 *         这是实现单向 OneToMany 的常规方法，更好的例子是 Department -> Address 的关系机
 *         是单向的集合映射，目标实体（Department）不具有回原（Department）的 N：1 映射
 *
 *         这里记录的是1的一方，到多的一方的映射，因为不知道N的大小，不能讲信息直接记录在 1 的表中（数据规模不明确）
 *         所以单独生成一张联接表，记录他们之间的关系，这个关系可以无限多
 *
 *         这里可以使用 @JonTable来定义这个联接表，见 n_n_User
 *
 * 2. Department:
 *      Nothing
 *      User表中会自动生成 department 的外键列，但是User实体无法直接访问到
 *

 * 三、双向关系
 * 1. Department:
 *      1) @OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
 *         @JoinColumn(name = "department")
 *         指定User表中的外键，这里确保会生成一个外键（如果不存在此列，会创建一个外键列）
 *
 *      2) @OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY, mappedBy = "department_ref")
 *         此方式的效果与1）一样，但是 mappedBy 必须是已经User表中，已经存在的列的名字

 * 2. Department:
 *      1) private String department;  类型为 Department的ID类型：String
 *         定义一个列，不加任何注解，名称与JoinColumn中一致（否则新定义的这个列，就不是外键了）
 *         这样设置的实体User就可以访问到了（添加User时，可以指定department的属性了）
 *         该方法不一定正规？
 *
 *      2) @ManyToOne
 *         private n_1_Department department_ref;
 *         会在User列生成一个外键列，名称为 原实体中属性名_目标实体主键，即 department_ref_name，类型为String
 *
 *  Note：
 *      1）mappedBy：如果两边都不设置 mappedBy，会导致两边都设置了对方的外键列，造成无法使用？
 *         owner一方拥有外键列，通常 Many 的一方都要设置 mappedBy, 否则双方都是关系的所有者
 *
 *         如果不使用mapped，那么双向关系就是“分离的”，两个方向没有任何关系，可以认为是分别建立了两个单向联系
 *         使用mapped，就是为了将这两个单向联系整合在一起（逻辑表达式这样，但table的物理表达上，可能信息更少，因为只需要建一个外键列即可，而不是两个）
 *
 *       mappedBy 和 JoinColumn 的效果有时可以类似？
 *
 *      2) 延迟设置
 *         单值关系是立即加载，集合之的关系默认为延迟加载
 *
 */
public class n_1_Department {

    @Id
    private String name;

    /**
     * OneToMany: 定义一对多的单向关联
     *
     * cascade: 设置级联关系：删除 n_1_Department 时，会删除所有相关的 n_n_User
     *
     * fetch: 延迟加载，在表级别的关联上，延迟加载比较有用
     *  实际上，这里是针对集合的，所有默认就是LAZY的
     */
    //@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY, mappedBy = "department_ref")
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
    //private Collection<n_n_User> users;
    /**
     * 这种方式，要制定 OneToMany 中的 targetEntity = n_n_User.class, 持久化提供程序需要此信息
     */
    //private Collection           users;
    private List<n_1_User> users1 = new ArrayList<n_1_User>();
}
