package com.clj.reptilehouse.common.excel;

import java.util.ArrayList;
import java.util.List;

import org.springside.modules.utils.Reflections;

import com.clj.reptilehouse.common.util.WebFuncUtil;

public class ExcelUtil {
	public static String[] list2arrStr(List<String> list){
		String[] arrStr = new String[list.size()];
		for( int i=0; i<list.size(); i++ ){
			arrStr[i] = list.get(i);
		}
		return arrStr;
	}
	
	public static List<String> arrStr2list(String[] arrStr){
		List<String> list = new ArrayList<String>();
		for( String str: arrStr ){
			list.add(str);
		}
		return list;
	}
	
	public static String[] getInRangeStr(String[] input, String[] range) {
		return getInRangeStr(input, range, true);
	}	
	
	public static String[] getInRangeStr(String[] input, String[] range, boolean inputOrder) {
		List<String> list = new ArrayList<String>();
		String[] arrStr1 = inputOrder ? input:range;
		String[] arrStr2 = inputOrder ? range:input;
		for( String str1: arrStr1){
			for( String str2: arrStr2){
				if( str1.equals(str2) ){
					list.add(str1);
					break;
				}
			}
		}
		return list2arrStr(list);
	}
	
	public static String[] getAttNames(String[] arrColName, String[] excelColName, String[] excelAttName){
		String[] arrAttName = new String[arrColName.length];
		for( int i=0; i<arrColName.length; i++ ){
			for( int j=0; j<excelColName.length; j++ ){
				if( arrColName[i].equals(excelColName[j]) ){
					arrAttName[i] = excelAttName[j];
					break;
				}
			}
		}
		return arrAttName;
	}
	
	public static List<List<Object>> getListData(List<Object> listObj, String[] arrAttName){
		List<List<Object>> listData = new ArrayList<List<Object>>();
		for( Object object: listObj ){
			listData.add(getObjectData(object, arrAttName));
		}
		return listData;
	}
	
	public static List<Object> getObjectData(Object object, String[] arrAttName){
		List<Object> listObj = new ArrayList<Object>();
		for( String attName:arrAttName ){
			String[] colAtts = attName.split("@");
			Object objCol = Reflections.getFieldValue(object, colAtts[0]);
			
			if( colAtts.length == 1){
				listObj.add(objCol);
			}
			else{
				String value = null;
				if( objCol != null){
					switch(colAtts.length){
					case 2:
						value = WebFuncUtil.getResValue(colAtts[1], objCol.toString());
						break;
					case 4:
						value = WebFuncUtil.getObjColValue(colAtts[1], colAtts[2], colAtts[3], objCol.toString());
						break;
					}		
				}
				listObj.add(value);				
			}
		}
		return listObj;
	}

//	public static Excel makeExcel(String excelMdlName, String[] excelColNames, String[] excelAttNames, String[] colNames, List<List<Object>> listData ){
//		
//						
//		
//		return excel;
//	}
	
	public static void main(String[] args) {
		System.out.println("ExcelUtil test");
		String[] input = {"1","22","33"};
		String[] range = {"1","22","333","4444"};
		String[] output = getInRangeStr(input, range);
		for( String s:output){
			System.out.println(s);
		}
	}
}
