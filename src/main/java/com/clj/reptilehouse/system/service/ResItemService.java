package com.clj.reptilehouse.system.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;

import com.clj.reptilehouse.common.Global;
import com.clj.reptilehouse.common.SampleResItem;
import com.clj.reptilehouse.common.query.QueryBuilder;
import com.clj.reptilehouse.common.query.SortDirection;
import com.clj.reptilehouse.common.tree.TreeService;
import com.clj.reptilehouse.common.tree.TreeUtils;
import com.clj.reptilehouse.common.util.IdWorker;
import com.clj.reptilehouse.common.util.JsonUtil;
import com.clj.reptilehouse.system.dao.SysResGroupDao;
import com.clj.reptilehouse.system.dao.SysResItemDao;
import com.clj.reptilehouse.system.entity.SysResGroup;
import com.clj.reptilehouse.system.entity.SysResItem;
import com.clj.reptilehouse.system.service.ResItemService;

/**
 * 系统资源管理类
 * 
 * @author jxs
 */
//Spring Bean的标识.
@Service
//默认将类中的所有public函数纳入事务管理.
@Transactional
public class ResItemService extends TreeService<SysResItem>{
	private static Logger logger = LoggerFactory.getLogger(ResItemService.class);

	private static String objAttNames = "id,name,code,no,remark,status@resItemStatus,treeCode";
	private static String jsonAttNames = "id,name,code,no,remark,status,treeCode";
	private static String objAttNames1 = "code,name";
	private static String jsonAttNames1 = "id,text";
	private static String childAttName = "children";

	private static int treeLevelLength = 3;
	
	public String lastMassage = "";
	
	@Autowired
	private SysResGroupDao sysResGroupDao;

	@Autowired
	private SysResItemDao sysResItemDao;

	@Transactional(readOnly = true)
	public String getJsonObjStr(final SysResItem obj) {
		return JsonUtil.getJsonObjInfo(obj, objAttNames, jsonAttNames);
	}
	
	@Transactional(readOnly = true)
	public String getJsonObjStr(final SysResItem obj, int type) {
		if( type == 1 ){
			return  JsonUtil.getJsonObjInfo(obj, objAttNames1, jsonAttNames1);			
		}
		return JsonUtil.getJsonObjInfo(obj, objAttNames, jsonAttNames);
	}

	@Transactional(readOnly = true)
	public String getJsonListStr(final List<SysResItem> list) {
		return JsonUtil.getJsonListInfo(list, objAttNames, jsonAttNames);
	}

	@Transactional(readOnly = true)
	public String getJsonPageStr(final Page<SysResItem> page) {
		return JsonUtil.getJsonPageInfo(page, objAttNames, jsonAttNames);
	}
	
	@Transactional(readOnly = true)
	public String getJsonListStr(final List<SysResItem> list, int type) {
		if( type == 1 ){
			return JsonUtil.getJsonListInfo(list, objAttNames1, jsonAttNames1);
		}
		return JsonUtil.getJsonListInfo(list, objAttNames, jsonAttNames);
	}
	
	@Transactional(readOnly = true)
	public String getJsonTreeStr(final List<SysResItem> list) {
		return JsonUtil.getJsonTreeInfo(list, objAttNames, jsonAttNames, childAttName);
	}
	
	@Transactional(readOnly = true)
	public String getJsonTreeStr(final List<SysResItem> list, int type) {
		if( type == 1 ){
			return JsonUtil.getJsonTreeInfo(list, objAttNames1, jsonAttNames1, childAttName);
		}
		return  JsonUtil.getJsonTreeInfo(list, objAttNames, jsonAttNames, childAttName);
	}
	
	@Transactional(readOnly = true)
	public SysResItem getResItem(Long id) {
		return  sysResItemDao.selectByPrimaryKey(id);
	}
	
	@Transactional(readOnly = true)
	public boolean isItemCodeUnique(Long grpId,Long resId, String resCode,String action) {
		String query =QueryBuilder.custom()
				.andEquivalent("grp_id",grpId)
				.andEquivalent("code",resCode).build();
		List<SysResItem> list=sysResItemDao.list(query);
		if(list!=null&&!list.isEmpty()){
			if("add".equals(action)||("edit".equals(action)&&list.get(0).getId().longValue()!=resId.longValue()))
			return  false;
		}
		return  true;
	}

