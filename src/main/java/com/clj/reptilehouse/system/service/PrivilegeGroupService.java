package com.clj.reptilehouse.system.service;

import java.util.Iterator;
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
import com.clj.reptilehouse.common.tree.TreeService;
import com.clj.reptilehouse.common.tree.TreeUtils;
import com.clj.reptilehouse.common.util.IdWorker;
import com.clj.reptilehouse.common.util.JsonUtil;
import com.clj.reptilehouse.system.dao.SysPrivilegeGroupDao;
import com.clj.reptilehouse.system.entity.SysPrivilegeGroup;
import com.clj.reptilehouse.system.entity.SysPrivilegeItem;

/**
 * 系统权限组管理类
 * 
 * @author jxs
 */
//Spring Bean的标识.
@Service
//默认将类中的所有public函数纳入事务管理.
@Transactional
public class PrivilegeGroupService extends TreeService<SysPrivilegeGroup>{
	private static Logger logger = LoggerFactory.getLogger(PrivilegeGroupService.class);

	private static String objAttNames = "id,name,remark,status,treeCode,parentId";
	private static String jsonAttNames = "id,name,remark,status,treeCode,parentId";
	private static String childAttName = "children";

	private static int treeLevelLength = 3;
	
	public String lastMassage = "";

	@Autowired
	private SysPrivilegeGroupDao sysPrivilegeGroupDao;
	@Autowired
	private PrivilegeItemService privilegeItemService;
	
	@Autowired
	private RoleService roleService;
	
	@Transactional(readOnly = true)
	public String getJsonObjStr(final SysPrivilegeGroup obj) {
		return JsonUtil.getJsonObjInfo(obj, objAttNames, jsonAttNames);
	}

	@Transactional(readOnly = true)
	public String getJsonListStr(final List<SysPrivilegeGroup> list) {
		return JsonUtil.getJsonListInfo(list, objAttNames, jsonAttNames);
	}
	
	@Transactional(readOnly = true)
	public String getJsonTreeStr(final List<SysPrivilegeGroup> list) {
		return JsonUtil.getJsonTreeInfo(list, objAttNames, jsonAttNames, childAttName);
	}

	@Transactional(readOnly = true)
	public String getJsonPageStr(final Page<SysPrivilegeGroup> page) {
		return JsonUtil.getJsonPageInfo(page, objAttNames, jsonAttNames, childAttName);
	}

	@Transactional(readOnly = true)
	public List<SysPrivilegeGroup> getAllPrivGroup() {
		QueryBuilder qb =QueryBuilder.custom()
				.withSortName("tree_code", SortDirection.ASC);
		return sysPrivilegeGroupDao.list(qb.build());
	}

	@Transactional(readOnly = true)
	public List<SysPrivilegeGroup> getAllUserPrivGroup(Integer userType) {
		List<SysPrivilegeGroup> list = sysPrivilegeGroupDao.selectByUserType(userType);
		keepTreeList(list, 1, treeLevelLength);
		return list;
	}

	@Transactional(readOnly = true)
	public List<SysPrivilegeGroup> getAllRolePrivGroup() {
		List<SysPrivilegeGroup> list = sysPrivilegeGroupDao.selectByIsRoleDis();
		//keepTreeList(list, 1, treeLevelLength);
		return list;
	}
	
	@Transactional(readOnly = true)
	public List<SysPrivilegeGroup> searchPrivGroup(final List<PropertyFilter> filters) {
		return null;////PrivGrpDao.find(filters);
	}
	
	@Transactional(readOnly = true)
	public Page<SysPrivilegeGroup> searchPrivGroup(final PageRequest page, final List<PropertyFilter> filters) {
		if( page!=null && page.getOrderBy()!=null ){
			String realOrderBy = JsonUtil.getObjAttName(page.getOrderBy(), objAttNames, jsonAttNames );
			page.setOrderBy(realOrderBy);
		}
		return null;////PrivGrpDao.findPage(page, filters);
	}

	@Transactional(readOnly = true)
	public SysPrivilegeGroup getPrivGroup(Long id) {
		return sysPrivilegeGroupDao.selectByPrimaryKey(id);
	}
	
	public List<SysPrivilegeGroup> getGrpsByUid(Long userId){
		List<SysPrivilegeGroup> rlist=sysPrivilegeGroupDao.selectByUid(userId);
		return rlist;
	}
	
	public List<SysPrivilegeGroup> getGrpsBySuperuser(){
		List<SysPrivilegeGroup> rlist=sysPrivilegeGroupDao.selectBySuperuser();
		return rlist;
	}
	public void addPrivGroup(SysPrivilegeGroup privGrp) {
		logger.debug("savePowGrp...");
		long now =System.currentTimeMillis();
		privGrp.setId(IdWorker.nextId());
		privGrp.setCreatedTime(now);
		privGrp.setModifiedTime(now);
		String treeCode =getNewChildCode(privGrp.getParentId(), treeLevelLength);
		privGrp.setTreeCode(treeCode);
		sysPrivilegeGroupDao.insert(privGrp);
	}

