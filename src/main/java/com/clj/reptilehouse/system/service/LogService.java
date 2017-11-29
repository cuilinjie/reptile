package com.clj.reptilehouse.system.service;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.PageRequest;
import org.springside.modules.orm.PropertyFilter;

import com.clj.reptilehouse.common.query.QueryBuilder;
import com.clj.reptilehouse.common.query.SortDirection;
import com.clj.reptilehouse.common.util.IdWorker;
import com.clj.reptilehouse.common.util.JsonUtil;
import com.clj.reptilehouse.system.dao.SysLogDao;
import com.clj.reptilehouse.system.entity.SysLog;
import com.clj.reptilehouse.system.service.LogService;


/**
 * 系统日志管理类
 * 
 * @author jxs
 */
//Spring Bean的标识.
@Service
//默认将类中的所有public函数纳入事务管理.
@Transactional
public class LogService {

	private static Logger logger = LoggerFactory.getLogger(LogService.class);

	private static String objAttNames = "id,title,content,level,flag,funcName,operateType,userId,operater,username,orgId,orgName,orgCode,clientIp,remark,createdTime,modifiedTime";
	private static String jsonAttNames = "id,title,content,level,flag,funcName,operateType,userId,operater,username,orgId,orgName,orgCode,clientIp,remark,createdTime,modifiedTime";

	@Autowired
	private SysLogDao sysLogDao;
	
	@Transactional(readOnly = true)
	public String getJsonObjStr(final SysLog obj) {
		return JsonUtil.getJsonObjInfo(obj, objAttNames, jsonAttNames);
	}

	@Transactional(readOnly = true)
	public String getJsonListStr(final List<SysLog> list) {
		return JsonUtil.getJsonListInfo(list, objAttNames, jsonAttNames);
	}

	@Transactional(readOnly = true)
	public String getJsonPageStr(final Page<SysLog> page) {
		return JsonUtil.getJsonPageInfo(page, objAttNames, jsonAttNames);
	}
	
	@Transactional(readOnly = true)
	public List<SysLog> getAllLog() {
		return sysLogDao.list("1=1 order by created_time desc");
	}
	
	@Transactional(readOnly = true)
	public List<SysLog> getAllUserLog(Long userId) {
		QueryBuilder qb=QueryBuilder.custom();
		qb.andEquivalent("user_id",userId);
		qb.withSortName("created_time", SortDirection.DESC);
		return sysLogDao.list(qb.build());
	}
	
	@Transactional(readOnly = true)
	public List<SysLog> searchLog(final List<PropertyFilter> filters) {
		return null;//find(filters);
	}
	
	@Transactional(readOnly = true)
	public Page<SysLog> searchLog(final PageRequest page, final List<PropertyFilter> filters) {
		if( page!=null && page.getOrderBy()!=null ){
			String realOrderBy = JsonUtil.getObjAttName(page.getOrderBy(), objAttNames, jsonAttNames );
			page.setOrderBy(realOrderBy);
		}
		return null;//findPage(page, filters);
	}
	
	@Transactional(readOnly = true)
	public SysLog getLog(Long id) {
		return sysLogDao.selectByPrimaryKey(id);
	}

	public void addLog(SysLog entity) {
		entity.setId(IdWorker.nextId());
		sysLogDao.insert(entity);
	}
	public void saveLog(SysLog entity) {
		logger.debug("saveLog(id:{})...", entity.getId());
		sysLogDao.updateByPrimaryKey(entity);
	}

	public void deleteLog(Long id) {
		logger.debug("deleteLog(id:{})...", id);
		sysLogDao.deleteByPrimaryKey(id);
	}

	public void deleteLog(String[] ids) {
		for(String id:ids){
			deleteLog(Long.valueOf(id));
		}
	}

	public void clearAllLog() {
		sysLogDao.deleteAll();
	}

	public void clearAllUserLog(Long userId) {
		String query=QueryBuilder.custom().andEquivalent("user_id",userId).build();
		sysLogDao.deleteBy(query);
	}

	public void clearLogByTime(Long clearTime) {
		String query=QueryBuilder.custom()
				.andLessThan("created_time", clearTime).build();
		sysLogDao.deleteBy(query);
	}

	public void clearUserLogByTime(Long userId, Timestamp clearTime) {
		String query=QueryBuilder.custom()
				.andLessThan("created_time", clearTime)
				.andEquivalent("user_id",userId).build();
		sysLogDao.deleteBy(query);
	}


}
