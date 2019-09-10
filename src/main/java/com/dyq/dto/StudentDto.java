package com.dyq.dto;

/**
 * @author 丁艳青
 * @site www.dyq.com
 * @company xxx
 * @create 2019-09-03 21:30
 */
public class StudentDto {
    private Integer id;
    private String name;

    @Override
    public String toString() {
        return "StudentDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public StudentDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public StudentDto() {
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
}
