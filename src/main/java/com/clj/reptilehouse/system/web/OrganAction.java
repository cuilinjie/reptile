package com.clj.reptilehouse.system.web;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.clj.reptilehouse.common.BaseAction;
import com.clj.reptilehouse.common.Global;
import com.clj.reptilehouse.common.excel.ExpExcelUtil;
import com.clj.reptilehouse.common.util.BeanUtil;
import com.clj.reptilehouse.common.util.JsonUtil;
import com.clj.reptilehouse.common.util.SystemUtil;
import com.clj.reptilehouse.system.entity.SysOrganization;
import com.clj.reptilehouse.system.entity.SysUser;
import com.clj.reptilehouse.system.service.OrganService;
import com.clj.reptilehouse.system.web.OrganAction;


@Controller
@RequestMapping(value = "/system/organ")
public class OrganAction extends BaseAction{
	private final String viewPath = "/system/organ";

	private static Logger logger = LoggerFactory.getLogger(OrganAction.class);

	@Autowired
	private OrganService organService;
		 
	@RequestMapping(value = "/main.do")
	public String main(ModelMap map) {
		logger.info( "Enter main.do ..." );
		SysUser curUser = SystemUtil.getLoginUser();
		if( curUser == null ){
			return Global.RESULT_ERROR_OUTTIME;
		}
		List<SysOrganization> list = null;
		if( SystemUtil.isSuperUser(curUser) ){
			list = organService.getAllOrgan();
		}
		else{
			SysOrganization userOrg = SystemUtil.getLoginUserMainOrg();
			if( userOrg != null ){
				list = organService.findOrganByCode(userOrg.getTreeCode());			
			}
		}
		String jsonStr = organService.getJsonTreeStr(list);
		map.put("jsonTreeData", jsonStr);
		return viewPath+"/main";
	}

	@RequestMapping(value = "/getData.do")
	public String getData(HttpServletResponse response) throws IOException{     
		logger.info( "Enter getData.do ..." );
		SysUser curUser = SystemUtil.getLoginUser();
		if( curUser == null ){
			return Global.RESULT_ERROR_OUTTIME;
		}
		List<SysOrganization> list = null;
		if( SystemUtil.isSuperUser(curUser) ){
			list = organService.getAllOrgan();
		}
		else{
			SysOrganization userOrg = SystemUtil.getLoginUserMainOrg();
			if( userOrg != null ){
				list = organService.findOrganByCode(userOrg.getTreeCode());			
			}
		}
		String jsonStr = organService.getJsonTreeStr(list);
	    response.getWriter().print(jsonStr);
	    return null;         
	}        
	
	@RequestMapping(value = "/getMinData.do")
	public String getMinData(String treeCode, HttpServletResponse response) throws IOException{     
		logger.info( "Enter getMinData.do ..." );
		if( StringUtils.isBlank(treeCode) ){
			SysUser curUser = SystemUtil.getLoginUser();
			if( curUser == null ){
				return null;
			}
			
			if( SystemUtil.isSuperUser(curUser) ){
				treeCode = "";
			}else{
				SysOrganization userOrg = SystemUtil.getLoginUserMainOrg();
				if( userOrg == null ){
					return null;
				}
				treeCode = userOrg.getTreeCode();					
			}
		}

		List<SysOrganization> list = organService.findOrganByCode(treeCode);			
		String jsonStr = organService.getJsonMinTreeStr(list);
	    response.getWriter().print(jsonStr);
	    return null;         
	}
	
	@RequestMapping(value = "/getParOrgData.do")
	public String getParOrgData(Long orgId, HttpServletResponse response) throws IOException{     
		logger.info( "Enter getParOrgData.do ..." );
		System.out.println("获取到的orgId是：》"+orgId);
		String treeCode="";
		if( orgId==null||orgId<1 ){
			SysOrganization userOrg = SystemUtil.getLoginUserMainOrg();
			if( userOrg == null ){
				return Global.RESULT_ERROR_OUTTIME;
			}
			SysOrganization parOrg = organService.getParOrg(userOrg.getId());
			if(parOrg!=null){
				treeCode = parOrg.getTreeCode();
			}else{
				treeCode =userOrg.getTreeCode();
			}
		}else{
			SysOrganization parOrg =organService.getParOrg(orgId);
			if(parOrg!=null){
				treeCode = parOrg.getTreeCode();
				if(StringUtils.isBlank(treeCode)){
					SysOrganization userOrg = SystemUtil.getLoginUserMainOrg();
					treeCode =userOrg.getTreeCode();
					System.out.println("获取到的treeCode3是：》"+treeCode);
				}
			}else{
				SysOrganization userOrg = SystemUtil.getLoginUserMainOrg();
				treeCode =userOrg.getTreeCode();
				System.out.println("获取到的treeCode2是：》"+treeCode);
			}
		}

		List<SysOrganization> list = organService.findOrganByCode(treeCode);			
		String jsonStr = organService.getJsonMinTreeStr(list);
		System.out.println("获取到的json串是：》"+jsonStr);
	    response.getWriter().print(jsonStr);
	    return null;         
	}  

	@RequestMapping(value = "/add.do")
	public String add(Long parentId, ModelMap map) {
		logger.info( "Enter add.do ..." );
		map.put("action", "add");
		SysOrganization parentOrgan = organService.fetchOrg(parentId);
		map.put("parentOrgan", parentOrgan);
		SysOrganization organ = new SysOrganization();
		organ.setParentId(parentId);
		organ.setStatus(Global.STATE_ON);
		map.put("organ", organ);
		return viewPath+"/edit";
	}

