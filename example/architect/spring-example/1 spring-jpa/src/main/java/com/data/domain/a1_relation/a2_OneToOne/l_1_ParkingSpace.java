package com.data.domain.a2_OneToOne;

import com.data.domain.a1_ManyToOne.n_1_User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity

/**
 * 单向一对一，参考 ManyToOne，基本相同
 *      l_1_User提供即可, OneToOne
 *
 * 双向一对一：
 *      双方都可以是关系所有者（联接列可以放在任一方），选定一方即可
 *      这里假定选择User作为Owner
 *
 *
 * l_1_ParkingSpace:
 *      1) @OneToOne(mappedBy = "parking_ref")
 *         private n_n_User user_ref;
 *         mappedBy的作用是指向关系的所有方（user_ref的类型对应的实体，即是所有方），其对应的外键类名称
 *
 * n_n_User：
 *      1) @OneToOne
 *         private l_1_ParkingSpace parking_ref;
 *
 *      2) @OneToOne
 *         @JoinColumn(name = "PSPACE_ID")
 *         private l_1_ParkingSpace parking_ref;
 *         JoinColumn 这里只是用来重写连接列的名字，没有其他作用
 *
 */

public class l_1_ParkingSpace {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String location;

    /**
     * mappedBy指出的是，table n_n_User 中的外键列名称
     */
    @OneToOne(mappedBy = "parking_ref")
    private l_1_User user_ref;

}
