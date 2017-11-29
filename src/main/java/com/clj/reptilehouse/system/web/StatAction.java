package com.clj.reptilehouse.system.web;

import java.io.IOException;
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
import com.clj.reptilehouse.system.entity.SysLoginInfo;
import com.clj.reptilehouse.system.entity.SysUser;
import com.clj.reptilehouse.system.service.LoginService;
import com.clj.reptilehouse.system.web.StatAction;

@Controller
@RequestMapping(value = "/system/stat")
public class StatAction {
	private final String viewPath = "/system/stat";

	private static Logger logger = LoggerFactory.getLogger(StatAction.class);

	@Autowired
	private LoginService loginService;
                                             
	
	@RequestMapping(value = "/loginStat.do")
	public String loginStat(ModelMap map) {
		logger.info( "Enter loginStat.do ..." );
		return viewPath+"/loginStat";
	}
	
	@RequestMapping(value = "/searchLoginStat.do")
	public String searchLoginStat(PageRequest pageReq, HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.info( "Enter searchLoginStat.do ..." );
		SysUser curUser = SystemUtil.getLoginUser();
		
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request, "filter");
		if( !curUser.getLoginName().equals(Global.USER_DEVADMIN)){
			PropertyFilter filter = new PropertyFilter("NES_loginName", Global.USER_DEVADMIN);
			filters.add(filter);
		}
		Page<SysLoginInfo> page = loginService.searchInfo(pageReq, filters);
		String jsonStr = JsonUtil.object2json(page);
		response.getWriter().print(jsonStr);
	    return null;         
	}


}
