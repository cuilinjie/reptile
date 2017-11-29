package com.clj.reptilehouse.common.util;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springside.modules.orm.PropertyFilter;

import com.clj.reptilehouse.common.Global;
import com.clj.reptilehouse.common.query.QueryBuilder;
import com.clj.reptilehouse.system.entity.SysLog;
import com.clj.reptilehouse.system.entity.SysOrganization;
import com.clj.reptilehouse.system.entity.SysUser;
import com.clj.reptilehouse.system.service.LogService;

public class SystemUtil {
	public static String webAppRootKey = "webapp.root";

	private static Map<String, Object> mapLoginUser = new HashMap<String, Object>();
	
	public static String getRootPath()
	{
		return System.getProperty(webAppRootKey);
	}
	
	public static String getRootPath1()
	{
		URL pathClass = Thread.currentThread().getContextClassLoader().getResource("");
		try
		{
			File file = new File(pathClass.toURI());
			String path = file.getAbsolutePath();
			if(path.indexOf("\\") >= 1)
			{
				path = path.replaceAll("\\\\","/");
			}
			int index = path.indexOf("/WEB-INF");
			if(index >= 1)
			{
				path = path.substring(0,index);
			}
			return path.trim();
		}catch(URISyntaxException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static synchronized void ActiveLoginUser(String loginUser) {
		Date now = new Date();
		mapLoginUser.put(loginUser, now);
	}

	public static synchronized void RemoveLoginUser(String loginUser) {
		mapLoginUser.remove(loginUser);
	}

	public static synchronized void CheckLoginUser() {
		Iterator<Entry<String, Object>> iter = mapLoginUser.entrySet().iterator();    
		while(iter.hasNext()) {    
		    Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iter.next();    
		    Date value = (Date)entry.getValue();    
		    Date now = new Date();
		    Long intv = now.getTime() - value.getTime();
		    if( intv > 60000 ){
		    	iter.remove();
		    }
		} 
	}

	public static synchronized int GetLoginUserNum() {
		return mapLoginUser.size();
	}

	public static void setLoginUser(SysUser user, String clientIp) {
		Subject subject = SecurityUtils.getSubject();
		subject.getSession().setAttribute(Global.LOGIN_USER, user);		
		subject.getSession().setAttribute(Global.REMOTE_IP, clientIp);		
		setLoginUserMainOrg(user);
	}

	public static void setLoginUserMainOrg(SysUser user) {
		if( user == null ){
			return;
		}
		SysOrganization org = ServiceUtil.getOrganService().getOrgan(user.getOrgId());
		setLoginUserMainOrg(org);		
	}
	
	public static void setLoginUserMainOrg(SysOrganization org) {
		if( org == null ){
			return;
		}
		Subject subject = SecurityUtils.getSubject();
		subject.getSession().setAttribute(Global.LOGIN_USER_MAIN_ORGAN, org);		
		//List<SysOrganization> listOrg = ServiceUtil.getOrganService().findOrganByCode(org.getTreeCode());
		//subject.getSession().setAttribute(Global.LOGIN_USER_SUB_ORGAN, listOrg);		
		//setLoginUserSubOrgIds(listOrg);
	}
	
	public static void setLoginUserSubOrgIds(List<SysOrganization> listOrg){
		if( listOrg == null || listOrg.size()==0 ){
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(" (");
		for( int i=0; i<listOrg.size(); i++  ){
			sb.append("'").append(listOrg.get(i).getId()).append("'");
			if( i<listOrg.size()-1 ){
				sb.append(", ");
			}
		}
		sb.append(") ");
		Subject subject = SecurityUtils.getSubject();
		subject.getSession().setAttribute(Global.LOGIN_USER_SUB_ORGAN_IDS, sb.toString());		
	}
	
	public static SysUser getLoginUser() {
		Subject subject = SecurityUtils.getSubject();
		return (SysUser) subject.getSession().getAttribute(Global.LOGIN_USER);
	}
	
	public static String getLoginUserIp() {
		Subject subject = SecurityUtils.getSubject();
		return (String) subject.getSession().getAttribute(Global.REMOTE_IP);
	}
	
	public static boolean isSuperUser(SysUser user) {
		if( user.getType() == Global.USER_TYPE_DEV ){
			return true;		
		}
		return false;
	}
	
	public static SysOrganization getLoginUserMainOrg() {
		Subject subject = SecurityUtils.getSubject();
		return (SysOrganization) subject.getSession().getAttribute(Global.LOGIN_USER_MAIN_ORGAN);
	}
	
	@SuppressWarnings("unchecked")
	public static List<SysOrganization> getLoginUserSubOrg(){
		Subject subject = SecurityUtils.getSubject();
		List<SysOrganization> listOrg = (List<SysOrganization>)subject.getSession().getAttribute(Global.LOGIN_USER_SUB_ORGAN);
		return listOrg;
	}
	
	public static String getLoginUserSubOrgIds(){
		Subject subject = SecurityUtils.getSubject();
		return (String)subject.getSession().getAttribute(Global.LOGIN_USER_SUB_ORGAN_IDS);
	}

	public static boolean addLog(String title, String content){
		SysUser user = getLoginUser();
		if( user == null ){
			return false;
		}
		long curTime = System.currentTimeMillis();
		SysLog log = new SysLog();
		log.setTitle(title);
		log.setContent(content);
		log.setModifiedTime(curTime);
		log.setCreatedTime(curTime);
		log.setUserId(user.getId());
		log.setUsername(user.getUsername());
		log.setOperater(user.getLoginName());
		log.setClientIp(getLoginUserIp());
		SysOrganization org = getLoginUserMainOrg();
		if( org != null ){
			log.setOrgId(org.getId());
			log.setOrgCode(org.getOrgCode());
			log.setOrgName(org.getOrgName());
		}
		LogService logService = ServiceUtil.getLogService();
		logService.addLog(log);
		return true;
	}
	
	public static String buildSQLCondition(List<PropertyFilter> filters){
		//0EQ, 1NE, 2LIKE, 3SLIKE, 4ELIKE, 5LT, 6GT, 7LE, 8GE, 9IN, 10NOTIN, 11ISNULL, 12ISNOTNULL, 13ISEMPTY, 14ISNOTEMPTY;
		QueryBuilder query =QueryBuilder.custom();
		for (Iterator i = filters.iterator(); i.hasNext(); ) {
			PropertyFilter filter = (PropertyFilter)i.next();
			String properName = filter.getPropertyName();
			Object propertyValue = filter.getMatchValue();
			PropertyFilter.MatchType matchytp = filter.getMatchType();
			switch (matchytp.ordinal()) {
			case 0:
				query.andEquivalent(properName, propertyValue);
				break;
			case 1:
				query.orEquivalent(properName, propertyValue);
				break;
			case 2:
				query.andLike(properName, "%"+propertyValue+"%");
				break;
			case 3:
				query.andLike(properName, "%"+propertyValue);
				break;
			case 4:
				query.andLike(properName, "%"+propertyValue);
				break;
			case 5:
				query.andLessThan(properName, propertyValue);
				break;
			case 6:
				query.andLessThan(properName, propertyValue);
				break;
			case 7:
				query.andGreaterThan(properName, propertyValue);
				break;
			case 8:
				query.andLessThan(properName, propertyValue);
				break;
			case 9:
				query.andGreaterThan(properName, propertyValue);
				break;
			case 10:
				query.andIn(properName, propertyValue);
				break;
			case 11:
				query.orIn(properName, propertyValue);
				break;
			default:
				break;
			}
		}
		return query.build();
		
	}

}
