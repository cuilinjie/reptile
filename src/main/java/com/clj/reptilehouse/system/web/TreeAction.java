package com.clj.reptilehouse.system.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/tree")
public class TreeAction {
	@RequestMapping(value = "/treeUser.do")
	public String treeUser(ModelMap map) {
		return "tree/treeUser";
	}
	
	@RequestMapping(value = "/treeOrg.do")
	public String treeOrg(ModelMap map) {
		return "tree/treeOrg";
	}
}
