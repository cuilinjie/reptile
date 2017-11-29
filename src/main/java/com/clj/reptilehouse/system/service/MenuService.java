package com.clj.reptilehouse.system.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clj.reptilehouse.common.Global;
import com.clj.reptilehouse.common.query.QueryBuilder;
import com.clj.reptilehouse.common.query.SortDirection;
import com.clj.reptilehouse.common.tree.TreeService;
import com.clj.reptilehouse.common.tree.TreeUtils;
import com.clj.reptilehouse.common.util.IdWorker;
import com.clj.reptilehouse.common.util.JsonUtil;
import com.clj.reptilehouse.system.dao.SysMenuDao;
import com.clj.reptilehouse.system.dao.SysPrivilegeItemDao;
import com.clj.reptilehouse.system.entity.SysMenu;
import com.clj.reptilehouse.system.entity.SysUser;
import com.clj.reptilehouse.system.service.MenuService;


/**
 * 系统菜单管理类
 * 
 * @author jxs
 */
//Spring Bean的标识.
@Service
//默认将类中的所有public函数纳入事务管理.
@Transactional
public class MenuService extends TreeService<SysMenu>{
	private static Logger logger = LoggerFactory.getLogger(MenuService.class);

	private static String objAttNames = "id,name,url,icon,remark,status,treeCode,parentId";
	private static String jsonAttNames = "id,name,url,icon,remark,status,treeCode,parentId";
	private static String childAttName = "children";

	private static int treeLevelLength = 3;
	
	public String lastMassage = "";

	@Autowired
	private SysMenuDao sysMenuDao;
	@Autowired
	private SysPrivilegeItemDao sysPrivilegeItemDao;

	@Transactional(readOnly = true)
	public String getJsonObjStr(final SysMenu obj) {
		return JsonUtil.getJsonObjInfo(obj, objAttNames, jsonAttNames);
	}

	@Transactional(readOnly = true)
	public String getJsonListStr(final List<SysMenu> list) {
		return JsonUtil.getJsonListInfo(list, objAttNames, jsonAttNames);
	}
	
	@Transactional(readOnly = true)
	public String getJsonTreeStr(final List<SysMenu> list) {
		return JsonUtil.getJsonTreeInfo(list, objAttNames, jsonAttNames, childAttName);
	}

	@Transactional(readOnly = true)
	public String getHtmlTreeStr(final List<SysMenu> list) {
		return TreeUtils.getHtmlTreeStr(list, "name", "url", "icon" );
	}

	@Transactional(readOnly = true)
	public List<SysMenu> getAllMenu() {
		return sysMenuDao.list(" 1=1 order by tree_code");
	}
	
	@Transactional(readOnly = true)
	public List<SysMenu> findUserAllMenu(Long userId)
	{
		List<SysMenu> list =sysMenuDao.listUserMenu(userId,null);
		keepTreeList(list, 1, treeLevelLength);
		return list;
	}
	
	
	@Transactional(readOnly = true)
	public List<SysMenu> findUserAllMenu(Long userId,String treeCode)
	{
		List<SysMenu> list =sysMenuDao.listUserMenuNI(userId,treeCode);
		keepTreeList(list, 1, treeLevelLength);
		return list;
	}
	@Transactional(readOnly = true)
	public List<SysMenu> findSubMenu(String treeCode)
	{
		String query =QueryBuilder.custom()
				.andLike("tree_code", treeCode+"%")
				.withSortName("tree_code", SortDirection.ASC)
				.build();
		return sysMenuDao.list(query);
	}
	
	@Transactional(readOnly = true)
	public List<SysMenu> findSubMenuNI(String treeCode)
	{
		String query =QueryBuilder.custom()
				.andGroup().andLike("tree_code", treeCode+"%")
				.andLike("tree_code", treeCode, true).endGroup()
				.andEquivalent("status",1)
				.withSortName("tree_code", SortDirection.ASC).build();
		return sysMenuDao.list(query);
	}
	
	@Transactional(readOnly = true)
	public List<SysMenu> findUserSubMenu(SysUser user, String treeCode)
	{
		List<SysMenu> list = null;
		if( user.getType()== Global.USER_TYPE_DEV ){
			list = findSubDevMenu(treeCode);
		}else{
			list = findUserSubMenu(user.getId(), treeCode);	
		}
		return list;
	}

	@Transactional(readOnly = true)
	public List<SysMenu> findUserMenu(Long userId, String treeCode)
	{
		List<SysMenu> list = sysMenuDao.listUserMenu(userId,treeCode);
		System.out.println(list.size());
		keepTreeList(list, 1, treeLevelLength);
		return list;
	}

	
	@Transactional(readOnly = true)
	public List<SysMenu> findUserSubMenu(Long userId, String treeCode)
	{
		List<SysMenu> list = sysMenuDao.listUserMenu(userId,treeCode+"%");
		keepTreeList(list, 1, treeLevelLength);
		return list;
	}
	
