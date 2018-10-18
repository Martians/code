package com.data.domain.a3_ManyToMany;

import com.data.domain.a2_OneToOne.l_1_ParkingSpace;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
/**
 * 拥有外键的一方，是关系的所有者
 */
public class n_n_User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "User_Project",
            joinColumns = @JoinColumn(name = "user_ref"),
            inverseJoinColumns = @JoinColumn(name="project_ref"))
    private Collection<n_n_Project> projects;
}
