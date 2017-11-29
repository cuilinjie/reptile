package com.clj.reptilehouse.system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.clj.reptilehouse.common.AbstractDao;
import com.clj.reptilehouse.system.entity.SysPrivilegeGroup;

public interface SysPrivilegeGroupDao extends AbstractDao<SysPrivilegeGroup>{
	
	List<SysPrivilegeGroup> selectByUserType(@Param(value="userType") Integer userType);
	
	List<SysPrivilegeGroup> selectByIsRoleDis();
	
	List<SysPrivilegeGroup> selectByItemIds(@Param(value="ids") String ids);
	
	List<SysPrivilegeGroup> selectByUid(@Param(value="userId") Long userId);
	
	List<SysPrivilegeGroup>  selectBySuperuser();
}