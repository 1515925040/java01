package com.dyq.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 丁艳青
 * @site www.dyq.com
 * @company xxx
 * @create 2019-09-05 22:01
 */
public class Product  implements Serializable {
    private Integer id;
    private String name;
    private Date  time;

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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", time=" + time +
                '}';
    }

    public Product() {
    }

    public Product(Integer id, String name, Date time) {
        this.id = id;
        this.name = name;
        this.time = time;
    }
}
