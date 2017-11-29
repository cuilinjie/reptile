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
import com.clj.reptilehouse.system.entity.SysParamGroup;
import com.clj.reptilehouse.system.entity.SysParamItem;
import com.clj.reptilehouse.system.service.ParamGroupService;
import com.clj.reptilehouse.system.service.ParamItemService;
import com.clj.reptilehouse.system.web.ParamItemAction;

@Controller
@RequestMapping(value = "/system/param/item")
public class ParamItemAction {
	private final String viewPath = "/system/param/item";
	
	private static Logger logger = LoggerFactory.getLogger(ParamItemAction.class);

	@Autowired
	private ParamItemService paramService;

	@Autowired
	private ParamGroupService privGrpService;

	
	@RequestMapping(value = "/main.do")
	public String main(Long grpId, ModelMap map) {
		logger.info( "Enter main.do ..." );
		SysParamGroup group = privGrpService.getParamGroup(grpId);
		map.put("group", group);
		List<SysParamItem> list = paramService.getGroupParamItem(grpId);
		String jsonStr = paramService.getJsonListStr(list);
		map.put("jsonListData", jsonStr);
		return viewPath+"/main";
	}                                              

	@RequestMapping(value = "/getData.do")
	public String getData(Long grpId, HttpServletResponse response) throws IOException{     
		logger.info( "Enter getData.do ..." );
		List<SysParamItem> list = paramService.getGroupParamItem(grpId);
		String jsonStr = paramService.getJsonListStr(list);
	    response.getWriter().print(jsonStr);
	    return null;         
	}                                                   

	@RequestMapping(value = "/search.do")
	public String search(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.info( "Enter search.do ..." );
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request, "filter");
		List<SysParamItem> list = paramService.searchParamItem(filters);
		String jsonStr = paramService.getJsonListStr(list);
		response.getWriter().print(jsonStr);
	    return null;         
	}

	@RequestMapping(value = "/add.do")
	public String add(Long grpId, ModelMap map) {
		logger.info( "Enter add.do ..." );
		map.put("action", "add");
		SysParamGroup group = privGrpService.getParamGroup(grpId);
		SysParamItem param = new SysParamItem();
		map.put("group", group);
		map.put("item", param);
		return viewPath+"/edit";
	}

	@RequestMapping(value = "/edit.do")
	public String edit(Long id, ModelMap map) {
		logger.info( "Enter edit.do ..." );
		map.put("action", "edit");
		SysParamItem param = paramService.getParamItem(id);
		SysParamGroup group = privGrpService.getParamGroup(param.getGrpId());
		map.put("group", group);
		map.put("item", param);
		return viewPath+"/edit";
	}

	@RequestMapping(value = "/save.do")
	public String save(String action, SysParamItem param, ModelMap map) {
		logger.info( "Enter save.do ..." );

		boolean validate = true;
		if( StringUtils.isBlank(param.getName()) ){
			validate = false;
			map.put("message", "参数名称不能为空！");
		}
		else if( StringUtils.isBlank(param.getCode()) ){
			validate = false;
			map.put("message", "参数编码不能为空！");
		}
		else if( !paramService.isParamCodeUnique(param.getGrpId(),param.getId(), param.getCode(),action) ){
			validate = false;
			map.put("message", "参数编码已经存在！");
		}
		
		if (validate) {
			if( "add".equals(action)){
				paramService.addParamItem(param);
			}		
			else{
				paramService.saveParamItem(param);		
			}
			String jsonStr = paramService.getJsonObjStr(param);
			map.put("saveData", jsonStr);
			map.put("result", Global.RESULT_SUCCESS);
		} else {
			SysParamGroup group = privGrpService.getParamGroup(param.getGrpId());
			map.put("group", group);
			map.put("result", Global.RESULT_ERROR);
		}
		map.put("item", param);
		map.put("action", action);
		
		return viewPath+"/edit";
	}
	
	@RequestMapping(value = "/moveUp.do")
	public String moveUp(Long id, HttpServletResponse response) throws IOException {
		logger.info( "Enter moveUp.do ..." );
		SysParamItem item = paramService.getParamItem(id);
		paramService.moveUpItem(item);
		response.getWriter().print(JsonUtil.JSON_RESULT_SUCCESS);
	    return null;         
	}
	
	@RequestMapping(value = "/moveDown.do")
	public String moveDown(Long id, HttpServletResponse response) throws IOException {
		logger.info( "Enter moveDown.do ..." );
		SysParamItem item = paramService.getParamItem(id);
		paramService.moveDownItem(item);
		response.getWriter().print(JsonUtil.JSON_RESULT_SUCCESS);
	    return null;         
	} 
	
	@RequestMapping(value = "/delete.do")
	public String delete(String id, HttpServletResponse response) throws IOException {
		logger.info( "Enter delete.do ..." );
		paramService.deleteParamItem(id.split(Global.SPLIT_KEY));
		response.getWriter().print(JsonUtil.JSON_RESULT_SUCCESS);
	    return null;         
	}

}
