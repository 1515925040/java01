package com.dyq.service;

import com.dyq.dao.StudentDao;
import com.dyq.dto.StudentDto;
import com.dyq.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author 丁艳青
 * @site www.dyq.com
 * @company xxx
 * @create 2019-08-20 21:52
 */
@Service
public class StudentServiceImpl implements  StudentService {
    @Autowired
    private StudentDao studentDao;
    @Override
    public void delete(Integer id) {
        studentDao.deleteById(id);
    }
    /**
     * @Description:
     * @Param: [id]
     * @return: com.dyq.entity.Student
     * @Author: Mr.dyq
     * @Date: 2019/8/22
     */
    public Student  queryBy(Integer id){

        Optional<Student> byId = studentDao.findById(id);
        Student student = byId.get();
        return   student;
    }


    public Page<Student> queryBySerach(StudentDto studentDto ,Integer page,Integer limit) {
        if(null == page){
            page=1;
        }
        if(limit == null){
            limit=3;
        }
        Pageable pageAble = PageRequest.of(page - 1, limit);
        /*Specification<Student > specification = new Specification<Student>() {
            @Override
            public Predicate toPredicate(Root<Student> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Path<String > id = root.get("id");
                Path<String > name = root.get("name");
                //Predicate id1 = criteriaBuilder.equal(root.get("id"), studentDto.getId());
                Predicate p1 = criteriaBuilder.like(id,  studentDto.getId().toString()+"%");
                Predicate p2= criteriaBuilder.like(name, "%" + studentDto.getName() + "%");
                Predicate predicate = criteriaBuilder.and(p1, p2);
                return predicate;
            }
        };*/
         Specification specification = new Specification() {
             @Override
             public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                 List<Predicate> list = new ArrayList<>();
                 if(studentDto.getId()== null){
                     //Predicate predicate = criteriaBuilder.equal(root.get("id").as(String.class), studentDto.getId());
                     Predicate predicate = criteriaBuilder.like(root.get("id").as(String.class), "%"+studentDto.getId()+"%");
                     list.add(predicate);
                 }
                 if(studentDto.getName() != null){
                     Predicate predicate = criteriaBuilder.like(root.get("name").as(String.class), "%" + studentDto.getName() + "%");
                     list.add(predicate);
                 }
                 Predicate [] predicates = new Predicate[list.size()];
                  predicates = list.toArray(predicates);
                 Predicate all = criteriaBuilder.and(predicates);
                 return all;
             }
         };

        Page<Student> pageAll = studentDao.findAll(specification, pageAble);

        return pageAll;
    }
}
