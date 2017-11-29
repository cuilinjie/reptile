package com.clj.reptilehouse.system.dao;

import com.clj.reptilehouse.common.AbstractDao;
import com.clj.reptilehouse.system.entity.SysLog;

public interface SysLogDao extends AbstractDao<SysLog>{
	
	void deleteAll();
	
	void deleteBy(String query);
}