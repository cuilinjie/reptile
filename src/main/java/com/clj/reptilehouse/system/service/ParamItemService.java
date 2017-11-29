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
import com.clj.reptilehouse.system.dao.SysParamItemDao;
import com.clj.reptilehouse.system.entity.SysParamGroup;
import com.clj.reptilehouse.system.entity.SysParamItem;
import com.clj.reptilehouse.system.service.ParamItemService;


/**
 * 系统参数管理类
 * 
 * @author jxs
 */
//Spring Bean的标识.
@Service
//默认将类中的所有public函数纳入事务管理.
@Transactional
public class ParamItemService {
	private static Logger logger = LoggerFactory.getLogger(ParamItemService.class);
	
	private static String objAttNames = "id,name,code,no,valType@paramValueType,valText,remark";
	private static String jsonAttNames = "id,name,code,no,valType,valText,remark";

	@Autowired
	private SysParamItemDao sysParamItemDao;

	@Transactional(readOnly = true)
	public String getJsonObjStr(final SysParamItem obj) {
		return JsonUtil.getJsonObjInfo(obj, objAttNames, jsonAttNames);
	}

	@Transactional(readOnly = true)
	public String getJsonListStr(final List<SysParamItem> list) {
		return JsonUtil.getJsonListInfo(list, objAttNames, jsonAttNames);
	}

	@Transactional(readOnly = true)
	public String getJsonPageStr(final Page<SysParamItem> page) {
		return JsonUtil.getJsonPageInfo(page, objAttNames, jsonAttNames);
	}

	@Transactional(readOnly = true)
	public List<SysParamItem> getAllParamItem() {
		return sysParamItemDao.list("1=1 order by `no`");
	}
	
	@Transactional(readOnly = true)
	public List<SysParamItem> getGroupParamItem(Long grpId) {
		String query =QueryBuilder.custom().andEquivalent("grp_id",grpId).withSortName("no",SortDirection.ASC).build();
		return sysParamItemDao.list(query);
	}
	
	@Transactional(readOnly = true)
	public List<SysParamItem> getGroupParamItem(SysParamGroup group) {
		return getGroupParamItem(group.getId());
	}
	
	@Transactional(readOnly = true)
	public List<SysParamItem> searchParamItem(final List<PropertyFilter> filters) {
		return null;//paramDao.find(filters);
	}
	
	@Transactional(readOnly = true)
	public Page<SysParamItem> searchParamItem(final PageRequest page, final List<PropertyFilter> filters) {
		if( page!=null && page.getOrderBy()!=null ){
			String realOrderBy = JsonUtil.getObjAttName(page.getOrderBy(), objAttNames, jsonAttNames );
			page.setOrderBy(realOrderBy);
		}
		return null;//paramDao.findPage(page, filters);
	}

	@Transactional(readOnly = true)
	public SysParamItem getParamItem(Long id) {
		return sysParamItemDao.selectByPrimaryKey(id);
	}

	public void addParamItem(SysParamItem entity) {
		logger.debug("saveParamItem...");
		entity.setId(IdWorker.nextId());
		Integer number = sysParamItemDao.selectMaxNo(entity.getGrpId());
		if(number!=null){
			number++;
		}else{
			number=0;
		}
		entity.setNo(number);
		long now =System.currentTimeMillis();
		entity.setCreatedTime(now);
		entity.setModifiedTime(now);
		sysParamItemDao.insert(entity);
	}

	public void saveParamItem(SysParamItem entity) {
		logger.debug("saveParamItem...");
		Integer number =entity.getNo();
		if( number == null||number<1 ){
			number = sysParamItemDao.selectMaxNo(entity.getGrpId());
			if(number==null){
				number=1;
			}
		}
		entity.setNo(number);
		long now =System.currentTimeMillis();
		entity.setModifiedTime(now);
		sysParamItemDao.updateByPrimaryKey(entity);
	}
		
