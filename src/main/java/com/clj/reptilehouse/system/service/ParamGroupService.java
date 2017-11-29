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
import com.clj.reptilehouse.common.util.IdWorker;
import com.clj.reptilehouse.common.util.JsonUtil;
import com.clj.reptilehouse.system.dao.SysParamGroupDao;
import com.clj.reptilehouse.system.dao.SysParamItemDao;
import com.clj.reptilehouse.system.entity.SysParamGroup;
import com.clj.reptilehouse.system.service.ParamGroupService;


/**
 * 系统参数组管理类
 * 
 * @author jxs
 */
//Spring Bean的标识.
@Service
//默认将类中的所有public函数纳入事务管理.
@Transactional
public class ParamGroupService {
	private static Logger logger = LoggerFactory.getLogger(ParamGroupService.class);
	
	private static String objAttNames = "id,no,name,code,status@paramGrpStatus,remark";
	private static String jsonAttNames = "id,no,name,code,status,remark";

	@Autowired
	private SysParamGroupDao sysParamGroupDao;
	@Autowired
	private SysParamItemDao sysParamItemDao;

	@Transactional(readOnly = true)
	public String getJsonObjStr(final SysParamGroup obj) {
		return JsonUtil.getJsonObjInfo(obj, objAttNames, jsonAttNames);
	}

	@Transactional(readOnly = true)
	public String getJsonListStr(final List<SysParamGroup> list) {
		return JsonUtil.getJsonListInfo(list, objAttNames, jsonAttNames);
	}

	@Transactional(readOnly = true)
	public String getJsonPageStr(final Page<SysParamGroup> page) {
		return JsonUtil.getJsonPageInfo(page, objAttNames, jsonAttNames);
	}

	@Transactional(readOnly = true)
	public List<SysParamGroup> getAllParamGroup() {
		return sysParamGroupDao.list("1=1 order by `no`");
	}
	
	@Transactional(readOnly = true)
	public List<SysParamGroup> getBusParamGroup() {
		String query=QueryBuilder.custom().andEquivalent("status",1).withSortName("no",SortDirection.ASC).build();
		return sysParamGroupDao.list(query);
	}
	
	@Transactional(readOnly = true)
	public List<SysParamGroup> searchParamGroup(final List<PropertyFilter> filters) {
		return null;////paramGroupDao.find(filters);
	}
	
	@Transactional(readOnly = true)
	public Page<SysParamGroup> searchParamGroup(final PageRequest page, final List<PropertyFilter> filters) {
		if( page!=null && page.getOrderBy()!=null ){
			String realOrderBy = JsonUtil.getObjAttName(page.getOrderBy(), objAttNames, jsonAttNames );
			page.setOrderBy(realOrderBy);
		}
		return null;////paramGroupDao.findPage(page, filters);
	}

	@Transactional(readOnly = true)
	public SysParamGroup getParamGroup(Long id) {
		return sysParamGroupDao.selectByPrimaryKey(id);
	}
	
	@Transactional(readOnly = true)
	public SysParamGroup getParamGroupByCode(String code) {
		String query =QueryBuilder.custom().andEquivalent("code",code).build();
		List<SysParamGroup> list=sysParamGroupDao.list(query);
		if(list!=null&&!list.isEmpty()) return list.get(0);
		return null;
	}
	
	public void addParamGroup(SysParamGroup entity) {
		logger.debug("addParamGroup...");
		entity.setId(IdWorker.nextId());
		Integer number = sysParamGroupDao.selectMaxNo()+1;
		entity.setNo(number);
		sysParamGroupDao.insert(entity);
	}

	public void saveParamGroup(SysParamGroup entity) {
		logger.debug("saveParamGroup...");
		Integer number =entity.getNo();
		if( number == null ||number<0){
			number = sysParamGroupDao.selectMaxNo()+1;
		}
		entity.setNo(number);
		sysParamGroupDao.updateByPrimaryKeySelective(entity);
	}
	
	public void moveItem(SysParamGroup item, int movePos){
		String query =QueryBuilder.custom()
				.andEquivalent("no",item.getNo()+movePos).build();
		List<SysParamGroup> list=sysParamGroupDao.list(query);
		SysParamGroup itemFind = null;
		if(list!=null&&!list.isEmpty()){
			itemFind=list.get(0);
		}
		if (itemFind != null) {
			Integer number = itemFind.getNo();
			itemFind.setNo(item.getNo());
			item.setNo(number);
			sysParamGroupDao.updateByPrimaryKey(itemFind);
			sysParamGroupDao.updateByPrimaryKey(item);
		}		
	}

	public void moveUpItem(SysParamGroup item) {
		logger.debug("moveUpItem(name:{})...", item.getName());
		moveItem(item, -1);
	}

	public void moveDownItem(SysParamGroup item) {
		logger.debug("moveUpItem(name:{})...", item.getName());
		moveItem(item, 1);
	}

	public void deleteParamGroup(Long id) {
		SysParamGroup item = sysParamGroupDao.selectByPrimaryKey(id);
		Integer number = item.getNo();
		sysParamGroupDao.updateNo(number);
		sysParamItemDao.deleteByGrpId(id);
		sysParamGroupDao.deleteByPrimaryKey(id);
	}

	public void deleteParamGroup(String[] ids) {
		for(String id:ids){
			deleteParamGroup(Long.valueOf(id));
		}
	}

	@Transactional(readOnly = true)
	public boolean isGrpCodeUnique(Long grpId, String grpCode,String action) {
		QueryBuilder query =QueryBuilder.custom();
		query.andEquivalent("code",grpCode);
		List<SysParamGroup> list=sysParamGroupDao.list(query.build());
		if(list!=null&&!list.isEmpty()){
			if("add".equals(action)||("edit".equals(action)&&!list.get(0).getId().equals(grpId)))
			return false;
		}
		return  true;
	}
	
}
