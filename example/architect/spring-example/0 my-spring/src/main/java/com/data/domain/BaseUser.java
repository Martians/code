package com.data.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * hibernate: http://docs.jboss.org/hibernate/jpa/2.1/api/
 *
 * */
@Entity
@Table(name="user_base") /** 如果不使用Table，默认名称为user */
public class BaseUser implements Serializable {

    private BaseUser() {}

    public BaseUser(String name, Integer age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }

    //@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Id
    @Column(name = "user_name", nullable = false)
    private String  name;
    private Integer age;
    private String  email;

    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
