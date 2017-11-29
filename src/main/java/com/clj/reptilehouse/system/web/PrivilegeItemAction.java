package com.clj.reptilehouse.system.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springside.modules.orm.PropertyFilter;

import com.clj.reptilehouse.common.Global;
import com.clj.reptilehouse.common.util.JsonUtil;
import com.clj.reptilehouse.system.entity.SysPrivilegeGroup;
import com.clj.reptilehouse.system.entity.SysPrivilegeItem;
import com.clj.reptilehouse.system.service.PrivilegeGroupService;
import com.clj.reptilehouse.system.service.PrivilegeItemService;

@Controller
@RequestMapping(value = "/system/privilege/item")
public class PrivilegeItemAction {
	private final String viewPath = "/system/privilege/item";
	
	private static Logger logger = LoggerFactory.getLogger(PrivilegeItemAction.class);

	@Autowired
	private PrivilegeItemService privilegeItemService;

	@Autowired
	private PrivilegeGroupService privGrpService;

	
	@RequestMapping(value = "/main.do")
	public String main(Long grpId, ModelMap map) {
		logger.info( "Enter main.do ..." );
		SysPrivilegeGroup group = privGrpService.getPrivGroup(grpId);
		map.put("group", group);
		List<SysPrivilegeItem> list = privilegeItemService.getByGrpId(grpId);
		String jsonStr = privilegeItemService.getJsonListStr(list);
		map.put("jsonListData", jsonStr);
		return viewPath+"/main";
	}                                              

	@RequestMapping(value = "/getData.do")
	public String getData(Long grpId, HttpServletResponse response) throws IOException{     
		logger.info( "Enter getData.do ..." );
		List<SysPrivilegeItem> list = privilegeItemService.getByGrpId(grpId);
		String jsonStr = privilegeItemService.getJsonListStr(list);
	    response.getWriter().print(jsonStr);
	    return null;         
	}                                                   

	@RequestMapping(value = "/search.do")
	public String search(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.info( "Enter search.do ..." );
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request, "filter");
		List<SysPrivilegeItem> list = privilegeItemService.searchPriv(filters);
		String jsonStr = privilegeItemService.getJsonListStr(list);
		response.getWriter().print(jsonStr);
	    return null;         
	}

	@RequestMapping(value = "/add.do")
	public String add(Long grpId, ModelMap map) {
		logger.info( "Enter add.do ..." );
		map.put("action", "add");
		SysPrivilegeGroup group = privGrpService.getPrivGroup(grpId);
		SysPrivilegeItem privilege = new SysPrivilegeItem();
		map.put("group", group);
		privilege.setIsBs(true);
		privilege.setIsDv(false);
		privilege.setIsRoleDis(true);
		privilege.setStatus(1);
		map.put("item", privilege);
		return viewPath+"/edit";
	}

	@RequestMapping(value = "/edit.do")
	public String edit(Long id,Long grpId, ModelMap map) {
		logger.info( "Enter edit.do ..." );
		map.put("action", "edit");
		SysPrivilegeGroup group = privGrpService.getPrivGroup(grpId);
		SysPrivilegeItem privilege = privilegeItemService.getPriv(id);
		map.put("group", group);
		map.put("item", privilege);
		return viewPath+"/edit";
	}

	@RequestMapping(value = "/save.do")
	public String save(String action, Long id, SysPrivilegeItem item, ModelMap map) {
		logger.info( "Enter save.do ..." );

		boolean validate = true;
		if( StringUtils.isBlank(item.getName()) ){
			validate = false;
			map.put("message", "权限名称不能为空！");
		}
		else if( StringUtils.isBlank(item.getCode()) ){
			validate = false;
			map.put("message", "权限编码不能为空！");
		}
		else if( !privilegeItemService.isPrivCodeUnique(id, item.getCode()) ){
			validate = false;
			map.put("message", "权限编码已经存在！");
		}
		
		if (validate) {
			String jsonStr = "";
			long now =System.currentTimeMillis();
			if( "add".equals(action)){
				item.setCreatedTime(now);
				item.setModifiedTime(now);
				privilegeItemService.addPriv(item);
				map.put("item", item);
				jsonStr = privilegeItemService.getJsonObjStr(item);
			}		
			else{
				SysPrivilegeItem editpriv = privilegeItemService.getPriv(id);
				if( editpriv == null ){
					map.put("result", Global.RESULT_ERROR);								
					map.put("message", "该权限已经被删除！");
					return viewPath+"/edit";
				}
				BeanUtils.copyProperties(item, editpriv);
				editpriv.setModifiedTime(now);
				privilegeItemService.savePriv(editpriv);	
				map.put("item", editpriv);
				jsonStr = privilegeItemService.getJsonObjStr(editpriv);
			}
			map.put("saveData", jsonStr);
			map.put("result", Global.RESULT_SUCCESS);
		} else {
			SysPrivilegeGroup group = privGrpService.getPrivGroup(item.getGrpId());
			map.put("group", group);
			map.put("result", Global.RESULT_ERROR);
		}
		map.put("item", item);
		map.put("action", action);		
		return viewPath+"/edit";
	}
	
	@RequestMapping(value = "/moveUp.do")
	public String moveUp(Long id, HttpServletResponse response) throws IOException {
		logger.info( "Enter moveUp.do ..." );
		SysPrivilegeItem item = privilegeItemService.getPriv(id);
		privilegeItemService.moveUpItem(item);
		response.getWriter().print(JsonUtil.JSON_RESULT_SUCCESS);
	    return null;         
	}
	
	@RequestMapping(value = "/moveDown.do")
	public String moveDown(Long id, HttpServletResponse response) throws IOException {
		logger.info( "Enter moveDown.do ..." );
		SysPrivilegeItem item = privilegeItemService.getPriv(id);
		privilegeItemService.moveDownItem(item);
		response.getWriter().print(JsonUtil.JSON_RESULT_SUCCESS);
	    return null;         
	} 
	
	@RequestMapping(value = "/delete.do")
	public String delete(String id, HttpServletResponse response) throws IOException {
		logger.info( "Enter delete.do ..." );
		privilegeItemService.deletePriv(id.split(Global.SPLIT_KEY));
		response.getWriter().print(JsonUtil.JSON_RESULT_SUCCESS);
	    return null;         
	}

}