	public void addResItem(SysResItem entity) {
		logger.debug("addResItem...");
		entity.setId(IdWorker.nextId());
		Long grpId = entity.getGrpId();
		SysResGroup grp=sysResGroupDao.selectByPrimaryKey(grpId);
		if( grp!=null&&grp.getType()==Global.GROUP_TYPE_LIST ){
			Integer number =sysResItemDao.selectMaxNoByGrpId(grpId);
			if(number!=null){
				number++;
			}else{
				number=0;
			}
			entity.setNo(number);		
		}
		else if(grp!=null&&grp.getType()==Global.GROUP_TYPE_TREE ){
			String treeCode = getNewChildCode(grpId, entity.getParentId(), treeLevelLength);
			entity.setTreeCode(treeCode);
		}
		sysResItemDao.insert(entity);
	}
	
	public void saveResItem(SysResItem entity) {
		logger.debug("saveResItem...");
		Long grpId = entity.getGrpId();
		SysResGroup grp=sysResGroupDao.selectByPrimaryKey(grpId);
		if( grp!=null&&grp.getType()==Global.GROUP_TYPE_LIST ){
			SysResItem item =sysResItemDao.selectByPrimaryKey(entity.getId());
			Integer number=item.getNo();
			if(number==null||number<1){
				number = sysResItemDao.selectMaxNoByGrpId(grpId)+1;
			}
			entity.setNo(number);
		}else if( grp!=null&&grp.getType()==Global.GROUP_TYPE_TREE ){
			String treeCode =getTreeCodeById(entity.getId());
			if( treeCode == null ){
				treeCode = getNewChildCode(grpId, entity.getParentId(), treeLevelLength);
			}
			entity.setTreeCode(treeCode);
		}

		sysResItemDao.updateByPrimaryKeySelective(entity);
	}

	public void deleteResItem(Long id) {
		SysResItem item =sysResItemDao.selectByPrimaryKey(id);
		if( item != null ){
			deleteResItem(item);
		}
	}

	public void deleteResItem(String[] ids) {
		for(String id:ids){
			deleteResItem(Long.valueOf(id));
		}
	}

	public void deleteResItem(SysResItem item) {
		logger.debug("deleteResItem(name:{})...", item.getName());
		Long grpId = item.getGrpId();
		SysResGroup grp=sysResGroupDao.selectByPrimaryKey(grpId);
		if( grp!=null&&grp.getType()==Global.GROUP_TYPE_LIST ){
			sysResItemDao.updateNo(grpId, item.getNo());
			sysResItemDao.deleteByPrimaryKey(item.getId());
		}
		else if( grp!=null&&grp.getType()==Global.GROUP_TYPE_TREE ){
			List<SysResItem> list =findNextTrees(grpId, item.getTreeCode(), treeLevelLength);
			deleteTree(grpId, item.getTreeCode());	
			int levelLength = item.getTreeCode().length();
			for (SysResItem obj : list)
			{
				String adjustCode = TreeUtils.getLevelLastTreeCode(obj.getTreeCode(), levelLength);
				obj.setTreeCode(adjustCode);
				sysResItemDao.updateByPrimaryKeySelective(obj);
			}
		}
	}
	
	public void moveItem(SysResItem item, int movePos) {
		String query=QueryBuilder.custom()
				.andEquivalent("grp_id", item.getGrpId())
				.andEquivalent("no", item.getNo()+movePos)
				.build();
		List<SysResItem> list=sysResItemDao.list(query);
		if(list!=null&&!list.isEmpty()){
			SysResItem itemFind=list.get(0);
			Integer number = itemFind.getNo();
			itemFind.setNo(item.getNo());
			item.setNo(number);
			sysResItemDao.updateByPrimaryKey(itemFind);
			sysResItemDao.updateByPrimaryKey(item);
		}
	}
	
	public void moveUpItem(SysResItem item) {
		logger.debug("moveUpItem(name:{})...", item.getName());
		moveItem(item, -1);
	}
	
	public void moveDownItem(SysResItem item) {
		logger.debug("moveDownItem(name:{})...", item.getName());
		moveItem(item, 1);
	}
	
	@Transactional(readOnly = true)
	public List<SysResItem> findMoveTrees(SysResItem item) {
		return  findOtherTrees(item.getGrpId(), item.getTreeCode());
	}

