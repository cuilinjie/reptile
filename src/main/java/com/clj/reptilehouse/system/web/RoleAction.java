package com.clj.reptilehouse.system.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.clj.reptilehouse.common.Global;
import com.clj.reptilehouse.common.util.JsonUtil;
import com.clj.reptilehouse.common.util.SystemUtil;
import com.clj.reptilehouse.system.entity.SysPrivilegeGroup;
import com.clj.reptilehouse.system.entity.SysPrivilegeItem;
import com.clj.reptilehouse.system.entity.SysRole;
import com.clj.reptilehouse.system.entity.SysRolePrivMap;
import com.clj.reptilehouse.system.entity.SysUser;
import com.clj.reptilehouse.system.service.PrivilegeGroupService;
import com.clj.reptilehouse.system.service.PrivilegeItemService;
import com.clj.reptilehouse.system.service.RoleService;
import com.clj.reptilehouse.system.web.RoleAction;

@Controller
@RequestMapping(value = "/system/role")
public class RoleAction {
	private final String viewPath = "/system/role";
	
	private static Logger logger = LoggerFactory.getLogger(RoleAction.class);

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private PrivilegeItemService privilegeItemService;
	
	@Autowired
	private PrivilegeGroupService privGrpService;
	
	
	@RequestMapping(value = "/main.do")
	public String main(ModelMap map) {
		logger.info( "Enter main.do ..." );
		List<SysRole> list = roleService.getAllRole();
		String jsonStr = roleService.getJsonListStr(list);
		map.put("jsonListData", jsonStr);
		return viewPath+"/main";
	}                                              

	@RequestMapping(value = "/getData.do")
	public String getData(HttpServletResponse response) throws IOException{     
		logger.info( "Enter getData.do ..." );
		List<SysRole> list = roleService.getAllRole();
		String jsonStr = roleService.getJsonListStr(list);
	    response.getWriter().print(jsonStr);
	    return null;         
	}    
	
	@ResponseBody
	@RequestMapping(value = "/getMinData.do")
	public List<SysRole> getMinData(HttpServletResponse response){     
		logger.info( "Enter getMinData.do ..." );
		SysUser curUser=SystemUtil.getLoginUser();
		List<SysRole> list = roleService.getRolesByOrgId(curUser.getOrgId());
	    return list;         
	}    

	@RequestMapping(value = "/add.do")
	public String add(Long orgId,ModelMap map) {
		logger.info( "Enter add.do ..." );
		map.put("action", "add");
		SysRole role = new SysRole();
		map.put("role", role);
		return viewPath+"/edit";
	}

	@RequestMapping(value = "/edit.do")
	public String edit(Long id, ModelMap map) {
		logger.info( "Enter edit.do ..." );
		map.put("action", "edit");
		SysRole role = roleService.getRole(id);
		map.put("role", role);
		return viewPath+"/edit";
	}

	@RequestMapping(value = "/save.do")
	public String save(String action, Long id, SysRole role, ModelMap map) {
		logger.info( "Enter save.do ..." );
		boolean validate = true;
		if( StringUtils.isBlank(role.getName()) ){
			validate = false;
			map.put("message", "角色名称不能为空！");
		}
		if(!roleService.isRoleNameUnique(id, role.getName()) ){
			validate = false;
			map.put("message", "角色名称已经存在！");
		}
		if( validate ){
			String jsonStr = "";
			if( "add".equals(action)){
				roleService.addRole(role);		
				map.put("role", role);
				jsonStr = roleService.getJsonObjStr(role);
			}else{
				SysRole editRole = roleService.getRole(id);
				BeanUtils.copyProperties(role, editRole,new String[]{"orgId"});
				roleService.saveRole(editRole);			
				map.put("role", editRole);
				jsonStr = roleService.getJsonObjStr(editRole);
			}
			map.put("saveData", jsonStr);
			map.put("result", Global.RESULT_SUCCESS);
		}
		else{
			map.put("role", role);
			map.put("result", Global.RESULT_ERROR);			
		}

		map.put("action", action);
		return viewPath+"/edit";
	}
	
	@RequestMapping(value = "/delete.do")
	public String delete(String id, HttpServletResponse response) throws IOException {
		logger.info( "Enter delete.do ..." );
		roleService.deleteRole(id.split(Global.SPLIT_KEY));
		response.getWriter().print(JsonUtil.JSON_RESULT_SUCCESS);
	    return null;         
	}
	
	@RequestMapping(value = "/mapPrivGo.do")
	public String mapPrivGo(Long id, ModelMap map) throws IOException{
		logger.info( "Enter moveGo.do ..." );
		SysRole role = roleService.getRole(id);
		map.put("role", role);
		SysUser curUser=SystemUtil.getLoginUser();
		List<SysPrivilegeGroup> list =null;
		if(Global.USER_DEVADMIN.equals(curUser.getLoginName())){
			list = privGrpService.getGrpsBySuperuser();
		}else{
			list = privGrpService.getGrpsByUid(curUser.getId());
		}
		List<SysPrivilegeItem> listpriv = privilegeItemService.getByRolePrivMap(id);
		String jsonStr =privGrpService.getJsonRolePrivChk(list,listpriv);
		map.put("jsonTreeData", jsonStr);
		return viewPath+"/mapPriv";
	}                                                   

	@RequestMapping(value = "/mapPriv.do")
	public String mapPriv(Long roleId, String[] chkPriv, HttpServletResponse response) throws IOException{     
		logger.info( "Enter mappriv.do ..." );
		if( chkPriv != null&&chkPriv.length>0 ){
			List<SysRolePrivMap> maps=new ArrayList<SysRolePrivMap>();
			for( int i=0; i<chkPriv.length; i++ ){
				SysRolePrivMap map=new SysRolePrivMap();
				map.setPrivId(Long.valueOf(chkPriv[i]));
				map.setRoleId(roleId);
				maps.add(map);
			}
			privilegeItemService.addRolePrivMap(maps,roleId);
		}
		response.getWriter().print(JsonUtil.JSON_RESULT_SUCCESS);
	    return null;         
	}   
}
