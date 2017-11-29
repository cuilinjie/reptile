package com.clj.reptilehouse.system.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springside.modules.utils.security.Cryptos;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.clj.reptilehouse.common.BaseAction;
import com.clj.reptilehouse.common.Global;
import com.clj.reptilehouse.common.tree.TreeUtils;
import com.clj.reptilehouse.common.util.JsonUtil;
import com.clj.reptilehouse.common.util.SystemUtil;
import com.clj.reptilehouse.system.entity.About;
import com.clj.reptilehouse.system.entity.SysMenu;
import com.clj.reptilehouse.system.entity.SysUser;
import com.clj.reptilehouse.system.service.LoginService;
import com.clj.reptilehouse.system.service.MenuService;
import com.clj.reptilehouse.system.service.ParamItemService;
import com.clj.reptilehouse.system.service.UserService;
import com.clj.reptilehouse.system.web.SystemAction;


@Controller
public class SystemAction extends BaseAction{
	
	private static Logger logger = LoggerFactory.getLogger(SystemAction.class);
	
	private static String menuAttNames = "id,name,url,treeCode";
	private static String menuJsonNames = "id,name,url,code";
	private static String childAttName = "children";

	@Autowired
	private MenuService menuService;
	@Autowired
	private ParamItemService paramItemService;
	@Autowired
	private UserService userService;
	@Autowired
	private LoginService loginService;

	
	@RequestMapping(value = "/login.do")
	public String login(String loginName, String loginPasswd, HttpServletRequest request, ModelMap map) {
		if( StringUtils.isBlank(loginName) || StringUtils.isBlank(loginPasswd)  ){
			return "redirect:/login.jsp";			
		}
		SysUser user = userService.findUserByLoginName(loginName);
		if( user == null ){
			map.put("message", "请输入正确的用户名和密码！");
			return "forward:/login.jsp";
		}
		String aesPasswd = Cryptos.desEncryptToHex(loginPasswd,Global.AES_KEY);
		if( !aesPasswd.equals(user.getLoginPwd())){
			map.put("message", "请输入正确的用户名和密码！");
			return "forward:/login.jsp";			
		}

		UsernamePasswordToken token = new UsernamePasswordToken(user.getLoginName(), user.getLoginPwd());
		token.setRememberMe(true);
		Subject subject = SecurityUtils.getSubject();
		subject.login(token);
		logger.info( "User [{}] logged in successfully.", subject.getPrincipal() );
		
		String clientIp = request.getRemoteAddr();
		SystemUtil.setLoginUser(user, clientIp);
		SystemUtil.ActiveLoginUser(user.getLoginName());
		
		loginService.AddInfo(loginName, user.getUsername(), clientIp);
		
		SystemUtil.addLog("系统登录", "登录成功！");
		return "redirect:/index.do";
	}

	@RequestMapping(value = "/logout.do")
	public String logout() {
		SysUser curUser = SystemUtil.getLoginUser();
		SystemUtil.RemoveLoginUser(curUser.getLoginName());
		SecurityUtils.getSubject().logout();
		return "redirect:/login.jsp";
	} 
	
	@RequestMapping(value = "/index.do")
	public String index(ModelMap map, HttpServletRequest request) {
		SysUser curUser = SystemUtil.getLoginUser();
		if( curUser == null ){
			return Global.RESULT_ERROR_OUTTIME;
		}
		
		List<SysMenu> listAll = menuService.findUserAllMenu(curUser);	
		String menuStyle = curUser.getMenuStyle();
		if( Global.MENU_STYLE_2.equals(menuStyle) ){
			String treeHtml = TreeUtils.getHtmlTreeStr(listAll, "name", "url", "icon");
			map.put("menuHtmlData", treeHtml);		
		}
		else if( Global.MENU_STYLE_3.equals(menuStyle) ){
			List<SysMenu> listTop = menuService.getTopMenu(listAll);
			map.put("topMenuList", listTop);		
			String treeData = "[]";
			if( listTop!=null && listTop.size()>0 ){
				String treeCode = listTop.get(0).getTreeCode();
				List<SysMenu> leftMenu = menuService.findUserSubMenu(curUser, treeCode);
				treeData = JsonUtil.getJsonTreeInfo(leftMenu, menuAttNames, menuJsonNames, childAttName);
				request.getSession().setAttribute(Global.CURRENT_LEFT_MENU_ROOT_CODE, treeCode);
			}
			map.put("treeMenuData", treeData);					
		}
		else {
			menuStyle = Global.MENU_STYLE_DEFAULT; 
			String treeData = JsonUtil.getJsonTreeInfo(listAll, menuAttNames, menuJsonNames, childAttName);
			map.put("treeMenuData", treeData);					
		}
		return "/"+menuStyle;
	}
	