	@Transactional(readOnly = true)
	public List<SysResItem> getGroupResItem(Long grpId) {
		SysResGroup group =sysResGroupDao.selectByPrimaryKey(grpId);
		if( group == null ){
			return  null;
		}
		List<SysResItem> list = null;
		if( group.getType() == Global.GROUP_TYPE_LIST ){
			String query=QueryBuilder.custom().andEquivalent("grp_id",grpId)
					.withSortName("no",SortDirection.ASC).build();
			list = sysResItemDao.list(query);
		}
		else if( group.getType() == Global.GROUP_TYPE_TREE ){
			String query=QueryBuilder.custom().andEquivalent("grp_id",grpId)
					.withSortName("tree_code",SortDirection.ASC).build();
			list = sysResItemDao.list(query);
		}
		return  list;
	}
	
	@Transactional(readOnly = true)
	public String findResContent(String groupCode, String resCode)
	{
		return sysResItemDao.selectNameBy(groupCode, resCode);
	}
	
	@Transactional(readOnly = true)
	public String findResCode(String groupCode, String resValue)
	{
		return sysResItemDao.selectCodeBy(groupCode, resValue);
	}

	@Transactional(readOnly = true)
	public List<SampleResItem> findSampleResItems(String grpCode, String filter)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("g.code='");
		sb.append(grpCode).append("' ");
		if (StringUtils.isNotBlank(filter))
		{
			sb.append(" and (").append(filter).append(")");
		}
		sb.append(" order by i.no");
		String hql = sb.toString();
		return sysResItemDao.listBy(hql); 
	}
	
	@Transactional(readOnly = true)
	public String getResJsonList(String grpId, String filter, String jsonCols)
	{
		String cols[] = jsonCols.split(",");
		Validate.isTrue(cols.length>=2);
		List<SampleResItem> list = findSampleResItems(grpId, filter);
		return  JsonUtil.getJsonListInfo(list, "value,name", jsonCols);
	}
	
	@Transactional(readOnly = true)
	public String getResJsonTree(String grpId, String filter, String jsonCols, String childName)
	{
		String cols[] = jsonCols.split(",");
		Validate.isTrue(cols.length>=2);
		List<SampleResItem> list = findSampleResItems(grpId, filter);
		return JsonUtil.getJsonTreeInfo(list,"value,name",jsonCols, childName);
	}
	
	@Transactional(readOnly = true)
	public String getObjJsonList(String objName, String filter, String objCols, String jsonCols)
	{
		/*String cols[] = jsonCols.split(",");
		Validate.isTrue(cols.length>0);
		String hql = "from "+objName;
		if( filter!=null && filter.length()>0 ){
			hql += " where "+filter;
		}
		List<Object> list = resItemDao.find(hql);
		return  JsonUtil.getJsonListInfo(list, objCols, jsonCols);*/
		return null;
	}
	
	@Transactional(readOnly = true)
	public String getObjJsonTree(String objName, String filter, String objCols, String jsonCols, String childName)
	{
		/*String cols[] = jsonCols.split(",");
		Validate.isTrue(cols.length>0);
		String hql = "from "+objName;
		if( filter!=null && filter.length()>0 ){
			hql += " where "+filter;
		}
		List<Object> list = resItemDao.find(hql);
		return JsonUtil.getJsonTreeInfo(list, objCols, jsonCols, childName);*/
		return null;
	}

	@Transactional(readOnly = true)
	public List<SampleResItem> findSampleResItems(String objName, String textAtt, String valueAtt, String filter, String order)
	{
		return  findSampleResItems(objName, textAtt, valueAtt, null, filter, order);
	}

	@Transactional(readOnly = true)
	public List<SampleResItem> findSampleResItems(String objName, String textAtt, String valueAtt, String treeCodeAtt, String filter, String order)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("select ").append(textAtt).append(" as name,").append(valueAtt).append(" as value ");
		if ( !StringUtils.isBlank(treeCodeAtt) ){
			sb.append(",").append(treeCodeAtt).append(" as treeCode ");
		}
		sb.append(" from ").append(objName);
		if ( !StringUtils.isBlank(filter) ){
			sb.append(" where ").append(filter);
		}
		if ( !StringUtils.isBlank(order)){
			sb.append(" order by ").append(order);
		}
		String hql = sb.toString();		
		List<SampleResItem> list =sysResItemDao.listFrom(hql);
		return list; 
	}
	
}
