package com.clj.reptilehouse.system.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;
import org.springside.modules.utils.security.Cryptos;

import com.clj.reptilehouse.common.Global;
import com.clj.reptilehouse.common.ServiceException;
import com.clj.reptilehouse.common.excel.ExcelObj;
import com.clj.reptilehouse.common.page.PageHelper;
import com.clj.reptilehouse.common.page.PageInfo;
import com.clj.reptilehouse.common.query.QueryBuilder;
import com.clj.reptilehouse.common.query.SortDirection;
import com.clj.reptilehouse.common.util.IdWorker;
import com.clj.reptilehouse.common.util.JsonUtil;
import com.clj.reptilehouse.common.util.SystemUtil;
import com.clj.reptilehouse.system.dao.SysOrganizationDao;
import com.clj.reptilehouse.system.dao.SysRoleDao;
import com.clj.reptilehouse.system.dao.SysUserDao;
import com.clj.reptilehouse.system.entity.SysOrganization;
import com.clj.reptilehouse.system.entity.SysRole;
import com.clj.reptilehouse.system.entity.SysUser;
import com.clj.reptilehouse.system.service.ShiroDbRealm;
import com.clj.reptilehouse.system.service.UserService;

/**
 * 系统用户管理类
 * 
 * @author jxs
 */
//Spring Bean的标识.
@Service
//默认将类中的所有public函数纳入事务管理.
@Transactional
public class UserService {

	private static Logger logger = LoggerFactory.getLogger(UserService.class);

	private static String objAttNames = "id,type@userType,loginName,username,orgId@sys_organization@id@org_name";
	private static String jsonAttNames = "id,type,loginName,username,orgName";

	@Autowired
	private SysUserDao sysUserDao;

	@Autowired
	private SysRoleDao sysRoleDao;
	
	@Autowired
	private SysOrganizationDao sysOrganizationDao;
	
	@Autowired
	private OrganService organService;
	
	@Autowired(required = false)
	private ShiroDbRealm shiroRealm;
	
	@Autowired
	private RoleService roleService;
	
	public String lastMassage = "";
	
	@Transactional(readOnly = true)
	public String getLastMassage() {
		return lastMassage;
	}

	//-- User Manager --//
	@Transactional(readOnly = true)
	public String getJsonObjStr(final SysUser obj) {
		return JsonUtil.getJsonObjInfo(obj, objAttNames, jsonAttNames);
	}

	@Transactional(readOnly = true)
	public String getJsonListStr(final List<SysUser> list) {
		return JsonUtil.getJsonListInfo(list, objAttNames, jsonAttNames);
	}

	@Transactional(readOnly = true)
	public String getJsonPageStr(final Page<SysUser> page) {
		return JsonUtil.getJsonPageInfo(page, objAttNames, jsonAttNames);
	}

	@Transactional(readOnly = true)
	public List<SysUser> getAllUser() {
		String query =QueryBuilder.custom().withSortName("created_time",SortDirection.ASC).build();
		return  sysUserDao.list(query);
	}
	
	@Transactional(readOnly = true)
	public List<SysUser> getAllUserButDev() {
		String query =QueryBuilder.custom()
				.andEquivalent("type", 9, true)
				.withSortName("org_id,created_time",SortDirection.ASC)
				.build();
		return sysUserDao.list(query);
	}

	@Transactional(readOnly = true)
	public List<SysUser> findAllManageUser(String treeCode, Integer type) {
		List<SysUser> list =sysUserDao.selectMngUser(treeCode+"%",type);
		return list;
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = true)
	public List<SysUser> findMngUserBy(String treeCode) {
		Map map = new HashMap();
		map.put("treeCode",treeCode+"%");
		List<SysUser> list =sysUserDao.selectMngUserBy(map);
		return list;
	}
	
	@Transactional(readOnly = true)
	public List<SysUser> findMngUserBy(Long orgId) {
		return sysUserDao.selectSubUsersByOid(orgId);
	}
	
