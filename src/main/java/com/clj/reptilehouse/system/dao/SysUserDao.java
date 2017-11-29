package com.clj.reptilehouse.system.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.clj.reptilehouse.common.AbstractDao;
import com.clj.reptilehouse.system.entity.SysUser;

public interface SysUserDao extends AbstractDao<SysUser>{

	SysUser selectByLoginName(String loginName);

	List<SysUser> ListById(@Param(value = "id") long id);

	List<SysUser> selectMngUserBy(@SuppressWarnings("rawtypes") Map map);

	List<SysUser> selectMngUser(@Param(value = "treeCode") String treeCode,
			@Param(value = "type") Integer type);

	SysUser selectByLimits(Long id);
	
	List<SysUser> selectSubUsersByUid(@Param(value = "userId") long userId);
	
	List<SysUser> selectSubUsersByOid(@Param(value = "orgId") long orgId);
	
	int insertBatch(List<SysUser> list);
	
	int updateBatch(List<SysUser> list);
	
	int membersMoveById(@Param("orgId") Long orgId,@Param("ids") String ids);
	
	int membersMoveByOrgId(@Param("targetOrgId") Long targetOrgId,@Param("originOrgId") Long originOrgId);
	
}