package com.clj.reptilehouse.system.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;

import com.clj.reptilehouse.common.excel.ExcelObj;
import com.clj.reptilehouse.common.query.QueryBuilder;
import com.clj.reptilehouse.common.tree.TreeService;
import com.clj.reptilehouse.common.tree.TreeUtils;
import com.clj.reptilehouse.common.util.BeanUtil;
import com.clj.reptilehouse.common.util.IdWorker;
import com.clj.reptilehouse.common.util.JsonUtil;
import com.clj.reptilehouse.system.dao.SysOrganizationDao;
import com.clj.reptilehouse.system.dao.SysUserDao;
import com.clj.reptilehouse.system.entity.SysOrganization;
import com.clj.reptilehouse.system.entity.SysUser;
import com.clj.reptilehouse.system.service.OrganService;

/**
 * 系统机构管理类
 * 
 * @author jxs
 */
//Spring Bean的标识.
@Service
//默认将类中的所有public函数纳入事务管理.
@Transactional
public class OrganService extends TreeService<SysOrganization>{
	private static Logger logger = LoggerFactory.getLogger(OrganService.class);

	private static String objAttNames = "id,orgName,orgCode,status,treeCode,parentId";
	private static String jsonAttNames = "id,orgName,orgCode,status,treeCode,parentId";
	private static String objMinAttNames = "id,orgName";
	private static String jsonMinAttNames = "id,orgName";
	private static String childAttName = "children";
	private static int treeLevelLength = 3;
	
	/*private static String excelMdlName = "导入机构模板";
	private static String excelColNames = "上级机构名称,机构名称,机构编码,机构类型,机构电话,机构地址,邮政编码,机构说明";
	private static String excelAttNames = "parentId@OrgOrganization@orgId@orgName,orgName,orgCode,orgType@organType,phone,address,zipCode,description";
	private static int orgStateUp = 1;
	*/
	private String lastMassage = "";

	@Autowired
	private SysOrganizationDao sysOrganizationDao;
	@Autowired
	private SysUserDao sysUserDao;
	
	@Transactional(readOnly = true)
	public String getLastMassage() {
		return lastMassage;
	}

	@Transactional(readOnly = true)
	public String getJsonObjStr(final SysOrganization obj) {
		return JsonUtil.getJsonObjInfo(obj, objAttNames, jsonAttNames);
	}

	@Transactional(readOnly = true)
	public String getJsonListStr(final List<SysOrganization> list) {
		return JsonUtil.getJsonListInfo(list, objAttNames, jsonAttNames);
	}
	
	@Transactional(readOnly = true)
	public String getJsonTreeStr(final List<SysOrganization> list) {
		return JsonUtil.getJsonTreeInfo(list, objAttNames, jsonAttNames, childAttName);
	}

	@Transactional(readOnly = true)
	public String getJsonMinTreeStr(final List<SysOrganization> list) {
		return JsonUtil.getJsonTreeInfo(list, objMinAttNames, jsonMinAttNames, childAttName);
	}

	@Transactional(readOnly = true)
	public String getJsonPageStr(final Page<SysOrganization> page) {
		return JsonUtil.getJsonPageInfo(page, objAttNames, jsonAttNames, childAttName);
	}

	@Transactional(readOnly = true)
	public List<SysOrganization> getAllOrgan() {
		List<SysOrganization> list= sysOrganizationDao.list("1=1 order by tree_code");
		return list;
	}

	@Transactional(readOnly = true)
	public List<SysOrganization> findOrganByCode(String treeCode) {
		List<SysOrganization> list= findSubTree(treeCode);
		return list;
	}
	/**
	 * 获取根据treecode获取所辖机构id
	 * @param treeCode
	 */
	@Transactional(readOnly = true)
	public String getOrgIdsBy(String treeCode){
		StringBuffer sb=new StringBuffer();
		List<SysOrganization> list=findSubTree(treeCode);
		for(int i=0;i<list.size();i++){
			sb.append(list.get(i).getId());
			if(i!=(list.size()-1))sb.append(",");
		}
		return sb.toString();
	}
	
