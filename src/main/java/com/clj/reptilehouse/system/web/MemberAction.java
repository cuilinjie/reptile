package com.clj.reptilehouse.system.web;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springside.modules.utils.security.Cryptos;

import com.clj.reptilehouse.common.Global;
import com.clj.reptilehouse.common.page.PageInfo;
import com.clj.reptilehouse.common.util.BeanUtil;
import com.clj.reptilehouse.common.util.JsonUtil;
import com.clj.reptilehouse.common.util.SystemUtil;
import com.clj.reptilehouse.common.util.UploadUtil;
import com.clj.reptilehouse.system.entity.SysMenu;
import com.clj.reptilehouse.system.entity.SysUser;
import com.clj.reptilehouse.system.service.MenuService;
import com.clj.reptilehouse.system.service.RoleService;
import com.clj.reptilehouse.system.service.UserService;

@Controller
@RequestMapping(value = "/system/member")
public class MemberAction {
	private final String viewPath = "/system/member/member";
	private static Logger logger = LoggerFactory.getLogger(MemberAction.class);
	
	@Autowired
	private UserService userService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private RoleService roleService;
	
	@RequestMapping(value = "/mainTab.do")
	public String mainTab(String treeCode,ModelMap map) {
		logger.info( "Enter mainTab.do ..." );
		SysUser curUser = SystemUtil.getLoginUser();
		List<SysMenu> list=menuService.findUserAllMenu(curUser,treeCode);
		String menuData = JsonUtil.getJsonListInfo(list,"id,name,url,treeCode","id,name,url,treeCode");
		map.put("menuData", menuData);
		return "/system/member/mainTab";
	}
	@RequestMapping(value = "/main.do")
	public String main(ModelMap map) {
		logger.info( "Enter main.do ..." );
		return viewPath+"/main";
	}
	@RequestMapping(value = "/add.do")
	public String add(Long gid,String gname,ModelMap map,HttpServletResponse response) {
		/*logger.info( "Enter add.do ..." );
		SysOrganization userOrg = SystemUtil.getLoginUserMainOrg();
		SysUser user = SystemUtil.getLoginUser();
		map.put("gid",gid);
		map.put("gname", gname);
		//map.put("roles", roleService.getRolesByTreeCode(userOrg.getTreeCode(),user.getType()));
*/		return viewPath+"/add";
	}
	@RequestMapping(value = "/edit.do")
	public String edit(Long id,ModelMap map,HttpServletResponse response) {
		logger.info( "Enter add.do ..." );
		/*SysOrganization userOrg = SystemUtil.getLoginUserMainOrg();
		SysUser sysuser = userService.getUser(id);
		sysuser.setLoginPwd(Cryptos.desDecryptFromHex(sysuser.getLoginPwd(),Global.AES_KEY));
		map.put("data", sysuser);*/
		//map.put("roles", roleService.getRolesByTreeCode(userOrg.getTreeCode(),sysuser.getType()));
		return viewPath+"/edit";
	}
	@RequestMapping(value = "/delete.do")
	public String delete(String ids, HttpServletResponse response) throws IOException {
		logger.info( "Enter delete.do ..." );
		SysUser curUser = SystemUtil.getLoginUser();
		String[] userIds=ids.split(",");
		for(String userId:userIds){
			if( userId.equals(String.valueOf(curUser.getId()))){
				response.getWriter().printf(JsonUtil.JSON_RESULT_FALSE_MSGMDL, "不能删除当前登录用户！");
				return null;
			}
			userService.deleteUser(Long.valueOf(userId));
		}
		response.getWriter().print(JsonUtil.JSON_RESULT_SUCCESS);
		return null;
	}
	@ResponseBody
	@RequestMapping(value = "/getUser.do",method = RequestMethod.GET)
	public SysUser getUser(Long id, ModelMap map) {
		logger.info( "Enter getUser.do ..." );
		SysUser user = userService.getUserWith(id);
		return user;
	}
	@RequestMapping(value = "/memGrp.do")
	public String mainGrp(ModelMap map) {
		logger.info( "Enter memGrp.do ..." );
		return viewPath+"/memGrp";
	}
	@RequestMapping(value = "/editMemGrp.do")
	public String editMemGrp(String action,ModelMap map) {
		logger.info( "Enter editMemGrp.do ..." );
		map.put("action", action);
		return viewPath+"/editMemGrp";
	}
	@ResponseBody
	@RequestMapping(value = "/getMembers.do",method = RequestMethod.POST)
	public PageInfo<SysUser> getMembers(String userId,SysUser user){
		logger.info( "Enter getMembers.do ..." );
	    return userService.findMembersByPage(userId,user);         
	}
	
