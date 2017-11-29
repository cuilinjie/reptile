package com.clj.reptilehouse.system.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clj.reptilehouse.common.Global;
import com.clj.reptilehouse.common.query.QueryBuilder;
import com.clj.reptilehouse.common.query.SortDirection;
import com.clj.reptilehouse.common.util.IdWorker;
import com.clj.reptilehouse.common.util.JsonUtil;
import com.clj.reptilehouse.system.dao.SysPrivilegeItemDao;
import com.clj.reptilehouse.system.dao.SysRoleDao;
import com.clj.reptilehouse.system.entity.SysRole;
import com.clj.reptilehouse.system.service.RoleService;


/**
 * 系统角色管理类
 * 
 * @author jxs
 */
//Spring Bean的标识.
@Service
//默认将类中的所有public函数纳入事务管理.
@Transactional
public class RoleService {
	private static Logger logger = LoggerFactory.getLogger(RoleService.class);
	private static String objAttNames = "id,name,code,remark,status";
	private static String jsonAttNames = "id,name,code,remark,status";
	private static String childAttName = "children";
	
	@Autowired
	private SysRoleDao sysRoleDao;
	
	@Autowired
	private SysPrivilegeItemDao sysPrivilegeItemDao;

	@Transactional(readOnly = true)
	public String getJsonObjStr(final SysRole obj) {
		return JsonUtil.getJsonObjInfo(obj, objAttNames, jsonAttNames);
	}

	@Transactional(readOnly = true)
	public String getJsonListStr(final List<SysRole> list) {
		return JsonUtil.getJsonListInfo(list, objAttNames, jsonAttNames);
	}
	@Transactional(readOnly = true)
	public String getJsonTreeStr(final List<SysRole> list) {
		return JsonUtil.getJsonTreeInfo(list, objAttNames, jsonAttNames, childAttName);
	}
	@Transactional(readOnly = true)
	public List<SysRole> getAllRole() {
		return sysRoleDao.list();
	}
	
	@Transactional(readOnly = true)
	public List<SysRole> getRolesByOrgId(Long orgId) {
		String query =QueryBuilder.custom().andEquivalent("org_id",orgId).build();
		return sysRoleDao.list(query);
	}

	@Transactional(readOnly = true)
	public List<SysRole> getAllUserRole(Integer userType) {
		if( userType == Global.USER_TYPE_DEV ){
			return  sysRoleDao.list();
		}
		String query =QueryBuilder.custom()
				.andEquivalent("status", 1)
				.withSortName("name",SortDirection.ASC)
				.build();
		return sysRoleDao.list(query);
	}
	
	@Transactional(readOnly = true)
	public SysRole getRole(Long id) {
		return  sysRoleDao.selectByPrimaryKey(id);
	}

	public void addRole(SysRole entity) {
		logger.debug("addRole...");
		entity.setId(IdWorker.nextId());
		long now =System.currentTimeMillis();
		entity.setCreatedTime(now);
		entity.setModifiedTime(now);
		sysRoleDao.insert(entity);
	}

	public void saveRole(SysRole entity) {
		logger.debug("saveRole...");
		entity.setModifiedTime(System.currentTimeMillis());
		sysRoleDao.updateByPrimaryKeySelective(entity);
	}

	public void deleteRole(Long id) {
		logger.debug("deleteRole(id:{})...", id);
		sysRoleDao.deleteByPrimaryKey(id);
		sysPrivilegeItemDao.deleteRolePrivMap(id);
	}

	public void deleteRole(String[] ids) {
		for(String id:ids){
			deleteRole(Long.valueOf(id));
		}
	}
	
	@Transactional(readOnly = true)
	public boolean isRoleNameUnique(Long roleId, String roleName) {
		QueryBuilder qb=QueryBuilder.custom();
		//qb.andEquivalent("org_id", orgId);
		qb.andEquivalent("name", roleName);
		List<SysRole> list=sysRoleDao.list(qb.build());
		if(list!=null&&!list.isEmpty()){
			if(roleId!=null){
				if(!list.get(0).getId().equals(roleId)) return false;
			}else{
				return false;
			}
		}
		return true;
	}
	
}
