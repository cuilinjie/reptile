package com.clj.reptilehouse.system.web;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.clj.reptilehouse.system.web.SetupAction;

@Controller
@RequestMapping(value = "/system/setup")
public class SetupAction {
	private final String viewPath = "/system/setup";
	
	private static Logger logger = LoggerFactory.getLogger(SetupAction.class);
                                 
	@RequestMapping(value = "/main.do")
	public String main(ModelMap map) {
		logger.info("系统设置>>>>");
		return viewPath+"/main";
	}
}
