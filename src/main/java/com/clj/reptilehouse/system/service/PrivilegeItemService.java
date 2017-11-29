package com.clj.reptilehouse.system.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.PageRequest;
import org.springside.modules.orm.PropertyFilter;

import com.clj.reptilehouse.common.query.QueryBuilder;
import com.clj.reptilehouse.common.query.SortDirection;
import com.clj.reptilehouse.common.util.IdWorker;
import com.clj.reptilehouse.common.util.JsonUtil;
import com.clj.reptilehouse.system.dao.SysPrivilegeItemDao;
import com.clj.reptilehouse.system.entity.SysMenuPrivMap;
import com.clj.reptilehouse.system.entity.SysPrivilegeGroup;
import com.clj.reptilehouse.system.entity.SysPrivilegeItem;
import com.clj.reptilehouse.system.entity.SysRolePrivMap;


/**
 * 系统权限管理类
 * 
 * @author jxs
 */
//Spring Bean的标识.
@Service
//默认将类中的所有public函数纳入事务管理.
@Transactional
public class PrivilegeItemService {
	private static Logger logger = LoggerFactory.getLogger(PrivilegeItemService.class);
	
	private static String objAttNames = "id,name,code,no,isBs,isDv,isRoleDis,status@privStatus,remark";
	private static String jsonAttNames = "id,name,code,no,isBs,isDv,isRoleDis,status,remark";

	@Autowired
	private SysPrivilegeItemDao sysPrivilegeItemDao;

	@Transactional(readOnly = true)
	public String getJsonObjStr(final SysPrivilegeItem obj) {
		return  JsonUtil.getJsonObjInfo(obj, objAttNames, jsonAttNames);
	}

	@Transactional(readOnly = true)
	public String getJsonListStr(final List<SysPrivilegeItem> list) {
		return  JsonUtil.getJsonListInfo(list, objAttNames, jsonAttNames);
	}

	@Transactional(readOnly = true)
	public String getJsonPageStr(final Page<SysPrivilegeItem> page) {
		return  JsonUtil.getJsonPageInfo(page, objAttNames, jsonAttNames);
	}

	@Transactional(readOnly = true)
	public List<SysPrivilegeItem> getAllPriv() {
		String query=QueryBuilder.custom().withSortName("no",SortDirection.ASC).build();
		return  sysPrivilegeItemDao.list(query);
	}
	
	@Transactional(readOnly = true)
	public List<SysPrivilegeItem> findUserAllPriv(Long userId)
	{
		List<SysPrivilegeItem> list =sysPrivilegeItemDao.selectByUserId(userId);
		return list;
	}
	
	@Transactional(readOnly = true)
	public List<SysPrivilegeItem> getByGrpId(Long grpId) {
		String query=QueryBuilder.custom()
				.andEquivalent("grp_id", grpId)
				.withSortName("no",SortDirection.ASC).build();
		return sysPrivilegeItemDao.list(query);
	}
	
	@Transactional(readOnly = true)
	public List<SysPrivilegeItem> getGroupPriv(SysPrivilegeGroup group) {
		return  getByGrpId(group.getId());
	}
	
	@Transactional(readOnly = true)
	public List<SysPrivilegeItem> searchPriv(final List<PropertyFilter> filters) {
		return  null;// //privilegeDao.find(filters);
	}
	
	@Transactional(readOnly = true)
	public Page<SysPrivilegeItem> searchPriv(final PageRequest page, final List<PropertyFilter> filters) {
		if( page!=null && page.getOrderBy()!=null ){
			String realOrderBy = JsonUtil.getObjAttName(page.getOrderBy(), objAttNames, jsonAttNames );
			page.setOrderBy(realOrderBy);
		}
		return  null;// //privilegeDao.findPage(page, filters);
	}

	@Transactional(readOnly = true)
	public SysPrivilegeItem getPriv(Long id) {
		return sysPrivilegeItemDao.selectByPrimaryKey(id);
	}

	public void addPriv(SysPrivilegeItem entity) {
		logger.debug("savepriv...");
		entity.setId(IdWorker.nextId());
		Integer number =sysPrivilegeItemDao.selectMaxNo(entity.getGrpId());
		if(number==null||number<0){
			number=0;
		}else{
			number++;
		}
		entity.setNo(number);
		sysPrivilegeItemDao.insert(entity);
	}

