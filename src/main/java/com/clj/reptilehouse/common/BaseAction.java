package com.clj.reptilehouse.common;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.clj.reptilehouse.common.Global;
import com.clj.reptilehouse.common.TimestampEditor;

public class BaseAction {
	
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	
	@InitBinder  
	protected void initBinder(HttpServletRequest request,  ServletRequestDataBinder binder) throws Exception {  
		binder.registerCustomEditor(Timestamp.class, new TimestampEditor());
	}  
	
	@ExceptionHandler(Exception.class)       
	public String handleException(Exception ex, HttpServletResponse response) throws Exception {     
	    if(ex instanceof org.springframework.web.multipart.MaxUploadSizeExceededException){  
			String outStr = getCheckResult(Global.RESULT_ERROR,"上传文件超过限定大小！");
			response.getWriter().print(outStr);
	    }  
	    return null;         
	}  
	
	public String getCheckResult(String result, String message){
		return "<script>parent.checkResult('"+result+"','"+message+"')</script>";
	}
	
	protected void stringGsonOut(String str) {
		response.setContentType("application/json;charset=utf-8");
		try {
			response.getWriter().println(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	protected void htmlOut(String htmlString) {
		response.setContentType("text/html;charset=UTF-8");
		try {
			response.getWriter().print(htmlString);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	protected void jsonOut(Object object) {
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString;
		try {
			jsonString = objectMapper.writeValueAsString(object);
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().println(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
