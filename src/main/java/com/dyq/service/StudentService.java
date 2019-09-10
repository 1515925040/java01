package com.dyq.service;

import com.dyq.dto.StudentDto;
import com.dyq.entity.Student;
import org.springframework.data.domain.Page;

/**
 * @author 丁艳青
 * @site www.dyq.com
 * @company xxx
 * @create 2019-08-20 21:51
 */
public interface StudentService {
    public void delete(Integer id);
    public Student queryBy(Integer id);
    public Page<Student> queryBySerach(StudentDto studentDto , Integer page, Integer limit);
}
