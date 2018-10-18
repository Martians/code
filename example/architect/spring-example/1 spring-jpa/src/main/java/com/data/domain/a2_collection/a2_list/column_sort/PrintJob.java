package com.data.domain.a2_collection.a2_list.column_sort;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "collection_list_sort_job")
@Entity
public class PrintJob {
    @Id
    private Integer id;
    private String name;

    @ManyToOne
    private PrintQueue queue;
}
