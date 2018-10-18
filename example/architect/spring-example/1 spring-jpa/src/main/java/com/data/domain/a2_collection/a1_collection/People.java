package com.data.domain.a2_collection.a1_collection;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class People {

    @Column(name = "p_name")
    private String name;
    private Integer age;
}
