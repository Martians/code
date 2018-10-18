package com.data.domain.a2_OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
/**
 * 拥有外键的一方，是关系的所有者
 */
public class l_1_User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToOne
    /**
     * 在这里，JoinColumn的作用是修改列名
     */
    @JoinColumn(name = "PSPACE_ID")
    private l_1_ParkingSpace parking_ref;
}
