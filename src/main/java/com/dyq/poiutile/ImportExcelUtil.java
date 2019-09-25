package com.dyq.poiutile;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiaofeng.he on 2018/06/22
 * <p>
 *     参考：JML(http://blog.csdn.net/jml1437710575/article/details/52051278)
 * </p>
 * poi 实现 excel 导入 工具类
 */
public class ImportExcelUtil {

    //正则表达式 用于匹配属性的第一个字母
    private static final String REGEX = "[a-zA-Z]";

    /**
     * Excel数据转 list
     * @param originUrl Excel表的所在路径
     * @param datePattern 日期格式 如"yyyy-MM-dd hh:mm:ss"
     * @param fields 模板定义的属性名的行位置
     * @param startRow 从第几行开始
     * @param endRow 到第几行结束 (0表示所有行;正数表示到第几行结束;负数表示到倒数第几行结束)
     * @param clazz 要返回的对象集合的类型
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> List<T> importExcel(String originUrl, String datePattern, String[] fields, int startRow, int endRow, Class<T> clazz) throws IOException {
        // 判断文件是否存在
        File file = new File(originUrl);
        if (!file.exists()) {
            throw new IOException("文件名为" + file.getName() + "Excel文件不存在！");
        }
        InputStream fis = new FileInputStream(file);

        return doImportExcel(fis, datePattern, fields, startRow, endRow, clazz);
    }

    /**
     * Excel数据转 list
     * @param inputStream Excel文件输入流
     * @param datePattern 日期格式 如"yyyy-MM-dd hh:mm:ss"
     * @param fields 模板定义的属性名的行位置
     * @param startRow 从第几行开始
     * @param endRow 到第几行结束 (0表示所有行;正数表示到第几行结束;负数表示到倒数第几行结束)
     * @param clazz 要返回的对象集合的类型
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> List<T> importExcel(InputStream inputStream, String datePattern, String[] fields, int startRow, int endRow, Class<T> clazz) throws IOException {
        return doImportExcel(inputStream, datePattern,  fields, startRow, endRow, clazz);
    }

    /**
     * 真正实现
     * @param inputStream
     * @param datePattern
     * @param fields
     * @param startRow
     * @param endRow
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    private static <T> List<T> doImportExcel(InputStream inputStream, String datePattern, String[] fields, int startRow, int endRow, Class<T> clazz) throws IOException {
        Workbook wb;
        Sheet sheet;
        Row filedsRow = null;
        List<Row> rowList = new ArrayList<>();
        try {
            // 去读Excel
            // HSSFWorkbook wb = new HSSFWorkbook(fis);
            // 使用workbook 支持2003/2007
            wb = WorkbookFactory.create(inputStream);
            sheet = wb.getSheetAt(0);
            // 获取最后行号
            int lastRowNum = sheet.getLastRowNum();

            int rowLength = lastRowNum;
            if (endRow > 0) {
                rowLength = endRow;
            } else if (endRow < 0) {
                rowLength = lastRowNum + endRow;
            }

           /* // 获取属性列字段
            filedsRow = sheet.getRow(filedsIndex);*/

            // 循环读取
            Row row;
            for (int i = startRow; i <= rowLength; i++) {
                row = sheet.getRow(i);
                rowList.add(row);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } catch (InvalidFormatException e1) {
            e1.printStackTrace();
        }

        return returnObjectList(datePattern, fields, rowList, clazz);
    }

    /**
     * 功能:返回指定的对象集合
     */
    private static <T>List<T> returnObjectList(String datePattern, String[] fields, List<Row> rowList,Class<T> clazz) {
        List<T> objectList=new ArrayList<>();
        try {
            T obj;
            String attribute;
            String value;

            for (Row row : rowList) {
                obj = clazz.newInstance();
               /* //获取对象属性值
                for (int j = 0; j < filedsRow.getLastCellNum(); j++) {

                    attribute = getCellValue(filedsRow.getCell(j));
                    if (!attribute.equals("")) {
                        value = getCellValue(row.getCell(j));
                        setAttrributeValue(obj, attribute, value, datePattern);
                    }
                }*/
                //-------------------------------------------------------
                for (int j = 0; j < fields.length; j++) {
                    attribute = fields[j];
                    if (!attribute.equals("")) {
                        value = getCellValue(row.getCell(j));
                        setAttrributeValue(obj, attribute, value, datePattern);
                    }
                }
                objectList.add(obj);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return objectList;
    }

    /**
     * 功能:获取单元格的值
     */
    private static String getCellValue(Cell cell) {
        Object result = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    result = cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    result = cell.getNumericCellValue();
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    result = cell.getBooleanCellValue();
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    result = cell.getCellFormula();
                    break;
                case Cell.CELL_TYPE_ERROR:
                    result = cell.getErrorCellValue();
                    break;
                case Cell.CELL_TYPE_BLANK:
                    break;
                default:
                    break;
            }
        }
        return result.toString();
    }

    /**
     * 功能:给指定对象的指定属性赋值
     */
    private static void setAttrributeValue(Object obj,String attribute,String value, String datePattern) {
        //得到该属性的set方法名
        String method_name = convertToMethodName(attribute,obj.getClass(),true);
        Method[] methods = obj.getClass().getMethods();
        for (Method method : methods) {
            /**
             * 因为这里只是调用bean中属性的set方法，属性名称不能重复
             * 所以set方法也不会重复，所以就直接用方法名称去锁定一个方法
             * （注：在java中，锁定一个方法的条件是方法名及参数）
             */
            if(method.getName().equals(method_name)) {
                Class<?>[] parameterC = method.getParameterTypes();
                try {
                    /**如果是(整型,浮点型,布尔型,字节型,时间类型),
                     * 按照各自的规则把value值转换成各自的类型
                     * 否则一律按类型强制转换(比如:String类型)
                     */
                    if(parameterC[0] == int.class || parameterC[0]== Integer.class) {
                        if(value != null && value.length() > 0) {
                            value = value.substring(0, value.lastIndexOf("."));
                            method.invoke(obj,Integer.valueOf(value));
                        }

                        break;
                    } else if(parameterC[0] == long.class || parameterC[0]== Long.class) {
                        if(value != null && value.length() > 0) {
                            value = value.substring(0, value.lastIndexOf("."));
                            method.invoke(obj,Long.valueOf(value));
                        }
                        break;
                    } else if(parameterC[0] == float.class || parameterC[0]== Float.class) {
                        if(value != null && value.length() > 0) {
                            method.invoke(obj, Float.valueOf(value));
                        }
                        break;
                    } else if(parameterC[0] == double.class || parameterC[0]== Double.class) {
                        if(value != null && value.length() > 0) {
                            method.invoke(obj, Double.valueOf(value));
                        }
                        break;
                    } else if(parameterC[0] == byte.class || parameterC[0]== Byte.class) {
                        if(value != null && value.length() > 0) {
                            method.invoke(obj, Byte.valueOf(value));
                        }
                        break;
                    } else if(parameterC[0] == boolean.class|| parameterC[0]== Boolean.class) {
                        if (value != null && value.length() > 0) {
                            method.invoke(obj, Boolean.valueOf(value));
                        }
                        break;
                    } else if(parameterC[0] == Date.class) {
                        if(value != null && value.length() > 0) {
                            //判断日期的值属于那种类型
                           /* System.out.println("value:"+value);
                            Calendar calendar = new GregorianCalendar(1900,0,-1);
                            Date d = calendar.getTime();
                            Date dd = DateUtils.addDays(d,Integer.valueOf(value.substring(0,value.indexOf("."))));*/
                            Date dd = HSSFDateUtil.getJavaDate(Double.valueOf(value));
                            SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
                            Date date=null;
                            String  ds= sdf.format(dd);
                            try {
                                date=sdf.parse(ds);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            method.invoke(obj,date);
                        }

                        break;
                    } else if(parameterC[0] == BigDecimal.class) {
                        if (value != null && value.length() > 0) {
                            method.invoke(obj, new BigDecimal(value));
                        }
                        break;
                    } else {
                        if (value != null && value.length() > 0) {
                            method.invoke(obj,parameterC[0].cast(value));
                        }
                        break;
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 功能:根据属性生成对应的set/get方法
     */
    private static String convertToMethodName(String attribute,Class<?> objClass,boolean isSet) {
        /** 通过正则表达式来匹配第一个字符 **/
        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(attribute);
        StringBuilder sb = new StringBuilder();
        /** 如果是set方法名称 **/
        if(isSet) {
            sb.append("set");
        } else {
            /** get方法名称 **/
            try {
                Field attributeField = objClass.getDeclaredField(attribute);
                /** 如果类型为boolean **/
                if(attributeField.getType() == boolean.class||attributeField.getType() == Boolean.class) {
                    sb.append("is");
                } else {
                    sb.append("get");
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        /** 针对以下划线开头的属性 **/
        if(attribute.charAt(0)!='_' && m.find()) {
            sb.append(m.replaceFirst(m.group().toUpperCase()));
        } else {
            sb.append(attribute);
        }
        return sb.toString();
    }
    //-------------------------------------------分割线-------------------------------------------------

    //参数说明:  fileName：文件名   projects：对象集合  columnNames： 列名   keys： map中的key--->对应对象属性名
    public static void start_download(HttpServletResponse response, String fileName, List<?> projects,
                                      String[] columnNames, String[] keys) throws IOException {
        //将集合中对象的属性  对应到  List<Map<String,Object>>
        List<Map<String,Object>> list=createExcelRecord(projects, keys);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            //将转换成的Workbook对象通过流形式下载
            createWorkBook(list,keys,columnNames).write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename="+ new String((fileName + ".xls").getBytes(), "iso-8859-1"));
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            throw e;
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
    }

    private static List<Map<String, Object>> createExcelRecord(List<?> projects, String[] keys) {
        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sheetName", "sheet");
        listmap.add(map);
        Object project=null;
        for (int j = 0; j < projects.size(); j++) {
            project=projects.get(j);
            Map<String, Object> mapValue = new HashMap<String, Object>();
            for(int i=0; i<keys.length; i++){
                mapValue.put(keys[i], getFieldValueByName(keys[i], project));
            }

            listmap.add(mapValue);
        }
        return listmap;
    }
    /**
     * 利用反射  根据属性名获取属性值
     * */
    private static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 创建excel文档对象
     * @param keys list中map的key数组集合
     * @param columnNames excel的列名
     * */
    private static Workbook createWorkBook(List<Map<String, Object>> list, String []keys, String columnNames[]) {
        // 创建excel工作簿
        Workbook wb = new HSSFWorkbook();
        // 创建第一个sheet（页），并命名
        Sheet sheet = wb.createSheet(list.get(0).get("sheetName").toString());
        // 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
        for(int i=0;i<keys.length;i++){
            sheet.setColumnWidth((short) i, (short) (35.7 * 150));
        }

        // 创建第一行
        Row row = sheet.createRow((short) 0);

        // 创建两种单元格格式
        CellStyle cs = wb.createCellStyle();
        CellStyle cs2 = wb.createCellStyle();

        // 创建两种字体
        Font f = wb.createFont();
        Font f2 = wb.createFont();

        // 创建第一种字体样式（用于列名）
        f.setFontHeightInPoints((short) 10);
        f.setColor(IndexedColors.BLACK.getIndex());
        f.setBoldweight(Font.BOLDWEIGHT_BOLD);

        // 创建第二种字体样式（用于值）
        f2.setFontHeightInPoints((short) 10);
        f2.setColor(IndexedColors.BLACK.getIndex());

        // 设置第一种单元格的样式（用于列名）
        cs.setFont(f);
        cs.setBorderLeft(CellStyle.BORDER_THIN);
        cs.setBorderRight(CellStyle.BORDER_THIN);
        cs.setBorderTop(CellStyle.BORDER_THIN);
        cs.setBorderBottom(CellStyle.BORDER_THIN);
        cs.setAlignment(CellStyle.ALIGN_CENTER);

        // 设置第二种单元格的样式（用于值）
        cs2.setFont(f2);
        cs2.setBorderLeft(CellStyle.BORDER_THIN);
        cs2.setBorderRight(CellStyle.BORDER_THIN);
        cs2.setBorderTop(CellStyle.BORDER_THIN);
        cs2.setBorderBottom(CellStyle.BORDER_THIN);
        cs2.setAlignment(CellStyle.ALIGN_CENTER);
        //设置列名
        for(int i=0;i<columnNames.length;i++){
            Cell cell = row.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(cs);
        }
        //设置每行每列的值
        for (short i = 1; i < list.size(); i++) {
            // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
            // 创建一行，在页sheet上
            Row row1 = sheet.createRow((short) i);
            // 在row行上创建一个方格
            for(short j=0;j<keys.length;j++){
                Cell cell = row1.createCell(j);
                cell.setCellValue(list.get(i).get(keys[j]) == null?" ": list.get(i).get(keys[j]).toString());
                cell.setCellStyle(cs2);
            }
        }
        return wb;
    }

    public static void main(String[] args) {
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