	public void savePriv(SysPrivilegeItem entity) {
		logger.debug("savepriv...");
		sysPrivilegeItemDao.updateByPrimaryKeySelective(entity);
	}
		
	public void moveItem(SysPrivilegeItem item, int movePos) {
		String query=QueryBuilder.custom()
				.andEquivalent("grp_id", item.getGrpId())
				.andEquivalent("no", item.getNo()+movePos)
				.build();
		List<SysPrivilegeItem> list=sysPrivilegeItemDao.list(query);
		if(list!=null&&!list.isEmpty()){
			SysPrivilegeItem itemFind=list.get(0);
			Integer number = itemFind.getNo();
			itemFind.setNo(item.getNo());
			item.setNo(number);		
			sysPrivilegeItemDao.updateByPrimaryKeySelective(itemFind);
			sysPrivilegeItemDao.updateByPrimaryKeySelective(item);
		}
	}
	
	public void moveUpItem(SysPrivilegeItem item) {
		moveItem(item, -1);
	}
	
	public void moveDownItem(SysPrivilegeItem item) {
		moveItem(item, 1);
	}

	public void deletePriv(Long id) {
		SysPrivilegeItem item = getPriv(id);
		Long grpId = item.getGrpId();
		Integer number = item.getNo();
		sysPrivilegeItemDao.updateNo(grpId, number);
		sysPrivilegeItemDao.deleteByPrimaryKey(id);
		sysPrivilegeItemDao.deleteroleMapByPrivId(id);
		sysPrivilegeItemDao.deleteMapByPrivId(id);//删除map里的映射
		
	}

	public void deletePriv(String[] ids) {
		for(String id:ids){
			deletePriv(Long.valueOf(id));
		}
	}
	
	@Transactional(readOnly = true)
	public boolean isPrivCodeUnique(Long privId, String privCode) {
		QueryBuilder qb=QueryBuilder.custom();
		if(privId!=null&&privId>0){
			qb.andEquivalent("id", privId, true);
		}
		qb.andEquivalent("code", privCode);
		List<SysPrivilegeItem> list=sysPrivilegeItemDao.list(qb.build());
		if(list!=null&&!list.isEmpty()) return false;
		return true;
	}
	
	/*
	 * 操作 sys_role_priv_map
	 */
	public List<SysPrivilegeItem> getByRolePrivMap(Long roleId){
		return sysPrivilegeItemDao.selectByRolePrivMap(roleId);
	}
	
	public List<SysRolePrivMap> getRolePrivMap(Long roleId){
		String query =QueryBuilder.custom().andEquivalent("role_id",roleId).build();
		return sysPrivilegeItemDao.selectRolePrivMap(query);
	}
	
	public void deleteRolePrivMap(Long roleId){
		sysPrivilegeItemDao.deleteRolePrivMap(roleId);
	}
	
	public int addRolePrivMap(List<SysRolePrivMap> maps,Long roleId){
		sysPrivilegeItemDao.deleteRolePrivMap(roleId);
		return sysPrivilegeItemDao.insertRolePrivMap(maps);
	}
	
	/*
	 * 操作 sys_menu_priv_map
	 */
	public List<SysPrivilegeItem> getByMenuPrivMap(Long menuId){
		return sysPrivilegeItemDao.selectByMenuPrivMap(menuId);
	}
	
	public List<SysRolePrivMap> getMenuPrivMap(Long menuId){
		String query =QueryBuilder.custom().andEquivalent("menu_id",menuId).build();
		return sysPrivilegeItemDao.selectMenuPrivMap(query);
	}
	
	public void deleteMenuPrivMap(Long menuId){
		sysPrivilegeItemDao.deleteMenuPrivMap(menuId);
	}
	
	public int addMenuPrivMap(List<SysMenuPrivMap> maps,Long menuId){
		sysPrivilegeItemDao.deleteMenuPrivMap(menuId);
		return sysPrivilegeItemDao.insertMenuPrivMap(maps);
	}
}
