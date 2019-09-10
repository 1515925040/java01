package com.dyq;

import com.dyq.dao.StudentDao;
import com.dyq.entity.Student;
import com.dyq.service.StudentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

/**
 * @author 丁艳青
 * @site www.dyq.com
 * @company xxx
 * @create 2019-08-20 21:31
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class JpaTest {

    @Autowired
    private StudentDao studentDao;
    @Autowired
    private StudentService studentService;

    @Test
    public  void test1(){
        Student student = new Student();
        student.setId(100);
        student.setName("jpa测试");
        Student save = studentDao.save(student);
        System.out.println("student"+save);

    }

    @Test
    public void test2(){
        //studentDao.deleteById(100);
        //System.out.println("删除成功");
        Optional<Student> byId = studentDao.findById(1);
        System.out.println(byId);

       /* Specification specification = new Specification<Student>() {
            @Override
            public Predicate toPredicate(Root<> root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Path id = root.get("id");
                Path name = root.get("name");
                Predicate like1 = criteriaBuilder.like(id, "2");
                Predicate like2 = criteriaBuilder.like(name, "%d%");
                Predicate or = criteriaBuilder.or(like1, like2);

                return or;
            }
        };

        List all = studentDao.findAll(specification);
        for (Object o : all) {
            System.out.println(o);
        }*/

        List<Student> stus = studentDao.findAll(new Specification<Student>() {
            @Override
            public Predicate toPredicate(Root<Student> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //root.get("address")表示获取address这个字段名称,like表示执行like查询,%zt%表示值
                Predicate p1 = criteriaBuilder.like(root.get("name"), "%d%");
               // Predicate p2 = criteriaBuilder.like(root.get("id"),"2");
                //将两个查询条件联合起来之后返回Predicate对象
                return criteriaBuilder.or(p1);
            }
        });

        for (Student s: stus){
            System.out.println(s);
        }
    }

    @Test
    public  void test11(){


    }
}
