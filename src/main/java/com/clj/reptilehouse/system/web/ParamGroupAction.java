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
import com.clj.reptilehouse.common.util.SystemUtil;
import com.clj.reptilehouse.system.entity.SysParamGroup;
import com.clj.reptilehouse.system.entity.SysUser;
import com.clj.reptilehouse.system.service.ParamGroupService;
import com.clj.reptilehouse.system.web.ParamGroupAction;

@Controller
@RequestMapping(value = "/system/param/group")
public class ParamGroupAction {
	private final String viewPath = "/system/param/group";
	
	private static Logger logger = LoggerFactory.getLogger(ParamGroupAction.class);

	@Autowired
	private ParamGroupService resGroupService;
	
	
	@RequestMapping(value = "/main.do")
	public String main(ModelMap map) {
		logger.info( "Enter main.do ..." );
		List<SysParamGroup> list = getParamGroup();
		String jsonStr = resGroupService.getJsonListStr(list);
		map.put("jsonListData", jsonStr);
		return viewPath+"/main";
	}                               

	@RequestMapping(value = "/getData.do")
	public String getData(HttpServletResponse response) throws IOException{     
		logger.info( "Enter getData.do ..." );
		List<SysParamGroup> list = getParamGroup();
		String jsonStr = resGroupService.getJsonListStr(list);
	    response.getWriter().print(jsonStr);
	    return null;         
	}        
	
	private List<SysParamGroup> getParamGroup() {
		SysUser curUser = SystemUtil.getLoginUser();
		if( curUser == null ){
			return null;
		}
		List<SysParamGroup> list = null;
	   if( curUser.getType() == Global.USER_TYPE_DEV ){
			list = resGroupService.getAllParamGroup();
		}
		else{
			list = resGroupService.getBusParamGroup();
		}
		return list;
	}

	@RequestMapping(value = "/search.do")
	public String search(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.info( "Enter search.do ..." );
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request, "filter");
		List<SysParamGroup> list = resGroupService.searchParamGroup(filters);
		String jsonStr = resGroupService.getJsonListStr(list);
		response.getWriter().print(jsonStr);
	    return null;         
	}

	@RequestMapping(value = "/add.do")
	public String add(ModelMap map) {
		logger.info( "Enter add.do ..." );
		map.put("action", "add");
		SysParamGroup resGroup = new SysParamGroup();
		map.put("group", resGroup);
		return viewPath+"/edit";
	}

	@RequestMapping(value = "/edit.do")
	public String edit(Long id, ModelMap map) {
		logger.info( "Enter edit.do ..." );
		map.put("action", "edit");
		SysParamGroup resGroup = resGroupService.getParamGroup(id);
		map.put("group", resGroup);
		return viewPath+"/edit";
	}

	@RequestMapping(value = "/itemValue.do")
	public String itemValue(Long id, String grpCode, ModelMap map) {
		logger.info( "Enter itemValue.do ..." );
		SysParamGroup resGroup = null;
		if( id != null ){
			resGroup = resGroupService.getParamGroup(id);
		}
		else if( grpCode != null ){
			resGroup = resGroupService.getParamGroupByCode(grpCode);
		}
		map.put("group", resGroup);
		return viewPath+"/itemValue";
	}

	@RequestMapping(value = "/saveValue.do")
	public String saveValue(Long id, HttpServletRequest request, ModelMap map) {
		logger.info( "Enter saveValue.do ..." );
		SysParamGroup resGroup = resGroupService.getParamGroup(id);
		resGroupService.saveParamGroup(resGroup);
		map.put("group", resGroup);
		map.put("result", Global.RESULT_SUCCESS);
		return viewPath+"/itemValue";
	}

	@RequestMapping(value = "/save.do")
	public String save(String action, SysParamGroup resGroup, ModelMap map) {
		logger.info( "Enter save.do ..." );
		
		boolean validate = true;
		if( StringUtils.isBlank(resGroup.getName()) ){
			validate = false;
			map.put("message", "参数组名称不能为空！");
		}
		else if( StringUtils.isBlank(resGroup.getCode()) ){
			validate = false;
			map.put("message", "参数组编码不能为空！");
		}
		else if(!resGroupService.isGrpCodeUnique(resGroup.getId(), resGroup.getCode(),action) ){
			validate = false;
			map.put("message", "参数组编码已经存在！");
		}
		
		if( validate ){
			long now =System.currentTimeMillis();
			if( "add".equals(action)){
				resGroup.setCreatedTime(now);
				resGroup.setModifiedTime(now);
				resGroupService.addParamGroup(resGroup);	
			}
			else{
				resGroup.setModifiedTime(now);
				resGroupService.saveParamGroup(resGroup);					
			}
			String jsonStr = resGroupService.getJsonObjStr(resGroup);
			map.put("saveData", jsonStr);
			map.put("result", Global.RESULT_SUCCESS);
		}
		else{
			map.put("result", Global.RESULT_ERROR);			
		}

		map.put("group", resGroup);
		map.put("action", action);
		return viewPath+"/edit";
	}
	
	@RequestMapping(value = "/moveUp.do")
	public String moveUp(Long id, HttpServletResponse response) throws IOException {
		logger.info( "Enter moveUp.do ..." );
		SysParamGroup item = resGroupService.getParamGroup(id);
		resGroupService.moveUpItem(item);
		response.getWriter().print(JsonUtil.JSON_RESULT_SUCCESS);
	    return null;         
	}
	
	@RequestMapping(value = "/moveDown.do")
	public String moveDown(Long id, HttpServletResponse response) throws IOException {
		logger.info( "Enter moveDown.do ..." );
		SysParamGroup item = resGroupService.getParamGroup(id);
		resGroupService.moveDownItem(item);
		response.getWriter().print(JsonUtil.JSON_RESULT_SUCCESS);
	    return null;         
	} 

	@RequestMapping(value = "/delete.do")
	public String delete(String id, HttpServletResponse response) throws IOException {
		logger.info( "Enter delete.do ..." );
		resGroupService.deleteParamGroup(id.split(Global.SPLIT_KEY));
		response.getWriter().print(JsonUtil.JSON_RESULT_SUCCESS);
	    return null;         
	}

}
