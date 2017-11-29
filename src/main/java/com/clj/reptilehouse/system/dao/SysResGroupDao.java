package com.clj.reptilehouse.system.dao;

import com.clj.reptilehouse.common.AbstractDao;
import com.clj.reptilehouse.system.entity.SysResGroup;

public interface SysResGroupDao extends AbstractDao<SysResGroup>{
	Integer selectMaxNo();
}