	/*
	 * 验证登录名
	 */
	@RequestMapping(value = "/checkLogin.do", method = RequestMethod.POST)
	public void checkLoginName(String loginName,HttpServletResponse response) throws IOException{
		boolean flag=true;
		if(StringUtils.isNotBlank(loginName)){
			flag=userService.isLoginNameUnique(loginName);
		}
		response.getWriter().print(flag);
	}
	
	@RequestMapping(value = "/saveAdd.do",method = RequestMethod.POST)
	public void saveAdd(String action,Long roleId,boolean isEditPasswd,String isManager,SysUser user, ModelMap map,HttpServletResponse response) throws IOException{
		logger.info( "Enter save.do ..." );
		boolean validate = true;
		SysUser curUser = SystemUtil.getLoginUser();
		String message=null;
		if(("1").equals(isManager)){
			if(!userService.isMngUnique(user.getId(),user.getOrgId())){
				validate =false;
				message="当前机构已存在管理员！";
			}
			user.setType(1);
		}else{
			user.setType(0);
		}
		if( validate ){
			String operator = curUser.getLoginName();
			long now =System.currentTimeMillis();//获取系统当前时间
			user.setModifier(operator);
			user.setModifiedTime(now);
			if( "add".equals(action)){
				user.setLoginPwd(Cryptos.desEncryptToHex(user.getLoginPwd(),Global.AES_KEY));
				user.setCreator(operator);
				user.setCreatedTime(now);
				user.setLastLoginTime(now);
				userService.addUser(user);		
			}else{
				SysUser editUser = userService.getUser(user.getId());
				BeanUtil.copyProperties(user, editUser,new String[]{"loginPwd","loginName"});
				if( isEditPasswd ){
					editUser.setLoginPwd(Cryptos.desEncryptToHex(user.getLoginPwd(),Global.AES_KEY));
				}
				user.setModifiedTime(now);
				userService.saveUser(editUser);			
				map.put("user", editUser);
			}
			response.getWriter().print(JsonUtil.JSON_RESULT_SUCCESS);
		}else{
			response.getWriter().printf(JsonUtil.JSON_RESULT_FALSE_MSGMDL,message);
		}
	}
	@RequestMapping(value = "/move.do")
	public String move(String ids,Long grpId,HttpServletResponse response) throws IOException {
		logger.info( "Enter move.do ..." );
		SysUser curUser = SystemUtil.getLoginUser();
		String[] userIds=ids.split(",");
		for(String userId:userIds){
			if( userId.equals(String.valueOf(curUser.getId()))){
				response.getWriter().printf(JsonUtil.JSON_RESULT_FALSE_MSGMDL, "不能移动当前登录用户！");
				return null;
			}
		}
		userService.moveMembers(ids, grpId);
		response.getWriter().print(JsonUtil.JSON_RESULT_SUCCESS);
		return null;
	}
	@RequestMapping(value = "/personalEdit.do",method = RequestMethod.POST)
	public void personalEdit(boolean isEditPasswd,SysUser user,HttpServletRequest request, ModelMap map,HttpServletResponse response) throws IOException{
		logger.info( "Enter personalEdit.do ..." );
		if( isEditPasswd ){
			user.setLoginPwd(Cryptos.desEncryptToHex(user.getLoginPwd(),Global.AES_KEY));
		}
		userService.updateUser(user);
		//更改当前登录人session信息
		SysUser luser=SystemUtil.getLoginUser();
		luser.setUsername(user.getUsername());
		String clientIp = SystemUtil.getLoginUserIp();
		SystemUtil.setLoginUser(luser,clientIp);
		response.getWriter().print(Global.RESULT_SUCCESS);
	}
	
	/*
	 * 上传照片
	 */
	@RequestMapping(value = "upload.do")
	public void upload(@RequestParam MultipartFile file,HttpSession session,HttpServletResponse response) throws IOException {
		String result="error";
		// 保存文件 ,获取路径
		String realPath=session.getServletContext().getRealPath("/");
		//realPath = realPath.substring(0, realPath.indexOf("webapps") + 8);
		String faceUrl=UploadUtil.upload(file,realPath,"face");
		System.out.println("....."+faceUrl);
		if(StringUtils.isNotBlank(faceUrl)){
			result="success";
		}
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().print("{\"result\":\""+result+"\",\"faceUrl\":\""+URLEncoder.encode(faceUrl,"utf-8")+"\"}");
	}
	
	@RequestMapping(value = "/importExcel.do")
	public void importExcel(MultipartFile file, HttpServletResponse response) throws IOException{     
		logger.info( "Enter importExcel.do ..." );
		String result = Global.RESULT_ERROR;
		String message = "";
		if( file.getSize()<=0 ){
			message = "上传文件内容为空！";
		}else{
			if( userService.importExcel(file.getInputStream()) ){
				result = Global.RESULT_SUCCESS;
			}
			message = userService.getLastMassage();
		}
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().print("{\"result\":\""+result+"\",\"message\":\""+message+"\"}");
	}   
}
