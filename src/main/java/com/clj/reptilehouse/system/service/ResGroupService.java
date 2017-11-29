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

import com.clj.reptilehouse.common.Global;
import com.clj.reptilehouse.common.query.QueryBuilder;
import com.clj.reptilehouse.common.query.SortDirection;
import com.clj.reptilehouse.common.util.IdWorker;
import com.clj.reptilehouse.common.util.JsonUtil;
import com.clj.reptilehouse.system.dao.SysResGroupDao;
import com.clj.reptilehouse.system.dao.SysResItemDao;
import com.clj.reptilehouse.system.entity.SysResGroup;
import com.clj.reptilehouse.system.entity.SysResItem;
import com.clj.reptilehouse.system.service.ResGroupService;

/**
 * 系统资源管理类
 * 
 * @author jxs
 */
//Spring Bean的标识.
@Service
//默认将类中的所有public函数纳入事务管理.
@Transactional
public class ResGroupService {
	private static Logger logger = LoggerFactory.getLogger(ResGroupService.class);
	
	private static String objAttNames = "id,name,code,status@resGroupState,type@resGroupType,itemType@resItemType,remark";
	private static String jsonAttNames = "id,name,code,status,type,itemType,remark";

	private static int treeLevelLength = 3;

	@Autowired
	private SysResGroupDao sysResGroupDao;
	
	@Autowired
	private SysResItemDao sysResItemDao;

	@Transactional(readOnly = true)
	public String getJsonObjStr(final SysResGroup obj) {
		return  JsonUtil.getJsonObjInfo(obj, objAttNames, jsonAttNames);
	}

	@Transactional(readOnly = true)
	public String getJsonListStr(final List<SysResGroup> list) {
		return  JsonUtil.getJsonListInfo(list, objAttNames, jsonAttNames);
	}

	@Transactional(readOnly = true)
	public String getJsonPageStr(final Page<SysResGroup> page) {
		return  JsonUtil.getJsonPageInfo(page, objAttNames, jsonAttNames);
	}

	@Transactional(readOnly = true)
	public List<SysResGroup> getAllResGroup() {
		return  sysResGroupDao.list("1=1 order by `no`");
	}

	@Transactional(readOnly = true)
	public List<SysResGroup> getBufResGroup() {
		String query =QueryBuilder.custom()
				.andEquivalent("status", 1)
				.withSortName("no", SortDirection.ASC).build();
		return  sysResGroupDao.list(query);
	}
	
	@Transactional(readOnly = true)
	public List<SysResGroup> searchResGroup(final List<PropertyFilter> filters) {
		return  null;// //resGroupDao.find(filters);
	}
	
	@Transactional(readOnly = true)
	public Page<SysResGroup> searchResGroup(final PageRequest page, final List<PropertyFilter> filters) {
		if( page!=null && page.getOrderBy()!=null ){
			String realOrderBy = JsonUtil.getObjAttName(page.getOrderBy(), objAttNames, jsonAttNames );
			page.setOrderBy(realOrderBy);
		}
		return  null;// //resGroupDao.findPage(page, filters);
	}

	@Transactional(readOnly = true)
	public SysResGroup getResGroup(Long id) {
		return  sysResGroupDao.selectByPrimaryKey(id);
	}

	@Transactional(readOnly = true)
	public SysResGroup getResGroupByCode(String grpCode) {
		String query =QueryBuilder.custom()
				.andEquivalent("code",grpCode).build();
		List<SysResGroup> list=sysResGroupDao.list(query);
		if(list!=null&&!list.isEmpty()){
			return list.get(0);
		}
		return  null;
	}
	
	public void addResGroup(SysResGroup entity) {
		logger.debug("addResGroup...");
		entity.setId(IdWorker.nextId());
		int number =sysResGroupDao.selectMaxNo()+1;
		entity.setNo(number);
		sysResGroupDao.insert(entity);
	}

	public void saveResGroup(SysResGroup entity) {
		logger.debug("saveResGroup...");
		SysResGroup oldGrp = sysResGroupDao.selectByPrimaryKey(entity.getId());
		Integer number=oldGrp.getNo();
		if( number<1 ){
			number =sysResGroupDao.selectMaxNo()+1;
		}
		entity.setNo(number);
		if( oldGrp!=null&&oldGrp.getType() !=entity.getType()){
			Long grpId = entity.getId();
			if( entity.getType() == Global.GROUP_TYPE_LIST ){
				String query=QueryBuilder.custom().andEquivalent("grp_id",grpId)
						.withSortName("no",SortDirection.ASC).build();
				List<SysResItem> list = sysResItemDao.list(query);
				for( int i=0; i<list.size(); i++ ){
					SysResItem item = list.get(i);
					item.setNo(i+1);
					sysResItemDao.updateByPrimaryKeySelective(item);
				}
			}
			else if( entity.getType() == Global.GROUP_TYPE_TREE ){
				String treeCodeFormat = String.format("%%0%dd", treeLevelLength);
				String query=QueryBuilder.custom().andEquivalent("grp_id",grpId)
						.withSortName("tree_code",SortDirection.ASC).build();
				List<SysResItem> list = sysResItemDao.list(query);
				for( int i=0; i<list.size(); i++ ){
					SysResItem item = list.get(i);
					item.setTreeCode(String.format(treeCodeFormat, i+1));
					sysResItemDao.updateByPrimaryKeySelective(item);
				}
			}
		}
		sysResGroupDao.updateByPrimaryKeySelective(entity);
	}
	
	public void moveItem(SysResGroup item, int movePos){
		String query =QueryBuilder.custom()
				.andEquivalent("no",item.getNo()+movePos).build();
		List<SysResGroup> list=sysResGroupDao.list(query);
		SysResGroup itemFind = null;
		if(list!=null&&!list.isEmpty()){
			itemFind=list.get(0);
		}
		if (itemFind != null) {
			Integer number = itemFind.getNo();
			itemFind.setNo(item.getNo());
			item.setNo(number);
			sysResGroupDao.updateByPrimaryKey(itemFind);
			sysResGroupDao.updateByPrimaryKey(item);
		}		
	}

	public void moveUpItem(SysResGroup item) {
		moveItem(item, -1);
	}

	public void moveDownItem(SysResGroup item) {
		moveItem(item, 1);
	}

	public void deleteResGroup(Long id) {
		sysResGroupDao.deleteByPrimaryKey(id);
		sysResItemDao.deleteByGrpId(id);
	}

	public void deleteResGroup(String[] ids) {
		for(String id:ids){
			deleteResGroup(Long.valueOf(id));
		}
	}
	
	@Transactional(readOnly = true)
	public boolean isGrpCodeUnique(Long grpId, String grpCode,String action) {
		QueryBuilder query =QueryBuilder.custom();
		query.andEquivalent("code",grpCode);
		List<SysResGroup> list=sysResGroupDao.list(query.build());
		if(list!=null&&!list.isEmpty()){
			if("add".equals(action)||("edit".equals(action)&&list.get(0).getId().longValue()!=grpId.longValue()))
			return false;
		}
		return  true;
	}
	
}
