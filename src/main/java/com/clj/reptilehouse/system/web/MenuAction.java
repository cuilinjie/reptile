package com.clj.reptilehouse.system.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.clj.reptilehouse.common.Global;
import com.clj.reptilehouse.common.util.JsonUtil;
import com.clj.reptilehouse.common.util.SystemUtil;
import com.clj.reptilehouse.system.entity.SysMenu;
import com.clj.reptilehouse.system.entity.SysMenuPrivMap;
import com.clj.reptilehouse.system.entity.SysPrivilegeGroup;
import com.clj.reptilehouse.system.entity.SysPrivilegeItem;
import com.clj.reptilehouse.system.entity.SysUser;
import com.clj.reptilehouse.system.service.MenuService;
import com.clj.reptilehouse.system.service.PrivilegeGroupService;
import com.clj.reptilehouse.system.service.PrivilegeItemService;
import com.clj.reptilehouse.system.web.MenuAction;

@Controller
@RequestMapping(value = "/system/menu")
public class MenuAction {
	private final String viewPath = "/system/menu";

	private static Logger logger = LoggerFactory.getLogger(MenuAction.class);

	@Autowired
	private MenuService menuService;
	
	@Autowired
	private PrivilegeItemService privilegeItemService;
	
	@Autowired
	private PrivilegeGroupService privGrpService;
	
	
	@RequestMapping(value = "/main.do")
	public String main(ModelMap map) {
		logger.info( "Enter main.do ..." );
		List<SysMenu> list = null;
		SysUser curUser = SystemUtil.getLoginUser();
		if(Global.USER_DEVADMIN.equals(curUser.getLoginName())){
			list = menuService.getAllMenu();
		}else{
			list = menuService.findUserAllMenu(curUser.getId());
		}
		String jsonStr = menuService.getJsonTreeStr(list);
		map.put("jsonTreeData", jsonStr);
		return viewPath+"/main";
	}

	@RequestMapping(value = "/getData.do")
	public String getData(HttpServletResponse response) throws IOException{     
		logger.info( "Enter getData.do ..." );
		//List<SysMenu> list = menuService.getAllMenu();
		List<SysMenu> list = null;
		SysUser curUser = SystemUtil.getLoginUser();
		if(Global.USER_DEVADMIN.equals(curUser.getLoginName())){
			list = menuService.getAllMenu();
		}else{
			list = menuService.findUserAllMenu(curUser.getId());
		}
		String jsonStr = menuService.getJsonTreeStr(list);
	    response.getWriter().print(jsonStr);
	    return null;         
	}                                                   

	@RequestMapping(value = "/add.do")
	public String add(Long parentId, ModelMap map) {
		logger.info( "Enter add.do ..." );
		map.put("action", "add");
		SysMenu menu = new SysMenu();
		if(parentId==null) parentId=-1L;
		menu.setParentId(parentId);
		map.put("menu", menu);
		return viewPath+"/edit";
	}

	@RequestMapping(value = "/edit.do")
	public String edit(Long id, ModelMap map) {
		logger.info( "Enter edit.do ..." );
		map.put("action", "edit");
		SysMenu menu = menuService.getMenu(id);
		map.put("menu", menu);
		return viewPath+"/edit";
	}

	@RequestMapping(value = "/save.do")
	public String save(String action, Long id, SysMenu menu, ModelMap map) {
		logger.info( "Enter save.do ..." );
		boolean validate = true;
		if( StringUtils.isBlank(menu.getName()) ){
			validate = false;
			map.put("message", "菜单名称不能为空！");
		}
		if( validate ){
			String jsonStr = "";
			if( "add".equals(action)){
				menuService.addMenu(menu);
				map.put("menu", menu);
				jsonStr = menuService.getJsonObjStr(menu);
			}else{
				SysMenu editMenu = menuService.getMenu(id);
				if( editMenu == null ){
					map.put("result", Global.RESULT_ERROR);			
					map.put("message", "该菜单已经被删除！");
					return viewPath+"/edit";
				}
				BeanUtils.copyProperties(menu, editMenu);
				menuService.saveMenu(editMenu);		
				map.put("menu", editMenu);
				jsonStr = menuService.getJsonObjStr(editMenu);
			}
			map.put("saveData", jsonStr);
			map.put("result", Global.RESULT_SUCCESS);
		}
		else{
			map.put("menu", menu);
			map.put("result", Global.RESULT_ERROR);			
		}
		map.put("action", action);
		return viewPath+"/edit";
	}
	
	@RequestMapping(value = "/moveGo.do")
	public String moveGo(Long id, ModelMap map) throws IOException{     
		logger.info( "Enter moveGo.do ..." );
		SysMenu menu = menuService.getMenu(id);
		map.put("menu", menu);
		List<SysMenu> list = menuService.findMoveTrees(menu);
		String jsonStr = menuService.getJsonTreeStr(list);
		map.put("jsonTreeData", jsonStr);
		return viewPath+"/move";
	}                                                   

	@RequestMapping(value = "/move.do")
	public String move(String moveId, String targetId, int moveType, HttpServletResponse response) throws IOException{     
		logger.info( "Enter move.do ..." );
		if( menuService.moveMenu(moveId, targetId, moveType) ){
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
		menuService.deleteMenu(id.split(Global.SPLIT_KEY));
		response.getWriter().print(JsonUtil.JSON_RESULT_SUCCESS);
	    return null;         
	}
	
	@RequestMapping(value = "/mapPrivGo.do")
	public String mapPrivGo(Long id, ModelMap map) throws IOException{     
		logger.info( "Enter moveGo.do ..." );
		SysMenu menu =menuService.getMenu(id);
		map.put("menu", menu);
		SysUser curUser=SystemUtil.getLoginUser();
		List<SysPrivilegeGroup> list =null;
		if(Global.USER_DEVADMIN.equals(curUser.getLoginName())){
			list = privGrpService.getGrpsBySuperuser();
		}else{
			list = privGrpService.getGrpsByUid(curUser.getId());
		}
		List<SysPrivilegeItem> privlist=privilegeItemService.getByMenuPrivMap(id);
		String jsonStr = privGrpService.getJsonMenuPrivChk(list,privlist);
		map.put("jsonTreeData", jsonStr);
		return viewPath+"/mapPriv";
	}                                                   

	@RequestMapping(value = "/mapPriv.do")
	public String mapPriv(Long menuId, String[] chkPriv, HttpServletResponse response) throws IOException{     
		logger.info( "Enter mappriv.do ..." );
		if( chkPriv != null&&chkPriv.length>0 ){
			List<SysMenuPrivMap> maps=new ArrayList<SysMenuPrivMap>();
			for( int i=0; i<chkPriv.length; i++ ){
				SysMenuPrivMap mp=new SysMenuPrivMap();
				mp.setMenuId(menuId);
				mp.setPrivId(Long.valueOf(chkPriv[i]));
				maps.add(mp);
			}
			privilegeItemService.addMenuPrivMap(maps,menuId);
		}
		response.getWriter().print(JsonUtil.JSON_RESULT_SUCCESS);
	    return null;         
	}                                                   

}
