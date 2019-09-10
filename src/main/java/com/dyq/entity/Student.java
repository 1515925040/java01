package com.dyq.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author 丁艳青
 * @site www.dyq.com
 * @company xxx
 * @create 2019-08-20 21:27
 */
@Data
@Entity
@Table(name = "student")
public class Student  implements Serializable {
    @Id
    private Integer id;
    @Column
    private String name;

    public Student() {
    }

    public Student(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
