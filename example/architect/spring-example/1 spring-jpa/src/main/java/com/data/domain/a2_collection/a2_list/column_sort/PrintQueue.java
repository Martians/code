package com.data.domain.a2_collection.a2_list.column_sort;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "collection_list_sort_queue")
@Entity
public class PrintQueue {
    @Id
    private Integer id;
    private String name;

    @OneToMany(mappedBy="queue")
    /**
     * 持久化的有序列表
     *
     * 相当于在 PrintJob 中增加了一个整形的列 print_oder，用来存储 list 的顺序
     * 必须使用list作为容器，才能记录顺序
     */
    @OrderColumn(name="print_oder")
    private List<PrintJob> jobs;
}