	@Transactional(readOnly = true)
	public SysOrganization getTopOrgan() {
		List<SysOrganization> list =sysOrganizationDao.list("1=1 order by tree_code");
		if( list != null && list.size()>0 ){
			return list.get(0);
		}
		return null;
	}

	@Transactional(readOnly = true)
	public SysOrganization getOrgan(Long id) {
		if( id==null ){
			return null;
		}
		SysOrganization org= sysOrganizationDao.selectByPrimaryKey(id);
		return org;
	}
	@Transactional(readOnly = true)
	public SysOrganization fetchOrg(Long id) {
		if( id==null ){
			return null;
		}
		SysOrganization org= sysOrganizationDao.selectByPrimaryKey(id);
		return org;
	}
	
	public List<SysOrganization> getVisibleGroupForMaterial(long uid){
		List<SysOrganization> list=new ArrayList<SysOrganization>();
		SysUser user=sysUserDao.selectByPrimaryKey(uid);
		if(user!=null){
			SysOrganization org=sysOrganizationDao.selectByPrimaryKey(user.getOrgId());
			if(1==user.getType()){
				List<SysOrganization> sublist=getSubOrgList(user.getOrgId());
				list.addAll(sublist);
			}
			list.add(org);
			SysOrganization porg=sysOrganizationDao.selectByPrimaryKey(org.getParentId());
			if(porg!=null){
				list.add(porg);
			}
		}
		return list;
	}
	
	@Transactional(readOnly = true)
	public SysOrganization getParOrg(Long id) {
		Long orgId=0L;
		SysOrganization parOrg =new SysOrganization();
		if( id==null ){
			return null;
		}
		SysOrganization Organ =getOrgan(id);
		if(Organ!=null)
			orgId=Organ.getParentId();
		if(orgId!=null&&orgId>0)
			parOrg =getOrgan(orgId);
		return parOrg;
	}
	
