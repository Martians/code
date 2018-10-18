package com.data.domain.a1_ManyToOne;

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
public class n_1_User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    /**
     * 注意：如果表现这个关系，这里要写 n_1_Department 类型，而不是String
     *
     *
     */
    @ManyToOne
    private n_1_Department department_ref;
    //private String department;
}