	public void moveItem(SysParamItem item, int movePos) {
		String query=QueryBuilder.custom()
				.andEquivalent("grp_id", item.getGrpId())
				.andEquivalent("no", item.getNo()+movePos)
				.build();
		List<SysParamItem> list=sysParamItemDao.list(query);
		if(list!=null&&!list.isEmpty()){
			SysParamItem itemFind=list.get(0);
			Integer number = itemFind.getNo();
			itemFind.setNo(item.getNo());
			item.setNo(number);
			sysParamItemDao.updateByPrimaryKey(itemFind);
			sysParamItemDao.updateByPrimaryKey(item);
		}
	}
	
	public void moveUpItem(SysParamItem item) {
		logger.debug("moveUpItem(name:{})...", item.getName());
		moveItem(item, -1);
	}
	
	public void moveDownItem(SysParamItem item) {
		logger.debug("moveDownItem(name:{})...", item.getName());
		moveItem(item, 1);
	}

	public void deleteParamItem(Long id) {
		SysParamItem item = getParamItem(id);
		Long grpId = item.getGrpId();
		Integer number = item.getNo();
		sysParamItemDao.updateNo(grpId, number);
		sysParamItemDao.deleteByPrimaryKey(id);
	}

	public void deleteParamItem(String[] ids) {
		for(String id:ids){
			deleteParamItem(Long.valueOf(id));
		}
	}
	
	@Transactional(readOnly = true)
	public boolean isParamCodeUnique(Long grpId,Long paramId, String paramCode,String action) {
		QueryBuilder qb=QueryBuilder.custom();
		qb.andEquivalent("code", paramCode);
		qb.andEquivalent("grp_id", grpId);
		List<SysParamItem> list=sysParamItemDao.list(qb.build());
		if(list!=null&&!list.isEmpty()){
			if("add".equals(action)||("edit".equals(action)&&list.get(0).getId().longValue()!=paramId.longValue()))
			return false;
		}
		return true;
	}
	
	@Transactional(readOnly = true)
	public String getParamValue(String paramCode) {
		String query=QueryBuilder.custom().andEquivalent("code",paramCode).build();
		List<SysParamItem> list=sysParamItemDao.list(query);
		if(list!=null&&!list.isEmpty() ){
			return list.get(0).getValText();
		}
		return "";
	}
	
	public boolean checkParam(String paramCode, String checkWay, String checkValue) throws RuntimeException
	{
		String query=QueryBuilder.custom().andEquivalent("code",paramCode).build();
		List<SysParamItem> list=sysParamItemDao.list(query);
		if(list!=null&&!list.isEmpty() ){
			SysParamItem item = list.get(0);
			if (Global.PARAM_TYPE_INT.equals(item.getValType())) {
				int v1 = Integer.parseInt(item.getValText());
				int v2 = Integer.parseInt(checkValue);
				if ("==".equals(checkWay)) {
					return v1 == v2;
				} else if (">=".equals(checkWay)) {
					return v1 >= v2;
				} else if (">".equals(checkWay)) {
					return v1 > v2;
				} else if ("<=".equals(checkWay)) {
					return v1 <= v2;
				} else if ("<".equals(checkWay)) {
					return v1 < v2;
				}
			} else if (Global.PARAM_TYPE_FLOAT.equals(item.getValType())) {
				double v1 = Double.parseDouble(item.getValText());
				double v2 = Double.parseDouble(checkValue);
				if ("==".equals(checkWay)) {
					return v1 == v2;
				} else if (">=".equals(checkWay)) {
					return v1 >= v2;
				} else if (">".equals(checkWay)) {
					return v1 > v2;
				} else if ("<=".equals(checkWay)) {
					return v1 <= v2;
				} else if ("<".equals(checkWay)) {
					return v1 < v2;
				}
			} else {
				String v1 = item.getValText();
				String v2 = checkValue;
				int result = v1.compareTo(v2);
				if ("==".equals(checkWay)) {
					return result == 0;
				} else if (">=".equals(checkWay)) {
					return result >= 0;
				} else if (">".equals(checkWay)) {
					return result > 0;
				} else if ("<=".equals(checkWay)) {
					return result <= 0;
				} else if ("<".equals(checkWay)) {
					return result < 0;
				}
			}
		}
		return false;
	}
}
