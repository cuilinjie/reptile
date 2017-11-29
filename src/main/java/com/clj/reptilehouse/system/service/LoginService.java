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
import com.clj.reptilehouse.system.dao.SysLoginInfoDao;
import com.clj.reptilehouse.system.entity.SysLoginInfo;
import com.clj.reptilehouse.system.service.LoginService;


/**
 * 系统日志管理类
 * 
 * @author jxs
 */
//Spring Bean的标识.
@Service
//默认将类中的所有public函数纳入事务管理.
@Transactional
public class LoginService {

	private static Logger logger = LoggerFactory.getLogger(LoginService.class);

	@Autowired
	private SysLoginInfoDao sysLoginInfoDao;

	//-- Log Manager --//
	@Transactional(readOnly = true)
	public SysLoginInfo getInfo(Long id) {
		logger.debug("getInfo...");
		return sysLoginInfoDao.selectByPrimaryKey(id);
	}

	public void AddInfo(String loginName, String userName, String clientIp) {
		logger.debug("AddInfo...");
		SysLoginInfo info = null;
		String query =QueryBuilder.custom()
				.andEquivalent("login_name", loginName)
				.andEquivalent("client_ip", clientIp).build();
		List<SysLoginInfo> list=sysLoginInfoDao.list(query);
		if(list!=null&&!list.isEmpty()){
			info=list.get(0);
		}
		long now=System.currentTimeMillis();
		Timestamp stampnow=new Timestamp(now);
		if( info!=null ){
			Long num = info.getTotalLoginNo();
			info.setTotalLoginNo(num+1);
			info.setUsername(userName);
			info.setLastLoginTime(stampnow);
			info.setModifiedTime(now);
			sysLoginInfoDao.updateByPrimaryKeySelective(info);
		}else{
			info = new SysLoginInfo();
			info.setId(IdWorker.nextId());
			info.setLoginName(loginName);
			info.setUsername(userName);
			info.setClientIp(clientIp);
			info.setTotalLoginNo(1L);
			info.setLastLoginTime(stampnow);
			info.setCreatedTime(now);
			info.setModifiedTime(now);
			sysLoginInfoDao.insert(info);
		}
	}
	
	@Transactional(readOnly = true)
	public List<SysLoginInfo> getAllInfo() {
		logger.debug("getAllInfo...");
		String query =QueryBuilder.custom().withSortName("loginName",SortDirection.ASC).build();
		return sysLoginInfoDao.list(query);
	}

	@Transactional(readOnly = true)
	public Page<SysLoginInfo> searchInfo(final PageRequest page, final List<PropertyFilter> filters) {
		return null;//.findPage(page, filters);
	}

}
