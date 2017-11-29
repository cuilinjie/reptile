package com.clj.reptilehouse.system.dao;

import org.apache.ibatis.annotations.Param;

import com.clj.reptilehouse.common.AbstractDao;
import com.clj.reptilehouse.system.entity.SysParamGroup;

public interface SysParamGroupDao extends AbstractDao<SysParamGroup>{
	Integer selectMaxNo();
	
	void updateNo(@Param(value="no") Integer no);
}