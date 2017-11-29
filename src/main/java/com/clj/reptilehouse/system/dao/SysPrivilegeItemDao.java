package com.clj.reptilehouse.system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.clj.reptilehouse.common.AbstractDao;
import com.clj.reptilehouse.system.entity.SysMenuPrivMap;
import com.clj.reptilehouse.system.entity.SysPrivilegeItem;
import com.clj.reptilehouse.system.entity.SysRolePrivMap;

public interface SysPrivilegeItemDao extends AbstractDao<SysPrivilegeItem>{
	
	Integer selectNoById(@Param(value="id") Long id);
	
	Integer selectMaxNo(@Param(value="grpId") Long grpId);
	
	List<SysPrivilegeItem> selectByUserId(@Param(value="userId") Long userId);
	
	Integer updateNo(@Param(value="grpId") Long grpId,@Param(value="no") Integer no);
	
	/*
	 * 操作 sys_role_priv_map
	 */
	List<SysPrivilegeItem> selectByRolePrivMap(@Param(value="roleId") Long roleId);
	
	List<SysRolePrivMap> selectRolePrivMap(String query);
	
	void deleteRolePrivMap(@Param(value="roleId") Long roleId);
	
	int insertRolePrivMap(List<SysRolePrivMap> maps);
	
	/*
	 * 操作 sys_menu_priv_map
	 */
	List<SysPrivilegeItem> selectByMenuPrivMap(@Param(value="menuId") Long menuId);
	
	List<SysRolePrivMap> selectMenuPrivMap(String query);
	
	void deleteMenuPrivMap(@Param(value="menuId") Long menuId);
	
	int insertMenuPrivMap(List<SysMenuPrivMap> maps);
	
	/**
	 * 根据权限id,删除map里的权限映射
	 */
	void deleteMapByPrivId(@Param(value="privId") Long privId);
	/**
	 * 根据权限id,删除map里的权限映射
	 */
	void deleteroleMapByPrivId(@Param(value="privId") Long privId);
	
}