	@RequestMapping(value = "/edit.do")
	public String edit(Long id, ModelMap map) {
		logger.info( "Enter edit.do ..." );
		map.put("action", "edit");
		SysOrganization organ =organService.fetchOrg(id);
		map.put("organ", organ);
		SysOrganization parentOrgan = organService.fetchOrg(organ.getParentId());
		map.put("parentOrgan", parentOrgan);
		return viewPath+"/edit";
	}

	@RequestMapping(value = "/save.do")
	public String save(String action, Long id, SysOrganization organ, ModelMap map) {
		logger.info( "Enter save.do ..." );
		boolean validate = true;
		if( StringUtils.isBlank(organ.getOrgName()) ){
			validate = false;
			map.put("message", "机构名称不能为空！");
		}
		if(!organService.isOrgNameUnique(organ.getId(),organ.getOrgName())){
			validate = false;
			map.put("message", "机构名称已经存在！");
		}
		if( validate ){
			Long now = System.currentTimeMillis();//获取系统当前时间
			organ.setModifiedTime(now);
			String jsonStr = "";
			if( "add".equals(action)){
				if(organ.getParentId()==null)organ.setParentId(-1L);
				organ.setCreatedTime(now);
				organService.addOrgan(organ);
				map.put("organ", organ);
				jsonStr = organService.getJsonObjStr(organ);
			}
			else{
				SysOrganization editOrgan = organService.fetchOrg(id);
				if( editOrgan == null ){
					map.put("result", Global.RESULT_ERROR);			
					map.put("message", "该机构已经被删除！");
					return viewPath+"/edit";
				}
				BeanUtil.copyProperties(organ, editOrgan);
				organService.saveOrgan(editOrgan);
				map.put("organ", editOrgan);
				jsonStr = organService.getJsonObjStr(editOrgan);
			}
			map.put("saveData", jsonStr);
			map.put("result", Global.RESULT_SUCCESS);
		}
		else{
			map.put("organ", organ);
			map.put("result", Global.RESULT_ERROR);			
		}
		map.put("action", action);
		return viewPath+"/edit";
	}
	
	@RequestMapping(value = "/moveGo.do")
	public String moveGo(Long id, ModelMap map) throws IOException{     
		logger.info( "Enter moveGo.do ..." );
		SysOrganization organ =organService.fetchOrg(id);
		map.put("organ", organ);
		List<SysOrganization> list = organService.findMoveTrees(organ);
		String jsonStr = organService.getJsonTreeStr(list);
		map.put("jsonTreeData", jsonStr);
		return viewPath+"/move";
	}                                                   

	@RequestMapping(value = "/move.do")
	public String move(Long moveId, Long targetId, int moveType, HttpServletResponse response) throws IOException{     
		logger.info( "Enter move.do ..." );
		if( organService.moveOrgan(moveId, targetId, moveType) ){
			response.getWriter().print(JsonUtil.JSON_RESULT_SUCCESS);
		}
		else{
			response.getWriter().print(JsonUtil.JSON_RESULT_FALSE);			
		}
	    return null;         
	}                                                   
	
	@RequestMapping(value = "/delete.do")
	public String delete(String id, HttpServletResponse response) throws IOException {
		logger.info( "Enter delete.do ..." );
		organService.deleteOrgan(id.split(Global.SPLIT_KEY));
		response.getWriter().print(JsonUtil.JSON_RESULT_SUCCESS);
	    return null;         
	}
	
	@RequestMapping(value = "/importExcel.do")
	public void importExcel(MultipartFile impFile, HttpServletResponse response) throws IOException{     
		logger.info( "Enter importExcel.do ..." );
		String result = Global.RESULT_ERROR;
		String message = "";
		if( impFile.getSize()<=0 ){
			message = "上传文件内容为空！";
		}
		else{
			if( organService.importExcel(impFile.getInputStream()) ){
				result = Global.RESULT_SUCCESS;
			}
			message = organService.getLastMassage();
		}
		response.getWriter().print(getCheckResult(result, message));
	}   
	
	@RequestMapping(value="/export.do")
	public void deptExport(HttpServletResponse response) throws IOException{
		logger.info( "Enter main.do ..." );
		SysUser curUser = SystemUtil.getLoginUser();
		List<SysOrganization> list = null;
		if( SystemUtil.isSuperUser(curUser) ){
			list = organService.getAllOrgan();
		}else{
			SysOrganization userOrg = SystemUtil.getLoginUserMainOrg();
			if( userOrg != null ){
				list = organService.findOrganByCode(userOrg.getTreeCode());			
			}
		}
		String[] headers = {"机构名称", "机构编码"};//导出字段名称
	    String attr="orgName,orgCode";//导出映射字段
		String fileName="机构表.xls";//excel文件名
	    try {
	          response.setContentType("octets/stream");
	          response.addHeader("Content-Disposition","attachment;filename="+new String(fileName.getBytes("gb2312"), "iso8859-1"));  
	          OutputStream out = response.getOutputStream();
	          ExpExcelUtil.expExcel("机构表", headers,list,attr, out);
	          out.flush();
	          out.close();
	      } catch (Exception e) {
	    	  System.out.println("导出excel出错。。。");
	          e.printStackTrace();  
	      }
	}
}
