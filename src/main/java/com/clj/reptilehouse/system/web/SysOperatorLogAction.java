package com.clj.reptilehouse.system.web;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springside.modules.orm.PageRequest;
import org.springside.modules.orm.PropertyFilter;

import com.clj.reptilehouse.common.util.DateUtil;
import com.clj.reptilehouse.system.entity.SysOperatorLog;
import com.clj.reptilehouse.system.service.LogOperator;
import com.clj.reptilehouse.system.service.SysOperatorLogService;

@Controller
@RequestMapping(value = "/system/operator/operatorlog")
public class SysOperatorLogAction {
	
	private final String viewPath = "/system/operatorlog";

	private static Logger logger = LoggerFactory.getLogger(LogAction.class);
	
	@Autowired
	private  SysOperatorLogService sysOperatorLogService;
	@Autowired
	private LogOperator LogOperator;

	@RequestMapping(value = "/main.do")
	public String main(ModelMap map, HttpServletRequest request) {
		logger.info( "Enter main.do ..." );
		/*String operatorContext = "记录操作日志内容";
		String operatorPersion = "系统用户";
		String clientIpaddr = request.getLocalAddr();
		LogOperator.addLog(LogConst.OperatorModel.ACCOUNT_POLICY_MANAGE, LogConst.OperatorAction.ADD,
				operatorContext, operatorPersion, clientIpaddr);*/
		List<SysOperatorLog> list = sysOperatorLogService.getAllLog();
		String jsonStr = sysOperatorLogService.getJsonListStr(list);
		map.put("jsonData", jsonStr);
		return viewPath+"/main";
	}
	
	@RequestMapping(value = "/getData.do")
	public String getData(HttpServletResponse response) throws IOException{     
		logger.info( "Enter getData.do ..." );
		List<SysOperatorLog> list = sysOperatorLogService.getAllLog();
		String jsonStr = sysOperatorLogService.getJsonListStr(list);
	    response.getWriter().print(jsonStr);
	    return null;         
	}   
	
	@RequestMapping(value = "/view.do")
	public String view(Long id,String operatorModel,String operatorAction,String operatorPersion,String operatorContext,String clientIpaddr, ModelMap map) throws IOException {
		logger.info( "Enter view.do ..." );
		operatorModel = URLDecoder.decode(operatorModel,"UTF-8");
		operatorAction = URLDecoder.decode(operatorAction,"UTF-8");
		operatorPersion = URLDecoder.decode(operatorPersion,"UTF-8");
		operatorContext = URLDecoder.decode(operatorContext,"UTF-8");
		SysOperatorLog log = new SysOperatorLog();//sysOperatorLogService.getLog(id);
		log.setOperatorModel(operatorModel);
		log.setOperatorContext(operatorContext.replaceAll(";", "\r\n"));
//		log.setOperatorContext(operatorContext.replaceAll(":", "\r\n"));
//		log.setOperatorContext(operatorContext.replaceAll(",", "\r\n"));
		log.setOperatorPersion(operatorPersion);
		log.setOperatorActionStr(operatorAction);
		log.setClientIpaddr(clientIpaddr);
		log.setOperatorTimeStr(DateUtil.long2Str(id,"yyyy-MM-dd HH:mm:ss"));
		map.put("log", log);
		return viewPath+"/view";
	}
	
	@RequestMapping(value = "/search.do")
	public String search(PageRequest pageReq, HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.info( "Enter search.do ..." );
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request, "filter");
		List<SysOperatorLog> page = sysOperatorLogService.searchLog(filters);
		Collections.sort(page,new Comparator<SysOperatorLog>(){
            public int compare(SysOperatorLog arg0, SysOperatorLog arg1) {
                return arg1.getOperatorTime().compareTo(arg0.getOperatorTime());
            }
        });
		String jsonStr = sysOperatorLogService.getJsonListStr(page);
		response.getWriter().print(jsonStr);
		
	    return null;         
	}

}
