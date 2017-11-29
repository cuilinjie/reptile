package com.clj.reptilehouse.system.dao;

import org.apache.ibatis.annotations.Param;

import com.clj.reptilehouse.common.AbstractDao;
import com.clj.reptilehouse.system.entity.SysParamItem;

public interface SysParamItemDao extends AbstractDao<SysParamItem>{
	Integer selectMaxNo(@Param(value="grpId") Long grpId);
	
	Integer updateNo(@Param(value="grpId") Long grpId,@Param(value="no") Integer no);
	
	void deleteByGrpId(@Param(value="grpId") Long grpId);
}