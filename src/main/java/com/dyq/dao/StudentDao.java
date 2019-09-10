package com.dyq.dao;

import com.dyq.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author 丁艳青
 * @site www.dyq.com
 * @company xxx
 * @create 2019-08-20 21:30
 */
public interface StudentDao extends JpaRepository<Student,Integer>, JpaSpecificationExecutor<Student> {
    //void findAll(org.springframework.data.domain.Pageable pageAble, org.springframework.data.jpa.domain.Specification<com.dyq.dto.StudentDto> specification);
}
