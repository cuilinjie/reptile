package com.clj.reptilehouse.common.excel;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springside.modules.utils.Reflections;

import com.clj.reptilehouse.common.util.DateUtil;
import com.clj.reptilehouse.common.util.WebFuncUtil;
import com.clj.reptilehouse.system.entity.SysUser;

public class ExpExcelUtil {
	/** 
	    * 导出Excel的方法 
	    * @param title excel中的sheet名称 
	    * @param headers 表头 
	    * @param list 结果集 
	    * @param attr 输出参数
	    * @param out 输出流
	    * @throws Exception 
	    */  
		
	   @SuppressWarnings("rawtypes")
	   public static void expExcel(String title, String[] headers,List list,String attr, OutputStream out) throws Exception{
		   
		   String[] nodeAtts = attr.split(",");
	       // 声明一个工作薄  
	       HSSFWorkbook workbook = new HSSFWorkbook();  
	       // 生成一个表格  
	       HSSFSheet sheet = workbook.createSheet(title);  
	       // 设置表格默认列宽度为20个字节  
	       //sheet.setDefaultColumnWidth(18);
	       for(int n=0;n<headers.length;n++){
		          sheet.setColumnWidth(n, 4800);
		    }
	       // 生成一个样式  
	       HSSFCellStyle style = workbook.createCellStyle();  
	       // 设置这些样式  
	       style.setFillForegroundColor(HSSFColor.GOLD.index);  
	       style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
	       style.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
	       style.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
	       style.setBorderRight(HSSFCellStyle.BORDER_THIN);  
	       style.setBorderTop(HSSFCellStyle.BORDER_THIN);  
	       style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
	       // 生成一个字体  
	       HSSFFont font = workbook.createFont();  
	       font.setColor(HSSFColor.VIOLET.index);  
	       //font.setFontHeightInPoints((short) 12);  
	       font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
	       // 把字体应用到当前的样式  
	       style.setFont(font);  
	       // 指定当单元格内容显示不下时自动换行  
	       style.setWrapText(true);  
	       
	       int dataRow=0;
	       // 产生表格标题行  
	       HSSFRow row = sheet.createRow(dataRow);  
	       for (int i = 0; i < headers.length; i++) {  
	           HSSFCell cell = row.createCell(i);  
	           cell.setCellStyle(style);  
	           HSSFRichTextString text = new HSSFRichTextString(headers[i]);  
	            cell.setCellValue(text);  
	        }  
	        // 遍历集合数据，产生数据行  
	        if(list != null){  
	            int index = dataRow+1;  
	            for(int i=0;i<list.size();i++){  
	                row = sheet.createRow(index);
	                row.setHeight((short) 500);
	                Object obj =list.get(i);
	                for(int j=0;j<nodeAtts.length;j++){
	                	String value = getJsonColValue(obj, nodeAtts[j]);
	                	if(StringUtils.isNotBlank(value)&&(value.toLowerCase().endsWith("jpg")||value.toLowerCase().endsWith("png"))){
	                		createPic(value,(i+1),j,workbook,sheet);//生成图像
	                	}else{
	                		row.createCell(j).setCellValue(value);
	                	}
	                }
	                index++;  
	            }     
	        }  
	        workbook.write(out);
	        workbook.close();
	    }
		
		
	   /** 
	    * 导出Excel的方法 
	    * @param title excel中的sheet名称 
	    * @param headers 表头 
	    * @param exlColWidths 每列宽度设置
	    * @param list 结果集 
	    * @param attr 输出参数
	    * @param out 输出流
	    * @throws Exception 
	    */ 
	   @SuppressWarnings("rawtypes")
	   public static void expExcel(String title, String[] headers,int[] exlColWidths,List list,String attr, OutputStream out) throws Exception{
		   
		   String[] nodeAtts = attr.split(",");
	       // 声明一个工作薄  
	       HSSFWorkbook workbook = new HSSFWorkbook();  
	       // 生成一个表格  
	       HSSFSheet sheet = workbook.createSheet(title);  
	       // 设置表格默认列宽度为20个字节  
	       //sheet.setDefaultColumnWidth(18);
	       for(int n=0;n<exlColWidths.length;n++){
		          sheet.setColumnWidth(n, exlColWidths[n]);
		    }
	         
	       // 生成一个样式  
	       HSSFCellStyle style = workbook.createCellStyle();  
	       // 设置这些样式  
	       style.setFillForegroundColor(HSSFColor.GOLD.index);  
	       style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
	       style.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
	       style.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
	       style.setBorderRight(HSSFCellStyle.BORDER_THIN);  
	       style.setBorderTop(HSSFCellStyle.BORDER_THIN);  
	       style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
	       // 生成一个字体  
	       HSSFFont font = workbook.createFont();  
	       font.setColor(HSSFColor.VIOLET.index);  
	       //font.setFontHeightInPoints((short) 12);  
	       font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
	       // 把字体应用到当前的样式  
	       style.setFont(font);  
	       // 指定当单元格内容显示不下时自动换行  
	       style.setWrapText(true);  
	       
	       int dataRow=0;
	       // 产生表格标题行  
	       HSSFRow row = sheet.createRow(dataRow);  
	       for (int i = 0; i < headers.length; i++) {  
	           HSSFCell cell = row.createCell(i);  
	           cell.setCellStyle(style);  
	           HSSFRichTextString text = new HSSFRichTextString(headers[i]);  
	           cell.setCellValue(text);  
	        }
	        // 遍历集合数据，产生数据行  
	        if(list != null){  
	            int index = dataRow+1;  
	            for(int i=0;i<list.size();i++){
	                row = sheet.createRow(index);
	                row.setHeight((short) 500);
	                Object obj =list.get(i);
	                for(int j=0;j<nodeAtts.length;j++){
	                	String value = getJsonColValue(obj, nodeAtts[j]);
	                	if(StringUtils.isNotBlank(value)&&(value.toLowerCase().endsWith("jpg")||value.toLowerCase().endsWith("png"))){
	                		createPic(value,(i+1),j,workbook,sheet);//生成图像
	                	}else{
	                		row.createCell(j).setCellValue(value);
	                	}
	                }
	                index++;  
	            }     
	        }  
	        workbook.write(out);  
	        workbook.close();
	    }
	   
