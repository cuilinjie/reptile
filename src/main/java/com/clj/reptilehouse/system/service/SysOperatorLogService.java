package com.clj.reptilehouse.system.service;

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
import com.clj.reptilehouse.common.util.JsonUtil;
import com.clj.reptilehouse.common.util.SystemUtil;
import com.clj.reptilehouse.system.dao.SysOperatorLogDao;
import com.clj.reptilehouse.system.entity.SysOperatorLog;


@Service
@Transactional
public class SysOperatorLogService { 

	private static Logger logger = LoggerFactory.getLogger(SysOperatorLogService.class);

	
	
	@Autowired
	private SysOperatorLogDao sysOperatorLogDao;
	
	private static String objAttNames = "operatorTime,logType,operatorModel@operatorModel,operatorAction@operatorAction,operatorContext,operatorPersion,clientIpaddr,insertTime";
	private static String jsonAttNames = "operatorTime,logType,operatorModel,operatorAction,operatorContext,operatorPersion,clientIpaddr,insertTime";

	
	@Transactional(readOnly = true)
	public String getJsonObjStr(final SysOperatorLog obj) { 
		return JsonUtil.getJsonObjInfo(obj, objAttNames, jsonAttNames);
	}

	@Transactional(readOnly = true)
	public String getJsonListStr(final List<SysOperatorLog> list) {
		return JsonUtil.getJsonListInfo(list, objAttNames, jsonAttNames);
	}

	@Transactional(readOnly = true)
	public String getJsonPageStr(final Page<SysOperatorLog> page) {
		return JsonUtil.getJsonPageInfo(page, objAttNames, jsonAttNames);
	}
	
	@Transactional(readOnly = true)
	public List<SysOperatorLog> getAllLog() {
		return sysOperatorLogDao.list("1=1 order by operator_time desc");
	}
	
	@Transactional(readOnly = true)
	public List<SysOperatorLog> getAllUserLog(Long userId) {
		QueryBuilder qb=QueryBuilder.custom();
		qb.andEquivalent("user_id",userId);
		qb.withSortName("created_time", SortDirection.DESC);
		return sysOperatorLogDao.list(qb.build());
	}
	
	@Transactional(readOnly = true)
	public List<SysOperatorLog> searchLog(final List<PropertyFilter> filters) {
		String sql = SystemUtil.buildSQLCondition(filters);
		System.out.println(sql);
		return sysOperatorLogDao.list(sql);
	}
	
	@Transactional(readOnly = true)
	public Page<SysOperatorLog> searchLog(final PageRequest page, final List<PropertyFilter> filters) {
		if( page!=null && page.getOrderBy()!=null ){
			String realOrderBy = JsonUtil.getObjAttName(page.getOrderBy(), objAttNames, jsonAttNames );
			page.setOrderBy(realOrderBy);
		}
		return null;//findPage(page, filters);
	}
	
	@Transactional(readOnly = true)
	public SysOperatorLog getLog(Long id) {
		return sysOperatorLogDao.selectByPrimaryKey(id);
	}

	public void addLog(SysOperatorLog entity) {
		sysOperatorLogDao.insert(entity);
	}
	public void saveLog(SysOperatorLog entity) {
		logger.debug("saveLog(id:{})...");
		sysOperatorLogDao.updateByPrimaryKey(entity);
	}
	/**
	 * 
	 * @param operatorModel 操作管理模块,调用方法 LogConst.OperatorModel.
	 * @param operatorAction 操作对象【查询，新增，更新，删除，导出，导入】,调用方法 LogConst.OperatorAction.
	 * @param operatorContext 操作内容
	 * @param operatorPersion 操作人
	 * @param clientIpaddr 客户IP地址
	 */
	public void addOperatorLog(String operatorModel,int operatorAction,String operatorContext,String operatorPersion,String clientIpaddr){
		SysOperatorLog sysOperatorLog = new SysOperatorLog();
		sysOperatorLog.setOperatorTime(System.currentTimeMillis());
		sysOperatorLog.setOperatorModel(operatorModel);
		sysOperatorLog.setOperatorAction(operatorAction);
		sysOperatorLog.setOperatorPersion(operatorPersion);
		sysOperatorLog.setClientIpaddr(clientIpaddr);
		sysOperatorLog.setOperatorContext(operatorContext);
		sysOperatorLog.setInsertTime(System.currentTimeMillis());
		sysOperatorLogDao.insertSelective(sysOperatorLog);
	}
	
	
}
