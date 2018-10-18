package com.data.domain.a2_collection.a1_collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "collection_park")
public class Park {
    @Id
    private int id;
    private String name;

    /**
     * 这里将 People 当做集合来存储，而不是一个独立的实体
     * 使用 Embeddable 类型 来存储
     *
     * 会专门生成一个集合表，来存储这个关系
     *
     * 如果在Collection指定类型，就不用写 targetClass
     *
     */
    @ElementCollection(targetClass = People.class)
    /**
     * 定义集合表的信息，不使用默认的名称
     */
    @CollectionTable(name = "park_people_stor",
        joinColumns = @JoinColumn(name = "park_ref"))
    private Collection peopleSets;

    /**
     * 这里将 String 当做集合来存储，而不是一个独立的实体
     * 使用普通类型来存储
     *
     * 会生成一个中间表，表名为 实体名_field名，使用实体的主键作为外键索引，使用locations作为列名
     */
    @ElementCollection
    /**
     * 转换列名
     */
    @Column(name = "location_item")
    private Set<String> locations;
}
