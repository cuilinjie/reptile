package com.clj.reptilehouse.system.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springside.modules.orm.PropertyFilter;

import com.clj.reptilehouse.common.Global;
import com.clj.reptilehouse.common.util.JsonUtil;
import com.clj.reptilehouse.system.entity.SysPrivilegeGroup;
import com.clj.reptilehouse.system.service.PrivilegeGroupService;

@Controller
@RequestMapping(value = "/system/privilege/group")
public class PrivilegeGroupAction {
	private final String viewPath = "/system/privilege/group";

	private static Logger logger = LoggerFactory.getLogger(PrivilegeGroupAction.class);

	@Autowired
	private PrivilegeGroupService privGrpService;
	
	@RequestMapping(value = "/main.do")
	public String main(ModelMap map) {
		logger.info( "Enter main.do ..." );
		List<SysPrivilegeGroup> list = privGrpService.getAllPrivGroup();
		String jsonStr = privGrpService.getJsonTreeStr(list);
		map.put("jsonTreeData", jsonStr);
		return viewPath+"/main";
	}

	@RequestMapping(value = "/getData.do")
	public String getData(HttpServletResponse response) throws IOException{     
		logger.info( "Enter getData.do ..." );
		List<SysPrivilegeGroup> list = privGrpService.getAllPrivGroup();
		String jsonStr = privGrpService.getJsonTreeStr(list);
	    response.getWriter().print(jsonStr);
	    return null;         
	}                   

	@RequestMapping(value = "/search.do")
	public String search(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.info( "Enter search.do ..." );
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request, "filter");
		List<SysPrivilegeGroup> list = privGrpService.searchPrivGroup(filters);
		String jsonStr = privGrpService.getJsonListStr(list);
		response.getWriter().print(jsonStr);
	    return null;         
	}

	@RequestMapping(value = "/add.do")
	public String add(Long parentId, ModelMap map) {
		logger.info( "Enter add.do ..." );
		map.put("action", "add");
		SysPrivilegeGroup privGrp = new SysPrivilegeGroup();
		if(parentId==null) parentId=-1L;
		privGrp.setParentId(parentId);
		map.put("group", privGrp);
		return viewPath+"/edit";
	}

	@RequestMapping(value = "/edit.do")
	public String edit(Long id, ModelMap map) {
		logger.info( "Enter edit.do ..." );
		map.put("action", "edit");
		SysPrivilegeGroup privGrp = privGrpService.getPrivGroup(id);
		map.put("group", privGrp);
		return viewPath+"/edit";
	}

	@RequestMapping(value = "/save.do")
	public String save(String action, SysPrivilegeGroup group, ModelMap map) {
		logger.info( "Enter save.do ..." );
		boolean validate = true;
		if( StringUtils.isBlank(group.getName()) ){
			validate = false;
			map.put("message", "权限组名称不能为空！");
		}
		if( validate ){
			if( "add".equals(action)){
				privGrpService.addPrivGroup(group);
			}else{
				privGrpService.savePrivGroup(group);		
			}
			String jsonStr = privGrpService.getJsonObjStr(group);
			map.put("saveData", jsonStr);
			map.put("result", Global.RESULT_SUCCESS);
		}
		else{
			map.put("result", Global.RESULT_ERROR);			
		}
		map.put("group", group);
		map.put("action", action);
		return viewPath+"/edit";
	}
	
	@RequestMapping(value = "/moveGo.do")
	public String moveGo(Long id, ModelMap map) throws IOException{     
		logger.info( "Enter moveGo.do ..." );
		SysPrivilegeGroup privGrp = privGrpService.getPrivGroup(id);
		map.put("privGrp", privGrp);
		List<SysPrivilegeGroup> list = privGrpService.findMoveTrees(privGrp);
		String jsonStr = privGrpService.getJsonTreeStr(list);
		map.put("jsonTreeData", jsonStr);
		return viewPath+"/move";
	}                                                   

	@RequestMapping(value = "/move.do")
	public String move(Long moveId, Long targetId, int moveType, HttpServletResponse response) throws IOException{     
		logger.info( "Enter move.do ..." );
		if( privGrpService.movePrivGroup(moveId, targetId, moveType) ){
			response.getWriter().print(JsonUtil.JSON_RESULT_SUCCESS);
		}
		else{
			response.getWriter().print(JsonUtil.JSON_RESULT_FALSE);			
		}
	    return null;         
	}                                                   
	
	@RequestMapping(value = "/delete.do")
	public String delete(String id, HttpServletResponse response) throws IOException {
		logger.info( "Enter delete.do ..." );
		privGrpService.deletePrivGroup(id.split(Global.SPLIT_KEY));
		response.getWriter().print(JsonUtil.JSON_RESULT_SUCCESS);
	    return null;         
	}

}
