package com.clj.reptilehouse.system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.clj.reptilehouse.common.AbstractDao;
import com.clj.reptilehouse.system.entity.SysMenu;

public interface SysMenuDao extends AbstractDao<SysMenu>{
	
	List<SysMenu> listUserMenu(@Param(value="userId") Long userId,@Param(value="treeCode") String treeCode);
	
	List<SysMenu> listUserMenuNI(@Param(value="userId") Long userId,@Param(value="treeCode") String treeCode);
	
	List<SysMenu> listLeftMenuByUid(@Param(value="userId") Long userId);
}