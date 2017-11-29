package com.clj.reptilehouse.system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.clj.reptilehouse.common.AbstractDao;
import com.clj.reptilehouse.common.SampleResItem;
import com.clj.reptilehouse.system.entity.SysResItem;

public interface SysResItemDao extends AbstractDao<SysResItem>{
	
	void deleteByGrpId(@Param(value="grpId") Long grpId);
	
	Integer selectMaxNoByGrpId(@Param(value="grpId") Long grpId);
	
	Integer updateNo(@Param(value="grpId") Long grpId,@Param(value="no") Integer no);
	
	String selectNameBy(@Param(value="grpCode") String grpCode,@Param(value="itemCode") String itemCode);
	
	String selectCodeBy(@Param(value="grpCode") String grpCode,@Param(value="name") String name);
	
	List<SampleResItem> listBy(String query);
	
	List<SampleResItem> listFrom(String query);
}