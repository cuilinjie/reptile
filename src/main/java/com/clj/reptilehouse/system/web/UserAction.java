package com.clj.reptilehouse.system.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springside.modules.utils.security.Cryptos;

import com.clj.reptilehouse.common.Global;
import com.clj.reptilehouse.common.util.BeanUtil;
import com.clj.reptilehouse.common.util.JsonUtil;
import com.clj.reptilehouse.common.util.SystemUtil;
import com.clj.reptilehouse.system.entity.SysMenu;
import com.clj.reptilehouse.system.entity.SysOrganization;
import com.clj.reptilehouse.system.entity.SysRole;
import com.clj.reptilehouse.system.entity.SysUser;
import com.clj.reptilehouse.system.service.MenuService;
import com.clj.reptilehouse.system.service.PrivilegeGroupService;
import com.clj.reptilehouse.system.service.PrivilegeItemService;
import com.clj.reptilehouse.system.service.RoleService;
import com.clj.reptilehouse.system.service.UserService;
import com.clj.reptilehouse.system.web.UserAction;

@Controller
@RequestMapping(value = "/system/user")
public class UserAction {
	private final String viewPath = "/system/user";
	
	private static Logger logger = LoggerFactory.getLogger(UserAction.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private PrivilegeItemService privilegeItemService;
	
	@Autowired
	private PrivilegeGroupService privGrpService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private MenuService menuService;
	
	
	@RequestMapping(value = "/main.do")
	public String main(ModelMap map) {
		logger.info( "Enter main.do ..." );
		List<SysUser> list = getUser();
		String jsonStr = userService.getJsonListStr(list);
		map.put("jsonListData", jsonStr);
		return viewPath+"/main";
	}                                              

	@RequestMapping(value = "/getData.do")
	public String getData(HttpServletResponse response) throws IOException{     
		logger.info( "Enter getData.do ..." );
		List<SysUser> list = getUser();
		String jsonStr = userService.getJsonListStr(list);
	    response.getWriter().print(jsonStr);
	    return null;         
	}        
	
	private List<SysUser> getUser() {
		SysUser curUser = SystemUtil.getLoginUser();
		if( curUser == null ){
			return null;
		}
		List<SysUser> list = null;
		if( curUser.getType() == Global.USER_TYPE_DEV ){
			list = userService.getAllUser();			
		}
		else if(curUser.getLoginName().equals(Global.USER_ADMIN)){
			list = userService.getAllUserButDev();			
		}else{
			SysOrganization mainOrg = SystemUtil.getLoginUserMainOrg();
			if( mainOrg != null ){				
				list = userService.findAllManageUser(mainOrg.getTreeCode(),curUser.getType());		
			}
		}
		return list;
	}

	@RequestMapping(value = "/add.do")
	public String add(ModelMap map) {
		logger.info( "Enter add.do ..." );
		map.put("action", "add");
		SysUser user = new SysUser();
		map.put("user", user);
		return viewPath+"/edit";
	}

	@RequestMapping(value = "/edit.do")
	public String edit(Long id, ModelMap map) {
		logger.info( "Enter edit.do ..." );
		map.put("action", "edit");
		SysUser user = userService.getUser(id);
		map.put("user", user);
		return viewPath+"/edit";
	}

	@RequestMapping(value = "/save.do")
	public String save(String action, Long id, boolean isEditPasswd, SysUser user, ModelMap map) {
		logger.info( "Enter save.do ..." );
		
		boolean validate = true;
		SysUser curUser = SystemUtil.getLoginUser();
		if( curUser == null ){
			validate = false;
			map.put("message", "系统登录超时！");			
		}
		else if( StringUtils.isBlank(user.getLoginName()) ){
			validate = false;
			map.put("message", "用户账号不能为空！");
		}
		else if(!userService.isLoginNameUnique(user.getLoginName(),user.getId()==null?0:user.getId()) ){
			validate = false;
			map.put("message", "用户账号已经存在！");
		}
		else if( ("add".equals(action) && StringUtils.isBlank(user.getLoginPwd())) 
			|| ("edit".equals(action) && isEditPasswd &&  StringUtils.isBlank(user.getLoginPwd())) ){
			validate = false;
			map.put("message", "用户密码不能为空！");
		}
		if( validate ){
			String operator = curUser.getLoginName();
			long now =System.currentTimeMillis();//获取系统当前时间
			user.setModifier(operator);
			user.setModifiedTime(now);
			String jsonStr = "";
			if( "add".equals(action)){
				user.setLoginPwd(Cryptos.desEncryptToHex(user.getLoginPwd(),Global.AES_KEY));
				user.setCreator(operator);
				user.setCreatedTime(now);
				user.setLastLoginTime(now);
				userService.addUser(user);		
				map.put("user", user);
				jsonStr = userService.getJsonObjStr(user);
			}
			else{
				SysUser editUser = userService.getUser(id);
				if( editUser == null ){
					map.put("user", user);
					map.put("result", Global.RESULT_ERROR);								
					map.put("message", "该用户已经被删除！");
					return viewPath+"/edit";
				}
				if( isEditPasswd ){
					user.setLoginPwd(Cryptos.desEncryptToHex(user.getLoginPwd(),Global.AES_KEY));
				}else{
					user.setLoginPwd(editUser.getLoginPwd());
				}
				BeanUtil.copyProperties(user, editUser);
				userService.saveUser(editUser);			
				map.put("user", editUser);
				jsonStr = userService.getJsonObjStr(editUser);
			}
			map.put("saveData", jsonStr);
			map.put("result", Global.RESULT_SUCCESS);
		}
		else{
			map.put("user", user);
			map.put("result", Global.RESULT_ERROR);			
		}

		map.put("action", action);
		return viewPath+"/edit";
	}
	
	@RequestMapping(value = "/delete.do")
	public String delete(String id, HttpServletResponse response) throws IOException {
		logger.info( "Enter delete.do ..." );
		SysUser curUser = SystemUtil.getLoginUser();
		if( id.equals(curUser.getId()) ){
			response.getWriter().printf(JsonUtil.JSON_RESULT_FALSE_MSGMDL, "不能删除当前登录用户！");
			return null;
		}
		userService.deleteUser(id.split(Global.SPLIT_KEY));
		response.getWriter().print(JsonUtil.JSON_RESULT_SUCCESS);
	    return null;         
	}
	
	@RequestMapping(value = "/mapRoleGo.do")
	public String mapRoleGo(Long id, ModelMap map) throws IOException{     
		logger.info( "Enter mapRoleGo.do ..." );
		SysUser user = userService.getUser(id);
		map.put("user", user);
		List<SysRole> list = roleService.getAllUserRole(user.getType());
		String jsonStr = roleService.getJsonListStr(list);
		map.put("jsonListData", jsonStr);
		ArrayList<Long> mapId = new ArrayList<Long>();
		mapId.add(user.getRoleId());
		String jsonMap = JsonUtil.object2json(mapId);
		map.put("jsonMapData", jsonMap);
		return viewPath+"/mapRole";
	}                  

	@RequestMapping(value = "/mapRole.do")
	public String mapRole(Long userId, Long mapId, HttpServletResponse response) throws IOException{     
		logger.info( "Enter mapRole.do ..." );
		SysUser editUser = userService.getUser(userId);
		editUser.setRoleId(mapId);
		userService.saveUser(editUser);
		response.getWriter().print(JsonUtil.JSON_RESULT_SUCCESS);
	    return null;         
	}   
	
	@RequestMapping(value = "/viewMenu.do")
	public String viewMenu(Long id, ModelMap map) throws IOException{     
		logger.info( "Enter viewMenu.do ..." );
		SysUser user = userService.getUser(id);
		map.put("user", user);

		List<SysMenu> list = menuService.findUserAllMenu(user);
		String jsonStr = menuService.getJsonTreeStr(list);
		map.put("jsonTreeData", jsonStr);
		return viewPath+"/viewMenu";
	}   
	
	@RequestMapping(value = "/setupGo.do")
	public String setupGo(ModelMap map) {
		SysUser curUser = SystemUtil.getLoginUser();
		if( curUser == null ){
			return Global.RESULT_ERROR_OUTTIME;
		}
		map.put("curUser", curUser);
		return viewPath+"/setup";
	}
	
	@RequestMapping(value = "/setup.do")
	public String setup(Long userId, boolean isEditPasswd, String loginPasswd,SysUser user, ModelMap map) {
		logger.info( "Enter setup.do ..." );
		SysUser curUser = SystemUtil.getLoginUser();
		if( curUser == null || !userId.equals(curUser.getId()) ){
			return Global.RESULT_ERROR_OUTTIME;
		}
		boolean validate = true;
		if( isEditPasswd &&  StringUtils.isBlank(loginPasswd) ){
				validate = false;
				map.put("message", "用户密码不能为空！");
		}
		if( validate ){
			if( isEditPasswd ){
				curUser.setLoginPwd(Cryptos.desEncryptToHex(loginPasswd, Global.AES_KEY));
			}
			if( user.getMobile() != null ){
				curUser.setMobile(user.getMobile());
			}
			if( user.getEmail() != null ){
				curUser.setEmail(user.getEmail());
			}
			String oldMenuType = curUser.getMenuStyle();
			if( user.getMenuStyle() != null ){
				curUser.setMenuStyle(user.getMenuStyle());
			}
			if( user.getSkinName() != null ){
				curUser.setSkinName(user.getSkinName());
			}
			userService.saveUser(curUser);
			map.put("result", Global.RESULT_SUCCESS);
			
			if(  user.getMenuStyle() != null && !oldMenuType.equals( user.getMenuStyle()) ){
				map.put("MenuStyleChange", "true");				
			}
		}
		else{
			map.put("result", Global.RESULT_ERROR);			
		}
		
		map.put("curUser", curUser);
		return viewPath+"/setup";
	}
}
