package com.data.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

/**
 * lombok自动生成代码：
 *      点开structure视图进行查看，自动生成了getter、setter等
 *      具体信息 https://projectlombok.org/features/Data，也可以从idea的plugins处，查看lombok插件信息
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
/**
 * @UniqueConstraint
 *
 */
@Table(name="user_table",
    /**
     * 创建索引方式1: UniqueConstraint
     * 创建索引方式2: Index
     *
     * 多种方法：http://blog.csdn.net/qq_35873847/article/details/78624855
     */
    uniqueConstraints = @UniqueConstraint(name = "department_email_index", columnNames={"department", "email"}),
    indexes = {@Index(name = "name_email_index", columnList = "name, email", unique = true)})
//@Accessors(chain=true)
/**
 * 1. 可以预先创建数据表,
 * 2. 可以通过定义Entity来创建表
 */
public class User implements Serializable {

    /////////////////////////////////////////////////////////////////////////
    /**
     * only for test usage
     */
    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
        this.date = new Date();
        this.email = name + "@126.com";
    }

    public User(String name, Integer age, String department) {
        this(name, age);
        this.department = department;
    }

    public User(String name, Integer age, String department, String email) {
        this(name, age, department);
        this.email = email;
    }

    /////////////////////////////////////////////////////////////////////////
    /**
     * 注意 import org.springframework.data.annotation.Id
     * import javax.persistence.id;
     */
    @Id
    /**
     * AUTO：    主键增长由JPA控制
     * TABLE:    专门生成一个表来维护，记录上一次生成的主键
     * SEQUENCE: 根据底层数据库的序列来生成主键
     * IDENTITY: 数据库维护自增长，不同的数据库有不同的策略
     *
     * 默认不写参数就是 AUTO
     */
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 这个值是外键，指向Department表
     *
     * 即使不声明这个字段，也是ok的，但是就无法通过User进行访问了
     */
    private String department;

    /**
     * http://blog.csdn.net/zjm_love/article/details/50417710
     *
     * 设置string的长度
     * @Column(name="contact_name", nullable=false,length=512)
     *
     * 通过sql语句定义列，通过Entity生成表定义时使用
     * @Column(name="contact_name", columnDefinition="clob not null")
     *
     * 不可修改：在主键、外键上通常可以加上这个限制
     * @Column(name="id", insertable=false,updatable=false)
     *
     * table属性表示当映射多个表时，指定表的表中的字段。默认值为主表的表名。有关多个表的映射将在本章的5.6小节中详细讲述
     */
    @Column(name="name", nullable = false)
    private String name;

    /**
     * 用于确定字段在数据库中的实际类型
     *
     * 将age当做text类型来存储 ？似乎也可行
     */
    @Column(columnDefinition="text", nullable = false)
    private Integer age;
    private String email;

    /**
     * 不会在数据库中生成字段
     */
    @Transient
    private String value;

    /**
     * 可以注解 Date、Calendar class
     *
     * DATE、TIME、TIMESTRAMP
     */
    @Temporal(TemporalType.DATE)
    private Date date;
    private Time time;

    @Column(name = "time_property")
    public Integer getTimer() {
        return 1;
    }
    public void setTimer(Integer a) {
        return;
    }


    /**
     * @OderbBy ???
     */

    //@GenericGenerator(name="uuid",strategy = "uuid2")
    @Embedded
    private Address address;
}