	public void savePrivGroup(SysPrivilegeGroup privGrp) {
		logger.debug("savePowGrp...");
		String treeCode = getTreeCodeById(privGrp.getId());
		if( treeCode == null ){
			treeCode = getNewChildCode(privGrp.getParentId(), treeLevelLength);
		}
		privGrp.setModifiedTime(System.currentTimeMillis());
		privGrp.setTreeCode(treeCode);
		sysPrivilegeGroupDao.updateByPrimaryKeySelective(privGrp);
	}

	public void deletePrivGroup(Long id) {
		logger.debug("deletePrivGroup(id:{})...", id);
		SysPrivilegeGroup privGrp = sysPrivilegeGroupDao.selectByPrimaryKey(id);
		if( privGrp == null ){
			return;
		}
		List<SysPrivilegeGroup> list = findNextTrees(privGrp.getTreeCode(), treeLevelLength);

		deleteTree(privGrp.getTreeCode());
		
		int levelLength = privGrp.getTreeCode().length();
		for (SysPrivilegeGroup obj : list)
		{
			String adjustCode = TreeUtils.getLevelLastTreeCode(obj.getTreeCode(), levelLength);
			obj.setTreeCode(adjustCode);
			sysPrivilegeGroupDao.updateByPrimaryKeySelective(obj);
		}
	}

	public void deletePrivGroup(String[] ids) {
		for(String id:ids){
			deletePrivGroup(Long.valueOf(id));
		}
	}
	
	@Transactional(readOnly = true)
	public List<SysPrivilegeGroup> findMoveTrees(SysPrivilegeGroup privGrp) {
		return findOtherTrees(privGrp.getTreeCode());
	}

	public boolean movePrivGroup(Long moveId, Long targetId, int moveType){
		if( moveNode(moveId, targetId, moveType, treeLevelLength) )
		{
			this.lastMassage = "移动权限组或目标权限组已经不存在!";
			return false;
		}
		
		return true;
	}
	
	@Transactional(readOnly = true)
	public String getJsonMenuPrivChk(final List<SysPrivilegeGroup> listNode, List<SysPrivilegeItem> setMapPriv) {
		String lastTreeCode = "";
		String curTreeCode = "";
		int nChild = 0;
		int nodeNum = listNode.size();
		
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		for (int i = 0; i < nodeNum; i++)
		{
			SysPrivilegeGroup group = listNode.get(i);
			if( group.getStatus() == 0 ){
				continue;
			}
			curTreeCode = group.getTreeCode();
			if( i>0 ){
				if( curTreeCode.length()>lastTreeCode.length()){
					sb.append(", \"").append(childAttName).append("\": [ ");
					nChild++;
				}
				else if( curTreeCode.length()<lastTreeCode.length() ){
					int nLevel = (lastTreeCode.length()-curTreeCode.length())/treeLevelLength;
					for( int k=0; k<nLevel; k++ ){
						sb.append("} ]");
						nChild--;
					}
					sb.append(" }, ");
				}
				else{
					sb.append(" }, ");
				}
			}
			List<SysPrivilegeItem> listpriv=group.getItemList();
			sb.append("{ \"name\":\"").append(group.getName()).append("\"");
			sb.append(", \"grpChk\":\"<input type='checkbox' id='")
			.append(group.getTreeCode()).append("' name='chkGrp' onclick='chkGroup(this)' >\"");
			 sb.append(", \"grpPrivChk\":\"").append(getMenuGrpPrivChk(listpriv,setMapPriv,curTreeCode)).append("\"");
			lastTreeCode = curTreeCode;			
		}
		while (nChild > 0) {
			sb.append(" } ]");
			nChild--;
		}
		if( nodeNum>0 ){
			sb.append(" }");			
		}	
		sb.append(" ]");
		
		return sb.toString();
	}
	
	@Transactional(readOnly = true)
	private String getMenuGrpPrivChk(final List<SysPrivilegeItem> setGrpPriv, List<SysPrivilegeItem> setChkPriv,String treeCode) {
		StringBuilder sb = new StringBuilder();
		Iterator<SysPrivilegeItem> it = setGrpPriv.iterator();
		while (it.hasNext()){ 
			SysPrivilegeItem privilege = it.next();
			sb.append(getPrivCheck(privilege, setChkPriv,treeCode));
		}
		return sb.toString();
	}
	