	@RequestMapping(value = "/subMenu.do")
	public String subMenu(String treeCode, HttpServletResponse response) throws IOException{     
		logger.info( "Enter subMenu.do ..." );
		SysUser curUser = SystemUtil.getLoginUser();
		if( curUser == null ){
		    response.getWriter().print(JsonUtil.JSON_RESULT_OUTTIME);
		    return null;         
		}
		List<SysMenu> list = menuService.findUserSubMenu(curUser, treeCode);
		String treeData = JsonUtil.getJsonTreeInfo(list, menuAttNames, menuJsonNames, childAttName);				
	    response.getWriter().print(treeData);
	    return null;         
	}                                                   

	@RequestMapping(value = "/main.do")
	public String main(ModelMap map) {
		logger.info( "Enter subMenu.do ..." );
		return "/main";
	}

	@RequestMapping(value = "/online.do")
	public String online(HttpServletResponse response)  throws IOException{
//		logger.info( "Enter online.do ..." );
		SysUser curUser = SystemUtil.getLoginUser();
		if( curUser == null ){
		    response.getWriter().print(JsonUtil.JSON_RESULT_OUTTIME);
		    return null;         
		}

		Map<String,Object> map = new TreeMap<String,Object>();

		SystemUtil.ActiveLoginUser(curUser.getLoginName());
		map.put("onlineNum", SystemUtil.GetLoginUserNum());

		String data = JsonUtil.map2json(map);
	    response.getWriter().print(data);
	    return null;         
	}
	
	@RequestMapping(value = "/getDeadline.do")
	public String getDeadline(HttpServletResponse response)  throws IOException{
		logger.info( "Enter getDeadline.do ..." );

	    return null;         
	}
    
	@RequestMapping(value = "/importGo.do")
	public String importGo(String name, String url,String tplName, ModelMap map) throws IOException {
		logger.info("Enter importGo.do ...");
		map.put(Global.EXCEL_IMP_MDL_NAME, name);
		map.put(Global.EXCEL_IMP_URL, url);
		map.put("tplName",tplName);
		return "/common/import";
	}                                                 
    
	@RequestMapping(value = "/exportGo.do")
	public String exportGo(String colNames, String url, ModelMap map) throws IOException {
		logger.info("Enter exportGo.do ...");
		map.put(Global.EXCEL_EXP_COL_NAMES, colNames);
		map.put(Global.EXCEL_EXP_URL, url);
		return "/common/export";
	}                                                 
	
	@RequestMapping(value = "/about.do")
	public String about(ModelMap map) {
		logger.info( "Enter about.do ..." );
		About about=new About();
		about.setSysName(paramItemService.getParamValue("SystemName"));
		about.setVersion(paramItemService.getParamValue("SystemVersion"));
		about.setDeveloper(paramItemService.getParamValue("Developer"));
		about.setCopyright(paramItemService.getParamValue("SystemCopyRight"));
//		License li=licenseService.getLicenseInfo();
//		if(li!=null){
//			about.setLicenseUser(li.getLicenseUser());
//			about.setDeviceNum(li.getNetworkNum());
//			about.setDeadline(li.getDeadDate());
//		}
		String jsonStr=JsonUtil.bean2json(about);
		map.put("jsonStr", jsonStr);
		return "/about";
	}
}