	@Transactional(readOnly = true)
	public List<SysMenu> getTopMenu(List<SysMenu> list)
	{
		if( list == null ){
			return list;
		}
		List<SysMenu> listTop = new ArrayList<SysMenu>();
		for(SysMenu menu:list){
			if( menu.getTreeCode().length() == treeLevelLength ){
				listTop.add(menu);
			}
		}
		return listTop;
	}
	
	@Transactional(readOnly = true)
	public List<SysMenu> findUserTopMenu(Long userId)
	{
		List<SysMenu> list = findUserAllMenu(userId);
		return getTopMenu(list);
	}
	
	@Transactional(readOnly = true)
	public List<SysMenu> findUserAllMenu(SysUser user)
	{
		List<SysMenu> list = null;
		if( user.getType() == Global.USER_TYPE_DEV ){
			list = findAllDevMenu();
		}
		else{
			list = findUserAllMenu(user.getId());	
		}
		return list;
	}
	/*
	 * 根据用户的权限查询当前菜单下的所有子节点
	 */
	@Transactional(readOnly = true)
	public List<SysMenu> findUserAllMenu(SysUser user,String treeCode)
	{
		List<SysMenu> list = null;
		if( user.getType() == Global.USER_TYPE_DEV ){
			list = findSubMenuNI(treeCode);
		}
		else{
			list = findUserAllMenu(user.getId(),treeCode);	
		}
		return list;
	}
	
	@Transactional(readOnly = true)
	public List<SysMenu> findAllDevMenu()
	{
		String query=QueryBuilder.custom()
				.withSortName("tree_code",SortDirection.ASC).build();
		List<SysMenu> list = sysMenuDao.list(query);
		keepTreeList(list, 1, treeLevelLength);
		return list;
	}
	
	@Transactional(readOnly = true)
	public List<SysMenu> findSubDevMenu(String treeCode)
	{
		String query =QueryBuilder.custom()
				.andLike("tree_code", treeCode+"%")
				.withSortName("tree_code", SortDirection.ASC)
				.build();
		return sysMenuDao.list(query);
	}

	@Transactional(readOnly = true)
	public SysMenu getMenu(Long id) {
		return sysMenuDao.selectByPrimaryKey(id);
	}
	
	@Transactional(readOnly = true)
	public SysMenu getMenuByName(String name)
	{
		String query =QueryBuilder.custom()
				.andLike("name", name)
				.withSortName("tree_code", SortDirection.ASC)
				.build();
		List<SysMenu> list = sysMenuDao.list(query);
		if(list!=null&&!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
	
	public void addMenu(SysMenu entity) {
		logger.debug("saveMenu...");
		long now =System.currentTimeMillis();
		entity.setCreatedTime(now);
		entity.setModifiedTime(now);
		entity.setId(IdWorker.nextId());
		String treeCode =getNewChildCode(entity.getParentId(), treeLevelLength);
		entity.setTreeCode(treeCode);
		sysMenuDao.insert(entity);
	}

	public void saveMenu(SysMenu menu) {
		logger.debug("saveMenu...");
		menu.setModifiedTime(System.currentTimeMillis());
		sysMenuDao.updateByPrimaryKeySelective(menu);
	}

	public void deleteMenu(Long id) {
		logger.debug("deleteMenu(id:{})...", id);
		SysMenu menu =sysMenuDao.selectByPrimaryKey(id);
		if( menu == null ){
			return;
		}
		List<SysMenu> list =findNextTrees(menu.getTreeCode(), treeLevelLength);

		deleteTree(menu.getTreeCode());
		sysPrivilegeItemDao.deleteMenuPrivMap(id);//删除权限map表中数据
		int levelLength = menu.getTreeCode().length();
		for (SysMenu obj : list)
		{
			String adjustCode = TreeUtils.getLevelLastTreeCode(obj.getTreeCode(), levelLength);
			obj.setTreeCode(adjustCode);
			sysMenuDao.updateByPrimaryKeySelective(obj);
		}
	}

	public void deleteMenu(String[] ids) {
		for(String id:ids){
			deleteMenu(Long.valueOf(id));
		}
	}
	
	@Transactional(readOnly = true)
	public List<SysMenu> findMoveTrees(SysMenu menu) {
		return findOtherTrees(menu.getTreeCode());
	}

	public boolean moveMenu(String moveId, String targetId, int moveType){
		if( !moveNode(Long.valueOf(moveId), Long.valueOf(targetId), moveType, treeLevelLength) )
		{
			this.lastMassage = "移动菜单或目标菜单已经不存在!";
			return false;
		}
		return true;
	}
}
