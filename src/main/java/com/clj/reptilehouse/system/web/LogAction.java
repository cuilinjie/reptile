package com.clj.reptilehouse.system.web;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.PageRequest;
import org.springside.modules.orm.PropertyFilter;

import com.clj.reptilehouse.common.Global;
import com.clj.reptilehouse.common.util.JsonUtil;
import com.clj.reptilehouse.common.util.SystemUtil;
import com.clj.reptilehouse.system.entity.SysLog;
import com.clj.reptilehouse.system.entity.SysUser;
import com.clj.reptilehouse.system.service.LogService;
import com.clj.reptilehouse.system.web.LogAction;

@Controller
@RequestMapping(value = "/system/log")
public class LogAction {
	private final String viewPath = "/system/log";

	private static Logger logger = LoggerFactory.getLogger(LogAction.class);

	@Autowired
	private LogService logService;
	
	
	@RequestMapping(value = "/main.do")
	public String main(ModelMap map) {
		logger.info( "Enter main.do ..." );
		List<SysLog> list = logService.getAllLog();
		String jsonStr = logService.getJsonListStr(list);
		map.put("jsonData", jsonStr);
		return viewPath+"/main";
	}
	
	@RequestMapping(value = "/getData.do")
	public String getData(HttpServletResponse response) throws IOException{     
		logger.info( "Enter getData.do ..." );
		List<SysLog> list = logService.getAllLog();
		String jsonStr = logService.getJsonListStr(list);
	    response.getWriter().print(jsonStr);
	    return null;         
	}   
	
	@RequestMapping(value = "/view.do")
	public String view(Long id, ModelMap map) throws IOException {
		logger.info( "Enter view.do ..." );
		SysLog log = logService.getLog(id);
		map.put("log", log);
		return viewPath+"/view";
	}
	
	@RequestMapping(value = "/delete.do")
	public String delete(String id, HttpServletResponse response) throws IOException {
		logger.info( "Enter delete.do ..." );
		logService.deleteLog(id.split(Global.SPLIT_KEY));
		response.getWriter().print(JsonUtil.JSON_RESULT_SUCCESS);
	    return null;         
	}
	@RequestMapping(value = "/clearGo.do")
	public String clearGo(ModelMap map) throws IOException {
		logger.info( "Enter clearGo.do ..." );
		return viewPath+"/clear";
	}
	
	@RequestMapping(value = "/clear.do")
	public String clear(int clearType, Timestamp clearTime, ModelMap map) throws IOException {
		logger.info( "Enter clear.do ..." );
		SysUser curUser = SystemUtil.getLoginUser();
		if( curUser == null ){
			return null;
		}
		boolean isSuper = SystemUtil.isSuperUser(curUser);
		if( clearType == 1 ){
			if( isSuper ){
				logService.clearAllLog();				
			}
			else{
				logService.clearAllUserLog(curUser.getId());				
			}
			map.put("result", Global.RESULT_SUCCESS);
		}
		else if( clearType == 2 ){
			if( isSuper ){
				logService.clearLogByTime(clearTime.getTime());				
			}
			else{
				logService.clearUserLogByTime(curUser.getId(), clearTime);				
			}
			map.put("result", Global.RESULT_SUCCESS);
		}
		else{
			map.put("result", Global.RESULT_ERROR);
			map.put("message", "清除日志类型不正确！");
		}
		return viewPath+"/clear";
	}
	
	@RequestMapping(value = "/search.do")
	public String search(PageRequest pageReq, HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.info( "Enter search.do ..." );
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request, "filter");
		Page<SysLog> page = logService.searchLog(pageReq, filters);
		String jsonStr = logService.getJsonPageStr(page);
		response.getWriter().print(jsonStr);
	    return null;         
	}


}
