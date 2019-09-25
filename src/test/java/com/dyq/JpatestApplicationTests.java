package com.dyq;

import com.dyq.entity.Student;
import com.dyq.poiutile.ExcelTestUtil;
import com.dyq.poiutile.ExportPOIUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JpatestApplicationTests {

    @Test
    public void contextLoads() {

        List<String> headList = new ArrayList<>();
        headList.add("标题1");
        headList.add("标题2");
        headList.add("标题3");
        headList.add("标题4");
        List<String> fieldList = new ArrayList<>();
        fieldList.add("id");
        fieldList.add("name");
        fieldList.add("pas");
        fieldList.add("sex");
        ArrayList<Map<String, Object>> maps = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("id", "id1");
        map.put("name", "name1");
        map.put("pas", "pa1111111111111111111111111111111111111111111111111s1");
        map.put("sex", "sex1");
        Map<String, Object> map2 = new HashMap<>();
        map2.put("id", "id2");
        map2.put("name", "name2");
        map2.put("pas", "pas2");
        map2.put("sex", "sex2");
        Map<String, Object> map3 = new HashMap<>();
        map3.put("id", "id3");
        map3.put("name", "name3");
        map3.put("pas", "pas3");
        map3.put("sex", "sex3");

        maps.add(map);
        maps.add(map3);
        maps.add(map2);

        ArrayList<Map<String, Object>> mapss = new ArrayList<>();
        List<Student> list = new ArrayList<>();
            list.add(new Student(1,"ding"));
            list.add(new Student(2,"yan"));
            list.add(new Student(3,"qing"));

            for(Student s :list){
               Map<String,Object>  m = new HashMap<>();
               m.put("id",s.getId());
               m.put("name",s.getName());
               mapss.add(m);
            }


        try {
            XSSFWorkbook workbook = ExcelTestUtil.exportExcel(headList, fieldList, mapss);
            FileOutputStream outputStream = new FileOutputStream("E:\\aaa\\xxxxx.xlsx");
            workbook.write(outputStream);// 输出到流中
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test(HttpServletResponse response){
        String fileName = "人员档案列表";
        //List<User> users = sRPService.exportList(ids);
        // 列名
        String columnNames[] = { "ID", "姓名", "性别", "所属部门", "所属单位", "邮箱",
                         "电话", "手机", "学历/学位", "专业/专科方向", "直属上级", "账号锁定" };
        // map中的key
        String keys[] = { "id", "userName", "gender", "dept", "unit", "email",
                "tel", "phone", "degree", "major", "parentName", "isLocked" };
        try {
            ExportPOIUtils.start_download(response, fileName, null,columnNames, keys);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void testa(){
         /*HttpServletResponse response;
        String fileName = "人员档案列表";

        //List<User> users = sRPService.exportList(ids);
        List<Object> list = new ArrayList<>();
        // 列名
        String columnNames[] = { "ID", "姓名", "性别", "所属部门", "所属单位", "邮箱",
                         "电话", "手机", "学历/学位", "专业/专科方向", "直属上级", "账号锁定"};
        // map中的key
        String keys[] = { "id", "userName", "gender", "dept", "unit", "email",
                "tel", "phone", "degree", "major", "parentName", "isLocked" };
        try {
            ExportPOIUtils.start_download(response, fileName, list,columnNames, keys);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

}