	   public static void createPic(String filepPath,int curRow,int curCol,HSSFWorkbook workbook,HSSFSheet sheet){
	        try {
	        	//生成图片
	        	File file=new File(filepPath);
	        	if(file.exists()){
			        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
			        BufferedImage bufferImg = ImageIO.read(file);
					ImageIO.write(bufferImg,"JPG",byteArrayOut);
					HSSFPatriarch patriarch = sheet.createDrawingPatriarch(); 
				    HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0,(short)curCol,curRow,(short)(curCol+1),curRow+1); 
				    patriarch.createPicture(anchor , workbook.addPicture(byteArrayOut.toByteArray(),HSSFWorkbook.PICTURE_TYPE_PNG));
	        	}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	    }
	   public static String getJsonColValue(Object obj, String colStr )
		{
			String[] colAtts = colStr.split("@");
			Object objCol = Reflections.getFieldValue(obj, colAtts[0]);
			
			if( colAtts.length == 1){
				if(objCol!=null)
					return String.valueOf(objCol);
				else
					return "";
			}

			String value = null;
			if( objCol != null){
				switch(colAtts.length){
				case 2:
					value = WebFuncUtil.getResValue(colAtts[1], objCol.toString());
					break;
				case 3:
					value = DateUtil.long2Str(Long.valueOf(objCol.toString()),"default".equals(colAtts[2])?"yyyy-MM-dd HH:mm:ss":colAtts[2]);
					break;
				case 4:
					value = WebFuncUtil.getObjColValue(colAtts[1], colAtts[2], colAtts[3], objCol.toString());
					break;
				}		
			}
			return value;
		}
	   
	public static void main(String[] args) {
		   List<SysUser> list=new ArrayList<SysUser>();
		   SysUser user=new SysUser();
		   user.setLoginName("test");
		   user.setUsername("user1");
		   SysUser user1=new SysUser();
		   user1.setLoginName("test1");
		   user1.setUsername("user2");
		   list.add(user1);list.add(user);
		   String[] headers = {"登录名", "姓名","头像"};//导出字段名称
		   String attr="loginName,username,company";//导出映射字段
		   try {
		          OutputStream out = new FileOutputStream("d:/test/exp.xls");  
		          expExcel("用户信息", headers, list, attr, out);
		          out.close();
		   } catch (Exception e) {
		    	  System.out.println("导出excel出错。。。");
		          e.printStackTrace();  
		   }
	}
}
