package com.clj.reptilehouse.system.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springside.modules.orm.PropertyFilter;

import com.clj.reptilehouse.common.Global;
import com.clj.reptilehouse.common.util.JsonUtil;
import com.clj.reptilehouse.common.util.SystemUtil;
import com.clj.reptilehouse.system.entity.SysResGroup;
import com.clj.reptilehouse.system.entity.SysUser;
import com.clj.reptilehouse.system.service.ResGroupService;
import com.clj.reptilehouse.system.web.ResGroupAction;

@Controller
@RequestMapping(value = "/system/res/group")
public class ResGroupAction {
	private final String viewPath = "/system/res/group";
	
	private static Logger logger = LoggerFactory.getLogger(ResGroupAction.class);

	@Autowired
	private ResGroupService resGroupService;
	
	@RequestMapping(value = "/main.do")
	public String main(ModelMap map) {
		logger.info( "Enter main.do ..." );
		List<SysResGroup> list = getResGroup();
		String jsonStr = resGroupService.getJsonListStr(list);
		map.put("jsonListData", jsonStr);
		return viewPath+"/main";
	}                                              

	@RequestMapping(value = "/getData.do")
	public String getData(HttpServletResponse response) throws IOException{     
		logger.info( "Enter getData.do ..." );
		List<SysResGroup> list = getResGroup();
		String jsonStr = resGroupService.getJsonListStr(list);
	    response.getWriter().print(jsonStr);
	    return null;         
	}                                                   
	
	private List<SysResGroup> getResGroup() {
		SysUser curUser = SystemUtil.getLoginUser();
		if( curUser == null ){
			return null;
		}
		List<SysResGroup> list = null;
		if( curUser.getType() == Global.USER_TYPE_DEV ){
			list = resGroupService.getAllResGroup();
		}
		else{
			list = resGroupService.getBufResGroup();
		}
		return list;
	}

	@RequestMapping(value = "/search.do")
	public String search(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.info( "Enter search.do ..." );
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request, "filter");
		List<SysResGroup> list = resGroupService.searchResGroup(filters);
		String jsonStr = resGroupService.getJsonListStr(list);
		response.getWriter().print(jsonStr);
	    return null;         
	}

	@RequestMapping(value = "/add.do")
	public String add(ModelMap map) {
		logger.info( "Enter add.do ..." );
		map.put("action", "add");
		SysResGroup resGroup = new SysResGroup();
		map.put("group", resGroup);
		return viewPath+"/edit";
	}

	@RequestMapping(value = "/edit.do")
	public String edit(Long id, ModelMap map) {
		logger.info( "Enter edit.do ..." );
		map.put("action", "edit");
		SysResGroup resGroup = resGroupService.getResGroup(id);
		map.put("group", resGroup);
		return viewPath+"/edit";
	}

	@RequestMapping(value = "/save.do")
	public String save(String action, SysResGroup group, ModelMap map) {
		logger.info( "Enter save.do ..." );
		boolean validate = true;
		if( StringUtils.isBlank(group.getName()) ){
			validate = false;
			map.put("message", "资源组名称不能为空！");
		}
		else if( StringUtils.isBlank(group.getCode()) ){
			validate = false;
			map.put("message", "资源组编码不能为空！");
		}
		else if(!resGroupService.isGrpCodeUnique(group.getId(),group.getCode(),action)){
			validate = false;
			map.put("message", "资源组编码已经存在！");
		}
		
		if( validate ){
			long now =System.currentTimeMillis();
			if( "add".equals(action)){
				group.setCreatedTime(now);
				group.setModifiedTime(now);
				resGroupService.addResGroup(group);
			}else{
				group.setModifiedTime(now);
				resGroupService.saveResGroup(group);							
			}
			String jsonStr = resGroupService.getJsonObjStr(group);
			map.put("saveData", jsonStr);
			map.put("result", Global.RESULT_SUCCESS);
		}else{
			map.put("result", Global.RESULT_ERROR);			
		}

		map.put("action", action);
		map.put("group", group);
		return viewPath+"/edit";
	}
	
	@RequestMapping(value = "/moveUp.do")
	public String moveUp(Long id, HttpServletResponse response) throws IOException {
		logger.info( "Enter moveUp.do ..." );
		SysResGroup item = resGroupService.getResGroup(id);
		resGroupService.moveUpItem(item);
		response.getWriter().print(JsonUtil.JSON_RESULT_SUCCESS);
	    return null;         
	}
	
	@RequestMapping(value = "/moveDown.do")
	public String moveDown(Long id, HttpServletResponse response) throws IOException {
		logger.info( "Enter moveDown.do ..." );
		SysResGroup item = resGroupService.getResGroup(id);
		resGroupService.moveDownItem(item);
		response.getWriter().print(JsonUtil.JSON_RESULT_SUCCESS);
	    return null;         
	} 

	@RequestMapping(value = "/delete.do")
	public String delete(String id, HttpServletResponse response) throws IOException {
		logger.info( "Enter delete.do ..." );
		resGroupService.deleteResGroup(id.split(Global.SPLIT_KEY));
		response.getWriter().print(JsonUtil.JSON_RESULT_SUCCESS);
	    return null;         
	}

}
