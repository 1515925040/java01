package com.dyq.poiutile;

/**
 * @author 丁艳青
 * @site www.dyq.com
 * @company xxx
 * @create 2019-08-21 22:21
 */


import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Excel(POI)工具类
 */
public class ExcelTestUtil {





    /**
     * @param headList  Excel文件Head标题集合
     * @param fieldList Excel文件Field标题集合
     * @param dataList  Excel文件数据内容部分
     * @throws XSSFWorkbook
     */
    public static XSSFWorkbook exportExcel( List<String> headList,
                                           List<String> fieldList, List<Map<String,Object>> dataList)
            throws Exception {
        // 创建新的Excel 工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 在Excel工作簿中建一工作表，其名为缺省值
        // 如要新建一名为"效益指标"的工作表，其语句为：
        // HSSFSheet sheet = workbook.createSheet("效益指标");
        XSSFSheet sheet = workbook.createSheet("效益指标");
        // 在索引0的位置创建行（最顶端的行）
        XSSFRow firstRow = sheet.createRow(0);// 第一个sheet的第一行为标题
        // ===========设置第一行标题=================
        for (int i = 0; i < headList.size(); i++) {
            // 在索引0的位置创建单元格（左上端）
            XSSFCell cell = firstRow.createCell(i);
            // 定义单元格为字符串类型
            cell.setCellType(XSSFCell.CELL_TYPE_STRING);
            // 在单元格中输入一些内容
            cell.setCellValue(headList.get(i));
        }
        // =============设置内容=============
        for (int n = 0; n < dataList.size(); n++) {
            // 在索引1的位置创建行（最顶端的行）
            XSSFRow row_value = sheet.createRow(n + 1);
            Map<String, Object> dataMap = dataList.get(n);
            // ===============================
            for (int i = 0; i < fieldList.size(); i++) {
                // 在索引0的位置创建单元格（左上端）
                XSSFCell cell = row_value.createCell(i);
                // 定义单元格为字符串类型
                cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                // 在单元格中输入一些内容
                cell.setCellValue(objToString(dataMap.get(fieldList.get(i))));
            }
            // ===============================
        }
        return workbook;
    }


    private static String objToString(Object obj) {
        if (obj == null) {
            return "";
        } else {
            if (obj instanceof String) {
                return (String) obj;
            } else if (obj instanceof Date) {
                return null;// DateUtil.dateToString((Date)
                // obj,DateUtil.DATESTYLE_SHORT_EX);
            } else {
                return obj.toString();
            }
        }
    }

    /**
     * 读取 Excel文件内容
     *
     * @param inputstream 文件输入流
     * @return
     * @throws Exception
     */
    public static List<List<Object>> readExcelByInputStream(
            InputStream inputstream) throws Exception {
        // 结果集
        List<List<Object>> list = new ArrayList<List<Object>>();

        XSSFWorkbook workbook = new XSSFWorkbook(inputstream);

        // 遍历该表格中所有的工作表，i表示工作表的数量 getNumberOfSheets表示工作表的总数
        XSSFSheet sheet = workbook.getSheetAt(0);

        // 遍历该行所有的行,j表示行数 getPhysicalNumberOfRows行的总数

        // //System.out.println("excel行数： "+hssfsheet.getPhysicalNumberOfRows());
        for (int j = 0; j < sheet.getPhysicalNumberOfRows(); j++) {
            XSSFRow row = sheet.getRow(j);
            if (row != null) {
                int col = row.getPhysicalNumberOfCells();
                // 单行数据
                List<Object> arrayString = new ArrayList<Object>();
                for (int i = 0; i < col; i++) {
                    XSSFCell cell = row.getCell(i);
                    if (cell == null) {
                        arrayString.add("");
                    } else if (cell.getCellType() == 0) {
                        arrayString.add(new Double(cell.getNumericCellValue())
                                .toString());
                    } else {// 如果EXCEL表格中的数据类型为字符串型
                        arrayString.add(cell.getStringCellValue().trim());
                    }
                }
                list.add(arrayString);
            }
        }
        return list;
    }

}