	@Transactional(readOnly = true)
	public SysUser findMngBy(Long orgId) {
		QueryBuilder query =QueryBuilder.custom();
		query.andEquivalent("type",1);
		query.andEquivalent("org_id", orgId);
		List<SysUser> list =sysUserDao.list(query.build());
		if(list!=null&&!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
	@Transactional(readOnly = true)
	public boolean isMngUnique(Long id,Long orgId){
		QueryBuilder query =QueryBuilder.custom();
		query.andEquivalent("type",1);
		query.andEquivalent("org_id", orgId);
		List<SysUser> list =sysUserDao.list(query.build());
		if(list!=null&&!list.isEmpty()){
			if(id!=null){
				if(!list.get(0).getId().equals(id)) return false;
			}else{
				return false;
			}
		}
		return true;
	}
	/**
	 * 根据用户id获取直接领导
	 * @param userId
	 * @return
	 */
	public SysUser getLeaderBy(Long userId){
		SysUser user=sysUserDao.selectByPrimaryKey(userId);
		SysUser mnguser=null;
		if(user.getType()==1){
			SysOrganization porg=organService.getParOrg(user.getOrgId());
			if(porg!=null){
				mnguser=findMngBy(porg.getId());
			}
		}else{
			mnguser=findMngBy(user.getOrgId());
		}
		return mnguser;
	}
	/**
	 * 根据treecode获取所有用户id
	 * @param treeCode
	 * @return
	 */
	public String getUserIdsBy(String treeCode){
		List<SysUser> list=new ArrayList<SysUser>();
		String[] codes=treeCode.split(",");
		for(String code:codes){
			List<SysUser> users=findMngUserBy(code);
			list.addAll(users);
		}
		Set<Long> set=new TreeSet<Long>();
		for(int i=0;i<list.size();i++){
			set.add(list.get(i).getId());
		}
		String ids="";
		if(!set.isEmpty()){
			ids=set.toString().replaceAll("\\s*", "");;
			ids=ids.substring(1,ids.length()-1);
		}
		return ids;
	}
	
	/**
	 * 根据机构id获取所有用户id
	 * @param orgId
	 * @return
	 */
	public String getUserIdsBy(Long orgId){
		StringBuffer sb=new StringBuffer();
		List<SysUser> list=sysUserDao.selectSubUsersByOid(orgId);
		for(int i=0;i<list.size();i++){
			sb.append(list.get(i).getId());
			if(i!=(list.size()-1))sb.append(",");
		}
		return sb.toString();
	}
	
	/**
	 * 根据用户id获取所辖用户id
	 * @param orgId
	 * @return
	 */
	public String getUserIdsByUid(Long userId){
		StringBuffer sb=new StringBuffer();
		List<SysUser> list=sysUserDao.selectSubUsersByUid(userId);
		for(int i=0;i<list.size();i++){
			sb.append(list.get(i).getId());
			if(i!=(list.size()-1))sb.append(",");
		}
		return sb.toString();
	}
	
	@Transactional(readOnly = true)
	public SysUser getUser(Long id) {
		return sysUserDao.selectByPrimaryKey(id);
	}
	
	@Transactional(readOnly = true)
	public SysUser getUserWith(Long id) {
		SysUser user=sysUserDao.selectByPrimaryKey(id);
		SysOrganization org=sysOrganizationDao.selectByPrimaryKey(user.getOrgId());
		if(org!=null){
			user.setOrgName(org.getOrgName());
		}
		return user;
	}
	
	public void addUser(SysUser entity) {
		logger.debug("addUser...");
		entity.setId(IdWorker.nextId());
		sysUserDao.insert(entity);
	}

	public void saveUser(SysUser entity) {
		logger.debug("saveUser...");
		sysUserDao.updateByPrimaryKeySelective(entity);
		shiroRealm.clearCachedAuthorizationInfo(entity.getLoginName());
	}
	
	public void updateUser(SysUser entity) {
		entity.setModifiedTime(System.currentTimeMillis());
		sysUserDao.updateByPrimaryKeySelective(entity);
	}

	/**
	 * 删除用户,如果尝试删除超级管理员将抛出异常.
	 */
	public void deleteUser(Long id) {
		logger.debug("deleteUser(id:{})...", id);
		SysUser user=getUser(id);
		if ("admin".equals(user.getLoginName())||"devadmin".equals(user.getLoginName())) {
			logger.warn("操作员{}尝试删除超级管理员用户", SecurityUtils.getSubject().getPrincipal());
			throw new ServiceException("不能删除超级管理员用户");
		}
		sysUserDao.deleteByPrimaryKey(id);
	}

	public void deleteUser(String[] ids) {
		for(String id:ids){
			deleteUser(Long.valueOf(id));
		}
	}
	/**
	 * 人员分组
	 */
	public void moveMembers(String ids,Long grpId) {
		sysUserDao.membersMoveById(grpId, ids);
	}
	
	@Transactional(readOnly = true)
	public boolean isLoginNameUnique(String loginName) {
		boolean flag=true;
		String query =QueryBuilder.custom().andEquivalent("login_name",loginName).build();
		List<SysUser> list= sysUserDao.list(query);
		if(list!=null&&!list.isEmpty()){
			flag=false;
		}
		return flag;
	}
	
	@Transactional(readOnly = true)
	public boolean isLoginNameUnique(String loginName,long id) {
		boolean flag=true;
		List<SysUser> list= sysUserDao.list("lower(login_name)='" + loginName.toLowerCase() + "' and id != " + id);
		if(list!=null&&!list.isEmpty()){
			flag=false;
		}
		return flag;
	}

	@Transactional(readOnly = true)
	public SysUser findUserByLoginName(String loginName) {
		return sysUserDao.selectByLoginName(loginName);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PageInfo<SysUser> findMembersByPage(String userId,SysUser user) {
		PageHelper.startPage(user.getPageNum(),user.getPageSize());
		List<SysUser> ulist =new ArrayList<SysUser>();
		if(user.getType()!=null&&user.getType()==1){
			ulist=sysUserDao.ListById(Long.valueOf(userId));
		}else{
			if(StringUtils.isBlank(userId)){
				userId=SystemUtil.getLoginUserMainOrg().getTreeCode();
			}
			Map map = new HashMap();
			map.put("treeCode",userId+"%");
			map.put("username", user.getUsername());
			ulist =sysUserDao.selectMngUserBy(map);
		}
		PageInfo<SysUser> pageInfo = new PageInfo<SysUser>(ulist);
		return pageInfo;
	}
	
	/**
	 * 用户批量导入，包括批量更新
	 * @param stream
	 */
	public boolean importExcel(InputStream stream){
		String excelAttNames = "orgId@sys_organization@id@org_name,roleName,username,"
				+ "loginName,loginPwd,gender,political,position,company,email,mobile,qq";
		this.lastMassage = "";
		ExcelObj<SysUser> excelUtil = new ExcelObj<SysUser>(SysUser.class);
		List<SysUser> list = excelUtil.getObjListFrom(stream, excelAttNames);
		if( list==null || list.size()==0 ){
			this.lastMassage = "导入失败！请检查导入文件数据！";
			return false;
		}
		SysUser curUser = SystemUtil.getLoginUser();
		SysOrganization org=SystemUtil.getLoginUserMainOrg();
		String operator = curUser.getLoginName();
		String orgIds=organService.getOrgIdsBy(org.getTreeCode());//获取所辖机构id
		long now=System.currentTimeMillis();//获取系统当前时间
		int addNum=0;
		int updateNum=0;
		List<SysUser> insertlist=new ArrayList<SysUser>();
		List<SysUser> updatelist=new ArrayList<SysUser>();
		for(int i=0; i<list.size(); i++){
			SysUser user = list.get(i);
			if(user.getOrgId()==null||!verifInOrg(orgIds,user.getOrgId())){
				this.lastMassage = "第"+(i+1)+"行,所在组织为空或不存在！";return false;
			}
			Long roleId=verifInRole(user.getOrgId(),user.getRoleName());
			if(roleId==null){
				this.lastMassage = "第"+(i+1)+"行,角色为空或不存在，请添加角色！";return false;
			}
			if(StringUtils.isBlank(user.getLoginName())||StringUtils.isBlank(user.getLoginPwd())){
				this.lastMassage = "第"+(i+1)+"行,登录名称或登录密码不能为空！";return false;
			}
			SysUser ufind = findUserByLoginName(user.getLoginName());
			user.setModifier(operator);
			user.setModifiedTime(now);
			user.setRoleId(roleId);
			if(ufind!=null){
				if(user.getOrgId().equals(ufind.getOrgId())){
					this.lastMassage = "第"+(i+1)+"行,登录名已经存在！";
					return false;
				}
				BeanUtils.copyProperties(user, ufind, new String[]{"createdTime","id","type","status"});
				ufind.setLoginPwd(Cryptos.desEncryptToHex(user.getLoginPwd(),Global.AES_KEY));
				updatelist.add(ufind);
				updateNum++;
			}else{
				user.setId(IdWorker.nextId());
				user.setLoginPwd(Cryptos.desEncryptToHex(user.getLoginPwd(),Global.AES_KEY));
				user.setStatus(true);
				user.setCreator(operator);
				user.setType(0);
				user.setCreatedTime(now);
				user.setLastLoginTime(now);
				insertlist.add(user);
				addNum++;
			}
		}
		if(!insertlist.isEmpty()) sysUserDao.insertBatch(insertlist);
		if(!updatelist.isEmpty()) sysUserDao.updateBatch(updatelist);
		this.lastMassage = String.format("导入完毕！新增%d条记录，更新%d条记录。", addNum, updateNum);
		return true;
	}

	public SysUser findByLimits(Long id) {
		return sysUserDao.selectByLimits(id);
	}
	
	public boolean verifInOrg(String ids,Long orgId){
		if(StringUtils.isBlank(ids)) return false;
		String[] aids=ids.split(",");
		for(String oid:aids){
			if(oid.equals(String.valueOf(orgId))) return true;
		}
		return false;
	}
	public Long verifInRole(Long orgId,String roleName){
		List<SysRole> list=roleService.getRolesByOrgId(orgId);//获取当前机构角色
		if(StringUtils.isBlank(roleName)) return null;
		for(SysRole role:list){
			if(roleName.equals(role.getName())) return role.getId();
		}
		return null;
	}
}
