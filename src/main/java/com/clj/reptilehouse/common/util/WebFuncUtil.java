package com.clj.reptilehouse.common.util;

import java.util.List;

import com.clj.reptilehouse.common.SampleResItem;
import com.clj.reptilehouse.common.util.NetworkUtil;
import com.clj.reptilehouse.common.util.ServiceUtil;
import com.clj.reptilehouse.common.util.StringUtil;
import com.clj.reptilehouse.system.entity.SysMenu;
import com.clj.reptilehouse.system.service.MenuService;
import com.clj.reptilehouse.system.service.ParamItemService;
import com.clj.reptilehouse.system.service.ResItemService;

public class WebFuncUtil {

	private static ResItemService resService = ServiceUtil.getResService();
	private static ParamItemService paramService = ServiceUtil.getParamService();
	private static MenuService menuService = ServiceUtil.getMenuService();

    public static String getResValue(String grpCode, String resCode){
		return resService.findResContent(grpCode, resCode);
	}
    
    public static String getResCode(String grpCode, String resValue){
		return resService.findResCode(grpCode, resValue);
	}
    
    public static String getResValue(String grpCode, String resCode, String defaultValue){
		String value = resService.findResContent(grpCode, resCode);
		if( value==null || "".equals(value) ){
			return defaultValue;
		}
		return value;
	}
    
    public static String getParamValue(String paramCode){
		return paramService.getParamValue(paramCode);
	}
    
    public static String getParamValue(String paramCode, String defaultValue){
    	String value = paramService.getParamValue(paramCode);	
		if( value==null || "".equals(value) ){
			return defaultValue;
		}
		return value;
	}
    
    public static String getObjColValue(String objName, String col1Name, String col2Name, String col1Value){		
		return getObjColValue(objName, col1Name, col2Name, col1Value, "");	
	}
	
    public static String getObjColValue(String objName, String col1Name, String col2Name, String col1Value, String defaultValue){		
		String filter= col1Name+"='"+col1Value+"'";
		List<SampleResItem> items = null;
		if ( objName!= null && col1Name!= null && col2Name!= null ){
			items = resService.findSampleResItems(objName, col2Name, col1Name, filter, null);
		}

		if( items==null || items.size()==0 || items.get(0).getName()==null){
			return defaultValue;
		}
		
		return items.get(0).getName();	
	}
    
    public static String getResJsonList(String grpCode, String filter, String jsonCols){
    	return resService.getResJsonList(grpCode, filter, jsonCols);
	}
    
    public static String getResJsonTree(String grpCode, String filter, String jsonCols, String childName){
    	return resService.getResJsonTree(grpCode, filter, jsonCols, childName);
	}
    
    public static String getObjJsonList(String grpCode, String filter, String objCols, String jsonCols){
    	return resService.getObjJsonList(grpCode, filter, objCols, jsonCols);
	}
    
    public static String getObjJsonTree(String grpCode, String filter, String objCols, String jsonCols, String childName){
    	return resService.getObjJsonTree(grpCode, filter, objCols, jsonCols, childName);
	}
    
    public static String long2ip(Long ip){
    	if( ip==null || ip==0 ){
    		return "";
    	}
    	return NetworkUtil.str_addr(ip);
	}
    
    public static String titleContent(String menuName, String mdlName, String webPath){
    	String content = mdlName;
    	SysMenu menu = menuService.getMenuByName(menuName);
    	if( menu != null ){
    		if( !StringUtil.isBlank(mdlName) ){
    			if( !StringUtil.isBlank(menu.getUrl()) ){
    				content = "<a onclick=\"gotoURL('"+ webPath+"/"+menu.getUrl()+"')\" href=\"#\">" + menu.getName() + "</a> > " + content;
    			}
    			else{
        			content = menu.getName() + " > " + content;
    			}
    		}
    		else{
    			content = menu.getName();
    		}
 	    	while( menu != null && menu.getParentId()!=null ){
	    		menu = menuService.getMenu(menu.getParentId());
	    		if( menu != null ){
	    			if( !StringUtil.isBlank(menu.getUrl()) ){
	    				content = "<a onclick=\"gotoURL('"+ webPath+"/"+menu.getUrl()+"')\" href=\"#\">" + menu.getName() + "</a> > " + content;
	    			}
	    			else{
	    				content = menu.getName() + " > " + content;
	    			}
	    		}
	    	}
    	}
    	if( content.length()>0 ){
    		content = "<div id=\"page-title\">"+content+"</div>";
    	}
    	return content;
	}
    
}
