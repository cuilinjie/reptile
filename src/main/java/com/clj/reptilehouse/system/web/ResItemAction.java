package com.clj.reptilehouse.system.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.clj.reptilehouse.common.Global;
import com.clj.reptilehouse.common.util.JsonUtil;
import com.clj.reptilehouse.system.entity.SysResGroup;
import com.clj.reptilehouse.system.entity.SysResItem;
import com.clj.reptilehouse.system.service.ResGroupService;
import com.clj.reptilehouse.system.service.ResItemService;
import com.clj.reptilehouse.system.web.ResItemAction;

@Controller
@RequestMapping(value = "/system/res/item")
public class ResItemAction {
	private final String viewPath = "/system/res/item";

	private static Logger logger = LoggerFactory.getLogger(ResItemAction.class);

	@Autowired
	private ResItemService resItemService;
	@Autowired
	private ResGroupService resGroupService;
	
	
	private String getJsonGroupItems(final SysResGroup group, int type){
		String jsonStr="";
		List<SysResItem> listItem = resItemService.getGroupResItem(group.getId());
		if( group.getType() == Global.GROUP_TYPE_TREE ){
			jsonStr = resItemService.getJsonTreeStr(listItem, type);
		}else{
			jsonStr = resItemService.getJsonListStr(listItem, type);
		}
		return jsonStr;
	}
	
	@RequestMapping(value = "/main.do")
	public String main(Long grpId, ModelMap map) {
		logger.info( "Enter main.do ..." );
		SysResGroup group = resGroupService.getResGroup(grpId);
		map.put("group", group);
		String jsonStr = getJsonGroupItems(group, 0);
		map.put("jsonData", jsonStr);
		return viewPath+"/main";
	}

	@RequestMapping(value = "/getData.do")
	public String getData(Long grpId, String grpCode, HttpServletResponse response) throws IOException{     
		logger.info( "Enter getData.do ..." );
		SysResGroup group = null;
		if( grpId!=null ){
			group = resGroupService.getResGroup(grpId);
		}
		else if( !StringUtils.isBlank(grpCode) ){
			group = resGroupService.getResGroupByCode(grpCode);
		}
		String jsonStr = getJsonGroupItems(group, 0);
	    response.getWriter().print(jsonStr);
	    return null;         
	}
	
	@RequestMapping(value = "/getMinData.do")
	public String getMinData(Long grpId, String grpCode, HttpServletResponse response) throws IOException{     
		logger.info( "Enter getMinData.do ..." );
		SysResGroup group = null;
		if( grpId!=null ){
			group = resGroupService.getResGroup(grpId);
		}
		else if( !StringUtils.isBlank(grpCode) ){
			group = resGroupService.getResGroupByCode(grpCode);
		}
		String jsonStr = getJsonGroupItems(group, 1);
	    response.getWriter().print(jsonStr);
	    return null;         
	}                                                     

	@RequestMapping(value = "/add.do")
	public String add(Long grpId, Long parentId, ModelMap map) {
		logger.info( "Enter add.do ..." );
		map.put("action", "add");
		SysResGroup group = resGroupService.getResGroup(grpId);
		SysResItem item = new SysResItem();
		item.setParentId(parentId);
		map.put("item", item);
		map.put("group",group);
		return viewPath+"/edit";
	}

	@RequestMapping(value = "/edit.do")
	public String edit(Long id, ModelMap map) {
		logger.info( "Enter edit.do ..." );
		map.put("action", "edit");
		SysResItem item = resItemService.getResItem(id);
		SysResGroup group = resGroupService.getResGroup(item.getGrpId());
		map.put("item", item);
		map.put("group",group);
		return viewPath+"/edit";
	}

	@RequestMapping(value = "/save.do")
	public String save(String action, SysResItem item, ModelMap map) {
		logger.info( "Enter save.do ..." );
		
		boolean validate = true;
		if( StringUtils.isBlank(item.getName()) ){
			validate = false;
			map.put("message", "资源项名称不能为空！");
		}
		else if( StringUtils.isBlank(item.getCode()) ){
			validate = false;
			map.put("message", "资源项编码不能为空！");
		}
		else if(!resItemService.isItemCodeUnique(item.getGrpId(),item.getId(),item.getCode(),action) ){
			validate = false;
			map.put("message", "资源项编码已经存在！");
		}	
		if (validate) {
			long now =System.currentTimeMillis();
			if( "add".equals(action)){
				item.setCreatedTime(now);
				item.setModifiedTime(now);
				resItemService.addResItem(item);
			}		
			else{
				item.setModifiedTime(now);
				resItemService.saveResItem(item);				
			}
			String jsonStr = resItemService.getJsonObjStr(item);
			map.put("saveData", jsonStr);
			map.put("result", Global.RESULT_SUCCESS);
		} else {
			SysResGroup group = resGroupService.getResGroup(item.getGrpId());
			map.put("group",group);
			map.put("result", Global.RESULT_ERROR);
		}
		map.put("item", item);
		map.put("action", action);
		return viewPath+"/edit";
	}                                                
	
	@RequestMapping(value = "/moveUp.do")
	public String moveUp(Long id, HttpServletResponse response) throws IOException {
		logger.info( "Enter moveUp.do ..." );
		SysResItem item = resItemService.getResItem(id);
		resItemService.moveUpItem(item);
		response.getWriter().print(JsonUtil.JSON_RESULT_SUCCESS);
	    return null;         
	}
	
	@RequestMapping(value = "/moveDown.do")
	public String moveDown(Long id, HttpServletResponse response) throws IOException {
		logger.info( "Enter moveDown.do ..." );
		SysResItem item = resItemService.getResItem(id);
		resItemService.moveDownItem(item);
		response.getWriter().print(JsonUtil.JSON_RESULT_SUCCESS);
	    return null;         
	}
	
	@RequestMapping(value = "/moveGo.do")
	public String moveGo(Long id, ModelMap map) throws IOException{     
		logger.info( "Enter moveGo.do ..." );
		SysResItem item = resItemService.getResItem(id);
		map.put("item", item);
		List<SysResItem> list = resItemService.findMoveTrees(item);
		String jsonStr = resItemService.getJsonTreeStr(list);
		map.put("jsonTreeData", jsonStr);
		return viewPath+"/move";
	}                                                   

	@RequestMapping(value = "/move.do")
	public String move(Long moveId, Long targetId, int moveType, HttpServletResponse response) throws IOException{     
		logger.info( "Enter move.do ..." );
		if( resItemService.moveNode(moveId, targetId, moveType,3) ){
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
		resItemService.deleteResItem(id.split(Global.SPLIT_KEY));
		response.getWriter().print(JsonUtil.JSON_RESULT_SUCCESS);
	    return null;         
	}


}