	@Transactional(readOnly = true)
	public SysOrganization getOrganByName(String name) {
		if( StringUtils.isBlank(name) ){
			return null;
		}
		String query =QueryBuilder.custom().andEquivalent("org_name",name).build();
		List<SysOrganization> list =sysOrganizationDao.list(query);
		if(list!=null&&!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
	
	@Transactional(readOnly = true)
	public List<SysOrganization> getSubOrgList(Long parentId) {
		if( parentId==null||parentId<1){
			return null;
		}
		String query =QueryBuilder.custom().andEquivalent("parent_id",parentId).build();
		return sysOrganizationDao.list(query);
	}
	
	@Transactional(readOnly = true)
	public List<Long> getSubOrgIds(Long parentId) {
		if( parentId==null||parentId<1){
			return null;
		}
		List<Long> listids =new ArrayList<Long>();
		listids.add(parentId);
		List<SysOrganization> orglist=getSubOrgList(parentId);
		if(orglist!=null&&!orglist.isEmpty()){
			for(SysOrganization org:orglist){
				listids.add(org.getId());
			}
		}
		return listids;
	}
	
	@Transactional(readOnly = true)
	public SysOrganization getOrganBy(String name, Long parentId) {
		if( StringUtils.isBlank(name) ){
			return null;
		}
		QueryBuilder qb=QueryBuilder.custom();
		qb.andEquivalent("org_name",name);
		if(parentId==null||parentId<1){
			qb.orLessThan("parent_id", 0);
		}else{
			qb.andEquivalent("parent_id",parentId);
		}
		System.out.println(qb.build());
		List<SysOrganization> list=sysOrganizationDao.list(qb.build());
		if(list!=null&&!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
	
	@Transactional(readOnly = true)
	public SysOrganization getOrganBy(String name) {
		if( StringUtils.isBlank(name) ){
			return null;
		}
		QueryBuilder qb=QueryBuilder.custom();
		qb.andEquivalent("org_name",name);
		List<SysOrganization> list=sysOrganizationDao.list(qb.build());
		if(list!=null&&!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
	
	@Transactional(readOnly = true)
	public boolean loadOrganAtts(SysOrganization entity, String attNames){
		return false;//loadObjAtts(entity, attNames);
	}
	
	public void addOrgan(SysOrganization organ) {
		logger.debug("saveOrgan...");
		organ.setId(IdWorker.nextId());
		String treeCode =getNewChildCode(organ.getParentId(), treeLevelLength);
		organ.setTreeCode(treeCode);
		sysOrganizationDao.insert(organ);
	}

	public void saveOrgan(SysOrganization organ) {
		logger.debug("saveOrgan...");
		sysOrganizationDao.updateByPrimaryKeySelective(organ);
	}

	public void deleteOrgan(Long id) {
		logger.debug("deleteOrgan(id:{})...", id);
		SysOrganization organ =sysOrganizationDao.selectByPrimaryKey(id);
		if( organ == null ){
			return;
		}
		//更新用户机构
		sysUserDao.membersMoveByOrgId(organ.getParentId(),id);
		List<SysOrganization> list =findNextTrees(organ.getTreeCode(), treeLevelLength);
		deleteTree(organ.getTreeCode());
		
		int levelLength = organ.getTreeCode().length();
		for (SysOrganization obj : list)
		{
			String adjustCode = TreeUtils.getLevelLastTreeCode(obj.getTreeCode(), levelLength);
			obj.setTreeCode(adjustCode);
			sysOrganizationDao.updateByPrimaryKey(obj);
		}
	}

	public void deleteOrgan(String[] ids) {
		for(String id:ids){
			deleteOrgan(Long.valueOf(id));
		}
	}
	
	@Transactional(readOnly = true)
	public List<SysOrganization> findMoveTrees(SysOrganization organ) {
		return findOtherTrees(organ.getTreeCode());
	}

	public boolean moveOrgan(Long moveId, Long targetId, int moveType){
		this.lastMassage = "";
		if ( !moveNode(moveId, targetId, moveType, treeLevelLength) )
		{
			this.lastMassage = "移动机构或目标机构已经不存在!";
			return false;
		}
		return true;
	}
	
	public boolean importExcel(InputStream stream){
		this.lastMassage = "";
		ExcelObj<SysOrganization> excelUtil = new ExcelObj<SysOrganization>(SysOrganization.class);
		String excelAttNames ="orgName,orgCode";
		List<SysOrganization> list = excelUtil.getObjListFrom(stream, excelAttNames);
		if( list==null || list.size()==0 ){
			this.lastMassage = "导入失败！请检查导入文件数据！";
			return false;
		}
		long now =System.currentTimeMillis();//获取系统当前时间
		int addNum=0;
		int updateNum=0;
		for(int i=0; i<list.size(); i++){
			SysOrganization org = list.get(i);
			SysOrganization orgFind =getOrganBy(org.getOrgName());
			org.setModifiedTime(now);
			if( orgFind == null ){
				org.setCreatedTime(now);
				org.setStatus(1);
				org.setParentId(-1L);
				org.setId(IdWorker.nextId());
				addOrgan(org);
				addNum++;
			}else{
				//new String[]{"parentId","createdTime","status"}
				BeanUtil.copyProperties(org, orgFind);
				saveOrgan(orgFind);
				updateNum++;
			}
		}
		this.lastMassage = String.format("导入完毕！新增%d条记录，更新%d条记录。", addNum, updateNum);
		return true;
	}
	
	
	@Transactional(readOnly = true)
	public boolean isOrgNameUnique(Long id,String name) {
		String query =QueryBuilder.custom().andEquivalent("org_name",name).build();
		List<SysOrganization> list =sysOrganizationDao.list(query);
		if(list!=null&&!list.isEmpty()){
			if(id!=null){
				if(!list.get(0).getId().equals(id)) return false;
			}else{
				return false;
			}
		}
		return true;
	}
}
