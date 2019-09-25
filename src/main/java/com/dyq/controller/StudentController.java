package com.dyq.controller;

import com.dyq.dto.StudentDto;
import com.dyq.entity.Product;
import com.dyq.entity.Student;
import com.dyq.poiutile.ExportPOIUtils;
import com.dyq.poiutile.ImportExcelUtil;
import com.dyq.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 丁艳青
 * @site www.dyq.com
 * @company xxx
 * @create 2019-08-20 21:53
 */
@RestController
public class StudentController {
    @Autowired
    private StudentService studentService;

    @RequestMapping("delete/{id}")
    public void test(@PathVariable(value = "id")Integer id){
        studentService.delete(id);
    }

    @RequestMapping("queryAllBySerach")
    public Object queryBySerach(Integer page,Integer limit){
        StudentDto studentDto = new StudentDto();
        studentDto.setId(1);
        studentDto.setName("d");
        Page<Student> students = studentService.queryBySerach(studentDto, page, limit);
        System.out.println("students: " +students);
        return  students;

    }

    @RequestMapping("download")
    public void download(HttpServletResponse response, String ids){
        String fileName = "人员档案列表";

        // List<User> users = sRPService.exportList(ids);
        List<Product> list = new ArrayList<>();
        Product product = new Product();
        product.setId(1);
        product.setName("丁");
        product.setTime(new Date());
       // list.add(product);
        Product product1 = new Product();
        product1.setId(1);
        product1.setName("");
       /* SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        String format = simpleDateFormat.format(new Date());
            Date currentTime = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = formatter.format(currentTime);
            //Date parse1 = formatter.parse(dateString);*/
        String  strDate= "2007-1-18";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
           ParsePosition pos = new ParsePosition(0);
         Date strtodate = formatter.parse(strDate, pos);
        product1.setTime(strtodate );
       // list.add(product1);

        // 列名
        String columnNames[] = { "ID", "姓名" , "时间"};
        // map中的key
        String keys[] = { "id", "name", "time"};
        try {
            ExportPOIUtils.start_download(response, fileName, list,columnNames, keys);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @RequestMapping("download1")
    public void download1(HttpServletResponse response, String ids){
        String fileName = "人员档案列表";

        List<Product> list = new ArrayList<>();
        Product product = new Product();
        product.setId(1);
        product.setName("丁");
        product.setTime(new Date());
        // list.add(product);
        Product product1 = new Product();
        product1.setId(1);
        product1.setName("");
       /* SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        String format = simpleDateFormat.format(new Date());
            Date currentTime = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = formatter.format(currentTime);
            //Date parse1 = formatter.parse(dateString);*/
        String  strDate= "2007-1-18";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        product1.setTime(strtodate );
        // list.add(product1);
        try{
           // ImportOrExportPoiUtil.exportExcel("导出测试",list,response.getOutputStream(),null);

        }catch (Exception e)
        {

        }
    }

    @RequestMapping("download2")
    public void download2(HttpServletResponse response, String ids){
        String fileName = "人员档案列表";

        List<Product> list = new ArrayList<>();
        Product product = new Product();
        product.setId(1);
        product.setName("丁");
        product.setTime(new Date());
         list.add(product);
        Product product1 = new Product();
        product1.setId(1);
        product1.setName("");
        product1.setTime(new Date());
         list.add(product1);
         String keys[] ={"id","name","time"};
         String columns[] = {"订单id","名称","时间"};
        try{
            // ImportOrExportPoiUtil.exportExcel("导出测试",list,response.getOutputStream(),null);
            ImportExcelUtil.start_download(response,"测试",list,columns,keys);

        }catch (Exception e)
        {
        }



    }

    @RequestMapping("write")
    public void write(HttpServletResponse response, String ids){
        String fileName = "人员档案列表";
        List<Product> list = new ArrayList<>();
        Product product = new Product();
        product.setId(1);
        product.setName("丁");
        product.setTime(new Date());
        list.add(product);
        Product product1 = new Product();
        product1.setId(1);
        product1.setName("");
        product1.setTime(new Date());
        list.add(product1);
        String keys[] ={"id","name","time"};
        String columns[] = {"订单id","名称","时间"};
        try{
            // ImportOrExportPoiUtil.exportExcel("导出测试",list,response.getOutputStream(),null);
           // ImportExcelUtil.start_download(response,"测试",list,columns,keys);
            String url ="F:\\测试2.xlsx";
            String fields[] ={"id","name","time"};

            List<Product> products = ImportExcelUtil.importExcel(url, "yyyy-MM-dd", fields, 1, 0, Product.class);
            for(Product product2 :products){
                System.out.println("product: "+product2);
            }
        }catch (Exception e)
        {
        }

    }
}
