package com.clj.reptilehouse.common.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.clj.reptilehouse.common.excel.ColT;
import com.clj.reptilehouse.common.excel.Excel;


public class Excel {
	private Workbook wb = null;
	private CellStyle csTitle = null;
	private CellStyle csData = null;
	
	private String fileName = "";
	private String excelName = "";

	public Excel(String filename) {
		if(filename!=null){
			fileName = filename;
			fileName.trim();
			int n1 = fileName.lastIndexOf("/");
			int n2 = fileName.lastIndexOf("\\");
			int n = n2>n1 ? n2:n1;
			if (n >= 0) {
				//filePath = fileName.substring(0, n);
				excelName = fileName.substring(n + 1);
			} else {
				excelName = fileName;
			}
			File file = new File(fileName);
			if (file.exists()) {
				FileInputStream in = null;  
				try {
					in = new FileInputStream(fileName);// 
					wb = WorkbookFactory.create(in);
				} catch (InvalidFormatException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					if( in != null ){
						in.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		initCellStyle();
	}

	public Excel(FileInputStream stream) {
		try {
			wb = WorkbookFactory.create(stream);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		initCellStyle();
	}
	
	public void initCellStyle(){
		if( wb == null ){
			return;
		}
		
	    csTitle = wb.createCellStyle();
	    csTitle.setBorderTop(CellStyle.BORDER_THIN);
	    csTitle.setBorderRight(CellStyle.BORDER_THIN);
	    csTitle.setBorderBottom(CellStyle.BORDER_THIN);
	    csTitle.setBorderLeft(CellStyle.BORDER_THIN);
	    csTitle.setTopBorderColor(IndexedColors.BLACK.getIndex());
	    csTitle.setRightBorderColor(IndexedColors.BLACK.getIndex());
	    csTitle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	    csTitle.setLeftBorderColor(IndexedColors.BLACK.getIndex());

	    csData = wb.createCellStyle();
	    csData.setBorderTop(CellStyle.BORDER_THIN);
	    csData.setBorderRight(CellStyle.BORDER_THIN);
	    csData.setBorderBottom(CellStyle.BORDER_THIN);
	    csData.setBorderLeft(CellStyle.BORDER_THIN);
	    csData.setTopBorderColor(IndexedColors.BLACK.getIndex());
	    csData.setRightBorderColor(IndexedColors.BLACK.getIndex());
	    csData.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	    csData.setLeftBorderColor(IndexedColors.BLACK.getIndex());
	}
	
	public String getExcelName(){
		return this.excelName;
	}
	
	public void setExcelName(String excelName){
		this.excelName = excelName;
	}
	
	public CellStyle getCsTitle() {
		return csTitle;
	}

	public void setCsTitle(CellStyle csTitle) {
		this.csTitle = csTitle;
	}

	public CellStyle getCsData() {
		return csData;
	}

	public void setCsData(CellStyle csData) {
		this.csData = csData;
	}

	public Sheet getSheet0(){
		Sheet sheet = wb.getSheetAt(0);
		if (sheet == null) {
			sheet = wb.createSheet();
		}
		return sheet;
	}

	public String[] getTitleNames() {
		if( wb == null ){
			return null;
		}
		Sheet sheet = getSheet0();
		if (sheet == null) {
			return null;
		}
		Row row = sheet.getRow(0);
		if (row == null) {
			return null;
		}
		List<Object> list = getRowObjList(row);

		String[] titles = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			titles[i] = list.get(i).toString();
		}
		return titles;
	}

	public void delCell(Row row, int index) {
		// 把后面的cell往前移
		int lastIndex = row.getLastCellNum();
		for(int i=index; i<lastIndex-1; i++) {
			Cell cur = row.getCell(i);
			Cell next = row.getCell(i+1);
			if( cur!=null && next!=null ){
				cur.setCellType(next.getCellType());
				cur.setCellStyle(next.getCellStyle());
				cur.setCellValue(next.getStringCellValue());
				cur.removeCellComment();
				cur.setCellComment(next.getCellComment());
			}
			else if( cur!= null){
				cur.removeCellComment();
				row.removeCell(cur);
			}
		}
		Cell last = row.getCell(lastIndex);
		if( last != null ){
			last.removeCellComment();
			row.removeCell(last);
		}
	}
	
	public boolean setTitleNames(String[] colNames) {
		if( wb == null ){
			return false;
		}
		Sheet sheet = getSheet0();
		if (sheet == null) {
			return false;
		}
		
		int lastRow = sheet.getLastRowNum(); // excel数据文件的行数
		if( lastRow == 0 ){
			createRow(sheet, lastRow, colNames, csTitle);
		}
		else{
			for (int i=lastRow; i>0; i--) {
				sheet.removeRow(sheet.getRow(i));
			}
			Row row = sheet.getRow(0);
			for( int i=0; i<colNames.length; i++ ){
				boolean find = false;
				Cell findCell = null;
				int j=i;
				for( ; j<row.getLastCellNum(); j++ ){
					findCell = row.getCell(j);
					if( findCell.getStringCellValue().equals(colNames[i]) ){
						find = true;
						break;
					}
				}
				Cell curCell = row.getCell(i);
				if (curCell == null) {
					curCell = row.createCell(i);
				}
				if( find ){
					if( i!=j ){
						curCell.setCellType(findCell.getCellType());
						curCell.setCellStyle(findCell.getCellStyle());
						curCell.setCellValue(findCell.getStringCellValue());
						curCell.removeCellComment();
						curCell.setCellComment(findCell.getCellComment());
					}
				}
				else{
					curCell.setCellValue(colNames[i]);
					curCell.removeCellComment();
				}
			}
			for (int i=colNames.length; i<row.getLastCellNum(); i++) {
				Cell cell = row.getCell(i);
				if( cell != null ){
					cell.removeCellComment();
					row.removeCell(cell);
				}
			}
		}
		for (int i=0; i<colNames.length; i++) {
			sheet.autoSizeColumn(i);
		}

		return true;
	}

	public List<List<Object>> getDataList() {
		if( wb == null ){
			return null;
		}
		Sheet sheet = getSheet0();
		if (sheet == null) {
			return null;
		}
		List<List<Object>> dataList = new ArrayList<List<Object>>();
		int lastRow = sheet.getLastRowNum();	// excel数据文件的行数
		for ( int i=1; i<=lastRow; i++ ) {
			Row row = sheet.getRow(i);
			List<Object> rowList = getRowObjList(row);
			if( rowList != null ){
				dataList.add(rowList);
			}
		}
		return dataList;
	}
	
	public List<Object> getRowObjList(Row row) {
		if (row == null) {
			return null;
		}

		List<Object> listObj = new ArrayList<Object>();
		for (int i = 0; i < row.getLastCellNum(); i++) {
			// 读取单元格
			Cell cell = row.getCell(i);
			Object obj = getCellObj(cell);
			listObj.add(obj);
		}
		return listObj;
	}
	
	public String[] getRowArrStr(Row row) {
		if (row == null) {
			return null;
		}

		String[] arrStr = new String[row.getLastCellNum()];
		for (int i = 0; i < row.getLastCellNum(); i++) {
			// 读取单元格
			Cell cell = row.getCell(i);
			Object obj = getCellObj(cell);
			arrStr[i] = obj==null ? null:obj.toString();
		}
		return arrStr;
	}

	public Object getCellObj(Cell cell) {
		if (cell == null) {
			return null;
		}

		Object object = null;
		// 判断获取类型
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
			object = cell.getNumericCellValue();
			break;
		case Cell.CELL_TYPE_STRING:
			object = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			object = new Boolean(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_BLANK:
			object = "";
			break;
		case Cell.CELL_TYPE_FORMULA:
			int a = (cell.getCellFormula().indexOf("+") + 1) + (cell.getCellFormula().indexOf('/') + 1)
					+ (cell.getCellFormula().indexOf('*') + 1) + (cell.getCellFormula().indexOf('-') + 1);
			if (a <= 0) {
				object = cell.getCellFormula();
			} else if (a > 0) {
				object = cell.getNumericCellValue();
			}
			break;
		case Cell.CELL_TYPE_ERROR:
			object = new Byte(cell.getErrorCellValue());
			break;
		default:
			System.out.println("未知类型");
			break;
		}
		return object;
	}

	@SuppressWarnings("unused")
	private Object getRightTypeValue(Object object, int colType) throws Exception {
		switch (colType) {
		case ColT.NO:// 不做任何转换
			break;
		case ColT.cSTRING:
			object = object.toString();
			break;
		case ColT.cINT:
			if (object instanceof Double) {
				object = ((Double) object).intValue();
			}
			break;
		case ColT.cFlOAT:
			if (object instanceof Double) {
				object = ((Double) object).floatValue();
			}
			break;
		case ColT.cDOUBLE:
			if (!(object instanceof Double)) {
				if (object instanceof String) {
					object = Double.parseDouble(object.toString());
				}
			}
			break;
		case ColT.cDATE:
			if (!(object instanceof Date)) {
				if (object instanceof String) {
					// 按照格式yyyy-MM-dd转换
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					object = sdf.parse(object.toString());
				} else if (object instanceof Double) {
					int day = ((Double) object).intValue();
					Calendar c = Calendar.getInstance();
					c.set(1900, 0, 1);
					c.add(Calendar.DAY_OF_YEAR, day);
					object = c.getTime();
				} else {
					throw new Exception("日期格式错误");
				}
			}
			break;
		case ColT.cLONG:
			if (object instanceof Double) {
				object = ((Double) object).longValue();
			}
			break;
		case ColT.cCode:
			// 需要根据KV转换规则进行转换
			break;
		}
		return object;
	}
	
	public boolean createRows(List<List<Object>> listData ){
		Sheet sheet = getSheet0();
		if (sheet == null) {
			return false;
		}
		int lastRow = sheet.getLastRowNum();	// excel数据文件的行数
		for( List<Object> listObj: listData){
			lastRow++;
			if( !createRow(sheet, lastRow, listObj) ){
				return false;
			}
		}
		int colLength = 0;
		if( listData.size()>0 ){
			colLength = listData.get(0).size();
		}
		for (int i=0; i<colLength; i++) {
			sheet.autoSizeColumn(i);
		}
		return true;
	}
	
	public boolean createRow(Sheet sheet, int rowIndex, String[] arrStr ){
		return createRow(sheet, rowIndex, arrStr, csData);
	}
	
	public boolean createRow(Sheet sheet, int rowIndex, String[] arrStr, CellStyle style ){
		Row row = sheet.createRow(rowIndex);
		if( row == null ){
			return false;
		}
        for(int i=0; i<arrStr.length; i++){
        	Cell cell = row.createCell(i);
        	cell.setCellStyle(style);
        	cell.setCellValue(arrStr[i]);
        }
        return true;
	}	
	public boolean createRow(Sheet sheet, int rowIndex, List<Object> listObj ){
		return createRow(sheet, rowIndex, listObj, csData);
	}
	
	public boolean createRow(Sheet sheet, int rowIndex, List<Object> listObj, CellStyle style ){
		Row row = sheet.createRow(rowIndex);
		if( row == null ){
			return false;
		}
        for(int i=0; i<listObj.size(); i++){
        	Cell cell = row.createCell(i);
        	cell.setCellStyle(style);
        	setCellValue(cell, listObj.get(i));
        }
        return true;
	}
	
	public void setCellValue(Cell cell, Object object){
		if (object instanceof Integer) {
			int i = ((Integer) object).intValue();
			cell.setCellValue(i);
		} else if (object instanceof String) {
			String s = (String) object;
			cell.setCellValue(s);
		} else if (object instanceof Double) {
			double d = ((Double) object).doubleValue();
			cell.setCellValue(d);
		} else if (object instanceof Float) {
			float f = ((Float) object).floatValue();
			cell.setCellValue(f);
		} else if (object instanceof Long) {
			long l = ((Long) object).longValue();
			cell.setCellValue(l);
		} else if (object instanceof Boolean) {
			boolean b = ((Boolean) object).booleanValue();
			cell.setCellValue(b);
		} else if (object instanceof Date) {
			Date d = (Date) object;
			cell.setCellValue(d);
		} else{
			cell.setCellValue(object.toString());
		}
	}
	
	public boolean write(HttpServletResponse response) {
		try {
			response.setContentType("application/ms-excel");
			response.setHeader("Content-disposition", "attachment; filename=" + new String(excelName.getBytes(), "iso-8859-1") );
			OutputStream out = response.getOutputStream();
//			FileOutputStream fileOut = new FileOutputStream("d:\\workbook.xls");
//			wb.write(fileOut);
			wb.write(out);
			out.flush();
			out.close();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	public static void main(String[] args) {
		System.out.println("Excel test");
		String file = "d:\\test.xls";
		Excel excel = new Excel(file);
		String[] titles = excel.getTitleNames();
		System.out.println("....");
		for (int i = 0; i < titles.length; i++) {
			System.out.println(titles[i]);
		}
		System.out.println("....");
	}

}