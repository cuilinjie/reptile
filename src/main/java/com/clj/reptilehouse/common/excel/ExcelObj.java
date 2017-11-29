package com.clj.reptilehouse.common.excel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springside.modules.utils.Reflections;

import com.clj.reptilehouse.common.excel.ExcelObj;
import com.clj.reptilehouse.common.util.WebFuncUtil;

public class ExcelObj<T> {
	
	Class<T> tClass;
	
	Map<String, String> mapType = new HashMap<String, String>();
	
	public ExcelObj(Class<T> t) {
		tClass = t;
	}
	
	public List<T> getObjListFrom(String filePath, String objAttNames){
		try {
			FileInputStream in = new FileInputStream(filePath);
			return getObjListFrom(in, objAttNames);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> getObjListFrom(InputStream stream, String objAttNames){
		if( StringUtils.isBlank(objAttNames) ){
			return null;
		}
		
		T objCheck = null;
		try {
			objCheck = tClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}

		String[] attNames = objAttNames.split(",");
		for( String attName:attNames ){
			String[] atts = attName.split("@");
			if( !getFieldType(objCheck, atts[0])){
				return null;
			}
			if( atts.length == 5 ){
				if( !getFieldType(objCheck, atts[4]) ){
					return null;
				}
			}
		}

		Workbook wb = null;
		try {
			wb = WorkbookFactory.create(stream);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		Sheet sheet = wb.getSheetAt(0);
		List<T> list = new ArrayList();
		int lastRow = sheet.getLastRowNum();	// excel数据文件的行数
		for ( int i=1; i<=lastRow; i++ ) {
			Row row = sheet.getRow(i);
			T obj = loadObjFromRow(row, attNames);
			if( obj != null ){
				list.add(obj);
			}
		}
		
		return list;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String,List<T>> getMultiObjListFrom(InputStream stream, String objAttNames){
		if( StringUtils.isBlank(objAttNames) ){
			return null;
		}
		T objCheck = null;
		try {
			objCheck = tClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}

		String[] attNames = objAttNames.split(",");
		for( String attName:attNames ){
			String[] atts = attName.split("@");
			if( !getFieldType(objCheck, atts[0])){
				return null;
			}
			if( atts.length == 5 ){
				if( !getFieldType(objCheck, atts[4]) ){
					return null;
				}
			}
		}

		Workbook wb = null;
		try {
			wb = WorkbookFactory.create(stream);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		Sheet sheet = wb.getSheetAt(0);
		List<T> list = new ArrayList();
		int lastRow = sheet.getLastRowNum();	// excel数据文件的行数
		for ( int i=1; i<=lastRow; i++ ) {
			Row row = sheet.getRow(i);
			T obj = loadObjFromRow(row, attNames);
			if( obj != null ){
				list.add(obj);
			}
		}
		Sheet sheet1 = wb.getSheetAt(1);
		List<T> list1 = new ArrayList();
		int lastRow1 = sheet1.getLastRowNum();	// excel数据文件的行数
		for ( int i=1; i<=lastRow1; i++ ) {
			Row row = sheet1.getRow(i);
			T obj = loadObjFromRow(row, attNames);
			if( obj != null ){
				list1.add(obj);
			}
		}
		Map<String,List<T>> map =new HashMap<String,List<T>>();
		map.put(sheet.getSheetName(),list);
		map.put(sheet1.getSheetName(),list1);
		return map;
	}
	
	private boolean getFieldType(T obj, String fieldName){
		Field field = Reflections.getAccessibleField(obj, fieldName);
		if( field == null ){
			return false;
		}
		mapType.put(fieldName, field.getType().getName());
		return true;
	}
	
	private T loadObjFromRow(Row row, String[] attNames){
		if( row == null ){
			return null;
		}

		T obj = null;
		try {
			obj = tClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
		
		for( int i=0; i<attNames.length; i++ ){
			Cell cell = row.getCell(i);
			if( !loadObjFromCell(cell, obj, attNames[i]) ){
				return null;
			}
		}
		return obj;
	}

	private boolean loadObjFromCell(Cell cell, T obj, String attName){
		if( cell == null ){
			return true;
		}
		
		Object cellValue = loadObjFromCell(cell);
		
		String[] atts = attName.split("@");
		Object value = null;
		if( atts.length>1 ){
			value = getAttValue(atts, cellValue);			
			if( atts.length == 5 ){
				try {
					Reflections.setFieldValue(obj, atts[4], cellValue);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		else{
			value = cellValue;
		}
		
		value = getRealValue(value, mapType.get(atts[0]));
				
		try {
			Reflections.setFieldValue(obj, atts[0], value);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private Object getRealValue(Object value, String type){
		Object realValue = null;
		if( "java.lang.String".equals(type) ){
			realValue = value.toString();
		}
		else if( "java.lang.Long".equals(type) ){
			try{
				realValue = (long)(Double.parseDouble(value.toString()));				
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		else if( "java.lang.Integer".equals(type) ){
			try {
				realValue = (int)(Double.parseDouble(value.toString()));	
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		else if( "java.lang.Float".equals(type) ){
			try {
				realValue = Float.parseFloat(value.toString());
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		else if( "java.lang.Double".equals(type) ){
			try {
				realValue = Double.parseDouble(value.toString());
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		else if( "java.lang.Boolean".equals(type) ){
			try {
				realValue = Boolean.valueOf(value.toString());
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		else if( "java.sql.Timestamp".equals(type) ){
			try {
				realValue = Timestamp.valueOf(value.toString());
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		else if( "java.sql.Date".equals(type) ){
			try {
				realValue = Date.valueOf(value.toString());
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return realValue;
	}
	
	private Object loadObjFromCell(Cell cell){
		Object value = null;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			value = cell.getRichStringCellValue().getString();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				value = cell.getDateCellValue();
			} else {
				value = cell.getNumericCellValue();
			}
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			value = cell.getBooleanCellValue();
			break;
		case Cell.CELL_TYPE_FORMULA:
			value = cell.getCellFormula();
			break;
		default:
			value = "";
		}		
		
		return value;
	}
	
	private Object getAttValue(String[] colAtts, Object cellValue){
		Object value = null;
		switch(colAtts.length){
		case 2:
			value = WebFuncUtil.getResCode(colAtts[1], cellValue.toString());
			break;
		case 4:
			value = WebFuncUtil.getObjColValue(colAtts[1], colAtts[3], colAtts[2], cellValue.toString());
			break;
		case 5:
			value = WebFuncUtil.getObjColValue(colAtts[1], colAtts[3], colAtts[2], cellValue.toString());
			break;
		default:
			value = cellValue;
			break;
		}
		
		return value;
	}

	public static void main(String[] args){
		/*System.out.println("ExcelUtil test");
		ExcelObj<SysUser> excelUtil = new ExcelObj<SysUser>(SysUser.class);
		List<SysUser> list = excelUtil.getObjListFrom("d:\\test.xlsx", "userId@SysUser@userId@loginName,userName");
		System.out.println("....");
		for(int i=0; i<list.size(); i++){
			System.out.println(" userId:"+list.get(i).getUserId()+" userName:"+list.get(i).getUserName());			
		}
		System.out.println("....");*/
	}
	
}