	@Transactional(readOnly = true)
	private String getPrivCheck(final SysPrivilegeItem privilege,List<SysPrivilegeItem> setChkPriv,String treeCode) {
		StringBuilder sb = new StringBuilder();
		sb.append("<input type='checkbox' group='")
			.append(treeCode)
			.append("' name='chkPriv' value='")
			.append(privilege.getId()).append("' ");
		if (setChkPriv!=null&&setChkPriv.contains(privilege)) {
			sb.append("checked ");
		}
		sb.append(">").append(privilege.getName()).append("　");
		return sb.toString();
	}

	
	@Transactional(readOnly = true)
	public String getJsonUserPowChk(final List<SysPrivilegeGroup> listNode, List<SysPrivilegeItem> setMapPriv, Integer userType) {
		String lastTreeCode = "";
		String curTreeCode = "";
		int nChild = 0;
		int nodeNum = listNode.size();
		
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		for (int i = 0; i < nodeNum; i++)
		{
			SysPrivilegeGroup group = listNode.get(i);
			if( group.getStatus() == 0 ){
				continue;
			}
			curTreeCode = group.getTreeCode();
			if( i>0 ){
				if( curTreeCode.length()>lastTreeCode.length()){
					sb.append(", \"").append(childAttName).append("\": [ ");
					nChild++;
				}
				else if( curTreeCode.length()<lastTreeCode.length() ){
					int nLevel = (lastTreeCode.length()-curTreeCode.length())/treeLevelLength;
					for( int k=0; k<nLevel; k++ ){
						sb.append("} ]");
						nChild--;
					}
					sb.append(" }, ");
				}
				else{
					sb.append(" }, ");
				}
			}
			List<SysPrivilegeItem> listpriv=group.getItemList();
			sb.append("{ \"grpName\":\"").append(group.getName()).append("\"");
			sb.append(", \"grpChk\":\"<input type='checkbox' id='")
			.append(group.getTreeCode()).append("' name='chkGrp' onclick='chkGroup(this)' >\"");
			sb.append(", \"grpPrivChk\":\"").append(getUserGrpPrivChk(listpriv, setMapPriv, userType)).append("\"");
			lastTreeCode = curTreeCode;			
		}
		while (nChild > 0) {
			sb.append(" } ]");
			nChild--;
		}
		if( nodeNum>0 ){
			sb.append(" }");			
		}	
		sb.append(" ]");
		
		return sb.toString();
	}
	
	@Transactional(readOnly = true)
	private String getUserGrpPrivChk(final List<SysPrivilegeItem> setGrpPriv, List<SysPrivilegeItem> setChkPriv, Integer userType) {
		StringBuilder sb = new StringBuilder();
		Iterator<SysPrivilegeItem> it = setGrpPriv.iterator();
		while (it.hasNext()){ 
			SysPrivilegeItem privilege = it.next();
			if( (userType==Global.USER_TYPE_DEV && privilege.getIsDv())
					|| (userType!=Global.USER_TYPE_DEV && privilege.getIsBs()) ){
				sb.append(getPrivCheck(privilege, setChkPriv,""));
			}
		}
		return sb.toString();
	}
	
	@Transactional(readOnly = true)
	public String getJsonRolePrivChk(final List<SysPrivilegeGroup> listNode, List<SysPrivilegeItem> setMapPriv) {
		String lastTreeCode = "";
		String curTreeCode = "";
		int nChild = 0;
		int nodeNum = listNode.size();
		
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		for (int i = 0; i < nodeNum; i++)
		{
			SysPrivilegeGroup group = listNode.get(i);
			if( group.getStatus() == 0 ){
				continue;
			}
			curTreeCode = group.getTreeCode();
			if( i>0 ){
				if( curTreeCode.length()>lastTreeCode.length()){
					sb.append(", \"").append(childAttName).append("\": [ ");
					nChild++;
				}
				else if( curTreeCode.length()<lastTreeCode.length() ){
					int nLevel = (lastTreeCode.length()-curTreeCode.length())/treeLevelLength;
					for( int k=0; k<nLevel; k++ ){
						sb.append("} ]");
						nChild--;
					}
					sb.append(" }, ");
				}
				else{
					sb.append(" }, ");
				}
			}
			List<SysPrivilegeItem> listpriv=group.getItemList();
			sb.append("{ \"grpName\":\"").append(group.getName()).append("\"");
			sb.append(", \"grpChk\":\"<input type='checkbox' id='")
			.append(group.getTreeCode()).append("' name='chkGrp' onclick='chkGroup(this)' >\"");
			sb.append(", \"grpPrivChk\":\"").append(getRoleGrpPrivChk(listpriv,setMapPriv,curTreeCode)).append("\"");
			lastTreeCode = curTreeCode;			
		}
		while (nChild > 0) {
			sb.append(" } ]");
			nChild--;
		}
		if( nodeNum>0 ){
			sb.append(" }");			
		}	
		sb.append(" ]");
		
		return sb.toString();
	}
	
	@Transactional(readOnly = true)
	private String getRoleGrpPrivChk(final List<SysPrivilegeItem> setGrpPriv,List<SysPrivilegeItem> setChkPriv,String treeCode) {
		StringBuilder sb = new StringBuilder();
		Iterator<SysPrivilegeItem> it = setGrpPriv.iterator();
		while (it.hasNext()){ 
			SysPrivilegeItem privilege = it.next();
			if( privilege.getIsRoleDis() ){
				sb.append(getPrivCheck(privilege,setChkPriv,treeCode));
			}
		}
		return sb.toString();
	}
}
