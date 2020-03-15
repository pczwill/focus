package com.focus.test.util;


import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class PoiUtil {
	/**
    * 导出Excel
    * @param sheetName sheet名称
    * @param title 标题
    * @param values 内容
    * @param wb HSSFWorkbook对象
    * @return
    */
   public static HSSFWorkbook getHSSFWorkbook(String sheetName,String []title,String [][]values, HSSFWorkbook wb){

       // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
       if(wb == null){
           wb = new HSSFWorkbook();
       }

       // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
       HSSFSheet sheet = wb.createSheet(sheetName);

       // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
       HSSFRow row = sheet.createRow(0);

       // 第四步，创建单元格，并设置值表头 设置表头居中
       HSSFCellStyle style = wb.createCellStyle();
//       style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式

       //声明列对象
       HSSFCell cell = null;

       //创建标题
       for(int i=0;i<title.length;i++){
           cell = row.createCell(i);
           cell.setCellValue(title[i]);
           cell.setCellStyle(style);
       }

       //创建内容
       for(int i=0;i<values.length;i++){
           row = sheet.createRow(i + 1);
           for(int j=0;j<values[i].length;j++){
               //将内容按顺序赋给对应的列对象
               row.createCell(j).setCellValue(values[i][j]);
           }
       }
       return wb;
   }

   //发送响应流方法
   public static void setResponseHeader(HttpServletResponse response, String fileName) {
       try {
           try {
               fileName = new String(fileName.getBytes(),"ISO8859-1");
           } catch (UnsupportedEncodingException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
           }
           response.setContentType("application/octet-stream;charset=ISO8859-1");
           response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
           response.addHeader("Pargam", "no-cache");
           response.addHeader("Cache-Control", "no-cache");
       } catch (Exception ex) {
           ex.printStackTrace();
       }
   }
}
